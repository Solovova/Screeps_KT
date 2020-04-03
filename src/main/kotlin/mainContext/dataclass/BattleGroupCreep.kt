package mainContext.dataclass

import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.RoomPosition

data class BattleGroupCreep(var creep: Creep? = null,
                            var role: Int = 0,
                            var pos: RoomPosition? = null,
                            var body: Array<BodyPartConstant> = arrayOf(),
                            var upgrade: String = "",
                            var spawnID: String = "",
                            var posChainMove: RoomPosition? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as BattleGroupCreep

        if (creep != other.creep) return false
        if (role != other.role) return false
        if (pos != other.pos) return false
        if (posChainMove != other.posChainMove) return false
        if (spawnID != other.spawnID) return false
        if (!body.contentEquals(other.body)) return false
        if (upgrade != other.upgrade) return false

        return true
    }

    override fun hashCode(): Int {
        var result = creep?.hashCode() ?: 0
        result = 31 * result + role
        result = 31 * result + (pos?.hashCode() ?: 0)
        result = 31 * result + body.contentHashCode()
        result = 31 * result + upgrade.hashCode()
        result = 31 * result + spawnID.hashCode()
        result = 31 * result + (posChainMove?.hashCode() ?: 0)
        return result
    }
}