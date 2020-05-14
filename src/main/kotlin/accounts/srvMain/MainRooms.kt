package accounts.srvMain

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.ResourceConstant

fun AccountInitMain.initMainRoomOut(mr: MainRoom) {
    val defenceRoomLow: Array<String> = arrayOf("E52N38","E58N37","E53N38")
    val defenceRoomLowHits = 1_000_000
    val defenceRoomNormHits = 2_000_000 //Default
    val defenceRoomHi: Array<String> = arrayOf("E51N35","E52N35","E53N35","E58N43")
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

    if (mr.constant.levelOfRoom == 2) {
        mr.constant.creepUpgradeRole[7] = true
        mr.constant.creepUpgradeRole[10] = true
    }

    if (mr.constant.levelOfRoom == 3) {
        mr.constant.creepUpgradeRole[10] = true
        mr.constant.creepUpgradeRole[19] = true
    }

    if (mr.name in arrayOf("E51N35", "E52N35", "E53N35")) {
        mr.constant.defenceHits = 3000000
    }

    //ToDo auto
    if (mr.name == "E54N39") mr.needMineral["GH2O".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E52N38") mr.needMineral["XGH2O".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E52N37") mr.needMineral["L".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E54N37") mr.needMineral["O".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E58N39") mr.needMineral["H".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E59N36") mr.needMineral["Z".unsafeCast<ResourceConstant>()] = 10000
    if (mr.name == "E57N39") mr.needMineral["U".unsafeCast<ResourceConstant>()] = 10000

    //if (mr.name == "E54N39") mr.need[0][30] = 1
}

fun AccountInitMain.initSlaveRoomOut(sr: SlaveRoom) {
    if (sr.mr.name == "E55N53" && sr.name == "E55N54") {
        //sr.need[0][9] = 1
    }
//
//    if (sr.mr.name == "E56N53" && sr.name == "E55N53") {
//        sr.need[0][0] = 0
//        sr.need[0][1] = 5
//    }
//
//
//    if (sr.mr.name == "E57N51" && sr.name == "E58N51") {
//        sr.need[0][11] = 1
//    }
}
