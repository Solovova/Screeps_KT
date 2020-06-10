package logic.creep.tasks.slaveroom

import creep.doTaskGoTo
import mainContext.dataclass.mainRoom
import mainContext.dataclass.role
import screeps.api.*
import screeps.api.structures.Structure

class LMTasksSlaveRoomRepairRoad {
    fun repair(creep: Creep) {
        if (creep.memory.role !in arrayOf(106, 108, 1106, 1108, 121, 123, 125, 1121, 1123, 1125)) return
        if (creep.room.name == creep.memory.mainRoom) return
        val room: Room = Game.rooms[creep.pos.roomName] ?: return
        val fFind: Array<Structure> = (room.lookForAt(LOOK_STRUCTURES, creep.pos.x, creep.pos.y)
                ?: arrayOf())
                .filter { it.structureType == STRUCTURE_ROAD && it.hits < (it.hitsMax - 500) }.toTypedArray()
        if (fFind.isNotEmpty()) creep.repair(fFind[0])
    }
}