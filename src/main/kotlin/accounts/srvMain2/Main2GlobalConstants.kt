package accounts.srvMain2

import mainContext.constants.GlobalConstant
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

fun AccountInitMain2.initGlobalConstantsOut(gc: GlobalConstant) {
    gc.username = "vsolo0"

    gc.creepUpgradablePartsRange[19] = mapOf<BodyPartConstant,List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(150000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(100000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    gc.creepUpgradablePartsRange[7] = mapOf<BodyPartConstant,List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(10000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(10000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    gc.creepUpgradablePartsRange[101] = mapOf<BodyPartConstant,List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(10000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(10000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    gc.creepUpgradablePartsRange[10] = mutableMapOf<BodyPartConstant,List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(2000, "XLH2O".unsafeCast<ResourceConstant>()),
                            Pair(2000, "LH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "LH".unsafeCast<ResourceConstant>())
                    )
    )
}