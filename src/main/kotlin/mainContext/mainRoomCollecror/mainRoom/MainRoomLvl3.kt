package mainContext.mainRoomCollecror.mainRoom

import screeps.api.*
import screeps.api.structures.StructureContainer

fun MainRoom.needCorrection3() {
    //1 harvester ,carrier ,filler , small harvester-filler, small filler
    //1.1 harvester ,carrier

    //Defence
    val hostileCreeps = this.room.find(FIND_HOSTILE_CREEPS).filter { !it.name.startsWith("invader") }
    if (hostileCreeps.isNotEmpty()) {
        this.need[0][31] = 2
        this.need[0][5] = 2
        mc.lm.lmMessenger.log("INFO", this.name, "Attacked type player", COLOR_RED)
        return
    }


    //-----------------------------

    if (this.structureLinkNearSource.containsKey(0))
        if (this.need[1][1] == 0) this.need[1][1] = 1

    if (this.structureLinkNearSource.containsKey(1))
        if (this.need[1][3] == 0) this.need[1][3] = 1

    //1.2 filler
    if (this.need[0][5] == 0) this.need[0][5] = 1 //filler
    if (this.need[1][5] == 0) this.need[1][5] = 1 //filler

    //1.3 small filler
    if ((this.have[5] == 0) && (this.getResourceInStorage() > 2000)) this.need[0][9] = 1
    if ((this.have[5] == 0) && (this.getResourceInStorage() <= 2000)) this.need[0][0] = 2
    if (this.getResourceInStorage() == 0) this.need[0][0] = 2

    //2 Upgrader
    if ((this.getResourceInStorage() > this.constant.energyUpgradeLvl8Controller)
            && (!this.constant.defenceNeedUpgrade || ((mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0) > (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0)))
    )
            {
        this.need[1][19] = 1
    }

    //2.1 Small upgrader
    if (this.need[0][6] == 0 && this.need[1][6] == 0 && this.need[2][6] == 0 &&
            this.need[0][7] == 0 && this.need[1][7] == 0 && this.need[2][7] == 0 &&
            this.have[6] == 0 && this.have[7] == 0 && this.getTicksToDowngrade() < 50000)
        this.need[0][13] = 1

    //8 Builder
    if (this.constant.creepUseBigBuilder) {
        if ((this.constant.defenceNeedUpgrade && this.getResourceInStorage() > this.constant.energyUpgradeDefence)
                || (this.constructionSite.isNotEmpty() && this.getResourceInStorage() > this.constant.energyBuilder)
                //|| ((mc.mineralData[RESOURCE_ENERGY]?.quantity ?: 0) > (mc.mineralData[RESOURCE_ENERGY]?.need ?: 0)
                //        && this.getResourceInStorage() > this.constant.energyUpgradeDefence)
        ) {
            this.need[1][10] = 1
        }
        this.need[1][11] = this.have[10]
    } else {
        if ((this.constructionSite.isNotEmpty()) && (this.getResourceInStorage() > this.constant.energyBuilder)) {
            if (this.constructionSite.size > 2) this.need[1][8] = 2
            else this.need[1][8] = 1
        }
    }


    //14 Logist
    this.need[0][14] = 1

    //15 Mineral harvesting
    if (this.mineral.mineralAmount > 0 &&
            this.structureContainerNearMineral.size == 1
            && this.structureExtractor.size == 1) {
        if (getResource(this.mineral.mineralType) < (this.constant.mineralMaxInRoom + 50000))
            this.need[1][15] = 1
        if (getResource(this.mineral.mineralType) > this.constant.mineralMaxInRoom)
            mc.lm.lmMessenger.log("INFO", this.name, "Mineral full", COLOR_RED)
    }

    val container: StructureContainer? = this.structureContainerNearMineral[0]
    if (container != null && (container.store[this.mineral.mineralType]
                    ?: 0) > 0) this.need[1][16] = 1

    //11 cleaner
    if (this.constant.needCleaner) this.need[2][17] = 1

    //18 Lab filler
    if ((this.structureLabSort.isNotEmpty() && this.constant.reactionActive != "") ||
            this.constant.creepUpgradeRole.filter { it.value }.isNotEmpty()) {
        this.need[1][18] = 1
    }

    //Manual defence
    this.need[1][20] = this.room.find(FIND_FLAGS).filter { it.color == COLOR_PURPLE }.size
    this.need[1][21] = this.room.find(FIND_FLAGS).filter { it.color == COLOR_BLUE }.size
    this.need[1][22] = this.room.find(FIND_FLAGS).filter { it.color == COLOR_CYAN }.size
}