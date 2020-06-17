package accounts.srvMain2

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N3", "W3N1", "W8N2", "W6N3", "W3N5", "W4N3"))

    //Colonization E51N41
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N4", "W5N2","W4N3")) //M0
    const.getMainRoomConstant("W3N1").initSlaveRoomConstantContainer(arrayOf("W4N1", "W3N2", "W2N1")) //M1
    const.getMainRoomConstant("W8N2").initSlaveRoomConstantContainer(arrayOf("W8N3", "W7N2")) //M2
    const.getMainRoomConstant("W6N3").initSlaveRoomConstantContainer(arrayOf("W7N3")) //M3
    const.getMainRoomConstant("W3N5").initSlaveRoomConstantContainer(arrayOf("W2N5")) //M4
    const.getMainRoomConstant("W4N3").initSlaveRoomConstantContainer(arrayOf()) //M5

}

fun AccountInitMain2.initBodyOut(const: Constants) {
    const.globalConstant.defenceLimitUpgrade = 300000

    const.s(0,2).model = SlaveRoomType.colonize
    const.m(5).creepSpawn = false

    const.m(0).reactionActiveArr = arrayOf("LH","LH2O","XLH2O","")
    const.m(1).reactionActiveArr = arrayOf("OH","LH","XLH2O","")
    const.m(2).reactionActiveArr = arrayOf("OH","LH2O","XLH2O","")
}