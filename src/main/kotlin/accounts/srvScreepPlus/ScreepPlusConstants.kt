package accounts.srvScreepPlus

import mainContext.constants.Constants

fun AccountInitScreepPlus.initHeadOut(const: Constants) {
    const.initMainRoomConstantContainer( arrayOf("W3N4") )
    //const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N2","W6N3"))
}

fun AccountInitScreepPlus.initBodyOut(const: Constants) {
    //const.m(0).note = "room 0"
    //const.s(0,1).autoBuildRoad = true
}