package logic.develop

import mainContext.MainContext
import screeps.api.FIND_STRUCTURES
import screeps.api.STRUCTURE_RAMPART

class LMDevelopSumAllRamparts(val mainContext: MainContext) {
    fun show(){
        var rampartsQty: Int = 0
        var rampartsHeal: Long = 0

        for (room in mainContext.mainRoomCollector.rooms.values) {
            val ramparts = room.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_RAMPART }
            val heals:Int = ramparts.sumBy { it.hits }
            rampartsQty += ramparts.size
            rampartsHeal += heals
        }
        println(" Dev ramparts Qty: $rampartsQty Heals: $rampartsHeal Mid heal: ${rampartsHeal/rampartsQty}")
    }
}