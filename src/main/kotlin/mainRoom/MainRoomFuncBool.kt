package mainRoom

fun MainRoom.boolRunLab(): Boolean { //if true - run reactions
    if (this.constant.reactionActive == "") return false
    if (this.structureLabSort.size !in arrayOf(3, 6, 10)) return false
    if (this.getQuantityAllMineralInStorage() >= this.constant.mineralAllMaxInStorage) return false
    return true
}