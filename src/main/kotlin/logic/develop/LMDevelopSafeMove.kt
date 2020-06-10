package logic.develop

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.*

class LMDevelopSafeMove(val mc: MainContext) {
    fun gets(fPos1: RoomPosition, fPos2: RoomPosition, inSwampCost: Int = 10, inPlainCost: Int = 2, safeRange: Int = 4): PathFinder.Path {
        fun roomCallback(roomName: String): CostMatrix {
            val room: Room = Game.rooms[roomName] ?: return PathFinder.CostMatrix()
            val costs = PathFinder.CostMatrix()
            room.find(FIND_MY_STRUCTURES).forEach { struct ->
                if (struct.structureType === STRUCTURE_ROAD) {
                    costs.set(struct.pos.x, struct.pos.y, 1)
                } else if (struct.structureType !== STRUCTURE_CONTAINER &&
                        (struct.structureType !== STRUCTURE_RAMPART)) {
                    costs.set(struct.pos.x, struct.pos.y, 0xff)
                }
            }

            room.find(FIND_STRUCTURES).forEach { struct ->
                if (struct.structureType == STRUCTURE_ROAD)
                    costs.set(struct.pos.x, struct.pos.y, 1)
            }

            room.find(FIND_CONSTRUCTION_SITES).forEach { cs ->
                if (cs.structureType == STRUCTURE_ROAD)
                    costs.set(cs.pos.x, cs.pos.y, 1)
            }

            room.find(FIND_HOSTILE_STRUCTURES).forEach { struct ->
                costs.set(struct.pos.x, struct.pos.y, 0xff)
            }

//            room.find(FIND_HOSTILE_CREEPS).forEach { enemy ->
//                for (dx in (enemy.pos.x-safeRange)..(enemy.pos.x+safeRange))
//                    for (dy in (enemy.pos.y-safeRange)..(enemy.pos.y+safeRange)) {
//                        if (dx !in 0..49 || dy !in 0..49) continue
//                        costs.set(dx, dy, 0xff)
//                    }
//            }

//            room.find(FIND_HOSTILE_STRUCTURES).filter { it.structureType == STRUCTURE_KEEPER_LAIR }.forEach { keeper ->
//                if ((keeper as StructureKeeperLair).ticksToSpawn<30) {
//                    for (dx in (keeper.pos.x-safeRange)..(keeper.pos.x+safeRange))
//                        for (dy in (keeper.pos.y-safeRange)..(keeper.pos.y+safeRange)) {
//                            if (dx !in 0..49 || dy !in 0..49) continue
//                            costs.set(dx, dy, 0xff)
//                        }
//                }
//            }

            room.find(FIND_HOSTILE_STRUCTURES).filter { it.structureType == STRUCTURE_KEEPER_LAIR }.forEach { keeper ->
                for (dx in (keeper.pos.x - safeRange)..(keeper.pos.x + safeRange))
                    for (dy in (keeper.pos.y - safeRange)..(keeper.pos.y + safeRange)) {
                        if (dx !in 0..49 || dy !in 0..49) continue
                        costs.set(dx, dy, 0xff)
                    }
            }
            return costs
        }

        val goals = GoalWithRange(
                pos = fPos2,
                range = 1
        )

        return PathFinder.search(fPos1, goals, options {
            maxOps = 5000
            maxRooms = 6
            plainCost = inPlainCost
            swampCost = inSwampCost
            roomCallback = ::roomCallback
        })
    }


    fun testSafeWay() {
        val roomTo: MainRoom = mc.mainRoomCollector.rooms["E53N35"] ?: return
        val roomFrom: SlaveRoom = roomTo.slaveRooms["E55N35"] ?: return

        println("Test way start!!")
        val posFrom: RoomPosition = roomFrom.structureContainerNearSource[0]?.pos ?: return
        val posTo: RoomPosition = roomTo.structureStorage[0]?.pos ?: return

        val safeWay = getSafeWay(posFrom, posTo)
        println("Test way ${safeWay.incomplete} len: ${safeWay.path.size}")

    }

    private fun getSafeWay(posFrom: RoomPosition, posTo: RoomPosition, safeRange: Int = 4): PathFinder.Path {
        return gets(posFrom, posTo, safeRange)
    }
}