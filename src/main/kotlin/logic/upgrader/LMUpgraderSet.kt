package logic.upgrader

import mainContext.MainContext
import screeps.api.*

class LMUpgraderSet(val mc:MainContext) {
    fun calculate() {
        var counter = mc.mainRoomCollector.rooms.values.filter { it.have[19] > 0}.size
        val counterLvl2 = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 2}.sumBy{ it.have[7]}

        val mineralsNeed = (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0) - (mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0)
        var countOfRoomForUpgrade = 25
        if (countOfRoomForUpgrade == 0) countOfRoomForUpgrade = 1

        //Balancing
        if (Game.time % 1500 == 0) {
            val arrMinerals: Array<Int> = if (Memory["balancingMineral"] == null) arrayOf()
            else (Memory["balancingMineral"] as Array<Int>)

            val arrUpgrader: Array<Int> = if (Memory["balancingUpgrader"] == null) arrayOf()
            else (Memory["balancingUpgrader"] as Array<Int>)

            arrMinerals[arrMinerals.size] = mineralsNeed
            arrUpgrader[arrUpgrader.size] = countOfRoomForUpgrade

            Memory["balancingMineral"] = arrMinerals
            Memory["balancingUpgrader"] = arrUpgrader
        }

        mc.lm.lmMessenger.log("INFO","Glob","Upgrader QTY lvl3: $counter lvl2: $counterLvl2 Target:$countOfRoomForUpgrade Minerals: $mineralsNeed")
        if (Game.time % 10 != 0) return
        counter += counterLvl2


        val rooms = mc.mainRoomCollector.rooms.values.filter { it.getLevelOfRoom() == 3
                && it.getResource() > it.constant.energyUpgradeLvl8Controller
                && (it.have[19] == 0)
        }.sortedByDescending { it.getResource() }



        for (room in mc.mainRoomCollector.rooms.values) room.constant.useUpgraderLvl8 = false

        for (room in rooms) {
            if (counter>=countOfRoomForUpgrade) break
            counter++
            room.constant.useUpgraderLvl8 = true
        }
    }
}