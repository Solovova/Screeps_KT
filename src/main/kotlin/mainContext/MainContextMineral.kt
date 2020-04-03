package mainContext

import OrderRecord
import screeps.api.*
import RESOURCES_ALL
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.utils.toMap
import kotlin.math.max
import kotlin.math.min

fun MainContext.marketDeleteEmptyOffers() {
    val orders = Game.market.orders.toMap().filter {
        it.value.remainingAmount == 0
                && !it.value.active
                && it.value.amount == 0
    }
    for (order in orders) Game.market.cancelOrder(order.value.id)
}

fun MainContext.marketGetSellOrdersSorted(sellMineral: ResourceConstant, roomName: String): List<OrderRecord> {

    val buyPriceEnergy = this.constants.globalConstant.marketBuyPriceEnergy
    val result: MutableList<OrderRecord> = mutableListOf()

    val orders = Game.market.getAllOrders().filter {
        it.resourceType == sellMineral
                && it.type == ORDER_SELL
                && it.amount != 0
    }
    for (order in orders) {
        val transactionCost: Double = Game.market.calcTransactionCost(1000, order.roomName, roomName).toDouble() * buyPriceEnergy / 1000.0
        result.add(result.size, OrderRecord(order, order.price + transactionCost))
    }

    result.sortBy { it.realPrice }
    return result.toList()
}

fun MainContext.marketGetBuyOrdersSorted(sellMineral: ResourceConstant, roomName: String): List<OrderRecord> {

    val buyPriceEnergy = this.constants.globalConstant.marketBuyPriceEnergy
    val result: MutableList<OrderRecord> = mutableListOf()

    val orders = Game.market.getAllOrders().filter {
        it.resourceType == sellMineral
                && it.type == ORDER_BUY
                && it.amount != 0
    }

    for (order in orders) {
        val transactionCost: Double = Game.market.calcTransactionCost(1000, order.roomName, roomName).toDouble() * buyPriceEnergy / 1000.0
        result.add(result.size, OrderRecord(order, order.price - transactionCost))
    }

    result.sortByDescending { it.realPrice }
    return result.toList()
}

fun MainContext.marketShowSellOrdersRealPrice(resourceConstant: ResourceConstant = RESOURCE_ENERGY) {
    if (this.constants.mainRooms.isEmpty()) return
    val marketSell = this.marketGetSellOrdersSorted(resourceConstant, this.constants.mainRooms[0])
    console.log("Sell orders $resourceConstant}")
    for (record in marketSell) {
        val strPrice = record.order.price.asDynamic().toFixed(3).toString().padEnd(6)
        val strRealPrice = record.realPrice.asDynamic().toFixed(3).toString().padEnd(6)
        console.log("id: ${record.order.id} mineral: $resourceConstant price: $strPrice real price: $strRealPrice quantity:${record.order.remainingAmount}")
    }
}

fun MainContext.marketShowBuyOrdersRealPrice(resourceConstant: ResourceConstant = RESOURCE_ENERGY) {
    if (this.constants.mainRooms.isEmpty()) return
    val marketSell = this.marketGetBuyOrdersSorted(resourceConstant, this.constants.mainRooms[0])

    console.log("Buy orders $resourceConstant}")
    for (record in marketSell) {
        val strPrice = record.order.price.asDynamic().toFixed(3).toString().padEnd(6)
        val strRealPrice = record.realPrice.asDynamic().toFixed(3).toString().padEnd(6)
        console.log("id: ${record.order.id} price: $strPrice real price: $strRealPrice  quantity:${record.order.remainingAmount}")
    }
}

fun MainContext.mineralSellExcess() {
    for (resource in RESOURCES_ALL) {
        val mineralDataRecord = mineralData[resource] ?: continue
        if (mineralDataRecord.marketSellExcess == 0 || mineralDataRecord.quantity < mineralDataRecord.marketSellExcess) continue
        val mainRoomForSale: MainRoom = this.mainRoomCollector.rooms.values.firstOrNull { it.getResourceInTerminal(resource) > 5000 }
                ?: continue
        val orders = this.marketGetBuyOrdersSorted(resource, mainRoomForSale.name)
        if (orders.isEmpty()) continue
        val order = orders[0]
        if (order.realPrice > mineralDataRecord.priceMin) Game.market.deal(order.order.id, min(order.order.amount, 5000), mainRoomForSale.name)
    }
}

fun MainContext.mineralSellOrderCreate(resource: ResourceConstant, mineralDataRecord: MineralDataRecord): Double? {
    val quantitySkip = 30000
    val quantityOrderAmount = 10000
    if (mineralDataRecord.quantity < mineralDataRecord.marketSellExcess) return null

    val orders = Game.market.getAllOrders().filter {
        it.resourceType == resource
                && it.type == ORDER_SELL
                && it.remainingAmount != 0
                && it.amount != 0
                && it.roomName != mineralDataRecord.sellFromRoom
    }.sortedBy { it.price }
    var sumQuantity = 0
    var sellOrder: Market.Order? = null
    for (order in orders) {
        sumQuantity += order.remainingAmount
        sellOrder = order
        if (sumQuantity > quantitySkip) break
    }

    val myOrderPrice:Double = if (sellOrder == null) {
        mineralDataRecord.priceMax
    }else{
        max(mineralDataRecord.priceMin, sellOrder.price )//-0.001
    }


    //console.log("Create SELL order for $resource price: $myOrderPrice")

    val myOrders = Game.market.orders.values.toList().firstOrNull {
        it.resourceType == resource
                && it.type == ORDER_SELL
    }

    if (myOrders == null) {
        if (mineralDataRecord.sellFromRoom == "") return null
        Game.market.createOrder(ORDER_SELL, resource, myOrderPrice, quantityOrderAmount, mineralDataRecord.sellFromRoom)
    } else {
        if (myOrders.remainingAmount < quantityOrderAmount) Game.market.extendOrder(myOrders.id, quantityOrderAmount - myOrders.remainingAmount)
        if (myOrderPrice != myOrders.price) Game.market.changeOrderPrice(myOrders.id, myOrderPrice)
    }

    return myOrderPrice
}

