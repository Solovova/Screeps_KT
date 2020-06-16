package logic.directcontrol

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureRampart
import screeps.api.structures.StructureRoad
import screeps.api.structures.StructureStorage
import screeps.utils.toMap

class LMDirectFunctions(val mc: MainContext) {
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

    fun deleteRamparts(mainRoom: MainRoom) {
        val ramparts:List<StructureRampart>? = Game.rooms[mainRoom.name]
                ?.find(FIND_STRUCTURES)
                ?.filter { it.structureType == STRUCTURE_RAMPART}
                ?.map { it as StructureRampart }

        if (ramparts!= null)
            for (rampart in ramparts) {
                rampart.destroy()
            }

    }

    fun deleteRoadsSC(mainRoom: MainRoom) {
        val cs:Array<ConstructionSite>? = Game.rooms[mainRoom.name]?.find(FIND_CONSTRUCTION_SITES)


        if (cs!= null)
            for (c in cs) {
                c.remove()
            }

    }

    fun comparePos(Pos1: RoomPosition, Pos2: RoomPosition):Boolean {
        return (Pos1.x == Pos2.x && Pos1.y == Pos2.y && Pos1.roomName == Pos2.roomName)
    }

    fun showCons() {
        val mainRooms = mc.mainRoomCollector.rooms.values
        val cs: List<ConstructionSite> = Game.constructionSites.values.filter { csElem -> mainRooms.none { it.name == csElem.pos.roomName } }

        val groupCs = cs.groupBy{it.pos.roomName}.map { it.key to it.value.size }
        println(groupCs)
    }

    fun flagsDelete() {
        val flags = Game.flags.values.filter { it.color != COLOR_WHITE && it.secondaryColor != COLOR_WHITE}
        for (flag in flags) flag.remove()
    }

}