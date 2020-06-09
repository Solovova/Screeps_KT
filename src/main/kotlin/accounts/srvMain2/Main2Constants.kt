package accounts.srvMain2

import mainContext.constants.Constants

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N7","W2N5")) //"W3N1","W4N3"

    //Colonization E51N41
    const.getMainRoomConstant("W5N7").initSlaveRoomConstantContainer(arrayOf("W4N7","W5N8")) //M0
    const.getMainRoomConstant("W2N5").initSlaveRoomConstantContainer(arrayOf("W3N5","W2N4","W2N6")) //M1

}

fun AccountInitMain2.initBodyOut(const: Constants) {
    const.s(1,2).autoBuildRoad = true
    const.m(1).energyBuilder = 10000
}