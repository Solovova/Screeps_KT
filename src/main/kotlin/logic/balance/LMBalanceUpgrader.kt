package logic.balance

import mainContext.MainContext
import screeps.api.Game
import screeps.api.RESOURCE_ENERGY

class LMBalanceUpgrader(val mc:MainContext) {

    fun setNeedUpgrader() {
        val counterLvl3 = mc.mainRoomCollector.rooms.values.filter { it.have[19] > 0}.size
        val counterLvl2 = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 2}.sumBy{ it.have[7]}

        val mineralsNeed = (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0) - (mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0)
        var countOfRoomForUpgrade = 25
        if (countOfRoomForUpgrade == 0) countOfRoomForUpgrade = 1

        var counter = counterLvl3 + counterLvl2

        mc.lm.lmMessenger.log("INFO","Glob","Upgrader have: ($counterLvl3,$counterLvl2) $counter Target:$countOfRoomForUpgrade Minerals: $mineralsNeed Deficit: ${countOfRoomForUpgrade - counter}")

        if (Game.time % 10 != 0) return
        for (room in mc.mainRoomCollector.rooms.values)
            room.constant.needUpgrader = false


        val rooms = mc.mainRoomCollector.rooms.values.filter {
                it.getLevelOfRoom() == 3
                && it.getResource() > it.constant.energyUpgradeLvl8Controller
        }.sortedByDescending { it.getResource() }



        for (room in rooms) {
            if (counter>=countOfRoomForUpgrade) break
            counter++
            room.constant.needUpgrader = true
        }
    }
}