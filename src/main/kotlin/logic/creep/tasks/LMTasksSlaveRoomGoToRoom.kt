package logic.creep.tasks

import mainContext.MainContext
import screeps.api.*
import screeps.utils.toMap
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import slaveRoom

class LMTasksSlaveRoomGoToRoom(val mainContext: MainContext) {
    private fun goToRoomName(creep: Creep, roomName: String) {
        val exitDir = creep.room.findExitTo(roomName)
        val exitPath = creep.pos.findClosestByRange(exitDir)
        if (exitPath != null) if (creep.fatigue == 0) creep.moveTo(exitPath.x, exitPath.y)
    }

    fun goToRoom(creep: Creep, mainRoom: MainRoom) {
        if (creep.pos.roomName == creep.memory.slaveRoom) return

        //Use flag in current room
        var flag: Flag? = creep.room.find(FIND_FLAGS).firstOrNull { it.color == COLOR_GREY && it.secondaryColor == COLOR_GREY }
        if (flag != null) {
            creep.moveTo(flag.pos.x, flag.pos.y)
            return
        }

        //Use global guide flag
        val slaveRoom: SlaveRoom? = mainRoom.slaveRooms[creep.memory.slaveRoom]
        if (slaveRoom != null && slaveRoom.constant.pathUseGlobalGuideFlag) {
            flag = Game.flags.toMap().values.firstOrNull { it.color == COLOR_GREY && it.secondaryColor == COLOR_GREY }
            if (flag != null) {
                console.log("Find flag:" + flag.pos.roomName)
                if (flag.pos.roomName != creep.pos.roomName) {
                    goToRoomName(creep, flag.pos.roomName)
                    return
                }
            }
        }

        //Use path to room
        if (slaveRoom != null && slaveRoom.constant.pathToRoom.isNotEmpty()) {
            val indexNow: Int = slaveRoom.constant.pathToRoom.indexOf(creep.pos.roomName)
            if (indexNow != -1 && (indexNow+1)<slaveRoom.constant.pathToRoom.size) {
                console.log("Test Slave path: creep id:" + creep.id + " next room:" + slaveRoom.constant.pathToRoom[indexNow+1])
                goToRoomName(creep, slaveRoom.constant.pathToRoom[indexNow+1])
                return
            }
        }

        goToRoomName(creep, creep.memory.slaveRoom)
    }
}