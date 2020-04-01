package accounts.serverMain

import mainRoom.MainRoom
import screeps.api.ResourceConstant
import slaveRoom.SlaveRoom

fun constantMainRoomInitMain(mainRoom: MainRoom) {
    val defenceRoomLow: Array<String> = arrayOf("E52N38","E58N37","E53N38")
    val defenceRoomLowHits = 500000
    val defenceRoomNormHits = 1000000 //Default
    val defenceRoomHi: Array<String> = arrayOf("E51N35","E52N35","E53N35","E58N43")
    val defenceRoomHiHits = 2000000

    if (mainRoom.name in defenceRoomLow) {
        mainRoom.constant.defenceHits = defenceRoomLowHits
    } else {
        if (mainRoom.name in defenceRoomHi) {
            mainRoom.constant.defenceHits = defenceRoomHiHits
        }else{
            mainRoom.constant.defenceHits = defenceRoomNormHits
        }
    }

    if (mainRoom.constant.levelOfRoom == 2) {
        mainRoom.constant.creepUpgradeRole[7] = true
        mainRoom.constant.creepUpgradeRole[10] = true
    }

    if (mainRoom.constant.levelOfRoom == 3) {
        mainRoom.constant.creepUpgradeRole[10] = true
        mainRoom.constant.creepUpgradeRole[19] = true
    }

    if (mainRoom.name in arrayOf("E51N35", "E52N35", "E53N35")) {
        mainRoom.constant.defenceHits = 3000000
    }

//    if (mainRoom.name == "E57N34") mainRoom.constant.energyExcessSent = 220000
//    if (mainRoom.name == "E57N35") mainRoom.constant.energyExcessSent = 220000
//    if (mainRoom.name == "E52N33") mainRoom.constant.energyExcessSent = 220000
//    if (mainRoom.name == "E58N31") mainRoom.constant.energyExcessSent = 220000


    if (mainRoom.name == "E54N39") mainRoom.needMineral["G".unsafeCast<ResourceConstant>()] = 10000  //M6
    if (mainRoom.name == "E54N39") mainRoom.needMineral["GH2O".unsafeCast<ResourceConstant>()] = 10000  //M6
    if (mainRoom.name == "E52N38") mainRoom.needMineral["XGH2O".unsafeCast<ResourceConstant>()] = 10000  //M3
    if (mainRoom.name == "E52N37") mainRoom.needMineral["L".unsafeCast<ResourceConstant>()] = 10000  //M3
    if (mainRoom.name == "E54N37") mainRoom.needMineral["O".unsafeCast<ResourceConstant>()] = 10000  //M3
    if (mainRoom.name == "E58N39") mainRoom.needMineral["H".unsafeCast<ResourceConstant>()] = 10000  //M3
    if (mainRoom.name == "E59N36") mainRoom.needMineral["Z".unsafeCast<ResourceConstant>()] = 10000
    if (mainRoom.name == "E57N39") mainRoom.needMineral["U".unsafeCast<ResourceConstant>()] = 10000

    //if(mainRoom.name == "E54N39") mainRoom.constant.defenceHits = 4000000
}

fun constantSlaveRoomInitMain(slaveRoom: SlaveRoom) {


    if (slaveRoom.parent.name == "E57N51" && slaveRoom.name == "E55N51") {
        slaveRoom.need[0][0] = 0
        slaveRoom.need[0][1] = 6
    }


    if (slaveRoom.parent.name == "E57N51" && slaveRoom.name == "E58N51") {
        slaveRoom.need[0][11] = 1
    }
}