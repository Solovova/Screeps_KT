package logic.creep.tasks

import mainContext.MainContext
import mainContext.dataclass.TypeOfTask
import mainContext.dataclass.mainRoom
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.tasks.CreepTask
import screeps.api.*
import screeps.api.structures.StructureLink
import screeps.api.structures.StructureNuker
import screeps.api.structures.StructureStorage
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap
import kotlin.math.min

class LMTasksLogist(val mc: MainContext) {

    fun newTaskEmptyCreep(creep: Creep): Boolean {
        val mainRoom: MainRoom = mc.mainRoomCollector.rooms[creep.memory.mainRoom] ?: return false
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return false
        if (creep.store.getUsedCapacity() != 0) {
            val resTransfer = creep.store.toMap().filter { it.value != 0 }.toList().firstOrNull()
            if (resTransfer != null) {
                mc.tasks.add(creep.id, CreepTask(TypeOfTask.TransferTo, storage.id, storage.pos, resource = resTransfer.first, quantity = resTransfer.second))
                return true
            }
        }
        return false
    }

    private fun linkTransfer(creep: Creep, storage: StructureStorage, mainRoom: MainRoom): CreepTask? {
        val link: StructureLink = mainRoom.structureLinkNearStorage[0] ?: return null

        if (mainRoom.getLevelOfRoom() == 3
                && mainRoom.have[19] != 0
                && mainRoom.source.size == 1
                && link.store[RESOURCE_ENERGY] ?: 0 == 0) {
            return CreepTask(TypeOfTask.Transport, storage.id, storage.pos, link.id, link.pos, RESOURCE_ENERGY, creep.store.getCapacity() ?: 0)
        }

        //Need for don't take mineral back to storage
        if (mainRoom.getLevelOfRoom() == 3
                && mainRoom.have[19] != 0
                && mainRoom.source.size == 1) {
            return null
        }

        if (link.store[RESOURCE_ENERGY] ?: 0 != 0) {
            return CreepTask(TypeOfTask.Transport, link.id, link.pos, storage.id, storage.pos, RESOURCE_ENERGY, 0)
        }

        return null
    }

    private fun energyLogist(creep: Creep, storage: StructureStorage, terminal: StructureTerminal, mainRoom: MainRoom): CreepTask? {
        var carry: Int

        // 01 Terminal > 0 && Storage < this.constant.energyMinStorage
        val needInStorage01: Int = mainRoom.constant.energyMinStorage - mainRoom.getResourceInStorage()
        val haveInTerminal01: Int = mainRoom.getResourceInTerminal()

        carry = min(min(needInStorage01, haveInTerminal01), creep.store.getCapacity() ?: 0)
        if (carry > 0) return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, storage.id, storage.pos, RESOURCE_ENERGY, carry)

        // 02 Storage > this.constant.energyMaxStorage -> Terminal < this.constant.energyMaxTerminal
        val needInTerminal02: Int = mainRoom.constant.energyMaxTerminal - mainRoom.getResourceInTerminal()
        val haveInStorage02: Int = mainRoom.getResourceInStorage() - mainRoom.constant.energyMaxStorage
        carry = min(min(haveInStorage02, needInTerminal02), creep.store.getCapacity() ?: 0)

        if (carry > 0 && (carry == creep.store.getCapacity() || carry == needInTerminal02))
            return CreepTask(TypeOfTask.Transport, storage.id, storage.pos, terminal.id, terminal.pos, RESOURCE_ENERGY, carry)

        // 03 Storage > this.constant.energyMinStorage -> Terminal < this.constant.energyMinTerminal or this.constant.energyMaxTerminal if sent
        val needInTerminal03: Int = if (mainRoom.constant.sentEnergyToRoom == "") mainRoom.constant.energyMinTerminal - mainRoom.getResourceInTerminal()
        else mainRoom.constant.energyMaxTerminal - mainRoom.getResourceInTerminal()
        val haveInStorage03: Int = mainRoom.getResourceInStorage() - mainRoom.constant.energyMinStorage
        carry = min(min(needInTerminal03, haveInStorage03), creep.store.getCapacity() ?: 0)

        if (carry > 0 && (carry == creep.store.getCapacity() || carry == needInTerminal03))
            return CreepTask(TypeOfTask.Transport, storage.id, storage.pos, terminal.id, terminal.pos, RESOURCE_ENERGY, carry)

        // 04 Terminal > this.constant.energyMinTerminal or this.constant.energyMaxTerminal if sent && Storage < this.constant.energyMaxStorageEmergency
        val haveInTerminal04: Int = if (mainRoom.constant.sentEnergyToRoom == "") mainRoom.getResourceInTerminal() - mainRoom.constant.energyMinTerminal
        else mainRoom.getResourceInTerminal() - mainRoom.constant.energyMaxTerminal
        val needInStorage04: Int = mainRoom.constant.energyMaxStorage - mainRoom.getResourceInStorage()
        carry = min(min(haveInTerminal04, needInStorage04), creep.store.getCapacity() ?: 0)

