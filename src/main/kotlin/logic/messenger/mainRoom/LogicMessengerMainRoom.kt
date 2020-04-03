package logic.messenger.mainRoom

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*

enum class TypeOfMainRoomInfo {
    infoQueue,
    infoController,
    infoConstructionSites,
    infoNeedBuild,
    infoNeedSnapshot,
    infoReaction,
    infoRoomName,
    infoRoomDescribe,
    infoRoomLevel,
    infoRoomEnergy,
    infoPlaceInStorage,
    infoPlaceInTerminal,
    infoNeedUpgrade,
    infoAutoDefence
}

data class MainRoomInfoSetup(val type: TypeOfMainRoomInfo,
                             val describe: String,
                             val color: ColorConstant,
                             val colorAlarm: ColorConstant,
                             val width: Int,
                             val prefix: String = "",
                             val suffix: String = "")

class LogicMessengerMainRoom(val mainContext: MainContext) {
    private fun colorToHTMLColor(color: ColorConstant): String {
        return when (color) {
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

    private val mainRoomInfoSetup: List<MainRoomInfoSetup> = listOf(
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomName,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    7,
                    prefix = "",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomDescribe,
                    "",
                    COLOR_WHITE,
                    COLOR_WHITE,
                    3,
                    prefix = "(",
                    suffix = ")"),

            MainRoomInfoSetup(TypeOfMainRoomInfo.infoController,
                    "Controller",
                    COLOR_WHITE,
                    COLOR_RED,
                    38,
                    prefix = "(",
                    suffix = ")  "),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoQueue,
                    "Queue",
                    COLOR_YELLOW,
                    COLOR_ORANGE,
                    80),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoNeedBuild,
                    "Need building",
                    COLOR_YELLOW,
                    COLOR_RED,
                    40),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomName,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    7,
                    prefix = "",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomLevel,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    3,
                    prefix = " ",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomEnergy,
                    "Room energy",
                    COLOR_WHITE,
                    COLOR_RED,
                    14),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoConstructionSites,
                    "Construction sites",
                    COLOR_YELLOW,
                    COLOR_ORANGE,
                    5),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoNeedUpgrade,
                    "Need upgrade",
                    COLOR_WHITE,
                    COLOR_ORANGE,
                    4),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoAutoDefence,
                    "Def",
                    COLOR_WHITE,
                    COLOR_RED,
                    6),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoPlaceInStorage,
                    "Storage place",
                    COLOR_WHITE,
                    COLOR_RED,
                    10),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoPlaceInTerminal,
                    "Terminal place",
                    COLOR_WHITE,
                    COLOR_RED,
                    10),

            MainRoomInfoSetup(TypeOfMainRoomInfo.infoReaction,
                    "Labs",
                    COLOR_WHITE,
                    COLOR_ORANGE,
                    10),
            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomDescribe,
                    "",
                    COLOR_WHITE,
                    COLOR_WHITE,
                    3,
                    prefix = "(",
                    suffix = ")"),

            MainRoomInfoSetup(TypeOfMainRoomInfo.infoRoomName,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    7,
                    prefix = "",
                    suffix = "")

    )

    fun showInfo(mainRoom: MainRoom) {
        val roomInfo = this.getInfo(mainRoom)
        var allText = ""
        for (mainRoomInfoSetupRecord in mainRoomInfoSetup) {
            var text: String = roomInfo[mainRoomInfoSetupRecord.type]?.text ?: ""
            val alarm: Boolean = roomInfo[mainRoomInfoSetupRecord.type]?.alarm ?: false
            text = if (text.length > mainRoomInfoSetupRecord.width) text.substring(0, mainRoomInfoSetupRecord.width)
            else text.padEnd(mainRoomInfoSetupRecord.width)

            val color = if (!alarm) mainRoomInfoSetupRecord.color
            else mainRoomInfoSetupRecord.colorAlarm

            text = mainRoomInfoSetupRecord.prefix + text + mainRoomInfoSetupRecord.suffix
            text = "<font color=${colorToHTMLColor(color)} >$text</font>"
            allText += text

        }
        console.log(allText)
    }
}