package logic.defence

import logic.defence.mainRoom.LMMainRoomDefenceArea
import logic.defence.mainRoom.LMMainRoomUpgradeWall
import mainContext.MainContext

class LMDefence(val mainContext: MainContext) {
    val lmMainRoomDefenceArea: LMMainRoomDefenceArea = LMMainRoomDefenceArea()
    val lmMainRoomUpgradeWall: LMMainRoomUpgradeWall = LMMainRoomUpgradeWall(mainContext)
}