        if (carry > 0)
            return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, storage.id, storage.pos, RESOURCE_ENERGY, carry)

        return null
    }

    private fun mineralStorageToTerminal(creep: Creep, storage: StructureStorage, terminal: StructureTerminal, mainRoom: MainRoom): CreepTask? {
        //Mineral work
        //Storage -> Terminal this.constant.mineralMinTerminal but < this.constant.mineralAllMaxTerminal
        var carry: Int
        for (resInStorage in storage.store) {
            if (resInStorage.component1() == RESOURCE_ENERGY) continue
            val resourceStorage: ResourceConstant = resInStorage.component1()
            val quantityStorage: Int = resInStorage.component2()
            val quantityTerminal: Int = mainRoom.getResourceInTerminal(resourceStorage)
            val needInTerminal = mainRoom.constant.mineralMinTerminal - quantityTerminal
            val canMineralAllTerminal = mainRoom.constant.mineralAllMaxTerminal - (terminal.store.toMap().map { it.value }.sum()
                    - mainRoom.getResourceInTerminal(RESOURCE_ENERGY))
            if (canMineralAllTerminal <= 0) mc.lm.lmMessenger.log("INFO", mainRoom.name, "Terminal mineral is full", COLOR_RED)
            carry = min(min(min(needInTerminal, quantityStorage), creep.store.getCapacity() ?: 0), canMineralAllTerminal)

            if (carry > 0)
                return CreepTask(TypeOfTask.Transport, storage.id, storage.pos, terminal.id, terminal.pos, resourceStorage, carry)
        }

        return null
    }

    private fun fullNuker(creep: Creep, storage: StructureStorage, terminal: StructureTerminal, mainRoom: MainRoom, nuker: StructureNuker): CreepTask? {
        val globNeedEnergy: Int =  (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0) - (mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0)
        if (globNeedEnergy >0 ) return null

        val needEnergy: Int = nuker.store.getFreeCapacity(RESOURCE_ENERGY) ?: 0
        if (needEnergy != 0 && mainRoom.getResource(RESOURCE_ENERGY) > mainRoom.constant.energyUpgradeLvl8Controller) {
            return CreepTask(TypeOfTask.Transport, storage.id, storage.pos, nuker.id, nuker.pos, RESOURCE_ENERGY,
                    min(creep.store.getCapacity() ?: 0,needEnergy))
        }

        val needG: Int = nuker.store.getFreeCapacity(RESOURCE_GHODIUM) ?: 0
        if (needG != 0 && mainRoom.getResource(RESOURCE_GHODIUM) > 0) {
            return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, nuker.id, nuker.pos, RESOURCE_GHODIUM,
                    min(min(creep.store.getCapacity() ?: 0,needG),mainRoom.getResource(RESOURCE_GHODIUM)))
        }

        return null
    }

    private fun mineralTerminalToStorage(creep: Creep, storage: StructureStorage, terminal: StructureTerminal, mainRoom: MainRoom): CreepTask? {
        //Terminal -> Storage all mineral > this.constant.mineralMinTerminal
        var carry: Int

        for (resInTerminal in terminal.store) {
            if (resInTerminal.component1() == RESOURCE_ENERGY) continue
            val resourceTerminal: ResourceConstant = resInTerminal.component1()
            val quantityTerminal: Int = resInTerminal.component2()

            val haveInTerminal = quantityTerminal - mainRoom.constant.mineralMinTerminal
            carry = min(haveInTerminal, creep.store.getCapacity() ?: 0)
            if (carry > 0)
                return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, storage.id, storage.pos, resourceTerminal, carry)
        }

        return null
    }

    fun newTaskLinkAndTerminalStorageBalance(creep: Creep): Boolean {
        val mainRoom: MainRoom = mc.mainRoomCollector.rooms[creep.memory.mainRoom] ?: return false
        val terminal: StructureTerminal = mainRoom.structureTerminal[0] ?: return false
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return false

        var creepTask: CreepTask? = null

        if (creepTask == null) creepTask = this.linkTransfer(creep, storage, mainRoom)
        if (creepTask == null) creepTask = this.energyLogist(creep, storage, terminal, mainRoom)
        if (creepTask == null) creepTask = this.mineralStorageToTerminal(creep, storage, terminal, mainRoom)
        if (creepTask == null) creepTask = this.mineralTerminalToStorage(creep, storage, terminal, mainRoom)

        if (creepTask != null) {
            mc.tasks.add(creep.id, creepTask)
            return true
        }

        return false
    }



    fun newTaskNuke(creep: Creep): Boolean {
        val mainRoom: MainRoom = mc.mainRoomCollector.rooms[creep.memory.mainRoom] ?: return false

        if (!(mc.constants.globalConstant.nukerFilInRooms.isEmpty()
                || mainRoom.name in mc.constants.globalConstant.nukerFilInRooms)) return false

        val terminal: StructureTerminal = mainRoom.structureTerminal[0] ?: return false
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return false
        val nuker: StructureNuker = mainRoom.structureNuker[0] ?: return false

        var creepTask: CreepTask? = null

        if (creepTask == null) creepTask = this.fullNuker(creep, storage, terminal, mainRoom, nuker)

        if (creepTask != null) {
            mc.tasks.add(creep.id, creepTask)
            return true
        }

        return false
    }
}