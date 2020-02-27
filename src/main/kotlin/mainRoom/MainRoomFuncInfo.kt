package mainRoom

import screeps.api.RESOURCE_ENERGY

fun MainRoom.getQuantityAllMineralInStorage(): Int {
    return this.resStorage.filter { it.key != RESOURCE_ENERGY }.values.sum()
}