package logic.defence

import logic.defence.mainRoom.MainRoomDefence
import mainContext.MainContext

class LMDefence(val mainContext: MainContext) {
    val mainRoomDefence: MainRoomDefence = MainRoomDefence(mainContext)
}