package logic.balance

import mainContext.dataclass.WALL_HITS_MAX
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure

class LMBalanceBuilderWall(val mc: MainContext) {
    private fun getMinHits(mainRoom: MainRoom): Int {
        val structure: Structure = mainRoom.room.find(FIND_STRUCTURES).filter {
            (it.structureType == STRUCTURE_RAMPART || it.structureType == STRUCTURE_WALL)
        }.minBy { it.hits } ?: return WALL_HITS_MAX
        return structure.hits
    }

    private fun getNormalizedHits(mainRoom: MainRoom): Double {
        return mainRoom.constant.defenceMinHits.toDouble()
    }

    private fun refreshDefenceMinHits() {
        for (room in mc.mainRoomCollector.rooms.values)
            room.constant.defenceMinHits = getMinHits(room)
    }


    fun setNeedBuilder() {
        var quantityNeedBuilder = mc.getNumRoomWithTerminal() / 4
        if (quantityNeedBuilder == 0) quantityNeedBuilder = 1

        var counter = mc.mainRoomCollector.rooms.values.filter { it.have[10] > 0 }.size

        mc.lm.lmMessenger.log("INFO","Glob","Builder have: $counter Target:$quantityNeedBuilder Deficit: ${quantityNeedBuilder - counter}")

        //if (Game.time % 1000 == 0)



        if (Game.time % 11 != 0) return
        for (room in mc.mainRoomCollector.rooms.values) {
            room.constant.needBuilder = (room.have[10] > 0)
        }
        refreshDefenceMinHits()


        val rooms = mc.mainRoomCollector.rooms.values.filter {
            it.constant.defenceMinHits < mc.constants.globalConstant.defenceLimitUpgrade
                    && it.have[10] == 0
        }.sortedBy { this.getNormalizedHits(it) }

        for (room in rooms) {
            counter++
            room.constant.needBuilder = (counter <= quantityNeedBuilder)
        }
    }
}