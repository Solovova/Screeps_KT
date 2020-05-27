package accounts.srvTest

import mainContext.constants.Constants

fun AccountInitTest.initHeadOut(const: Constants) {
    const.initMainRoomConstantContainer( arrayOf("W5N3") )
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W4N3"))
}

fun AccountInitTest.initBodyOut(const: Constants) {
    const.m(0).note = "room 0"
    const.s(0,0).autoBuildRoad = true
}