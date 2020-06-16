package accounts.srvMain

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType

fun AccountInitMain.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("E54N37", "E59N36", "E52N38", "E52N37", "E54N39", "E51N39", "E53N38", "E51N37", "E59N38", "E58N37",
            "E52N36", "E58N39", "E57N39", "E57N37", "E53N39", "E49N39", "E47N39", "E51N41", "E52N35", "E51N35",
            "E54N41", "E53N35", "E58N43", "E58N44", "E58N45", "E59N46", "E57N51", "E55N51", "E56N53", "E55N53", "E59N52","E57N55"))

    //Colonization E51N41
    const.getMainRoomConstant("E54N37").initSlaveRoomConstantContainer(arrayOf("E53N37", "E54N36"))                       //M0
    const.getMainRoomConstant("E59N36").initSlaveRoomConstantContainer(arrayOf("E58N36"))                                //M1
    const.getMainRoomConstant("E52N38").initSlaveRoomConstantContainer(arrayOf())                                        //M2
    const.getMainRoomConstant("E52N37").initSlaveRoomConstantContainer(arrayOf())                                        //M3
    const.getMainRoomConstant("E54N39").initSlaveRoomConstantContainer(arrayOf("E54N38"))                                //M4
    const.getMainRoomConstant("E51N39").initSlaveRoomConstantContainer(arrayOf("E51N38"))                                //M5
    const.getMainRoomConstant("E53N38").initSlaveRoomConstantContainer(arrayOf())                                        //M6
    const.getMainRoomConstant("E51N37").initSlaveRoomConstantContainer(arrayOf())                                        //M7
    const.getMainRoomConstant("E59N38").initSlaveRoomConstantContainer(arrayOf("E59N37", "E59N39"))                       //M8
    const.getMainRoomConstant("E58N37").initSlaveRoomConstantContainer(arrayOf("E58N38"))                                //M9
    const.getMainRoomConstant("E52N36").initSlaveRoomConstantContainer(arrayOf("E51N36", "E53N36"))     //M10
    const.getMainRoomConstant("E58N39").initSlaveRoomConstantContainer(arrayOf())                                        //M11
    const.getMainRoomConstant("E57N39").initSlaveRoomConstantContainer(arrayOf("E56N39", "E57N38", "E56N38"))              //M12
    const.getMainRoomConstant("E57N37").initSlaveRoomConstantContainer(arrayOf("E57N36"))                                //M13
    const.getMainRoomConstant("E53N39").initSlaveRoomConstantContainer(arrayOf("E52N39"))                                //M14
    const.getMainRoomConstant("E49N39").initSlaveRoomConstantContainer(arrayOf("E49N38"))                                //M15
    const.getMainRoomConstant("E47N39").initSlaveRoomConstantContainer(arrayOf("E46N39", "E47N38"))                       //M16
    const.getMainRoomConstant("E51N41").initSlaveRoomConstantContainer(arrayOf("E51N42", "E52N41"))                       //M17
    const.getMainRoomConstant("E52N35").initSlaveRoomConstantContainer(arrayOf())                                //M18
    const.getMainRoomConstant("E51N35").initSlaveRoomConstantContainer(arrayOf())                                //M19
    const.getMainRoomConstant("E54N41").initSlaveRoomConstantContainer(arrayOf("E55N41"))                                        //M20
    const.getMainRoomConstant("E53N35").initSlaveRoomConstantContainer(arrayOf("E54N35","E55N35"))                                        //M21
    const.getMainRoomConstant("E58N43").initSlaveRoomConstantContainer(arrayOf("E57N43", "E59N43"))   //M22
    const.getMainRoomConstant("E58N44").initSlaveRoomConstantContainer(arrayOf("E57N44"))                                        //M23
    const.getMainRoomConstant("E58N45").initSlaveRoomConstantContainer(arrayOf("E59N45"))                                        //M24
    const.getMainRoomConstant("E59N46").initSlaveRoomConstantContainer(arrayOf()) //M25
    const.getMainRoomConstant("E57N51").initSlaveRoomConstantContainer(arrayOf("E57N52", "E58N51")) //M26
    const.getMainRoomConstant("E55N51").initSlaveRoomConstantContainer(arrayOf("E56N51")) //M27
    const.getMainRoomConstant("E56N53").initSlaveRoomConstantContainer(arrayOf("E56N52", "E57N53", "E56N54")) //28
    const.getMainRoomConstant("E55N53").initSlaveRoomConstantContainer(arrayOf("E55N52","E55N54")) //29
    const.getMainRoomConstant("E59N52").initSlaveRoomConstantContainer(arrayOf()) //30
    const.getMainRoomConstant("E57N55").initSlaveRoomConstantContainer(arrayOf()) //31

}

fun AccountInitMain.initBodyOut(const: Constants) {
    const.globalConstant.nukerFilInRooms = arrayOf("E54N37","E59N36","E52N38","E52N37","E54N39","E53N38","E51N39","E51N37",
    "E59N38","E58N37","E52N36","E58N39","E57N39","E57N37","E53N39","E49N39")
    const.s(21,1).model = SlaveRoomType.central
    const.s(21,1).checkpointRoom = "E54N35"
//    const.m(31).creepSpawn = false
//    const.s(28,3).model = 1
//    const.s(28,3).pathUseGlobalGuideFlag=true
//    const.s(28,3).pathToRoom= arrayOf("E56N53","E56N54","E56N55","E57N55")

    const.m(0).reactionActive = "GH2O"
    const.m(1).reactionActive = "GH"
    const.m(2).reactionActive = "GH2O"
    const.m(3).reactionActive = "GH2O"
    //const.m(4).reactionActive = "ZK"
    const.m(4).reactionActiveArr = arrayOf("ZK", "XGH2O")
    const.m(5).reactionActive = "OH"
    const.m(6).reactionActive = "OH"
    //const.m(7).reactionActive = "G"
    const.m(7).reactionActiveArr = arrayOf("G", "XGH2O")
    const.m(8).reactionActive = "OH"
    const.m(9).reactionActive = "GH"
    const.m(10).reactionActive = "OH"
    const.m(11).reactionActive = "OH"
    const.m(12).reactionActive = "OH"
    const.m(13).reactionActive = "ZK"
    const.m(14).reactionActive = "UL"
    const.m(15).reactionActive = "G"
    //const.m(16).reactionActive = "UL"
    const.m(16).reactionActiveArr = arrayOf("UL", "XGH2O")
    //const.m(17).reactionActive = "LH"
    const.m(17).reactionActiveArr = arrayOf("LH", "XGH2O")
    //const.m(18).reactionActive = "LH2O"
    const.m(18).reactionActiveArr = arrayOf("LH2O", "XGH2O")
    //const.m(19).reactionActive = "OH"
    const.m(19).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(20).reactionActive = "OH"
    const.m(20).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(21).reactionActive = "OH"
    const.m(21).reactionActiveArr = arrayOf("OH", "XGH2O")
    //const.m(22).reactionActive = "GH"
    const.m(22).reactionActiveArr = arrayOf("GH", "XGH2O")
    //const.m(23).reactionActive = "GH2O"
    const.m(23).reactionActiveArr = arrayOf("GH2O", "XGH2O")
    const.m(24).reactionActive = "GH2O"
    const.m(25).reactionActive = "ZK"
    const.m(26).reactionActive = "UL"
    const.m(27).reactionActive = "G"
    const.m(28).reactionActive = "GH"
    const.m(29).reactionActive = "XGH2O"
    const.m(30).reactionActive = "XGH2O"
    const.m(31).reactionActive = "XGH2O"
}