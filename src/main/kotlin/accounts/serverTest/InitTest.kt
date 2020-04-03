package accounts.serverTest

import mainContext.constants.Constants

fun Constants.initTestHead() {
    this.initMainRoomConstantContainer( arrayOf("W5N3") )
    this.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N2","W6N3"))                       //M0
}

fun Constants.initTestBody() {
    m(0).note = "room 0"
    s(0,1).autoBuildRoad = true
}