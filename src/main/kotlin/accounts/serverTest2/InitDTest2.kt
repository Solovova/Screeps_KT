package accounts.serverTest2

import mainRoom.MainRoom
import screeps.api.ResourceConstant
import slaveRoom.SlaveRoom

fun constantMainRoomInitTest2(mainRoom: MainRoom) {
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

fun constantSlaveRoomInitTest2(slaveRoom: SlaveRoom) {
    if (slaveRoom.parent.name == "W8N3" && slaveRoom.name == "W5N3") {
        slaveRoom.need[0][11] = 2
    }
}