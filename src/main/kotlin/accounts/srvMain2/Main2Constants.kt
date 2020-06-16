package accounts.srvMain2

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N3","W3N1","W8N2")) //"W3N1","W4N3"

    //Colonization E51N41
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N4","W5N2")) //M0
    const.getMainRoomConstant("W3N1").initSlaveRoomConstantContainer(arrayOf("W4N1","W3N2","W2N1")) //M1
    const.getMainRoomConstant("W8N2").initSlaveRoomConstantContainer(arrayOf("W8N3","W7N2")) //M2

}

fun AccountInitMain2.initBodyOut(const: Constants) {
    //const.s(2,0).autoBuildRoad = true
    //const.s(2,1).autoBuildRoad = true
    //const.m(2).energyBuilder = 10000
    //const.s(0,2).model = SlaveRoomType.colonize
    //const.s(0,2).pathToRoom = arrayOf("W5N3","W6N3","W7N3","W8N3","W8N2")

//    const.globalConstant.defenceLimitUpgrade = 50_000
    const.m(0).reactionActive = ""
}