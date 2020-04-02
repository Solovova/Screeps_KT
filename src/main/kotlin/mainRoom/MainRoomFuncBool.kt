package mainRoom

import logic.production.mineral.runReaction

fun MainRoom.boolRunLab(): Boolean { //if true - run reactions
    if (this.constant.reactionActive == "") return false
    if (this.structureLabSort.size !in arrayOf(3, 6, 10)) return false
    if (this.getQuantityAllMineralInStorage() >= this.constant.mineralAllMaxInStorage) return false
    if (!this.mainRoomCollector.mainContext.lm.lmProduction.lmMineral.runReaction(this.constant.reactionActive)) return false
    return true
}