package logic.defence.mainRoom

import WALL_HITS_MAX
import mainContext.MainContext
import mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure
import kotlin.math.min

class LMMainRoomUpgradeWall (val mainContext: MainContext) {
    private fun getMinHits(mainRoom: MainRoom): Double {
        var needDefenceHits = mainRoom.constant.defenceHits
        if (needDefenceHits == 0) needDefenceHits = 1

        val structure: Structure = mainRoom.room.find(FIND_STRUCTURES).filter {
            (it.structureType == STRUCTURE_RAMPART || it.structureType == STRUCTURE_WALL)
        }.minBy { it.hits } ?: return (WALL_HITS_MAX - needDefenceHits).toDouble() / needDefenceHits
        return (structure.hits - needDefenceHits).toDouble() / needDefenceHits
    }

    fun calculate() {
        if (Game.time % 1500 != 0) return

        val rooms = mainContext.mainRoomCollector.rooms.values.sortedBy { this.getMinHits(it) }
        val countOfRoomForUpgrade = 15
        var counter = 0
        for (room in rooms) {
            counter++
            room.constant.defenceNeedUpgrade = (counter<=countOfRoomForUpgrade)
        }
    }
}