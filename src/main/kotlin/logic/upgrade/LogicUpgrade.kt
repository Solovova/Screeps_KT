package logic.upgrade

import constants.GlobalConstant
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

class LogicUpgrade {
    fun setGlobalConstants(globalConstant: GlobalConstant) {
        globalConstant.creepUpgradableParts[7] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "GH2O".unsafeCast<ResourceConstant>())
        globalConstant.creepUpgradableParts[10] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "LH2O".unsafeCast<ResourceConstant>())
    }
}