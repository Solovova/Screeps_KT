package mainContext.mainRoomCollecror

import mainContext.MainContext
import mainContext.constants.MainRoomConstant
import creep.doTask
import creep.newTask
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import mainContext.dataclass.mainRoom
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.dataclass.role
import screeps.api.*
import screeps.utils.isEmpty
import screeps.utils.toMap
import screeps.utils.unsafe.delete
import mainContext.dataclass.slaveRoom
import mainContext.dataclass.tickDeath
import mainContext.dataclass.upgrade
import mainContext.dataclass.upgradeQuantity
import mainContext.dataclass.upgradeResource
import kotlin.math.roundToInt

class MainRoomCollector(private val mc: MainContext, names: Array<String>) {
    val rooms: MutableMap<String, MainRoom> = mutableMapOf()


    init {
        names.forEachIndexed { index, name ->
            val mainRoomConstant: MainRoomConstant? = this.mc.constants.mainRoomConstantContainer[name]
            if (mainRoomConstant != null && Game.rooms[name] != null && (Game.rooms[name]?.controller?.my == true))
                rooms[name] = MainRoom(mc, this, name, "M${index.toString().padStart(2, '0')}", mainRoomConstant)
            else mc.lm.lmMessenger.log("ERROR", name, "initialization don't see mainRoomConstant", COLOR_RED)
        }
    }

    fun creepsCalculate() {
        for (creep in Game.creeps.values) {
            if (creep.memory.tickDeath != 0
                    && creep.ticksToLive < creep.memory.tickDeath
                    && creep.store.toMap().map { it.value }.sum() == 0
            ) creep.suicide()

            //if (creep.memory.role == 18) creep.suicide()

            //
            if (creep.memory.role == 5 || creep.memory.role == 1005) {
                if (Memory["5Busy"] == null) Memory["5Busy"] = 0
                if (Memory["5Free"] == null) Memory["5Free"] = 0
                if (this.mc.tasks.isTaskForCreep(creep)) {
                    Memory["5Busy"] = Memory["5Busy"] + 1
                }else{
                    Memory["5Free"] = Memory["5Free"] + 1
                }
            }

            //ToDo can be more then one mainContext.dataclass.getUpgrade, not only then spawn
            val mainRoom: MainRoom = this.rooms[creep.memory.mainRoom] ?: continue

            this.mc.lm.lmCreep.lmUpgrade.creepSetLogic(creep,mainRoom)

            // Main rooms
            if (creep.memory.role in 0..99) {

                mainRoom.have[creep.memory.role]++

                if (creep.memory.role == 10) mainRoom.constant.creepIdOfBigBuilder = creep.id
                //Upgrade

            }

            // Slave rooms
            if (creep.memory.role in 100..199) {
                val slaveRoom: SlaveRoom = mainRoom.slaveRooms[creep.memory.slaveRoom] ?: continue

                slaveRoom.have[creep.memory.role - 100]++

                if (!slaveRoom.constant.roomHostile
                        && (creep.memory.role == 107 || creep.memory.role == 105)
                        && creep.hits < creep.hitsMax) creep.suicide()

                if (creep.memory.role == 126) slaveRoom.constant.creepIdMineralHarvester = creep.id
            }

            // Logist add transfer
            if (creep.memory.role == 14 || creep.memory.role == 1014) {
                val mainRoom: MainRoom = this.rooms[creep.memory.mainRoom] ?: continue
                for (res in creep.store.toMap()) mainRoom.resStorage[res.key] = (mainRoom.resStorage[res.key] ?: 0) + res.value
            }


        }

        //Add resource mainContext.dataclass.getUpgrade
        for (mainRoom in this.rooms.values) {
            if (mainRoom.creepNeedUpgradeID == "") continue
            val resource: ResourceConstant = mainRoom.creepNeedUpgradeResource.unsafeCast<ResourceConstant>()
            var resourceQuantityAllLabFiller = 0
            val creepsLabFiller = Game.creeps.toMap().filter { (it.value.memory.role == 18 || it.value.memory.role == 1018)
                    && it.value.memory.mainRoom == mainRoom.name}
            for (creep in creepsLabFiller) resourceQuantityAllLabFiller += creep.value.store[resource] ?: 0

            val lab = mainRoom.structureLabSort[2]
            val resourceQuantityLab2: Int = if (lab != null) lab.store[resource] ?: 0 else 0

            //console.log("Test $resourceQuantityAllLabFiller $resourceQuantityLab2")

            mainRoom.resTerminal[resource] = (mainRoom.resTerminal[resource] ?: 0) + resourceQuantityLab2 + resourceQuantityAllLabFiller
        }
    }

    fun creepsCalculateProfit() {
        if (Memory["profit"] == null) Memory["profit"] = object {}

        for (creep in Game.creeps.values) {
            if (creep.memory.role in arrayOf(106, 1006, 108, 1008, 121, 123, 125, 1121, 1123, 1125)) {
                val mainRoom: MainRoom = this.rooms[creep.memory.mainRoom] ?: continue
                val slaveRoom: SlaveRoom = mainRoom.slaveRooms[creep.memory.slaveRoom] ?: continue

                val carry: Int = creep.store[RESOURCE_ENERGY] ?: 0
                var oldCarry = 0
                if (Memory["profit"][creep.id] != null)
                    oldCarry = Memory["profit"][creep.id] as Int

                if ((carry - oldCarry) > 2) slaveRoom.profitPlus(carry - oldCarry)
                Memory["profit"][creep.id] = carry
            }
        }

        //clear
        try {
            for (key in js("Object").keys(Memory["profit"]).unsafeCast<Array<String>>())
                if (Game.getObjectById<Creep>(key) == null)
                    delete(Memory["profit"][key])
        } catch (e: Exception) {
            mc.lm.lmMessenger.log("ERROR", "Clear in creep profit", "", COLOR_RED)
        }
    }



    fun runNotEveryTick() {
        for (record in this.rooms) {
            try {
                record.value.runNotEveryTick()
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", "Room not every tick", record.value.room.name, COLOR_RED)
            }
        }
        this.houseKeeping()
    }

    fun runInEndOfTick() {
        for (room in rooms.values) {
            try {
                room.runInEndOfTick()
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", "Room in end of tick", room.name, COLOR_RED)
            }
        }

        val cpuStartCreeps = Game.cpu.getUsed()
        for (creep in Game.creeps.values) {
            try {
                creep.newTask(this.mc)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", "CREEP New task", "${creep.memory.mainRoom} ${creep.memory.slaveRoom} ${creep.memory.role} ${creep.id}", COLOR_RED)
            }

            try {
                creep.doTask(this.mc)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", "CREEP Do task", "${creep.memory.mainRoom} ${creep.memory.slaveRoom} ${creep.memory.role} ${creep.id}", COLOR_RED)
            }
        }

        Memory["CPUCreep"] = (Game.cpu.getUsed() - cpuStartCreeps).roundToInt()
    }

    private fun houseKeeping() {
        if (Game.creeps.isEmpty()) return
        for ((creepName, _) in Memory.creeps) {
            if (Game.creeps[creepName] == null) {
                delete(Memory.creeps[creepName])
            }
        }
    }
}