package accounts.srvTest

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

fun AccountInitTest.initMainRoomOut(mr: MainRoom) {
    val defenceRoomLow: Array<String> = arrayOf()
    val defenceRoomLowHits = 1_000_000
    val defenceRoomNormHits = 2_000_000 //Default
    val defenceRoomHi: Array<String> = arrayOf()
    val defenceRoomHiHits = 3_000_000

    if (mr.name in defenceRoomLow) {
        mr.constant.defenceHits = defenceRoomLowHits
    } else {
        if (mr.name in defenceRoomHi) {
            mr.constant.defenceHits = defenceRoomHiHits
        }else{
            mr.constant.defenceHits = defenceRoomNormHits
        }
    }

    mr.mc.lm.lmCreep.lmUpgrade.setMainRoomUpgradeConstants(mr)
}

fun AccountInitTest.initSlaveRoomOut(sr: SlaveRoom) {
    if (sr.mr.name == "W7N3" && sr.name == "W7N5") {
        sr.need[0][1] = 2
    }
}