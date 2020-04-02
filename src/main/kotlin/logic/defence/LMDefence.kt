package logic.defence

import logic.defence.mainRoom.MainRoomDefence
import mainContext.MainContext
import mainRoom.MainRoom

class LMDefence(val mainContext: MainContext) {
    val mainRoomDefence: MainRoomDefence = MainRoomDefence(mainContext)
}