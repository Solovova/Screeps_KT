package logic.messenger

import mainContext.MainContext
import screeps.api.FIND_STRUCTURES
import screeps.api.STRUCTURE_RAMPART

class LMMessengerRamparts(val mainContext: MainContext) {
    fun log(){
        var rampartsQty: Int = 0
        var rampartsHeal: Double = 0.0

        for (room in mainContext.mainRoomCollector.rooms.values) {
            val ramparts = room.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_RAMPART }
            val heals:Double = ramparts.sumByDouble { it.hits.toDouble() }
            rampartsQty += ramparts.size
            rampartsHeal += heals
        }

        val percent = rampartsHeal/rampartsQty*100/mainContext.constants.globalConstant.defenceLimitUpgrade
        mainContext.lm.lmMessenger.log("INFO","Glob",
                "Ramparts Qty: $rampartsQty Heals: $rampartsHeal Mid heal: ${rampartsHeal/rampartsQty} Percent: ${percent.asDynamic().toFixed(2)}")
    }
}