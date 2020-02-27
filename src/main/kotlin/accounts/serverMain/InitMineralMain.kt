package accounts.serverMain

import mainContext.MainContext
import mainContext.MineralDataRecord
import mainContext.getNumRoomWithContainer
import screeps.api.ResourceConstant

fun initMineralMain(mainContext: MainContext) {
    mainContext.mineralData["energy".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.044,
            priceMin = 0.030,
            marketSellExcess = mainContext.getNumRoomWithContainer()*500000,
            marketBuyLack = mainContext.getNumRoomWithContainer()*200000,
            buyToRoom = "E54N37"
    )

    mainContext.mineralData["O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.030,
            priceMin = 0.006,
            marketSellExcess = 200000,
            marketBuyLack = 6000000,
            sellFromRoom = "E54N37"
    )

    mainContext.mineralData["H".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.040,
            priceMin = 0.015,
            marketSellExcess = 200000,
            marketBuyLack = 6000000,
            sellFromRoom = "E58N39"
    )

    mainContext.mineralData["L".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.100,
            priceMin = 0.080,
            marketSellExcess = 200000,
            marketBuyLack = 6000000,
            sellFromRoom = "E52N37"
    )

    mainContext.mineralData["Z".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.055,
            priceMin = 0.027,
            marketSellExcess = 200000,
            marketBuyLack = 6000000,
            sellFromRoom = "E59N36"
    )

    mainContext.mineralData["U".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.035,
            priceMin = 0.030,
            marketSellExcess = 200000,
            marketBuyLack = 6000000,
            sellFromRoom = "E57N39"
    )

    mainContext.mineralData["XGH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 2.000,
            priceMin = 0.920,
            marketSellExcess = 0,
            sellFromRoom = "E52N38"
    )

    mainContext.mineralData["GH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 1.200,
            priceMin = 0.900,
            marketSellExcess = 0,
            sellFromRoom = "E54N39"
    )

    mainContext.mineralData["OH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.060,
            priceMin = 0.060,
            storeMax = 100000,
            buyToRoom = "E52N35",
            onlyDirectBuy = true
    )

    mainContext.mineralData["ZK".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.050,
            priceMin = 0.050,
            storeMax = 100000,
            buyToRoom = "E54N37",
            onlyDirectBuy = true
    )

    mainContext.mineralData["UL".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.050,
            priceMin = 0.050,
            storeMax = 100000,
            buyToRoom = "E53N39",
            onlyDirectBuy = true
    )
}