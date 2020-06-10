package logic.harvest

import constants.CacheCarrier
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType
import screeps.api.*
import kotlin.math.min
import kotlin.math.roundToInt

class LMHarvestGetCarrierAuto {
    fun gets (ret: PathFinder.Path, mainRoom: MainRoom, slaveRoom: SlaveRoom?, doNotCalculateRoads: Boolean = false, safeMove:Boolean = false): CacheCarrier {
        //ToDo SOURCE_ENERGY_KEEPER_CAPACITY
        val weight: Int
        val fMaxCapacity: Int
        val needCarriers: Int
        var needCapacity: Int
        val timeForDeath: Int
        var fBody : Array<BodyPartConstant>
        val pathSize:Int = if (doNotCalculateRoads) ret.cost else ret.path.size
        if (slaveRoom == null) {

            weight = (((SOURCE_ENERGY_CAPACITY +300)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            fMaxCapacity = min(mainRoom.room.energyCapacityAvailable / 150  *100,1600)
            needCarriers  = weight / fMaxCapacity + 1
            needCapacity = weight / needCarriers / 100 * 100 + 100
            timeForDeath = pathSize*2 + 20
            fBody = arrayOf()
            for (i in 0 until (needCapacity/100)) fBody += arrayOf(CARRY, CARRY, MOVE)
        }else{
            weight = if (slaveRoom.constant.model == SlaveRoomType.dangeon) (((SOURCE_ENERGY_KEEPER_CAPACITY + 1000)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            else (((SOURCE_ENERGY_CAPACITY +300)*pathSize*2).toDouble() / ENERGY_REGEN_TIME).roundToInt()
            fMaxCapacity = min((mainRoom.room.energyCapacityAvailable - 200) / 150 * 100 + 50,1550)
            needCarriers  = weight / fMaxCapacity + 1

            needCapacity = weight / needCarriers / 50 * 50 + 50
            if  (needCapacity / 100 * 100 == needCapacity) needCapacity += 50
            if ( needCapacity>1550) needCapacity = 1550
            timeForDeath = pathSize*2 + 20
            fBody = arrayOf(WORK, CARRY, MOVE)
            for (i in 0 until (needCapacity/100)) fBody += arrayOf(CARRY, CARRY, MOVE)
        }


        return CacheCarrier(default = false, tickRecalculate = Game.time, needCarriers = needCarriers, timeForDeath = timeForDeath, needBody = fBody, mPath = ret.path)
    }
}