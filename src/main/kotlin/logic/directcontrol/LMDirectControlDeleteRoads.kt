package logic.directcontrol

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureRoad
import screeps.api.structures.StructureStorage

class LMDirectControlDeleteRoads(val mc: MainContext) {
    private fun getRoadToSlaveRoom(mainRoom: MainRoom):Array<RoomPosition>? {
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return null
        var result:Array<RoomPosition> = arrayOf()
        for (slaveRoom in mainRoom.slaveRooms.values) {

            for (container in slaveRoom.structureContainer.values) {
                val road:Array<RoomPosition> = mc.lm.lmHarvestGetWayFromPosToPos.gets(storage.pos, container.pos).path
                result += road
            }
        }
        return result

    }

    fun deleteRoads(mainRoom: MainRoom) {
        val roads:List<StructureRoad>? = Game.rooms[mainRoom.name]
                ?.find(FIND_STRUCTURES)
                ?.filter { it.structureType == STRUCTURE_ROAD}
                ?.map { it as StructureRoad }

        val roadSafe = getRoadToSlaveRoom(mainRoom)
        if (roads!= null)
            for (road in roads) {
                if (roadSafe!=null && roadSafe.any
                        { it.x == road.pos.x && it.y == road.pos.y && it.roomName == road.pos.roomName}) continue
                road.destroy()
            }

    }

    fun deleteRoadsSC(mainRoom: MainRoom) {
        val cs:Array<ConstructionSite>? = Game.rooms[mainRoom.name]?.find(FIND_CONSTRUCTION_SITES)


        if (cs!= null)
            for (c in cs) {
                c.remove()
            }

    }
}