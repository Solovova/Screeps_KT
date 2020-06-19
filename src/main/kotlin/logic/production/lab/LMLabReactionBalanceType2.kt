package logic.production.lab

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.ResourceConstant

class LMLabReactionBalanceType2(val mc: MainContext) {
    private fun canStart(reaction: ResourceConstant): Boolean {
        val reactionCompounds = mc.lm.lmProduction.labFunc.getReactionCompounds(reaction)
        if (reactionCompounds.size != 2) return true

        for (compound in reactionCompounds) {
            val mineralDataRecord: MineralDataRecord = mc.mineralData[compound] ?: return false
            val startResource: Int = 10000 + mc.mainRoomCollector.rooms.values
                    .filter { it.constant.reactionActive == reaction.toString()}.size * 4000
            if (mineralDataRecord.quantity < startResource) return false
        }

        val mineralDataRecord: MineralDataRecord = mc.mineralData[reaction] ?: return false
        val balance = mineralDataRecord.quantityUp - mineralDataRecord.quantityDown
        return (mineralDataRecord.quantity + balance) < mineralDataRecord.balancingStart
    }

    fun balancingStartForRoom(mr: MainRoom):Boolean {
        if (mr.constant.reactionActiveArr.isNotEmpty()) {
            for (newReaction in mr.constant.reactionActiveArr) {
                if (newReaction == "") continue
                if (newReaction==mr.constant.reactionActive) return false
                if (canStart(newReaction.unsafeCast<ResourceConstant>())) {
                    mr.constant.reactionActive = newReaction
                    return true
                }
            }
        }
        return false
    }
}