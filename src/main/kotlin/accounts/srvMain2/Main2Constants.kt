package accounts.srvMain2

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N3")) //"W3N1","W4N3"

    //Colonization E51N41
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf()) //M0

}

fun AccountInitMain2.initBodyOut(const: Constants) {
//    const.globalConstant.defenceLimitUpgrade = 50_000
    //const.s(2,0).autoBuildRoad = true
    //const.s(2,1).autoBuildRoad = true
    //const.m(2).energyBuilder = 10000

    //const.s(1,3).model = SlaveRoomType.colonize
    //const.s(1,3).pathToRoom = arrayOf("W2N5","W2N4","W1N4","W0N4","W0N3","W0N2","W1N2","W2N2","W2N1","W3N1")

}