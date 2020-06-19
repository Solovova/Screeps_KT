package logic.balance

import mainContext.MainContext
import screeps.api.Game
import screeps.api.RESOURCE_ENERGY

class LMBalanceUpgrader(val mc:MainContext) {
    val balanceLog: LMBalanceLog = LMBalanceLog(mc)

    fun setNeedUpgrader() {
        val counterLvl3 = mc.mainRoomCollector.rooms.values.filter { it.have[19] > 0}.size
        val counterLvl2 = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 2}.sumBy{ it.have[7]}
        val qtyLvl2 = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 2}.size
        val qtyLvl3 = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 3}.size


        val mineralsNeed = (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0) - (mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0)

        val qtyUpgraderNeedLvl2 = qtyLvl2*4
        val qtyUpgraderMax = qtyUpgraderNeedLvl2 + qtyLvl3
        val qtyUpgraderNeed = balanceLog.getUpgrader(qtyUpgraderNeedLvl2, qtyUpgraderMax)

        balanceLog.saveLog(qtyUpgraderNeed, mineralsNeed)
        balanceLog.show()


        val qtyUpgraderNeedLvl3 = qtyUpgraderNeed - qtyUpgraderNeedLvl2
        var counter = counterLvl3

        mc.lm.lmMessenger.log("INFO","Glob"," Upgrader have: ($counterLvl3,$counterLvl2) ${counterLvl3 + counterLvl2} Target 2:$qtyUpgraderNeedLvl2 3:$qtyUpgraderNeedLvl3 Deficit 2:${qtyUpgraderNeedLvl2 - counterLvl2} 3:${qtyUpgraderNeedLvl3 - counterLvl3}")

        if (Game.time % 10 != 0) return
        for (room in mc.mainRoomCollector.rooms.values)
            room.constant.needUpgrader = false


        val rooms = mc.mainRoomCollector.rooms.values.filter {
                it.getLevelOfRoom() == 3
                && it.getResource() > it.constant.energyUpgradeLvl8Controller
        }.sortedByDescending { it.getResource() }



        for (room in rooms) {
            if (counter>=qtyUpgraderNeedLvl3) break
            counter++
            room.constant.needUpgrader = true
        }
    }
}