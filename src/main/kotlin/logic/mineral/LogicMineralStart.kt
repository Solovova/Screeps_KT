package logic.mineral

import RESOURCES_ALL
import accounts.initMineralData
import mainContext.MainContext
import mainContext.MineralDataRecord

fun LogicMineral.mineralDataFill(mainContext: MainContext) {
    initMineralData(mainContext)
    for (res in RESOURCES_ALL) {
        val quantity: Int = mainContext.mainRoomCollector.rooms.values.sumBy { it.getResource(res) }
        val need: Int = mainContext.mainRoomCollector.rooms.values.sumBy { it.needMineral[res] ?: 0 }
        val mineralDataRecord: MineralDataRecord? = mainContext.mineralData[res]
        if (mineralDataRecord == null) {
            if (quantity != 0 || need != 0) mainContext.mineralData[res] = MineralDataRecord(quantity = quantity, need = need)
        } else {
            mineralDataRecord.quantity = quantity
            mineralDataRecord.need = need
            mineralDataRecord.quantityDown = 0
            mineralDataRecord.quantityUp = 0
        }
    }
}