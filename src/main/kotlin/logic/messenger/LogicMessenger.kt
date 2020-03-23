package logic.messenger

import logic.messenger.mainRoom.LogicMessengerMainRoom
import logic.messenger.mineral.LogicMessengerMineral
import logic.messenger.slaveRoom.LogicMessengerSlaveRoom
import mainContext.MainContext
import screeps.api.*

class LogicMessenger(val mainContext: MainContext) {
    private val logicMessengerMainRoom: LogicMessengerMainRoom = LogicMessengerMainRoom(mainContext)
    private val logicMessengerSlaveRoom: LogicMessengerSlaveRoom = LogicMessengerSlaveRoom(mainContext)
    private val logicMessengerMineral: LogicMessengerMineral = LogicMessengerMineral(mainContext)

    fun messenger(type: String, room: String, text: String, color: ColorConstant = COLOR_GREY,
                  testBefore: String = "", colorBefore: ColorConstant = COLOR_WHITE,
                  testAfter: String = "", colorAfter: ColorConstant = COLOR_WHITE) {

        fun colorToHTMLColor(color: ColorConstant): String {
            return when(color) {
                COLOR_YELLOW -> "yellow"
                COLOR_RED -> "red"
                COLOR_GREEN -> "green"
                COLOR_BLUE -> "blue"
                COLOR_ORANGE -> "orange"
                COLOR_CYAN -> "cyan"
                COLOR_GREY -> "grey"
                else -> "white"
            }
        }

//    val mainRoom: MainRoom? = if (this.constants !== undefined && this.constants.mainRooms.isNotEmpty())
//        this.mainRoomCollector.rooms[this.constants.mainRooms[0]]
//    else null
//
//    if (mainRoom!= null) {
//        if (mainRoom.room.find(FIND_FLAGS).none { it.color == COLOR_BROWN && it.secondaryColor == COLOR_RED }) {
//            if (type == "QUEUE") return
//        }
//    }

        if (type == "TASK") return
        if (type == "TEST") return


        val prefix: String = when(type) {
            "HEAD" -> "00"
            "PROD" -> "03"
            "QUEUE" -> "07"
            "PROFIT" -> "09"
            "TASK" -> "11"
            "TEST" -> "13"
            "INFO" -> "94"
            "ERROR" -> "95"
            else -> "99"

        }

        val typeForMM = "$prefix$type${mainContext.messengerMap.size.toString().padStart(3,'0')}"


        val showText = "<font color=${colorToHTMLColor(color)} > $text </font>"
        val showTextBefore = "<font color=${colorToHTMLColor(colorBefore)} > $testBefore </font>"
        val showTextAfter = "<font color=${colorToHTMLColor(colorAfter)} > $testAfter </font>"

        //console.log(typeForMM)
        mainContext.messengerMap[typeForMM] = "$type : $room $showTextBefore $showText $showTextAfter"
    }

    private fun showMessengerInfo(){
        val sortedKeys = mainContext.messengerMap.keys.toList().sortedBy { it }

        for (key in sortedKeys)
            console.log(mainContext.messengerMap[key])
        mainContext.messengerMap.clear()
    }

    private fun showMainRoomInfo() {
        for (room in this.mainContext.mainRoomCollector.rooms.values) {
            this.logicMessengerMainRoom.showInfo(room)
        }
    }

    private fun showSlaveRoomInfo() {
        for (room in this.mainContext.mainRoomCollector.rooms.values)
            for (slaveRoom in room.slaveRooms.values) {
                if (slaveRoom.constant.model != 1) {
                    this.logicMessengerSlaveRoom.showInfo(slaveRoom)
                }
            }
    }

    private fun showMineralInfo() {
        logicMessengerMineral.showInfo()
    }

    fun showInfo() {
        this.showMineralInfo()
        this.showSlaveRoomInfo()
        this.showMessengerInfo()
        this.showMainRoomInfo()
    }
}