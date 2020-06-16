package logic.directcontrol

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*

class LMDirectControl (val mc: MainContext) {
    val lmDirectFunctions: LMDirectFunctions =LMDirectFunctions(mc)
    fun dcNN() {
//        try {
//            for (creep in Game.creeps.values)
//                if (creep.memory.slaveRoom == nameSlaveRoom) tasks.deleteTask(creep.id)
//
//        }catch (e: Exception) {
//            lm.lmMessenger.log("ERROR", "Direct control","", COLOR_RED)
//        }
//        lm.lmMessenger.log("TEST", "Direct control"," Erase all tasks in slave room: $nameSlaveRoom", COLOR_RED)
    }

    private fun directMainRoom(mr: MainRoom) {
        val flagsRedGreen = mc.flags.filter { it.pos.roomName == mr.name
                && it.color == COLOR_RED
                && it.secondaryColor == COLOR_GREEN }
        if (flagsRedGreen.isNotEmpty()) mc.lm.lmBuilding.lmBuildingSnapShot.restoreSnapShot(mr)
        for (flag in flagsRedGreen) flag.remove()

        val flagsRedRed = mc.flags.filter { it.pos.roomName == mr.name
                && it.color == COLOR_RED
                && it.secondaryColor == COLOR_RED }
        if (flagsRedRed.isNotEmpty()) mc.lm.lmBuilding.lmBuildingSnapShot.doSnapShot(mr)
        for (flag in flagsRedRed) flag.remove()

        val flagsRedGrey = mc.flags.filter { it.pos.roomName == mr.name
                && it.color == COLOR_RED
                && it.secondaryColor == COLOR_GREY }
        if (flagsRedGrey.isNotEmpty()) mc.lm.lmDevelop.runMainRoom(mr)
        for (flag in flagsRedGrey) flag.remove()

        val flagsRedBlue = mc.flags.filter { it.pos.roomName == mr.name
                && it.color == COLOR_RED
                && it.secondaryColor == COLOR_BLUE }
        if (flagsRedBlue.isNotEmpty()) {
            mc.lm.lmDefence.lmMainRoomDefenceArea.calculate(mr)
        }
        for (flag in flagsRedBlue) flag.remove()

        val flagsRedPurple = mc.flags.filter { it.pos.roomName == mr.name
                && it.color == COLOR_RED
                && it.secondaryColor == COLOR_PURPLE }
        if (flagsRedPurple.isNotEmpty()) {
            for (flag in flagsRedPurple){
                //mc.lm.lmDirectControl.lmDirectFunctions.deleteRoadsSC(mr)
                //mc.lm.lmDirectControl.lmDirectFunctions.flagsDelete()
                //mc.lm.lmDirectControl.lmDirectFunctions.deleteRamparts(mr)
            }


        }
        for (flag in flagsRedPurple) flag.remove()
    }

    fun runs() {
        for (room in mc.mainRoomCollector.rooms.values) {
            directMainRoom(room)
        }
    }
}