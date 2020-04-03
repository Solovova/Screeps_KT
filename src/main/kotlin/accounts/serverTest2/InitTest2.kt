package accounts.serverTest2

import mainContext.constants.Constants

fun Constants.initTest2Head() {
    this.initMainRoomConstantContainer( arrayOf("W8N3") )
    this.getMainRoomConstant("W8N3").initSlaveRoomConstantContainer(arrayOf("W7N3","W8N2","W5N3"))                       //M0
}

fun Constants.initTest2Body() {
    m(0).note = "room 0"
    s(0,2).model = 1
}