package logic.production.lab

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.getQuantityAllMineralInStorage
import screeps.api.COLOR_RED
import screeps.api.ResourceConstant

class LMLabMainRoomRun(private val mc: MainContext) {
    private fun needRun(mainRoom: MainRoom): Boolean {
        if (mainRoom.constant.reactionActive == "") return false
        if (mainRoom.structureLabSort.size !in arrayOf(3, 6, 10)) return false
        if (mainRoom.getQuantityAllMineralInStorage() >= mainRoom.constant.mineralAllMaxInStorage) return false

        val mineralDataRecord: MineralDataRecord =
                this.mc.mineralData[mainRoom.constant.reactionActive.unsafeCast<ResourceConstant>()]
                        ?: return true

        return mineralDataRecord.storeMax > mineralDataRecord.quantity
    }

    private fun runMainRoom(mainRoom: MainRoom) {
        if (!this.needRun(mainRoom)) return
        val lab0 = mainRoom.structureLabSort[0] ?: return
        val lab1 = mainRoom.structureLabSort[1] ?: return
        val reaction = mainRoom.constant.reactionActive.unsafeCast<ResourceConstant>()
        val reactionComponent = mc.constants.globalConstant.labReactionComponent[reaction]
                ?: return
        if (reactionComponent.size != 2) return
        if (lab0.mineralAmount != 0 && lab0.mineralType.unsafeCast<ResourceConstant>() != reactionComponent[0]) return
        if (lab1.mineralAmount != 0 && lab1.mineralType.unsafeCast<ResourceConstant>() != reactionComponent[1]) return
        for (ind in 2 until mainRoom.structureLabSort.size) {
            val lab = mainRoom.structureLabSort[ind] ?: continue
            if (ind == 2 && mainRoom.creepNeedUpgradeID != "") continue
            if (lab.cooldown != 0) continue
            lab.runReaction(lab0, lab1)
        }
    }

    fun run() {
        for (mr in mc.mainRoomCollector.rooms.values) {
            try {
                this.runMainRoom(mr)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", "LMLabMainRoomRun", mr.name, COLOR_RED)
            }
        }
    }
}
