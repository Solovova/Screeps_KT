package logic.production.lab

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.ResourceConstant

class LMLabReactionBalance(val mc: MainContext) {
    private fun canStart(mr: MainRoom, reaction: ResourceConstant): Boolean {

        return false
    }

    private fun needStop(mr: MainRoom): Boolean {
        val reactionCompounds = mc.lm.lmProduction.labFunc.getReactionCompounds(mr.constant.reactionActive.unsafeCast<ResourceConstant>())

        return false
    }

    //Need fill minerals
    private fun balancingForRoom(mr: MainRoom) {
        if (mr.constant.reactionActive != ""
                && mr.constant.reactionActiveArr.size > 1
                && needStop(mr)) {
            mr.constant.reactionActive = ""
        }

        if (mr.constant.reactionActive == ""
                && mr.constant.reactionActiveArr.isNotEmpty()) {
            for (newReaction in mr.constant.reactionActiveArr) {
                if (newReaction == "") continue
                if (canStart(mr, newReaction.unsafeCast<ResourceConstant>())) {
                    mr.constant.reactionActive = newReaction
                    break
                }
            }
        }
    }

    fun balancing() {
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                balancingForRoom(room)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing!")
            }
        }

    }
}