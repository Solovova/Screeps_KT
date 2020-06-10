package logic.develop

import mainContext.tasks.CreepTask
import screeps.api.Creep
import screeps.api.RoomPosition

class LMDevelopMoveByPath {

//    fun Creep.getNextPos(path: List<RoomPosition>, goFrom: String, goTo: String, task: CreepTask): RoomPosition? {
//        println("Teeeest next from to: $goFrom $goTo")
//        println(path[0])
//        if (this.pos.roomName == goFrom) { //find firs not this room
//            println("First in middle room:"+path.firstOrNull { it.roomName != goFrom })
//            return path.firstOrNull { it.roomName != goFrom }
//        }
//
//        if (this.pos.roomName == goTo) { //moteTo(pos)
//            return null
//        }
//
//        //find my pos in path and return next
//        val myPos = path.withIndex().firstOrNull { it.value.roomName == this.pos.roomName && it.value.x == this.pos.x && it.value.y == this.pos.y }
//
//        if (myPos != null) {
//            println("Next point:"+path[myPos.index + 1])
//            return path[myPos.index + 1]
//        }
//
//        return null
//    }

    //        var nextPos: RoomPosition? = null
//
//        //Move by path
//        //if (this.memory.role in arrayOf(121, 123, 125)) {
//        if (this.id == "5ee109abca7602e76db47cfe") {
//            val mainRoom: MainRoom? = mc.mainRoomCollector.rooms[this.memory.mainRoom]
//            if (mainRoom != null) {
//                val slaveRoom: SlaveRoom? = mainRoom.slaveRooms[this.memory.slaveRoom]
//                if (slaveRoom != null && slaveRoom.constant.model == SlaveRoomType.central) {
//                    val namePathToObject: String = when (this.memory.role) {
//                        121 -> "slaveContainer0"
//                        123 -> "slaveContainer1"
//                        125 -> "slaveContainer2"
//                        else -> ""
//                    }
//
//
//                    val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", mainRoom, slaveRoom, safeMove = true)
//                    if (carrierAuto?.mPath != null && carrierAuto.mPath.isNotEmpty()) {
//                        val path = carrierAuto.mPath.toList()
//                        nextPos = if (task.posObject0?.roomName == slaveRoom.name) {
//                            this.getNextPos(path, mainRoom.name, slaveRoom.name, task)
//                        } else {
//                            this.getNextPos(path.reversed(), slaveRoom.name, mainRoom.name, task)
//                        }
//                    }
//                }
//            }
//        }
//
//        if (nextPos == null) {
//            nextPos = pos
//        } else {
//            println("Teeeest next pos: $nextPos")
//        }
//        this.moveTo(nextPos)
}