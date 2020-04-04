package logic.defence

import mainContext.MainContext

class LMDefence(val mainContext: MainContext) {
    val lmMainRoomDefenceArea: LMMainRoomDefenceArea = LMMainRoomDefenceArea()
    val lmMainRoomUpgradeWall: LMMainRoomUpgradeWall = LMMainRoomUpgradeWall(mainContext)
    val lmMainRoomCreepAutoDefence: LMMainRoomCreepAutoDefence = LMMainRoomCreepAutoDefence(mainContext)
}