package accounts.srvMain2

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.ResourceConstant

fun AccountInitMain2.initMainRoomOut(mr: MainRoom) {
    if (mr.constant.levelOfRoom == 2) {
        mr.constant.creepUpgradeRole[7] = true
        mr.constant.creepUpgradeRole[10] = true
        mr.constant.creepUpgradeRole[101] = false
    }

    if (mr.constant.levelOfRoom == 3) {
        mr.constant.creepUpgradeRole[10] = true
        mr.constant.creepUpgradeRole[19] = true
        mr.constant.creepUpgradeRole[101] = false
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

fun AccountInitMain2.initSlaveRoomOut(sr: SlaveRoom) {
    if (sr.mr.name == "W3N5" && sr.name == "W3N6") {
        sr.need[0][0] = 1
        sr.need[0][3] = 0
        sr.need[0][1] = 6
        sr.need[0][11] = 0
    }
}
