package logic.defence

import mainContext.MainContext
import screeps.api.*

class LMMainRoomCreepAutoDefence(val mc: MainContext) {
    private val lmMainRoomCreepAutoDefencePath: LMMainRoomCreepAutoDefencePath = LMMainRoomCreepAutoDefencePath(mc)
    fun moveTo(creep: Creep, pos: RoomPosition, range: Int = 1):Boolean {
        if (creep.pos.inRangeTo(pos,range)) return true

        val path = lmMainRoomCreepAutoDefencePath.gets(creep.pos,pos,range =range )
        if (!path.incomplete) {
            creep.moveTo(path.path[0])
        }else{
            mc.lm.lmMessenger.log("INFO",creep.pos.roomName,"Incomplete ...", color = COLOR_YELLOW)
        }
        return false
    }

    fun task(creep: Creep) {

//        val flagsRedYellow: Flag = mc.flags.firstOrNull() { it.pos.roomName == creep.pos.roomName
//                && it.color == COLOR_RED
//                && it.secondaryColor == COLOR_YELLOW } ?: return

        val target: Creep = creep.room.find(FIND_HOSTILE_CREEPS).minBy { it.pos.getRangeTo(creep.pos) } ?: return

        if (!moveTo(creep, target.pos, 2)) {
            mc.lm.lmMessenger.log("INFO",creep.pos.roomName,"Move to ...", color = COLOR_YELLOW)
        }else{
            mc.lm.lmMessenger.log("INFO",creep.pos.roomName,"Can attack ...", color = COLOR_YELLOW)
            creep.rangedAttack(target)
        }
    }
}