fun MainContext.mineralGetBuyPrice(resource: ResourceConstant, mineralDataRecord: MineralDataRecord): Double {
    val quantitySkip = 5000

    val orders = Game.market.getAllOrders().filter {
        it.resourceType == resource
                && it.type == ORDER_BUY
                && it.remainingAmount != 0
                && it.amount != 0
                && it.roomName != mineralDataRecord.sellFromRoom
    }.sortedByDescending { it.price }
    var sumQuantity = 0
    var buyOrder: Market.Order? = null
    for (order in orders) {
        sumQuantity += order.remainingAmount
        buyOrder = order
        if (sumQuantity > quantitySkip) break
    }

    return  if (buyOrder == null) {
        mineralDataRecord.priceMin
    }else{
        min(mineralDataRecord.priceMax, buyOrder.price + 0.001)
    }
}

fun MainContext.mineralBuyOrderCreate(resource: ResourceConstant, mineralDataRecord: MineralDataRecord): Double? {
    if (mineralDataRecord.quantity > mineralDataRecord.marketBuyLack) return null
    val myOrderPrice: Double = this.mineralGetBuyPrice(resource, mineralDataRecord)
    val quantityOrderAmount = 10000

    //console.log("Create SELL order for $resource price: $myOrderPrice")

    val myOrders = Game.market.orders.values.toList().firstOrNull {
        it.resourceType == resource
                && it.type == ORDER_BUY
    }

    if (myOrders == null) {
        if (mineralDataRecord.buyToRoom == "") return null
        Game.market.createOrder(ORDER_BUY, resource, myOrderPrice, quantityOrderAmount, mineralDataRecord.buyToRoom)
    } else {
        if (myOrders.remainingAmount < quantityOrderAmount) Game.market.extendOrder(myOrders.id, quantityOrderAmount - myOrders.remainingAmount)
        if (myOrderPrice != myOrders.price) Game.market.changeOrderPrice(myOrders.id, myOrderPrice)
    }

    return myOrderPrice
}

fun MainContext.mineralSellDirect(resource: ResourceConstant, mineralDataRecord: MineralDataRecord, minPrice: Double) {
    if (mineralDataRecord.quantity < mineralDataRecord.marketSellExcess) return
    val mainRoomForSale: MainRoom = this.mainRoomCollector.rooms.values.firstOrNull { it.getResourceInTerminal(resource) > 5000 }
            ?: return
    val orders = this.marketGetBuyOrdersSorted(resource, mainRoomForSale.name)
    if (orders.isEmpty()) return
    val order = orders[0]
    //console.log("$resource ${order.realPrice}  $minPrice")
    if (order.realPrice > minPrice) Game.market.deal(order.order.id, min(order.order.amount, 5000), mainRoomForSale.name)
}

fun MainContext.mineralBuyDirect(resource: ResourceConstant, mineralDataRecord: MineralDataRecord):Boolean {
    if (mineralDataRecord.quantity > mineralDataRecord.marketSellExcess) return false
    if (mineralDataRecord.quantity > mineralDataRecord.storeMax) return false
    val mainRoomForBuyName: String = mineralDataRecord.buyToRoom
    if (mainRoomForBuyName == "") return false

    val orders = this.marketGetSellOrdersSorted(resource, mainRoomForBuyName)
    if (orders.isEmpty()) return false
    val order = orders[0]
    console.log("$resource ${order.realPrice}  ${mineralDataRecord.priceMax}")
    if (order.realPrice <= mineralDataRecord.priceMax) {
        Game.market.deal(order.order.id, min(order.order.amount, 5000), mainRoomForBuyName)
        return true
    }
    return false
}

fun MainContext.mineralSellBuy() {
    for (resource in RESOURCES_ALL) {
        val mineralDataRecord = mineralData[resource] ?: continue
        if (mineralDataRecord.quantity >= mineralDataRecord.marketSellExcess) {
            this.mineralSellOrderCreate(resource, mineralDataRecord)
            //if (minPrice != null) this.mineralSellDirect(resource, mineralDataRecord, minPrice)
        }


        if (mineralDataRecord.quantity < mineralDataRecord.marketBuyLack
                && Game.market.credits > 200000.0) {

            if (!this.mineralBuyDirect(resource,mineralDataRecord)) {
                this.mineralBuyOrderCreate(resource,mineralDataRecord)
            }
        }
    }
}

fun MainContext.mineralShowBuySellPrice(minerals: Array<ResourceConstant>) {
    for (mineral in minerals) {
        val mineralDataRecord: MineralDataRecord? = this.mineralData[mineral]
        if ( mineralDataRecord!= null) {
            val buyPrice = this.mineralGetBuyPrice(mineral,mineralDataRecord)
            console.log("Mineral $mineral : Buy price: $buyPrice")
        }
    }
}