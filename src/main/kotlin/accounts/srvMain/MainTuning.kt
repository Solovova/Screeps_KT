package accounts.srvMain

import mainContext.MainContext
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

fun AccountInitMain.initTuningOut(mc: MainContext) {
    for (mr in mc.mainRoomCollector.rooms.values) {
        if (mr.constant.reactionActive == "" && mr.constant.reactionActiveArr.isNotEmpty()) {
            mr.constant.reactionActive = mr.constant.reactionActiveArr[0]
        }

        if (mr.constant.reactionActiveArr.size > 1) {
            val quantityMineral: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.quantity ?: 0
            val productionStart: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.productionStart ?: 0
            val productionStop: Int = mc.mineralData[mr.constant.reactionActiveArr[0].unsafeCast<ResourceConstant>()]?.productionStop ?: 0
            if (mr.constant.reactionActive == mr.constant.reactionActiveArr[0]
                    && quantityMineral > productionStop) {
                mr.constant.reactionActive = mr.constant.reactionActiveArr[1]
            }

            if (mr.constant.reactionActive == mr.constant.reactionActiveArr[1]
                    && quantityMineral < productionStart) {
                mr.constant.reactionActive = mr.constant.reactionActiveArr[0]
            }
        }
    }

    val minXGH2O: Int = mc.mineralData["XGH2O".unsafeCast<ResourceConstant>()]?.quantity ?: 0
    val minXLH2O: Int = mc.mineralData["XLH2O".unsafeCast<ResourceConstant>()]?.quantity ?: 0

    if (minXGH2O > 500000) {
        mc.constants.globalConstant.creepUpgradableParts[19] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
    } else {
        mc.constants.globalConstant.creepUpgradableParts[19] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "GH2O".unsafeCast<ResourceConstant>())
    }

    if (minXGH2O > 10000) {
        mc.constants.globalConstant.creepUpgradableParts[7] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
    } else {
        mc.constants.globalConstant.creepUpgradableParts[7] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "GH2O".unsafeCast<ResourceConstant>())
    }

    if (minXLH2O > 10000) {
        mc.constants.globalConstant.creepUpgradableParts[10] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XLH2O".unsafeCast<ResourceConstant>())
    } else {
        mc.constants.globalConstant.creepUpgradableParts[10] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "LH2O".unsafeCast<ResourceConstant>())
    }
}