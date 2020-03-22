package accounts.serverMain

import mainContext.MainContext
import mainContext.MineralDataRecord
import mainContext.getNumRoomWithContainer
import screeps.api.ResourceConstant

fun initMineralMain(mainContext: MainContext) {
    mainContext.mineralData["energy".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = mainContext.getNumRoomWithContainer()*500000,
            marketBuyLack = mainContext.getNumRoomWithContainer()*200000,
            buyToRoom = "E54N37"
    )

    mainContext.mineralData["O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            sellFromRoom = "E54N37",
            buyToRoom = "E59N38"
    )

    mainContext.mineralData["H".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            sellFromRoom = "E58N39",
            buyToRoom = "E59N38"
    )

    mainContext.mineralData["L".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            sellFromRoom = "E52N37",
            buyToRoom = "E47N39"
    )

    mainContext.mineralData["Z".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            sellFromRoom = "E59N36",
            buyToRoom = "E54N39"
    )

    mainContext.mineralData["K".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            buyToRoom = "E54N39"
    )

    mainContext.mineralData["U".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.200,
            priceMin = 0.005,
            marketSellExcess = 200000,
            marketBuyLack = 20000,
            sellFromRoom = "E57N39",
            buyToRoom = "E47N39"
    )



    mainContext.mineralData["X".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 0.400,
            priceMin = 0.005,
            marketSellExcess = 400000,
            marketBuyLack = 20000,
            sellFromRoom = "E52N36",
            buyToRoom = "E57N39"
    )

    mainContext.mineralData["XGH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 2.000,
            priceMin = 0.920,
            marketSellExcess = 0,
            storeMax = 400000,
            sellFromRoom = "E52N38"
    )

    mainContext.mineralData["GH2O".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            priceMax = 1.200,
            priceMin = 0.900,
            storeMax = 400000,
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

    mainContext.mineralData["G".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000
    )

    mainContext.mineralData["GH".unsafeCast<ResourceConstant>()] = MineralDataRecord(
            storeMax = 100000
    )
}