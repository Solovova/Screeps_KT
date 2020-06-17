package accounts.srvMain2

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N3", "W3N1", "W8N2", "W6N3", "W3N5"))

    //Colonization E51N41
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N4", "W5N2")) //M0
    const.getMainRoomConstant("W3N1").initSlaveRoomConstantContainer(arrayOf("W4N1", "W3N2", "W2N1","W3N5")) //M1
    const.getMainRoomConstant("W8N2").initSlaveRoomConstantContainer(arrayOf("W8N3", "W7N2")) //M2
    const.getMainRoomConstant("W6N3").initSlaveRoomConstantContainer(arrayOf("W7N3")) //M3
    const.getMainRoomConstant("W3N5").initSlaveRoomConstantContainer(arrayOf()) //M4

}

fun AccountInitMain2.initBodyOut(const: Constants) {
    const.m(4).creepSpawn = false
    const.s(1,3).model = SlaveRoomType.colonize
    const.s(1,3).pathToRoom = arrayOf("W3N1","W3N2","W3N3","W2N3","W2N4","W2N5","W3N5")


    const.globalConstant.defenceLimitUpgrade = 30000

    const.m(0).reactionActiveArr = arrayOf("LH","LH2O","XLH2O","")
    const.m(1).reactionActiveArr = arrayOf("OH","LH","XLH2O","")
    const.m(2).reactionActiveArr = arrayOf("OH","LH2O","XLH2O","")
}