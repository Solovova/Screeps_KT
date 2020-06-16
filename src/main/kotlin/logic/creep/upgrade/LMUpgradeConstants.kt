package logic.creep.upgrade

import mainContext.constants.GlobalConstant
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

class LMUpgradeConstants {
    fun setGlobalConstant(gc: GlobalConstant) {
        gc.constants.accountInit.initGlobalConstants(gc)
    }
}