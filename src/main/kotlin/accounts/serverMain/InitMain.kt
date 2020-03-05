package accounts.serverMain

import constants.Constants

//Call before init of constants
fun Constants.initMainHead() {

                                               //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    this.initMainRoomConstantContainer( arrayOf("E54N37","E59N36","E52N38","E52N37","E54N39","E51N39","E53N38","E51N37","E59N38","E58N37",
                                                "E52N36","E58N39","E57N39","E57N37","E53N39","E49N39","E47N39","E51N41","E52N35","E51N35",
                                                "E54N41","E53N35") )

    //Colonization E51N41
    this.getMainRoomConstant("E54N37").initSlaveRoomConstantContainer(arrayOf("E53N37","E54N36"))                       //M0
    this.getMainRoomConstant("E59N36").initSlaveRoomConstantContainer(arrayOf("E58N36"))                                //M1
    this.getMainRoomConstant("E52N38").initSlaveRoomConstantContainer(arrayOf())                                        //M2
    this.getMainRoomConstant("E52N37").initSlaveRoomConstantContainer(arrayOf())                                        //M3
    this.getMainRoomConstant("E54N39").initSlaveRoomConstantContainer(arrayOf("E54N38"))                                //M4
    this.getMainRoomConstant("E51N39").initSlaveRoomConstantContainer(arrayOf("E51N38"))                                //M5
    this.getMainRoomConstant("E53N38").initSlaveRoomConstantContainer(arrayOf())                                        //M6
    this.getMainRoomConstant("E51N37").initSlaveRoomConstantContainer(arrayOf())                                        //M7
    this.getMainRoomConstant("E59N38").initSlaveRoomConstantContainer(arrayOf("E59N37","E59N39"))                       //M8
    this.getMainRoomConstant("E58N37").initSlaveRoomConstantContainer(arrayOf("E58N38"))                                //M9
    this.getMainRoomConstant("E52N36").initSlaveRoomConstantContainer(arrayOf("E51N36","E53N36"))     //M10
    this.getMainRoomConstant("E58N39").initSlaveRoomConstantContainer(arrayOf())                                        //M11
    this.getMainRoomConstant("E57N39").initSlaveRoomConstantContainer(arrayOf("E56N39","E57N38","E56N38"))              //M12
    this.getMainRoomConstant("E57N37").initSlaveRoomConstantContainer(arrayOf("E57N36"))                                //M13
    this.getMainRoomConstant("E53N39").initSlaveRoomConstantContainer(arrayOf("E52N39"))                                //M14
    this.getMainRoomConstant("E49N39").initSlaveRoomConstantContainer(arrayOf("E49N38"))                                //M15
    this.getMainRoomConstant("E47N39").initSlaveRoomConstantContainer(arrayOf("E46N39","E47N38"))                       //M16
    this.getMainRoomConstant("E51N41").initSlaveRoomConstantContainer(arrayOf("E51N42","E52N41"))                       //M17
    this.getMainRoomConstant("E52N35").initSlaveRoomConstantContainer(arrayOf("E52N34"))                                //M18
    this.getMainRoomConstant("E51N35").initSlaveRoomConstantContainer(arrayOf("E51N34"))                                //M19
    this.getMainRoomConstant("E54N41").initSlaveRoomConstantContainer(arrayOf())                                        //M20
    this.getMainRoomConstant("E53N35").initSlaveRoomConstantContainer(arrayOf())                                        //M21

    //E53N32 E56N31 E54N31
}

//Call after init constants and load from memory
fun Constants.initMainBody() {

    //Far transfer E51N33 -> E52N33 wait
    //Far transfer E59N38 -> E58N39
    //E57N35

//    m(3).sentEnergyToRoom = "E54N37"
//    m(4).sentEnergyToRoom = "E54N37"
//    m(6).sentEnergyToRoom = "E54N37"

    m(0).marketBuyEnergy = true
    m(14).creepUpgradeRole[7] = true
    //s(10,2).model = 1
    //s(10,3).model = 1
//    s(18,0).autoBuildRoad = true
//    s(19,0).autoBuildRoad = true


    //m(6).creepUpgradeRole[7] = true
    //m(3).creepUpgradeRole[7] = true

    m(0).reactionActive = "GH2O"
    m(1).reactionActive = "XGH2O"
    m(2).reactionActive = "GH2O"
    m(3).reactionActive = "GH2O"
    m(4).reactionActive = "ZK"
    m(5).reactionActive = "XGH2O"
    m(6).reactionActive = "G"
    m(7).reactionActive = "GH"
    m(8).reactionActive = "OH"
    m(9).reactionActive = "GH"
    m(10).reactionActive = "OH"
    m(11).reactionActive = "OH"
    m(12).reactionActive = "XGH2O"
    m(13).reactionActive = "XGH2O"
    m(14).reactionActive = "XGH2O"
    m(15).reactionActive = "XGH2O"
}



