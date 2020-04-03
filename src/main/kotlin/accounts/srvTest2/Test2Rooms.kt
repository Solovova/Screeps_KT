package accounts.srvTest2

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

fun AccountInitTest2.initMainRoomOut(mr: MainRoom) {
    val controller = mr.structureController[0]
    if (controller != null) {
        if (controller.level == 8) {
            mr.constant.creepUseBigBuilder = true
            mr.constant.defenceHits = 300000
            mr.constant.energyExcessSent = 90000

        }

        if (controller.level == 7) {
            mr.constant.creepUseBigBuilder = true
            mr.constant.defenceHits = 300000
            mr.constant.energyExcessSent = 90000
        }

        if (controller.level == 6) {
            mr.constant.creepUseBigBuilder = true
            mr.constant.defenceHits = 100000
            mr.constant.energyExcessSent = 90000
            //mainRoom.constant.creepUpgradeRole
        }


    }
}

fun AccountInitTest2.initSlaveRoomOut(sr: SlaveRoom) {
    if (sr.mr.name == "W8N3" && sr.name == "W5N3") {
        sr.need[0][11] = 2
    }
}