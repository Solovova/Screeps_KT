package logic.production.mineral

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.dataclass.REACTION_TIME
import screeps.api.ResourceConstant
import screeps.api.get

class LMMineralFillProduction (val mc: MainContext) {
    fun fill() {
        for (room in mc.mainRoomCollector.rooms.values) {
            if (room.constant.reactionActive != "") {
                val reaction = room.constant.reactionActive.unsafeCast<ResourceConstant>()
                if (room.structureLabSort.size !in arrayOf(3, 6, 10)) continue
                val reactionComponent = mc.constants.globalConstant.labReactionComponent[reaction]
                        ?: return
                if (reactionComponent.size != 2) return
                val reactionTime = REACTION_TIME[reaction] ?: 100
                val produce = 1000 * (room.structureLabSort.size - 2) * 5 / reactionTime

                val mineralDataRecord: MineralDataRecord? = mc.mineralData[reaction]
                if (mineralDataRecord != null) mineralDataRecord.quantityUp += produce
                else mc.mineralData[reaction] = MineralDataRecord(quantityUp = produce)
                for (ind in 0..1) {
                    val reactionComponentRc = reactionComponent[ind].unsafeCast<ResourceConstant>()
                    val mineralDataRecordComp: MineralDataRecord? = mc.mineralData[reactionComponentRc]
                    if (mineralDataRecordComp != null) mineralDataRecordComp.quantityDown += produce
                    else mc.mineralData[reactionComponentRc] = MineralDataRecord(quantityDown = produce)
                }
            }
        }
    }
}