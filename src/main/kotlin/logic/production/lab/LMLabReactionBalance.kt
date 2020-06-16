package logic.production.lab

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.ResourceConstant

class LMLabReactionBalance(val mc: MainContext) {
//    if (mr.constant.reactionActive == "" && mr.constant.reactionActiveArr.isNotEmpty()) {
//        mr.constant.reactionActive = mr.constant.reactionActiveArr[0]
//    }
//
//    if (mr.constant.reactionActiveArr.size > 1) {
//        val quantityMineral: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.quantity ?: 0
//        val productionStart: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.balancingStart ?: 0
//        val productionStop: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.balancingStop ?: 0
//
//        if (mr.constant.reactionActive == mr.constant.reactionActiveArr[0]
//                && quantityMineral > productionStop) {
//            mr.constant.reactionActive = mr.constant.reactionActiveArr[1]
//        }
//
//        if (mr.constant.reactionActive == mr.constant.reactionActiveArr[1]
//                && quantityMineral < productionStart) {
//            mr.constant.reactionActive = mr.constant.reactionActiveArr[0]
//        }
//    }


    private fun canStart(reaction: ResourceConstant): Boolean {
        val reactionCompounds = mc.lm.lmProduction.labFunc.getReactionCompounds(reaction)
        if (reactionCompounds.size != 2) return true

        for(compound in reactionCompounds) {
            val mineralDataRecord: MineralDataRecord = mc.mineralData[compound] ?: return false
            if (mineralDataRecord.quantity < 6000) return false
        }

        val mineralDataRecord: MineralDataRecord = mc.mineralData[reaction] ?: return false
        return mineralDataRecord.quantity < mineralDataRecord.balancingStart
    }

    private fun needStop(mr: MainRoom): Boolean {
        val reactionCompounds = mc.lm.lmProduction.labFunc.getReactionCompounds(mr.constant.reactionActive.unsafeCast<ResourceConstant>())
        if (reactionCompounds.size != 2) return true

        for(compound in reactionCompounds) {
            val mineralDataRecord: MineralDataRecord = mc.mineralData[compound] ?: return true
            if (mineralDataRecord.quantity < 2000) return true
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