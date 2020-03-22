package logic.messenger.mainRoom

import MainRoomInfoRecord
import creep.getDescribeForQueue
import mainRoom.MainRoom
import screeps.api.Creep
import screeps.api.Game
import screeps.api.get
import screeps.api.structures.StructureStorage
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap

fun LogicMessengerMainRoom.getInfoQueue(mainRoom: MainRoom): MainRoomInfoRecord {
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

fun LogicMessengerMainRoom.getInfoController(mainRoom: MainRoom): MainRoomInfoRecord {
    var result = "l: ${mainRoom.structureController[0]?.level} "
    if (mainRoom.structureController[0]?.level == 8) return MainRoomInfoRecord(result, false)
    result += "${mainRoom.structureController[0]?.progress}".padStart(9)
    result += "/ "
    result += "${mainRoom.structureController[0]?.progressTotal}".padStart(9)
    result += "  "
    result += "${(mainRoom.structureController[0]?.progressTotal
            ?: 0) - (mainRoom.structureController[0]?.progress ?: 0)}".padStart(9)
    return MainRoomInfoRecord(result, false)
}

fun LogicMessengerMainRoom.getInfoRoomLevel(mainRoom: MainRoom): MainRoomInfoRecord {
    val level = mainRoom.getLevelOfRoom()
    return if (level >= mainRoom.constant.levelOfRoom)
        MainRoomInfoRecord("$level", false)
    else MainRoomInfoRecord("$level/${mainRoom.constant.levelOfRoom}", true)
}

fun LogicMessengerMainRoom.getInfoNeedBuild(mainRoom: MainRoom): MainRoomInfoRecord {
    val resultEmpty = MainRoomInfoRecord("", false)

    if (mainRoom.constructionSite.isNotEmpty()) return resultEmpty
    mainRoom.structureController[0] ?: return resultEmpty
    val answer: String = mainRoom.missingStructures()
    if (answer=="") return resultEmpty
    return MainRoomInfoRecord(answer,true)
}

fun LogicMessengerMainRoom.getInfoConstructionSites(mainRoom: MainRoom): MainRoomInfoRecord {
    val resultEmpty = MainRoomInfoRecord("", false)

    if (mainRoom.constructionSite.isEmpty()) return resultEmpty
    mainRoom.structureController[0] ?: return resultEmpty
    return MainRoomInfoRecord("b:${mainRoom.constructionSite.size}",true)
}

fun LogicMessengerMainRoom.getInfoEnergy(mainRoom: MainRoom): MainRoomInfoRecord {
    val fullEnergy = mainRoom.getResource()
    val buyEnergy = if (mainRoom.constant.marketBuyEnergy) "buy" else ""
    return MainRoomInfoRecord("e: $fullEnergy $buyEnergy", fullEnergy <= 50000)
}

fun LogicMessengerMainRoom.getInfoPlaceInStorage(mainRoom: MainRoom): MainRoomInfoRecord {
    val resultEmpty = MainRoomInfoRecord("", false)
    val storage: StructureStorage = mainRoom.structureStorage[0] ?: return resultEmpty
    val placeInStorage: Int = storage.storeCapacity - storage.store.toMap().map { it.value }.sum()
    return if (placeInStorage<100000) MainRoomInfoRecord("sp: $placeInStorage", true)
    else return resultEmpty
}

fun LogicMessengerMainRoom.getInfoPlaceInTerminal(mainRoom: MainRoom): MainRoomInfoRecord {
    val resultEmpty = MainRoomInfoRecord("", false)
    val terminal: StructureTerminal = mainRoom.structureTerminal[0] ?: return resultEmpty
    val placeInTerminal: Int = terminal.storeCapacity - terminal.store.toMap().map { it.value }.sum()
    return if (placeInTerminal<50000) MainRoomInfoRecord("tp: $placeInTerminal", true)
    else return resultEmpty
}

fun LogicMessengerMainRoom.getInfoNeedUpgrade(mainRoom: MainRoom): MainRoomInfoRecord {
    return MainRoomInfoRecord(if (mainRoom.constant.defenceNeedUpgrade) "upg" else "",
            mainRoom.constant.defenceNeedUpgrade)
}

fun LogicMessengerMainRoom.getInfoReactionInfo(mainRoom: MainRoom): MainRoomInfoRecord {
    val resultEmpty = MainRoomInfoRecord("", false)
    if (mainRoom.structureLabSort.size !in arrayOf(3,6,10)) return resultEmpty
    return MainRoomInfoRecord("${mainRoom.structureLabSort.size}: ${mainRoom.constant.reactionActive}",
            mainRoom.constant.reactionActive == "")
}

fun LogicMessengerMainRoom.getInfoDefenceArea(mainRoom: MainRoom): MainRoomInfoRecord {
    return MainRoomInfoRecord("D:${mainRoom.constant.autoDefenceArea}",
            (mainRoom.constant.autoDefenceArea !in 30..1300))
}


fun LogicMessengerMainRoom.getInfo(mainRoom: MainRoom): Map<TypeOfMainRoomInfo, MainRoomInfoRecord> {
    val result: MutableMap<TypeOfMainRoomInfo, MainRoomInfoRecord> = mutableMapOf()
    result[TypeOfMainRoomInfo.infoQueue] = this.getInfoQueue(mainRoom)
    result[TypeOfMainRoomInfo.infoController] = this.getInfoController(mainRoom)
    result[TypeOfMainRoomInfo.infoRoomName] = MainRoomInfoRecord(mainRoom.name, false)
    result[TypeOfMainRoomInfo.infoRoomDescribe] = MainRoomInfoRecord(mainRoom.describe, false)
    result[TypeOfMainRoomInfo.infoRoomLevel] = this.getInfoRoomLevel(mainRoom)
    result[TypeOfMainRoomInfo.infoNeedBuild] = this.getInfoNeedBuild(mainRoom)
    result[TypeOfMainRoomInfo.infoConstructionSites] = this.getInfoConstructionSites(mainRoom)
    result[TypeOfMainRoomInfo.infoRoomEnergy] = this.getInfoEnergy(mainRoom)
    result[TypeOfMainRoomInfo.infoPlaceInStorage] = this.getInfoPlaceInStorage(mainRoom)
    result[TypeOfMainRoomInfo.infoPlaceInTerminal] = this.getInfoPlaceInTerminal(mainRoom)
    result[TypeOfMainRoomInfo.infoReaction] = this.getInfoReactionInfo(mainRoom)
    result[TypeOfMainRoomInfo.infoNeedUpgrade] = this.getInfoNeedUpgrade(mainRoom)
    result[TypeOfMainRoomInfo.infoAutoDefence] = this.getInfoDefenceArea(mainRoom)
    return result
}