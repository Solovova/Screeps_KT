package logic.mineral

import mainContext.MineralDataRecord
import screeps.api.ResourceConstant

fun LogicMineral.runReaction(reaction: String) : Boolean {
    val mineralDataRecord: MineralDataRecord = this.mainContext.mineralData[reaction.unsafeCast<ResourceConstant>()]
            ?: return true

    return mineralDataRecord.storeMax < mineralDataRecord.quantity
}