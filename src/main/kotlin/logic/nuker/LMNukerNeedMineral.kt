package logic.nuker

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.RESOURCE_GHODIUM
import screeps.api.structures.StructureNuker

class LMNukerNeedMineral(val mc: MainContext) {
    private fun setNeedMineral(mainRoom: MainRoom) {
        if (mc.constants.globalConstant.nukerFilInRooms.isEmpty()
                || mainRoom.name in mc.constants.globalConstant.nukerFilInRooms) {
            val nuker:StructureNuker = mainRoom.structureNuker[0] ?: return
            if (nuker.store.getFreeCapacity(RESOURCE_GHODIUM) != 0) {
                mainRoom.needMineral[RESOURCE_GHODIUM] = (mainRoom.needMineral[RESOURCE_GHODIUM] ?: 0) + 2000
            }
        }
    }

    fun fill() {
        for (room in mc.mainRoomCollector.rooms.values) {
            setNeedMineral(room)
        }
    }
}