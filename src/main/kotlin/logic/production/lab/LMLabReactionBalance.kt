package logic.production.lab

import mainContext.MainContext

class LMLabReactionBalance(val mc: MainContext) {
    private val labBalancingType1: LMLabReactionBalanceType1 = LMLabReactionBalanceType1(mc)
    private val labBalancingType2: LMLabReactionBalanceType2 = LMLabReactionBalanceType2(mc)

    fun balancing() {
        //One per tick

        //Type 1 balancing not interrupt reaction
        //Stop reaction only if don't have resource for reaction or mineral quantity > balancingStop
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType <= 2) {
                    if (labBalancingType1.balancingStopForRoom(room)) return
                }
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 1!")
            }
        }

        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType <= 2
                                && room.constant.reactionActive == "") {
                    if (labBalancingType1.balancingStartForRoom(room)) return
                }
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 2!")
            }
        }

        //Type 2 balancing can interrupt reaction
        //Stop reaction if need start reaction with big priority
        for (room in mc.mainRoomCollector.rooms.values) {
            try {
                if (room.constant.reactionActiveBalancingType == 2) {
                    if (labBalancingType2.balancingStartForRoom(room)) return
                }
            } catch (e: Exception) {
                mc.lm.lmMessenger.log("ERROR", room.name, "Error in balancing 3!")
            }
        }
    }
}