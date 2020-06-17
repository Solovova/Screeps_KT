package logic.production.lab

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import screeps.api.get

class LMLabReactionBalance(val mc: MainContext) {
    private fun canStart(reaction: ResourceConstant): Boolean {
        val reactionCompounds = mc.lm.lmProduction.labFunc.getReactionCompounds(reaction)
        if (reactionCompounds.size != 2) return true

        for(compound in reactionCompounds) {
            val mineralDataRecord: MineralDataRecord = mc.mineralData[compound] ?: return false
            val startResource: Int = 4000 + mc.mainRoomCollector.rooms.values
                    .filter { it.constant.reactionActive ==  reaction.toString()}.size * 4000
            if (mineralDataRecord.quantity < startResource) return false
        }

        val mineralDataRecord: MineralDataRecord = mc.mineralData[reaction] ?: return false
        return mineralDataRecord.quantity < mineralDataRecord.balancingStart
    }

    private fun haveMineralsForReactionInLabs(mr: MainRoom, reactionCompounds:List<ResourceConstant>):Boolean {
        val lab0 = mr.structureLabSort[0] ?: return false
        val lab1 = mr.structureLabSort[1] ?: return false
        return (lab0.store[reactionCompounds[0]] ?: 0) >= 5 && (lab1.store[reactionCompounds[1]] ?: 0) >= 5
    }

    private fun needStop(mr: MainRoom): Boolean {
        val reactionCompounds:List<ResourceConstant> = mc.lm.lmProduction.labFunc.getReactionCompounds(mr.constant.reactionActive.unsafeCast<ResourceConstant>())
        if (reactionCompounds.size != 2) return true
        for(compound in reactionCompounds) {
            val mineralDataRecord: MineralDataRecord = mc.mineralData[compound] ?: return true
            if (mineralDataRecord.quantity < 500 && !haveMineralsForReactionInLabs(mr, reactionCompounds)) return true
        }

        val reaction = mr.constant.reactionActive.unsafeCast<ResourceConstant>()
        val mineralDataRecord: MineralDataRecord = mc.mineralData[reaction] ?: return true
        return mineralDataRecord.quantity > mineralDataRecord.balancingStop
    }

    //Need fill minerals
    private fun balancingForRoom(mr: MainRoom) {

        if (mr.constant.reactionActive != ""
                && mr.constant.reactionActiveArr.size > 1
                && needStop(mr)) {
            mr.constant.reactionActive = ""
        }

        //println("Room: ${mr.name} needStop: ${needStop(mr)} canStart: ${canStart("LH".unsafeCast<ResourceConstant>())} reaction: ${mr.constant.reactionActive}")

        if (mr.constant.reactionActive == ""
                && mr.constant.reactionActiveArr.isNotEmpty()) {
            for (newReaction in mr.constant.reactionActiveArr) {
                if (newReaction == "") continue
                if (canStart(newReaction.unsafeCast<ResourceConstant>())) {
                    mr.constant.reactionActive = newReaction
                    break
                }
            }
        }

        //println("Room: ${mr.name} needStop: ${needStop(mr)} canStart: ${canStart("LH".unsafeCast<ResourceConstant>())} reaction: ${mr.constant.reactionActive}")
    }

    fun balancing() {
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                balancingForRoom(room)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing!")
            }
        }
    }
}