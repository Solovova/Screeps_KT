package accounts.main

fun AccountInitMain.initHeadOut() {

    this.constants.initMainRoomConstantContainer( arrayOf("E54N37","E59N36","E52N38","E52N37","E54N39","E51N39","E53N38","E51N37","E59N38","E58N37",
            "E52N36","E58N39","E57N39","E57N37","E53N39","E49N39","E47N39","E51N41","E52N35","E51N35",
            "E54N41","E53N35","E58N43","E58N44","E58N45","E59N46","E57N51") )


    this.constants.getMainRoomConstant("E54N37").initSlaveRoomConstantContainer(arrayOf("E53N37","E54N36"))                       //M0
    this.constants.getMainRoomConstant("E59N36").initSlaveRoomConstantContainer(arrayOf("E58N36"))                                //M1
    this.constants.getMainRoomConstant("E52N38").initSlaveRoomConstantContainer(arrayOf())                                        //M2
    this.constants.getMainRoomConstant("E52N37").initSlaveRoomConstantContainer(arrayOf())                                        //M3
    this.constants.getMainRoomConstant("E54N39").initSlaveRoomConstantContainer(arrayOf("E54N38"))                                //M4
    this.constants.getMainRoomConstant("E51N39").initSlaveRoomConstantContainer(arrayOf("E51N38"))                                //M5
    this.constants.getMainRoomConstant("E53N38").initSlaveRoomConstantContainer(arrayOf())                                        //M6
    this.constants.getMainRoomConstant("E51N37").initSlaveRoomConstantContainer(arrayOf())                                        //M7
    this.constants.getMainRoomConstant("E59N38").initSlaveRoomConstantContainer(arrayOf("E59N37","E59N39"))                       //M8
    this.constants.getMainRoomConstant("E58N37").initSlaveRoomConstantContainer(arrayOf("E58N38"))                                //M9
    this.constants.getMainRoomConstant("E52N36").initSlaveRoomConstantContainer(arrayOf("E51N36","E53N36"))     //M10
    this.constants.getMainRoomConstant("E58N39").initSlaveRoomConstantContainer(arrayOf())                                        //M11
    this.constants.getMainRoomConstant("E57N39").initSlaveRoomConstantContainer(arrayOf("E56N39","E57N38","E56N38"))              //M12
    this.constants.getMainRoomConstant("E57N37").initSlaveRoomConstantContainer(arrayOf("E57N36"))                                //M13
    this.constants.getMainRoomConstant("E53N39").initSlaveRoomConstantContainer(arrayOf("E52N39"))                                //M14
    this.constants.getMainRoomConstant("E49N39").initSlaveRoomConstantContainer(arrayOf("E49N38"))                                //M15
    this.constants.getMainRoomConstant("E47N39").initSlaveRoomConstantContainer(arrayOf("E46N39","E47N38"))                       //M16
    this.constants.getMainRoomConstant("E51N41").initSlaveRoomConstantContainer(arrayOf("E51N42","E52N41"))                       //M17
    this.constants.getMainRoomConstant("E52N35").initSlaveRoomConstantContainer(arrayOf())                                //M18
    this.constants.getMainRoomConstant("E51N35").initSlaveRoomConstantContainer(arrayOf())                                //M19
    this.constants.getMainRoomConstant("E54N41").initSlaveRoomConstantContainer(arrayOf())                                        //M20
    this.constants.getMainRoomConstant("E53N35").initSlaveRoomConstantContainer(arrayOf("E54N35"))                                        //M21
    this.constants.getMainRoomConstant("E58N43").initSlaveRoomConstantContainer(arrayOf("E57N43","E59N43"))   //M22
    this.constants.getMainRoomConstant("E58N44").initSlaveRoomConstantContainer(arrayOf("E57N44"))                                        //M23
    this.constants.getMainRoomConstant("E58N45").initSlaveRoomConstantContainer(arrayOf("E59N45"))                                        //M24
    this.constants.getMainRoomConstant("E59N46").initSlaveRoomConstantContainer(arrayOf()) //M25
    this.constants.getMainRoomConstant("E57N51").initSlaveRoomConstantContainer(arrayOf("E57N52","E58N51")) //M26 //"E58N51"
}