package logic.mineral

import REACTION_TIME
import RESOURCES_ALL
import accounts.initMineralData
import logic.lab.LogicLab
import mainContext.MainContext
import mainContext.MineralDataRecord
import screeps.api.ResourceConstant
import screeps.api.get

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

fun LogicMineral.mineralProductionFill(mainContext: MainContext) {
    for (room in mainContext.mainRoomCollector.rooms.values) {
        if (room.constant.reactionActive != "") {
            val reaction = room.constant.reactionActive.unsafeCast<ResourceConstant>()
            if (room.structureLabSort.size !in arrayOf(3, 6, 10)) continue
            val reactionComponent = mainContext.constants.globalConstant.labReactionComponent[reaction]
                    ?: return
            if (reactionComponent.size != 2) return
            val reactionTime = REACTION_TIME[reaction] ?: 100
            val produce = 1000 * (room.structureLabSort.size - 2) * 5 / reactionTime

            val mineralDataRecord: MineralDataRecord? = mainContext.mineralData[reaction]
            if (mineralDataRecord != null) mineralDataRecord.quantityUp += produce
            else mainContext.mineralData[reaction] = MineralDataRecord(quantityUp = produce)
            for (ind in 0..1) {
                val reactionComponentRc = reactionComponent[ind].unsafeCast<ResourceConstant>()
                val mineralDataRecordComp: MineralDataRecord? = mainContext.mineralData[reactionComponentRc]
                if (mineralDataRecordComp != null) mineralDataRecordComp.quantityDown += produce
                else mainContext.mineralData[reactionComponentRc] = MineralDataRecord(quantityDown = produce)
            }
        }
    }
}