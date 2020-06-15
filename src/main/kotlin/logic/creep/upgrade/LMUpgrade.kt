package logic.creep.upgrade

import mainContext.constants.GlobalConstant
import mainContext.MainContext
import mainContext.dataclass.role
import mainContext.dataclass.upgrade
import mainContext.dataclass.upgradeQuantity
import mainContext.dataclass.upgradeResource
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.BodyPartConstant
import screeps.api.Creep
import screeps.api.ResourceConstant
import screeps.api.WORK

class LMUpgrade(val mainContext: MainContext) {
    val needForUpgrade: LMUpgradeNeedFor = LMUpgradeNeedFor(mainContext)
    val setGlobalConstant: LMUpgradeConstants = LMUpgradeConstants()

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

    private fun getUpgradeParts(creep: Creep, mr: MainRoom): Map<BodyPartConstant, ResourceConstant>? {
        var upgradeParts = mr.constant.creepUpgradableParts[creep.memory.role] //Upgrade parts can be other in every room
        if (upgradeParts == null) upgradeParts = mr.mc.constants.globalConstant.creepUpgradableParts[creep.memory.role]
        return upgradeParts
    }

    fun creepSetLogic(creep: Creep, mr: MainRoom) {
        if (creep.spawning && creep.memory.upgrade == "") {
            if (mr.constant.creepUpgradeRole[creep.memory.role] == true) {
                val upgradeParts = this.getUpgradeParts(creep, mr)
                if (upgradeParts != null) {
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