package logic.production.lab

import mainContext.MainContext

class LMLabReactionBalance(val mc: MainContext) {
    private val labBalancingType1: LMLabReactionBalanceType1 = LMLabReactionBalanceType1(mc)
    private val labBalancingType2: LMLabReactionBalanceType2 = LMLabReactionBalanceType2(mc)

    fun balancing() {
        //Type 1 balancing not interrupt reaction
        //Stop reaction only if don't have resource for reaction or mineral quantity > balancingStop
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType <= 2) {
                    labBalancingType1.balancingStopForRoom(room)
                }
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 1!")
            }
        }

        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType <= 2
                                && room.constant.reactionActive == "") labBalancingType1.balancingStartForRoom(room)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 2!")
            }
        }

        //Type 2 balancing can interrupt reaction
        //Stop reaction if need start reaction with big priority
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType == 2) labBalancingType2.balancingStartForRoom(room)
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 3!")
            }
        }
    }
}