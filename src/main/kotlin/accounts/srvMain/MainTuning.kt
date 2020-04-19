package accounts.srvMain

import mainContext.MainContext
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

fun AccountInitMain.initTuningOut(mc: MainContext) {
    for (mr in mc.mainRoomCollector.rooms.values) {
        if (mr.constant.reactionActiveArr.size > 1) {
            val quantityMineral: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.quantity ?: 0
            if (mr.constant.reactionActive == mr.constant.reactionActiveArr[0]
                    && quantityMineral > 100000) {
                mr.constant.reactionActive = mr.constant.reactionActiveArr[1]
            }

            if (mr.constant.reactionActive == mr.constant.reactionActiveArr[1]
                    && quantityMineral < 50000) {
                mr.constant.reactionActive = mr.constant.reactionActiveArr[0]
            }
        }
    }

    val minXGH2O: Int = mc.mineralData["XGH2O".unsafeCast<ResourceConstant>()]?.quantity ?: 0
    if (minXGH2O > 150000) {
        mc.constants.globalConstant.creepUpgradableParts[19] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
    } else {
        mc.constants.globalConstant.creepUpgradableParts[19] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "GH2O".unsafeCast<ResourceConstant>())
    }
}