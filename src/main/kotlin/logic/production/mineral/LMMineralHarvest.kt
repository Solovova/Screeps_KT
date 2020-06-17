package logic.production.mineral

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.needCorrection3
import screeps.api.COLOR_RED
import screeps.api.COLOR_YELLOW
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import screeps.api.structures.StructureStorage
import screeps.utils.toMap

class LMMineralHarvest(val mc: MainContext) {
    fun useHarvester(mr: MainRoom, mineral: ResourceConstant): Boolean {
        val mineralDataRecord = mc.mineralData[mineral]

        val storage: StructureStorage? = mr.structureStorage[0]

        if (storage == null) {
            mc.lm.lmMessenger.log("ERROR", mr.name, "Harvester don't have storage!", COLOR_RED)
            return false
        }else{
            val mineralAllQuantity: Int = storage.store.toMap().filter { it.key != RESOURCE_ENERGY }.toList().sumBy { it.second }
            if (mineralAllQuantity > mr.constant.mineralAllMaxInStorage) {
                mc.lm.lmMessenger.log("INFO", mr.name, "Harvester mineralAllQuantity > mineralAllMaxInStorage!", COLOR_YELLOW)
                return false
            }
        }

        if (mr.getResource(mineral) > mr.constant.mineralMaxInRoom) {
            mc.lm.lmMessenger.log("INFO", mr.name, "Harvester mineral $mineral full", COLOR_YELLOW)
            return false
        }


        if (mineralDataRecord == null) {
            mc.lm.lmMessenger.log("ERROR", mr.name, "Harvester mineral $mineral is null!", COLOR_RED)
            return false
        } else {
            if (mineralDataRecord.quantity > mineralDataRecord.storeMax) {
                mc.lm.lmMessenger.log("ERROR", mr.name, "Harvester mineral $mineral rich max quantity ${mineralDataRecord.storeMax}", COLOR_YELLOW)
                return false
            }
        }
        return true
    }
}