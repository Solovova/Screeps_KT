package mainContext.dataclass

import screeps.api.IntConstant
import screeps.api.Record
import screeps.api.ResourceConstant

external val WALL_HITS_MAX : Int
external val LAB_MINERAL_CAPACITY: IntConstant
external val REACTION_TIME: Record<ResourceConstant, Int>
external val RESOURCES_ALL: Array<ResourceConstant>
external val REACTIONS: dynamic
external val BOOSTS: dynamic