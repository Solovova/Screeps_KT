package accounts.srvMain2

import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoomType
import screeps.api.BodyPartConstant
import screeps.api.ResourceConstant
import screeps.api.WORK

fun AccountInitMain2.initHeadOut(const: Constants) {
    //M0       M1       M2       M3       M4       M5       M6       M7       M8       M9
    const.initMainRoomConstantContainer(arrayOf("W5N3", "W3N1", "W8N2", "W6N3", "W3N5", "W4N3","W3N6"))

    //Colonization E51N41
    const.getMainRoomConstant("W5N3").initSlaveRoomConstantContainer(arrayOf("W5N4", "W5N2")) //M0
    const.getMainRoomConstant("W3N1").initSlaveRoomConstantContainer(arrayOf("W4N1", "W3N2", "W2N1")) //M1
    const.getMainRoomConstant("W8N2").initSlaveRoomConstantContainer(arrayOf("W8N3", "W7N2")) //M2
    const.getMainRoomConstant("W6N3").initSlaveRoomConstantContainer(arrayOf("W7N3", "W6N4")) //M3
    const.getMainRoomConstant("W3N5").initSlaveRoomConstantContainer(arrayOf("W2N5","W4N5")) //M4
    const.getMainRoomConstant("W4N3").initSlaveRoomConstantContainer(arrayOf("W4N2","W4N4")) //M5
    const.getMainRoomConstant("W3N6").initSlaveRoomConstantContainer(arrayOf("W3N7","W2N6")) //M6

}

fun AccountInitMain2.initBodyOut(const: Constants) {
    //const.s(5,1).roadBuild = true

    const.s(6,0).autoBuildRoad = true
    const.s(6,1).autoBuildRoad = true


    const.globalConstant.defenceLimitUpgrade = 1000000

    const.m(0).reactionActiveArr = arrayOf("OH", "LH", "LH2O", "XLH2O", "GH2O", "XGH2O", "")
    const.m(1).reactionActiveArr = arrayOf("ZK", "UL", "G", "GH", "OH", "GH2O", "XGH2O", "")
    const.m(2).reactionActiveArr = arrayOf("ZK", "UL", "G", "GH", "OH", "GH2O", "XGH2O", "")
    const.m(3).reactionActiveArr = arrayOf("XGH2O", "")
    const.m(4).reactionActiveArr = arrayOf("XGH2O", "")
    const.m(5).reactionActiveArr = arrayOf("XGH2O", "")


    const.globalConstant.username = "vsolo0"

    const.globalConstant.creepUpgradablePartsRange[19] = mapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(300000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(300000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(300000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    const.globalConstant.creepUpgradablePartsRange[7] = mapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(300000, "XGH2O".unsafeCast<ResourceConstant>()),
                            Pair(300000, "GH2O".unsafeCast<ResourceConstant>()),
                            Pair(300000, "GH".unsafeCast<ResourceConstant>())
                    )
    )

    const.globalConstant.creepUpgradablePartsRange[101] = mapOf<BodyPartConstant,List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(10000, "XLH2O".unsafeCast<ResourceConstant>()),
                            Pair(10000, "LH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "LH".unsafeCast<ResourceConstant>())
                    )
    )

    const.globalConstant.creepUpgradablePartsRange[10] = mutableMapOf<BodyPartConstant, List<Pair<Int, ResourceConstant>>>(
            WORK to
                    listOf(
                            Pair(2000, "XLH2O".unsafeCast<ResourceConstant>()),
                            Pair(2000, "LH2O".unsafeCast<ResourceConstant>()),
                            Pair(1000, "LH".unsafeCast<ResourceConstant>())
                    )
    )
}