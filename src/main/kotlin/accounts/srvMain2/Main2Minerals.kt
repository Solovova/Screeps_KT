package accounts.srvMain2

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import screeps.api.ResourceConstant

fun AccountInitMain2.initMineralOut(mc: MainContext) {
    mc.mineralData["O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 200000
    )

    mc.mineralData["H".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 300000
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
            balancingStart = 20000,
            balancingStop = 50000
    )

    mc.mineralData["OH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000,
            balancingStart = 20000,
            balancingStop = 50000
    )

    mc.mineralData["LH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000,
            balancingStart = 20000,
            balancingStop = 50000
    )

    mc.mineralData["XLH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000,
            balancingStart = 30000,
            balancingStop = 50000
    )
}