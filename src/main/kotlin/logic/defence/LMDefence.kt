package logic.defence

import mainContext.MainContext

class LMDefence(val mainContext: MainContext) {
    val lmMainRoomDefenceArea: LMMainRoomDefenceArea = LMMainRoomDefenceArea()
    val lmMainRoomCreepAutoDefence: LMMainRoomCreepAutoDefence = LMMainRoomCreepAutoDefence(mainContext)
}