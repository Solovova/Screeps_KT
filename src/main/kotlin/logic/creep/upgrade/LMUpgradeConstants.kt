package logic.creep.upgrade

import mainContext.constants.GlobalConstant
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

class LMUpgradeConstants {
    fun setGlobalConstant(gc: GlobalConstant) {
        gc.creepUpgradablePartsRange[19] = mutableMapOf<BodyPartConstant,MutableList<Pair<Int,ResourceConstant>>>(
                WORK to
                        mutableListOf(
                                Pair(150000, "XGH2O".unsafeCast<ResourceConstant>()),
                                Pair(100000, "GH2O".unsafeCast<ResourceConstant>()),
                                Pair(1000, "GH".unsafeCast<ResourceConstant>())
                        )
        )

        gc.creepUpgradablePartsRange[7] = mutableMapOf<BodyPartConstant,MutableList<Pair<Int,ResourceConstant>>>(
                WORK to
                        mutableListOf(
                                Pair(10000, "XGH2O".unsafeCast<ResourceConstant>()),
                                Pair(10000, "GH2O".unsafeCast<ResourceConstant>()),
                                Pair(1000, "GH".unsafeCast<ResourceConstant>())
                        )
        )

        gc.creepUpgradablePartsRange[101] = mutableMapOf<BodyPartConstant,MutableList<Pair<Int,ResourceConstant>>>(
                WORK to
                        mutableListOf(
                                Pair(10000, "XGH2O".unsafeCast<ResourceConstant>()),
                                Pair(10000, "GH2O".unsafeCast<ResourceConstant>()),
                                Pair(1000, "GH".unsafeCast<ResourceConstant>())
                        )
        )

        gc.creepUpgradablePartsRange[10] = mutableMapOf<BodyPartConstant,MutableList<Pair<Int,ResourceConstant>>>(
                WORK to
                        mutableListOf(
                                Pair(10000, "XLH2O".unsafeCast<ResourceConstant>()),
                                Pair(10000, "LH2O".unsafeCast<ResourceConstant>()),
                                Pair(1000, "LH".unsafeCast<ResourceConstant>())
                        )
        )




        gc.creepUpgradableParts[7] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
        gc.creepUpgradableParts[19] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
        gc.creepUpgradableParts[101] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "XGH2O".unsafeCast<ResourceConstant>())
        gc.creepUpgradableParts[10] = mutableMapOf<BodyPartConstant, ResourceConstant>(WORK to "LH2O".unsafeCast<ResourceConstant>())
    }
}