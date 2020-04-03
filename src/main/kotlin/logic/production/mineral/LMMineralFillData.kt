package logic.production.mineral

import RESOURCES_ALL
import mainContext.MainContext
import mainContext.MineralDataRecord

class LMMineralFillData (val mc: MainContext) {
    fun fill() {
        mc.constants.accountInit.initMineral(mc)
        for (res in RESOURCES_ALL) {
            val quantity: Int = mc.mainRoomCollector.rooms.values.sumBy { it.getResource(res) }
            val need: Int = mc.mainRoomCollector.rooms.values.sumBy { it.needMineral[res] ?: 0 }
            val mineralDataRecord: MineralDataRecord? = mc.mineralData[res]
            if (mineralDataRecord == null) {
                if (quantity != 0 || need != 0) mc.mineralData[res] = MineralDataRecord(quantity = quantity, need = need)
            } else {
                mineralDataRecord.quantity = quantity
                mineralDataRecord.need = need
                mineralDataRecord.quantityDown = 0
                mineralDataRecord.quantityUp = 0
            }
        }
    }
}