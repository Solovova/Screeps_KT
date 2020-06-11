package mainContext.mainRoomCollecror.mainRoom.slaveRoom

import constants.CacheCarrier
import screeps.api.*

fun SlaveRoom.correctionDangeon() {
    if (this.room != null) {
        val towerInvader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_TOWER }
        if (towerInvader != null) {
            this.constant.roomHostileType = 4
            this.constant.roomHostileNum = 5
            this.constant.roomHostile = true
        } else {
            val invader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_INVADER_CORE }
            if (invader != null) {
                this.constant.roomHostileType = 3
                this.constant.roomHostileNum = 1
                this.constant.roomHostile = true
            } else {
                val hostileCreeps = this.room.find(FIND_HOSTILE_CREEPS).filter { it.name.startsWith("invader") }
                this.constant.roomHostile = hostileCreeps.isNotEmpty()
                var typeAttack = 2 //ranged
                for (hostileCreep in hostileCreeps)
                    if (hostileCreep.body.firstOrNull { it.type == ATTACK } != null) {
                        typeAttack = 1
                        break
                    }
                this.constant.roomHostileType = typeAttack
                this.constant.roomHostileNum = hostileCreeps.size
            }
        }
    }

    //5 Defender
    if (this.constant.roomHostile) {
        mc.lm.lmMessenger.log("INFO", this.name, "Attacked type: ${this.constant.roomHostileType} num:${this.constant.roomHostileNum}", COLOR_RED)
        if (this.constant.roomHostileType == 4) {
            this.need[0][4] = 1
            //this.need[1][15] = 2
        } else {
            if (this.constant.roomHostileNum > 1) {
                if (this.room == null) this.need[0][4] = 1
            }
        }
//                    else {
//                        if (this.constant.roomHostileType == 2) this.need[1][10] = 1
//                        else this.need[1][11] = 1
//                    }

        return
    }


    this.need[0][4] = 1
    this.need[1][15] = 1

    if (this.constructionSite.size>10) {
        this.need[1][9] = 2
    }

    if (this.source.containsKey(0) && this.rescueFlag.containsKey(0)) this.need[1][20] = 1
    val carrierAuto0: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)
    if (carrierAuto0 != null) {
        if (this.need[1][21] == 0) this.need[1][21] = carrierAuto0.needCarriers
    }

    if (this.source.containsKey(1) && this.rescueFlag.containsKey(1)) this.need[1][22] = 1
    val carrierAuto1: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)
    if (carrierAuto1 != null) {
        if (this.need[1][23] == 0) this.need[1][23] = carrierAuto1.needCarriers
    }

    if (this.source.containsKey(2) && this.rescueFlag.containsKey(2)) this.need[1][24] = 1
    val carrierAuto2: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer2", this.mr, this)
    if (carrierAuto2 != null) {
        if (this.need[1][25] == 0) this.need[1][25] = carrierAuto2.needCarriers
    }

    //Mineral
    val mineral = this.mineral[0]
    if (mineral != null) {
        if (mineral.mineralAmount > 0) {
            if (mr.getResource(mineral.mineralType) < mr.constant.mineralMaxInRoom)
                this.need[1][26] = 1
            else mc.lm.lmMessenger.log("INFO", this.name, "Mineral full in parent room", COLOR_RED)
        }

        this.need[1][27] = this.have[26]
    }
}
