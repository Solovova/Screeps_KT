package logic.defence

import mainContext.dataclass.WALL_HITS_MAX
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure

class LMMainRoomUpgradeWall (val mainContext: MainContext) {
    private fun getMinHits(mainRoom: MainRoom): Int {
        val structure: Structure = mainRoom.room.find(FIND_STRUCTURES).filter {
            (it.structureType == STRUCTURE_RAMPART || it.structureType == STRUCTURE_WALL)
        }.minBy { it.hits } ?: return WALL_HITS_MAX
        return structure.hits
    }

    private fun getNormalizedHits(mainRoom: MainRoom) : Double {
        var needDefenceHits = mainRoom.constant.defenceHits
        if (needDefenceHits == 0) needDefenceHits = 1
        return (mainRoom.constant.defenceMinHits - needDefenceHits).toDouble() / needDefenceHits
    }

    fun calculate() {
        if (Game.time % 1500 != 0) return

        for (room in mainContext.mainRoomCollector.rooms.values)
            room.constant.defenceMinHits = getMinHits(room)

        val rooms = mainContext.mainRoomCollector.rooms.values.sortedBy { this.getNormalizedHits(it) }
        var countOfRoomForUpgrade = mainContext.getNumRoomWithTerminal() /4
        if (countOfRoomForUpgrade == 0) countOfRoomForUpgrade = 1
        var counter = 0
        for (room in rooms) {
            if (room.constant.defenceMinHits>mainContext.constants.globalConstant.defenceLimitUpgrade) continue
            counter++
            room.constant.defenceNeedUpgrade = (counter<=countOfRoomForUpgrade)
        }
    }
}