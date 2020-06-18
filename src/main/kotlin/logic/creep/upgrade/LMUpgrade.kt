package logic.creep.upgrade

import mainContext.MainContext
import mainContext.dataclass.role
import mainContext.dataclass.upgrade
import mainContext.dataclass.upgradeQuantity
import mainContext.dataclass.upgradeResource
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.ResourceConstant

class LMUpgrade(val mc: MainContext) {
    val needForUpgrade: LMUpgradeNeedFor = LMUpgradeNeedFor(mc)

    fun setMainRoomUpgradeConstants(mr: MainRoom) {
        if (mr.constant.levelOfRoom == 2) {
            mr.constant.creepUpgradeRole[7] = true
            mr.constant.creepUpgradeRole[10] = true
        }

        if (mr.constant.levelOfRoom == 3) {
            mr.constant.creepUpgradeRole[10] = true
            mr.constant.creepUpgradeRole[19] = true
            mr.constant.creepUpgradeRole[101] = true
        }
    }

    fun creepSetLogic(creep: Creep, mr: MainRoom) {
        if (creep.spawning && creep.memory.upgrade == "") {
            if (mr.constant.creepUpgradeRole[creep.memory.role] == true) {
                val upgradeParts =  mc.lm.lmCreep.lmUpgrade.needForUpgrade.getUpgradeParts(creep)
                if (upgradeParts.isNotEmpty()) {
                    for (upgradePart in upgradeParts) {
                        val quantityParts: Int = creep.body.filter { it.type == upgradePart.key }.size
                        if (quantityParts != 0) {
                            creep.memory.upgradeResource = upgradePart.value.unsafeCast<String>()
                            creep.memory.upgradeQuantity = quantityParts * 30
                            creep.memory.upgrade = "w"
                            break
                        }
                    }
                } else creep.memory.upgrade = "u"
            } else creep.memory.upgrade = "u"
        }

        if (creep.memory.upgrade == "w" && mr.creepNeedUpgradeID == "") {
            mr.creepNeedUpgradeID = creep.id
            mr.creepNeedUpgradeResource = creep.memory.upgradeResource.unsafeCast<ResourceConstant>()
            mr.creepNeedUpgradeResourceQuantity = creep.memory.upgradeQuantity
        }
    }
}