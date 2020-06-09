package mainContext.mainRoomCollecror.mainRoom.slaveRoom

import constants.CacheCarrier
import screeps.api.*
import screeps.api.structures.StructureController

fun SlaveRoom.correctionNormal() {
    if (this.room != null) {
        val towerInvader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_TOWER }
        if (towerInvader != null && towerInvader.isActive()) {
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
                val hostileCreeps = this.room.find(FIND_HOSTILE_CREEPS)
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
        mc.lm.lmMessenger.log("INFO", this.name, "Attacked tpe: ${this.constant.roomHostileType} num:${this.constant.roomHostileNum}", COLOR_RED)
        if (this.constant.roomHostileType == 4) {
            this.need[1][11] = 2
        } else {
            if (this.constant.roomHostileNum > 2) {
                if (this.room == null) this.need[0][4] = 1
            } else {
                if (this.constant.roomHostileType == 2) this.need[1][10] = this.constant.roomHostileNum
                else this.need[1][11] = this.constant.roomHostileNum
            }
        }
        return
    }

    //Reservation not my
    val protectedStructureController: StructureController? = this.structureController[0]
    if (protectedStructureController != null) {
        val reservation = protectedStructureController.reservation
        if (reservation != null && reservation.username == "Invader") {
            this.need[0][4] = 1
            return
        }
    }


    //
    if (this.room != null) {
        if (this.room.find(FIND_STRUCTURES).any { it.structureType == STRUCTURE_POWER_BANK }) return
        if (this.room.find(FIND_HOSTILE_CREEPS).isNotEmpty()) return
    }


    //1 Explorer 104
    if (this.room == null) {
        this.need[0][4] = 1
    }

    //2 Reserve 103
    if (protectedStructureController != null) {
        val reservation = protectedStructureController.reservation
        if (reservation != null && reservation.ticksToEnd < 2200) {
            this.need[0][3] = 1
        }
        if (reservation == null) this.need[0][3] = 1
    }

    //3 Harvester 105
    if (this.source[0] != null) this.need[0][5] = 1
    if (this.source[1] != null) this.need[0][7] = 1

    //4 Carrier
    val carrierAuto0: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)
    if (carrierAuto0 != null) {
        if (this.need[1][6] == 0) this.need[1][6] = carrierAuto0.needCarriers
    }

    val carrierAuto1: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)
    if (carrierAuto1 != null) {
        if (this.need[1][8] == 0) this.need[1][8] = carrierAuto1.needCarriers
    }

    //4 Builder 109
    if (this.constructionSite.size > 2 && this.need[1][9] == 0 &&
            this.structureContainerNearSource.size == this.source.size) this.need[1][9] = 2

}