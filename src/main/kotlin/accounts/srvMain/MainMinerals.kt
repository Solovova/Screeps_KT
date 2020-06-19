package accounts.srvMain

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import screeps.api.ResourceConstant

fun AccountInitMain.initMineralOut(mc: MainContext) {
    mc.mineralData["energy".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.020,
            marketSellExcess = mc.getNumRoomWithTerminal() * 250000,
            marketBuyLack = mc.getNumRoomWithTerminal() * 100000,
            sellFromRoom = "E54N37"
    )

    mc.mineralData["O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 300000,
            marketBuyLack = 30000,
            storeMax = 300000,
            sellFromRoom = "E54N37",
            buyToRoom = "E59N38"
    )

    mc.mineralData["H".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 400000,
            marketBuyLack = 60000,
            storeMax = 300000,
            sellFromRoom = "E58N39",
            buyToRoom = "E59N38"
    )

    mc.mineralData["L".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.280,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 30000,
            storeMax = 300000,
            sellFromRoom = "E52N37",
            buyToRoom = "E47N39"
    )

    mc.mineralData["Z".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 30000,
            storeMax = 300000,
            sellFromRoom = "E59N36",
            buyToRoom = "E54N39"
    )

    mc.mineralData["K".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 30000,
            storeMax = 300000,
            buyToRoom = "E54N39"
    )

    mc.mineralData["U".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 30000,
            storeMax = 300000,
            sellFromRoom = "E57N39",
            buyToRoom = "E47N39"
    )



    mc.mineralData["X".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 1.200,
            priceMin = 0.005,
            marketSellExcess = 400000,
            marketBuyLack = 30000,
            storeMax = 500000,
            sellFromRoom = "E52N36",
            buyToRoom = "E57N39"
    )

    mc.mineralData["XLH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.900,
            priceMin = 0.005,
            marketBuyLack = 30000,
            storeMax = 100000,
            buyToRoom = "E56N53",
            balancingStart = 20000,
            balancingStop = 100000
    )

    mc.mineralData["XGH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 2.000,
            priceMin = 1.400,
            marketSellExcess = 500000,
            storeMax = 700000,
            sellFromRoom = "E52N38",
            balancingStart = 600000,
            balancingStop = 600000
    )

    mc.mineralData["XGHO2".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 2.000,
            priceMin = 1.400,
            marketSellExcess = 200000,
            storeMax = 400000,
            sellFromRoom = "E54N37"
    )

    mc.mineralData["GH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 1.200,
            priceMin = 0.900,
            storeMax = 450000,
            marketSellExcess = 0,
            sellFromRoom = "E54N39",
            balancingStart = 300000,
            balancingStop = 400000
    )

    mc.mineralData["OH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.060,
            priceMin = 0.060,
            storeMax = 110000,
            buyToRoom = "E52N35",
            onlyDirectBuy = true,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["ZK".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.050,
            priceMin = 0.050,
            storeMax = 110000,
            buyToRoom = "E54N37",
            onlyDirectBuy = true,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["UL".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.050,
            priceMin = 0.050,
            storeMax = 110000,
            buyToRoom = "E53N39",
            onlyDirectBuy = true,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["G".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 110000,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["GH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 110000,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["LH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 110000,
            balancingStart = 50000,
            balancingStop = 100000
    )

    mc.mineralData["LH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 110000,
            balancingStart = 50000,
            balancingStop = 100000
    )

}