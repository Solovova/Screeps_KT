package accounts.serverTest

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

fun constantMainRoomInitTest(mainRoom: MainRoom) {
    val controller = mainRoom.structureController[0]
    if (controller != null) {
        if (controller.level == 8) {
            mainRoom.constant.creepUseBigBuilder = true
            mainRoom.constant.defenceHits = 300000
            mainRoom.constant.energyExcessSent = 90000

        }

        if (controller.level == 7) {
            mainRoom.constant.creepUseBigBuilder = true
            mainRoom.constant.defenceHits = 300000
            mainRoom.constant.energyExcessSent = 90000
        }

        if (controller.level == 6) {
            mainRoom.constant.creepUseBigBuilder = true
            mainRoom.constant.defenceHits = 100000
            mainRoom.constant.energyExcessSent = 90000
            //mainRoom.constant.creepUpgradeRole
        }


    }
    //if (mainRoom.name == "W5N3") mainRoom.needMineral["GH2O".unsafeCast<ResourceConstant>()] = 200000  //M0

}

fun constantSlaveRoomInitTest(slaveRoom: SlaveRoom) {
    if (slaveRoom.mr.name == "W7N3" && slaveRoom.name == "W7N5") {
        slaveRoom.need[0][1] = 2
    }
}