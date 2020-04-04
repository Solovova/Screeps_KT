package mainContext

import screeps.api.*
import constants.CacheCarrier
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.COLOR_YELLOW
import screeps.api.Game
import screeps.api.structures.Structure
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom


//Test all need information here, if return null, way is impossible
//type mainContainer0, mainContainer1

fun MainContext.getNumRoomWithContainer():Int {
    var result = 0
    for (mainRoom in this.mainRoomCollector.rooms.values) {
        if (Game.rooms[mainRoom.name] != null && mainRoom.structureStorage[0] != null) {
            result++
        }
    }
    return result
}