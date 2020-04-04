package logic.harvest

import constants.CacheCarrier
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.COLOR_YELLOW
import screeps.api.Game
import screeps.api.structures.Structure

class LMHarvestCacheRecordRoom(val mc:MainContext) {
    fun gets(type: String, mainRoom: MainRoom, slaveRoom: SlaveRoom? = null, recalculate: Boolean = false, inSwampCost: Int = 10, inPlainCost: Int = 2) : CacheCarrier? {
        var objectTo : Structure? = null

        when(type) {
            "mainContainer0" -> objectTo = mainRoom.structureContainerNearSource[0]
            "mainContainer1" -> objectTo = mainRoom.structureContainerNearSource[1]
            "slaveContainer0" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[0]
            "slaveContainer1" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[1]
            "slaveContainer2" -> if (slaveRoom != null)  objectTo = slaveRoom.structureContainerNearSource[2]
        }

        if (objectTo == null) return null

        val objectFrom : Structure = mainRoom.structureStorage[0] ?: return null

        val keyRecord : String = objectFrom.id + objectTo.id

        var carrierAuto: CacheCarrier? = mc.constants.globalConstant.dataCacheCarrierAuto[keyRecord]

        //if  (mainContext.dataclass.getSlaveRoom?.name == "E56N34") console.log(carrierAuto)
        if (recalculate || carrierAuto == null || carrierAuto.default || (carrierAuto.tickRecalculate + 1000) < Game.time){
            val ret = mc.lm.lmHarvestGetWayFromPosToPos.gets(objectFrom.pos, objectTo.pos, inSwampCost = inSwampCost, inPlainCost = inPlainCost)
            //if  (mainContext.dataclass.getSlaveRoom?.name == "E56N34") console.log(objectTo.pos)
            mc.lm.lmMessenger.log("TEST", mainRoom.name, "Recalculate ways: $type ${!ret.incomplete}", COLOR_YELLOW)
            if (!ret.incomplete) {
                if  (slaveRoom?.name == "E56N34") console.log(objectTo.pos)
                carrierAuto = mc.lm.lmHarvestGetCarrierAuto.gets(ret, mainRoom, slaveRoom = slaveRoom)
                mc.constants.globalConstant.dataCacheCarrierAuto[keyRecord] = carrierAuto
            }
        }
        return mc.constants.globalConstant.dataCacheCarrierAuto[keyRecord]
    }
}