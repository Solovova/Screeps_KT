package logic.creep.slaveRoom.tasks

import screeps.api.*
import screeps.utils.toMap
import mainRoom.MainRoom
import slaveRoom.SlaveRoom
import slaveRoom

class TaskGoToRoom(val creep: Creep, val mainRoom: MainRoom) {
    private fun goToRoomName(roomName: String) {
        val exitDir = creep.room.findExitTo(roomName)
        val exitPath = creep.pos.findClosestByRange(exitDir)
        if (exitPath != null) if (creep.fatigue == 0) creep.moveTo(exitPath.x, exitPath.y)
    }

    fun goToRoom() {
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
                    goToRoomName(flag.pos.roomName)
                    return
                }
            }
        }

        //Use path to room
        if (slaveRoom != null && slaveRoom.constant.pathToRoom.isNotEmpty()) {
            val indexNow: Int = slaveRoom.constant.pathToRoom.indexOf(creep.pos.roomName)
            if (indexNow != -1 && (indexNow+1)<slaveRoom.constant.pathToRoom.size) {
                console.log("Test Slave path: creep id:" + creep.id + " next room:" + slaveRoom.constant.pathToRoom[indexNow+1])
                goToRoomName(slaveRoom.constant.pathToRoom[indexNow+1])
                return
            }
        }

        goToRoomName(creep.memory.slaveRoom)
    }
}