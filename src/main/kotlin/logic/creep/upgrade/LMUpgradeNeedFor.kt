package logic.creep.upgrade

import mainContext.MainContext
import mainContext.dataclass.MineralDataRecord
import mainContext.dataclass.role
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.ResourceConstant

class LMUpgradeNeedFor(val mc: MainContext) {
    fun getUpgradeParts(creep: Creep):Map<BodyPartConstant, ResourceConstant> {
        val result:MutableMap<BodyPartConstant, ResourceConstant> = mutableMapOf()
        val upgradePartsRange = mc.constants.globalConstant.creepUpgradablePartsRange[creep.memory.role]
        if (upgradePartsRange!=null) {
            for (part in upgradePartsRange.keys) {
                val listPair: List<Pair<Int,ResourceConstant>> = upgradePartsRange[part] ?: continue
                for (mineralRec in listPair) {
                    val mineralData: MineralDataRecord  = mc.mineralData[mineralRec.second] ?: continue
                    if (mineralRec.first <= mineralData.quantity) {
                        result[part] = mineralRec.second
                        break
                    }
                }
            }
        }
        return result
    }
}