package logic.creep.tasks

import mainContext.MainContext
import mainContext.dataclass.TypeOfTask
import mainContext.dataclass.mainRoom
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.tasks.CreepTask
import screeps.api.Creep
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import screeps.api.structures.StructureLab
import screeps.api.structures.StructureStorage
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap
import kotlin.math.min

class LMTasksLabFiller(val mc: MainContext) {
    private fun emptyCreep(creep: Creep, storage: StructureStorage): CreepTask? {
        if (creep.store.getUsedCapacity() != 0) {
            val resTransfer = creep.store.toMap().filter { it.value != 0 }.toList().firstOrNull()
            if (resTransfer != null) {
                return CreepTask(TypeOfTask.TransferTo, storage.id, storage.pos, resource = resTransfer.first, quantity = resTransfer.second)
            }
        }
        return null
    }

    private fun fillEnergyToLab2(creep: Creep, terminal: StructureTerminal, mainRoom: MainRoom, lab2: StructureLab?): CreepTask? {
        if (lab2 == null) return null
        val needEnergy = min(lab2.store.getFreeCapacity(RESOURCE_ENERGY)
                ?: 0, mainRoom.getResourceInTerminal(RESOURCE_ENERGY))
        if (needEnergy >= creep.store.getCapacity()) {
            return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, lab2.id, lab2.pos, resource = RESOURCE_ENERGY, quantity = creep.store.getCapacity())
        }
        return null
    }

    private fun fillResourceForUpgradeToLab2(creep: Creep, terminal: StructureTerminal, mainRoom: MainRoom, lab2: StructureLab?): CreepTask? {
        if (mainRoom.creepNeedUpgradeID == "") return null
        if (lab2 == null) return null
        val resourceForUpgrade: ResourceConstant = mainRoom.creepNeedUpgradeResource ?: return null
        val resourceForUpgradeQuantity: Int = mainRoom.creepNeedUpgradeResourceQuantity
        if (resourceForUpgradeQuantity <= 0) return null

        val resLab2: Pair<ResourceConstant, Int> = lab2.store.toMap()
                .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                .firstOrNull() ?: Pair(resourceForUpgrade, 0)

        if (resLab2.first == resourceForUpgrade && resLab2.second >= resourceForUpgradeQuantity) return null

        if (resLab2.first != resourceForUpgrade && resLab2.second != 0) {
            return CreepTask(TypeOfTask.Transport, lab2.id, lab2.pos, terminal.id, terminal.pos,
                    resource = resLab2.first, quantity = min(creep.store.getCapacity(), resLab2.second))
        }

        if (resLab2.first == resourceForUpgrade && resLab2.second < resourceForUpgradeQuantity) {
            var quantityTransfer: Int = min(creep.store.getCapacity(), resourceForUpgradeQuantity - resLab2.second)
            quantityTransfer = min(quantityTransfer, mainRoom.getResourceInTerminal(resourceForUpgrade))

            return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, lab2.id, lab2.pos,
                    resource = resLab2.first, quantity = quantityTransfer)
        }
        return null
    }

    private fun checkResourceForReactionsInLab(creep: Creep, terminal: StructureTerminal, lab: StructureLab?, needResource: ResourceConstant?): CreepTask? {
        if (lab == null) return null
        val resLab: Pair<ResourceConstant, Int> = lab.store.toMap()
                .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                .firstOrNull() ?: return null

        if (needResource != resLab.first) {
            val quantityTransfer: Int = min(creep.store.getCapacity(), resLab.second)
            return CreepTask(TypeOfTask.Transport, lab.id, lab.pos, terminal.id, terminal.pos,
                    resource = resLab.first, quantity = quantityTransfer)
        }

        return null
    }

    private fun emptyLabs(creep: Creep, terminal: StructureTerminal, mainRoom: MainRoom): CreepTask? {
        var creepTask: CreepTask?
        for (labKey in mainRoom.structureLabSort.keys) {
            if (labKey == 2 && mainRoom.creepNeedUpgradeID != "") continue
            creepTask = checkResourceForReactionsInLab(creep, terminal, mainRoom.structureLabSort[labKey], null)
            if (creepTask != null) return creepTask
        }
        return null
    }

    private fun resourceForReactionsToLab(creep: Creep, terminal: StructureTerminal,lab: StructureLab,resLab: Pair<ResourceConstant, Int>):CreepTask? {
        var quantityTransfer: Int = lab.store.getFreeCapacity(resLab.first) ?: 0
        if (quantityTransfer < creep.store.getCapacity()) return null
        quantityTransfer = min(quantityTransfer,creep.store.getCapacity())
        return CreepTask(TypeOfTask.Transport, terminal.id, terminal.pos, lab.id, lab.pos,
                resource = resLab.first, quantity = min(quantityTransfer,creep.store.getCapacity()))
    }


    private fun resourceForReactions(creep: Creep, terminal: StructureTerminal, mainRoom: MainRoom, sourceLab: Array<StructureLab>): CreepTask? {
        var creepTask: CreepTask?
        if (mainRoom.constant.reactionActive == "") {
            creepTask = emptyLabs(creep, terminal, mainRoom)
            if (creepTask != null) return creepTask
        }

        val reaction: ResourceConstant? = mainRoom.constant.reactionActive.unsafeCast<ResourceConstant>()
        val reactionComponent: Array<ResourceConstant>? = mc.constants.globalConstant.labReactionComponent[reaction]

        if (reaction == null
                || mainRoom.structureLabSort.size !in arrayOf(3, 6, 10)
                || reactionComponent == null
                || reactionComponent.size != 2) {
            creepTask = emptyLabs(creep, terminal, mainRoom)
            if (creepTask != null) return creepTask
        } else {
            //All is ok, first check labs for needs resources
            for (labKey in mainRoom.structureLabSort.keys) {
                if (labKey == 2 && mainRoom.creepNeedUpgradeID != "") continue
                val needResource: ResourceConstant? = when (labKey) {
                    in 0..1 -> reactionComponent[labKey]
                    in 2..9 -> reaction
                    else -> null
                }

                creepTask = checkResourceForReactionsInLab(creep, terminal, mainRoom.structureLabSort[labKey], needResource)
                if (creepTask != null) return creepTask
            }

            //Fill resource for reaction
            val resLab0: Pair<ResourceConstant, Int> = sourceLab[0].store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(reactionComponent[0],0)
            val resLab1: Pair<ResourceConstant, Int> = sourceLab[1].store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(reactionComponent[1],0)
            creepTask = if (resLab0.second < resLab1.second) {
                resourceForReactionsToLab(creep, terminal,sourceLab[0],resLab0)
            }else{
                resourceForReactionsToLab(creep, terminal,sourceLab[1],resLab1)
            }
        }
        return creepTask
    }

    private fun getProductions(creep: Creep, terminal: StructureTerminal, mainRoom: MainRoom): CreepTask? {
        for (labKey in mainRoom.structureLabSort.keys) {
            if (labKey in 0..1) continue
            if (labKey == 2 && mainRoom.creepNeedUpgradeID != "") continue
            val lab: StructureLab = mainRoom.structureLabSort[labKey] ?: continue
            val resLab: Pair<ResourceConstant, Int> = lab.store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: continue
            if (resLab.second<creep.store.getCapacity()) continue
            return CreepTask(TypeOfTask.Transport, lab.id, lab.pos, terminal.id, terminal.pos,
                    resource = resLab.first, quantity = creep.store.getCapacity())
        }

        return null
    }


    fun newTask(creep: Creep):Boolean {
        val mainRoom: MainRoom = mc.mainRoomCollector.rooms[creep.memory.mainRoom] ?: return false
        val terminal: StructureTerminal = mainRoom.structureTerminal[0] ?: return false
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return false
        val lab0: StructureLab = mainRoom.structureLabSort[0] ?: return false
        val lab1: StructureLab = mainRoom.structureLabSort[1] ?: return false
        val sourceLab: Array<StructureLab> = arrayOf(lab0, lab1)

        var creepTask: CreepTask? = null

        if (creepTask == null) creepTask = this.emptyCreep(creep, storage)
        if (creepTask == null) creepTask = this.fillEnergyToLab2(creep, terminal, mainRoom, mainRoom.structureLabSort[2])
        if (creepTask == null) creepTask = this.fillResourceForUpgradeToLab2(creep, terminal, mainRoom, mainRoom.structureLabSort[2])
        if (creepTask == null) creepTask = this.resourceForReactions(creep, terminal, mainRoom, sourceLab)
        if (creepTask == null) creepTask = this.getProductions(creep, terminal, mainRoom)

        if (creepTask!=null) {
            mc.tasks.add(creep.id, creepTask)
            return true
        }
        return false
    }
}