package accounts.srvTest2

import mainContext.constants.Constants

fun AccountInitTest2.initHeadOut(const: Constants) {
    const.initMainRoomConstantContainer( arrayOf("W8N3") )
    const.getMainRoomConstant("W8N3").initSlaveRoomConstantContainer(arrayOf("W7N3","W8N2","W5N3"))
}

fun AccountInitTest2.initBodyOut(const: Constants) {
    const.m(0).note = "room 0"
    const.s(0,2).model = 1
}