package accounts.srvMain2

import mainContext.constants.Constants

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("E43N34"))

    //Colonization E51N41
    const.getMainRoomConstant("E43N34").initSlaveRoomConstantContainer(arrayOf())                       //M0

}

fun AccountInitMain2.initBodyOut(const: Constants) {

}