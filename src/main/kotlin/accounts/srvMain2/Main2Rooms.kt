package accounts.srvMain2

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom
import screeps.api.ResourceConstant

fun AccountInitMain2.initMainRoomOut(mr: MainRoom) {
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
    if (sr.mr.name == "W5N3" && sr.name == "W6N3") {
        sr.need[0][0] = 0
        sr.need[0][1] = 6
    }
}
