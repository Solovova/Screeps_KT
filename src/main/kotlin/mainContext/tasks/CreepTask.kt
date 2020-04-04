package mainContext.tasks

import mainContext.dataclass.TypeOfTask
import screeps.api.RESOURCE_ENERGY
import screeps.api.ResourceConstant
import screeps.api.RoomPosition

class CreepTask {
    val type: TypeOfTask
    val idObject0: String
    val posObject0: RoomPosition?
    val idObject1: String
    val posObject1: RoomPosition?
    val resource: ResourceConstant
    val quantity: Int
    var come: Boolean
    var take: Boolean

    fun toMemory(): dynamic {
        val d: dynamic = object {}
        d["type"] = this.type.ordinal
        d["idObject0"] = this.idObject0
        d["posObject0"] = this.posObject0
        d["idObject1"] = this.idObject1
        d["posObject1"] = this.posObject1
        d["resource"] = this.resource
        d["quantity"] = this.quantity
        d["come"] = this.come
        d["take"] = this.take
        return d
    }

    constructor (type: TypeOfTask, idObject0: String, posObject0: RoomPosition? = null, idObject1: String = "", posObject1: RoomPosition? = null, resource: ResourceConstant = RESOURCE_ENERGY, quantity: Int = 0) {
        this.type = type
        this.idObject0 = idObject0
        this.posObject0 = posObject0
        this.idObject1 = idObject1
        this.posObject1 = posObject1
        this.resource = resource
        this.quantity = quantity
        this.come = false
        this.take = false
    }

    constructor(d: dynamic) {
        this.type = TypeOfTask.values()[d["type"] as Int]
        this.idObject0 = d["idObject0"] as String
        if (d["posObject0"] != null) {
            this.posObject0 = RoomPosition(d["posObject0"]["x"] as Int, d["posObject0"]["y"] as Int, d["posObject0"]["roomName"] as String)
        }else{
            this.posObject0 = null
        }

        this.idObject1 = d["idObject1"] as String
        if (d["posObject1"] != null) {
            this.posObject1 = RoomPosition(d["posObject1"]["x"] as Int, d["posObject1"]["y"] as Int, d["posObject1"]["roomName"] as String)
        }else{
            this.posObject1 = null
        }

        this.resource = d["resource"].unsafeCast<ResourceConstant>()
        this.quantity = d["quantity"] as Int
        this.come = d["come"] as Boolean
        this.take = d["take"] as Boolean


    }
}