package logic.messenger

import MainRoomInfoRecord
import creep.getDescribeForQueue
import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureController
import screeps.api.structures.StructureStorage
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap

enum class TypeOfMainRoomInfo {
    InfoQueue,
    InfoController,
    InfoConstructionSites,
    InfoNeedBuild,
    InfoNeedSnapshot,
    InfoReaction,
    InfoRoomName,
    InfoRoomDescribe,
    InfoRoomLevel,
    InfoRoomEnergy,
    InfoPlaceInStorage,
    InfoPlaceInTerminal,
    InfoNeedUpgrade,
    InfoAutoDefence
}

data class MainRoomInfoSetup(val type: TypeOfMainRoomInfo,
                             val describe: String,
                             val color: ColorConstant,
                             val colorAlarm: ColorConstant,
                             val width: Int,
                             val prefix: String = "",
                             val suffix: String = "")

class LMMessengerMainRoom(val mainContext: MainContext) {
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
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomName,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    7,
                    prefix = "",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomDescribe,
                    "",
                    COLOR_WHITE,
                    COLOR_WHITE,
                    3,
                    prefix = "(",
                    suffix = ")"),

            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoController,
                    "Controller",
                    COLOR_WHITE,
                    COLOR_RED,
                    38,
                    prefix = "(",
                    suffix = ")  "),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoQueue,
                    "Queue",
                    COLOR_YELLOW,
                    COLOR_ORANGE,
                    80),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoNeedBuild,
                    "Need building",
                    COLOR_YELLOW,
                    COLOR_RED,
                    40),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomName,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    7,
                    prefix = "",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomLevel,
                    "",
                    COLOR_WHITE,
                    COLOR_RED,
                    3,
                    prefix = " ",
                    suffix = ""),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomEnergy,
                    "Room energy",
                    COLOR_WHITE,
                    COLOR_RED,
                    14),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoConstructionSites,
                    "Construction sites",
                    COLOR_YELLOW,
                    COLOR_ORANGE,
                    5),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoNeedUpgrade,
                    "Need upgrade",
                    COLOR_WHITE,
                    COLOR_ORANGE,
                    4),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoAutoDefence,
                    "Def",
                    COLOR_WHITE,
                    COLOR_RED,
                    6),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoPlaceInStorage,
                    "Storage place",
                    COLOR_WHITE,
                    COLOR_RED,
                    10),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoPlaceInTerminal,
                    "Terminal place",
                    COLOR_WHITE,
                    COLOR_RED,
                    10),

            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoReaction,
                    "Labs",
                    COLOR_WHITE,
                    COLOR_ORANGE,
                    10),
            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomDescribe,
                    "",
                    COLOR_WHITE,
                    COLOR_WHITE,
                    3,
                    prefix = "(",
                    suffix = ")"),

            MainRoomInfoSetup(TypeOfMainRoomInfo.InfoRoomName,
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

    private fun getInfoQueue(mainRoom: MainRoom): MainRoomInfoRecord {
        var textSpawning = ""
        for (spawn in mainRoom.structureSpawn) {
            val recordSpawning = spawn.value.spawning
            if (recordSpawning != null) {
                val creep: Creep? = Game.creeps[recordSpawning.name]
                textSpawning += creep?.getDescribeForQueue(this.mainContext) ?: ""
            }
        }

        var showText = textSpawning.padEnd(45) + ":"


        for (record in mainRoom.queue) {
            var prefix = ""
            if (record.mainRoom != record.slaveRoom)
                prefix = mainRoom.slaveRooms[record.slaveRoom]?.describe ?: "und"
            showText += "$prefix ${record.role},"
        }

        val alarm = (mainRoom.queue.size > 4)
        return MainRoomInfoRecord(showText, alarm)
    }

    private fun getInfoController(mainRoom: MainRoom): MainRoomInfoRecord {
        val controller: StructureController = mainRoom.structureController[0] ?: return MainRoomInfoRecord("", false)
        var result: String
        if (controller.level==8) {
            val defenceStatus: Int = mainRoom.constant.defenceMinHits - mainRoom.constant.defenceHits
            result = "def: $defenceStatus".padStart(15)
            return MainRoomInfoRecord(result, defenceStatus<0)
        }else{
            result = "l: ${mainRoom.structureController[0]?.level} "
            result += "${mainRoom.structureController[0]?.progress}".padStart(9)
            result += "/ "
            result += "${mainRoom.structureController[0]?.progressTotal}".padStart(9)
            result += "  "
            result += "${(mainRoom.structureController[0]?.progressTotal
                    ?: 0) - (mainRoom.structureController[0]?.progress ?: 0)}".padStart(9)
            return MainRoomInfoRecord(result, false)
        }
    }

    private fun getInfoRoomLevel(mainRoom: MainRoom): MainRoomInfoRecord {
        val level = mainRoom.getLevelOfRoom()
        return if (level >= mainRoom.constant.levelOfRoom)
            MainRoomInfoRecord("$level", false)
        else MainRoomInfoRecord("$level/${mainRoom.constant.levelOfRoom}", true)
    }

    private fun getInfoNeedBuild(mainRoom: MainRoom): MainRoomInfoRecord {
        val resultEmpty = MainRoomInfoRecord("", false)

        if (mainRoom.constructionSite.isNotEmpty()) return resultEmpty
        mainRoom.structureController[0] ?: return resultEmpty
        val answer: String = mainRoom.missingStructures()
        if (answer=="") return resultEmpty
        return MainRoomInfoRecord(answer,true)
    }

    private fun getInfoConstructionSites(mainRoom: MainRoom): MainRoomInfoRecord {
        val resultEmpty = MainRoomInfoRecord("", false)

        if (mainRoom.constructionSite.isEmpty()) return resultEmpty
        mainRoom.structureController[0] ?: return resultEmpty
        return MainRoomInfoRecord("b:${mainRoom.constructionSite.size}",true)
    }

    private fun getInfoEnergy(mainRoom: MainRoom): MainRoomInfoRecord {
        val fullEnergy = mainRoom.getResource()
        val buyEnergy = if (mainRoom.constant.marketBuyEnergy) "buy" else ""
        return MainRoomInfoRecord("e: $fullEnergy $buyEnergy", fullEnergy <= 50000)
    }

    private fun getInfoPlaceInStorage(mainRoom: MainRoom): MainRoomInfoRecord {
        val resultEmpty = MainRoomInfoRecord("", false)
        val storage: StructureStorage = mainRoom.structureStorage[0] ?: return resultEmpty
        val placeInStorage: Int = storage.storeCapacity - storage.store.toMap().map { it.value }.sum()
        return if (placeInStorage<100000) MainRoomInfoRecord("sp: $placeInStorage", true)
        else return resultEmpty
    }

    private fun getInfoPlaceInTerminal(mainRoom: MainRoom): MainRoomInfoRecord {
        val resultEmpty = MainRoomInfoRecord("", false)
        val terminal: StructureTerminal = mainRoom.structureTerminal[0] ?: return resultEmpty
        val placeInTerminal: Int = terminal.storeCapacity - terminal.store.toMap().map { it.value }.sum()
        return if (placeInTerminal<50000) MainRoomInfoRecord("tp: $placeInTerminal", true)
        else return resultEmpty
    }

    private fun getInfoNeedUpgrade(mainRoom: MainRoom): MainRoomInfoRecord {
        return MainRoomInfoRecord(if (mainRoom.constant.defenceNeedUpgrade) "upg" else "",
                mainRoom.constant.defenceNeedUpgrade)
    }

    private fun getInfoReactionInfo(mainRoom: MainRoom): MainRoomInfoRecord {
        val resultEmpty = MainRoomInfoRecord("", false)
        if (mainRoom.structureLabSort.size !in arrayOf(3,6,10)) return resultEmpty
        return MainRoomInfoRecord("${mainRoom.structureLabSort.size}: ${mainRoom.constant.reactionActive}",
                mainRoom.constant.reactionActive == "")
    }

    private fun getInfoDefenceArea(mainRoom: MainRoom): MainRoomInfoRecord {
        return MainRoomInfoRecord("D:${mainRoom.constant.autoDefenceArea}",
                (mainRoom.constant.autoDefenceArea !in 30..1300))
    }


    private fun getInfo(mainRoom: MainRoom): Map<TypeOfMainRoomInfo, MainRoomInfoRecord> {
        val result: MutableMap<TypeOfMainRoomInfo, MainRoomInfoRecord> = mutableMapOf()
        result[TypeOfMainRoomInfo.InfoQueue] = this.getInfoQueue(mainRoom)
        result[TypeOfMainRoomInfo.InfoController] = this.getInfoController(mainRoom)
        result[TypeOfMainRoomInfo.InfoRoomName] = MainRoomInfoRecord(mainRoom.name, false)
        result[TypeOfMainRoomInfo.InfoRoomDescribe] = MainRoomInfoRecord(mainRoom.describe, false)
        result[TypeOfMainRoomInfo.InfoRoomLevel] = this.getInfoRoomLevel(mainRoom)
        result[TypeOfMainRoomInfo.InfoNeedBuild] = this.getInfoNeedBuild(mainRoom)
        result[TypeOfMainRoomInfo.InfoConstructionSites] = this.getInfoConstructionSites(mainRoom)
        result[TypeOfMainRoomInfo.InfoRoomEnergy] = this.getInfoEnergy(mainRoom)
        result[TypeOfMainRoomInfo.InfoPlaceInStorage] = this.getInfoPlaceInStorage(mainRoom)
        result[TypeOfMainRoomInfo.InfoPlaceInTerminal] = this.getInfoPlaceInTerminal(mainRoom)
        result[TypeOfMainRoomInfo.InfoReaction] = this.getInfoReactionInfo(mainRoom)
        result[TypeOfMainRoomInfo.InfoNeedUpgrade] = this.getInfoNeedUpgrade(mainRoom)
        result[TypeOfMainRoomInfo.InfoAutoDefence] = this.getInfoDefenceArea(mainRoom)
        return result
    }
}