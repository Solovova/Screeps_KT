package accounts.srvMain2

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import screeps.api.ResourceConstant

fun AccountInitMain2.initMineralOut(mc: MainContext) {
    mc.mineralData["O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["H".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["L".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["Z".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["K".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["U".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["X".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 250000
    )

    mc.mineralData["LH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000,
            balancingStart = 10000,
            balancingStop = 50000
    )
}