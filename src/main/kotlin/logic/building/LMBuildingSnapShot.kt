package logic.building

import mainContext.MainContext
import mainContext.dataclass.RecordOfStructurePosition
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.Structure

class LMBuildingSnapShot (val mc:MainContext) {
    private val zipStructure: Map<String,String> = mapOf(
            "0" to STRUCTURE_SPAWN.toString(),
            "1" to STRUCTURE_EXTENSION.toString(),
            "2" to STRUCTURE_ROAD.toString(),
            "3" to STRUCTURE_WALL.toString(),
            "4" to STRUCTURE_RAMPART.toString(),
            "5" to STRUCTURE_KEEPER_LAIR.toString(),
            "6" to STRUCTURE_PORTAL.toString(),
            "7" to STRUCTURE_CONTROLLER.toString(),
            "8" to STRUCTURE_LINK.toString(),
            "9" to STRUCTURE_STORAGE.toString(),
            "a" to STRUCTURE_TOWER.toString(),
            "b" to STRUCTURE_OBSERVER.toString(),
            "c" to STRUCTURE_POWER_BANK.toString(),
            "d" to STRUCTURE_POWER_SPAWN.toString(),
            "e" to STRUCTURE_EXTRACTOR.toString(),
            "f" to STRUCTURE_LAB.toString(),
            "g" to STRUCTURE_TERMINAL.toString(),
            "h" to STRUCTURE_CONTAINER.toString(),
            "i" to STRUCTURE_NUKER.toString()
    )

    fun snapshotSerialize(arrayStructures : Array<Structure>) : String {
        var result = ""
        for (record in arrayStructures){
            val key = zipStructure.filterValues { it == record.structureType.toString() }.keys
            if (key.isEmpty()) continue
            result += "${key.first()},${record.pos.x},${record.pos.y};"
        }
        return result
    }

    fun snapshotDeserialize(str: String, name: String): Array<RecordOfStructurePosition> {
        var result:Array<RecordOfStructurePosition> = arrayOf()
        val elements = str.split(";")
        for (element in elements) {
            val struct = element.split(",")
            if (struct.size!=3) continue
            result += RecordOfStructurePosition( (zipStructure[struct[0]] ?: STRUCTURE_ROAD).unsafeCast<StructureConstant>(), RoomPosition(struct[1].toInt(),struct[2].toInt(),name))
        }
        return result
    }

    fun doSnapShot(mainRoom: MainRoom) {
        val structures = mainRoom.room.find(FIND_STRUCTURES)
        if (Memory["snap"] == null) Memory["snap"] = object {}
        Memory["snap"][mainRoom.name] = snapshotSerialize(structures)
    }

    fun restoreSnapShot(mainRoom: MainRoom){
        if (mainRoom.room.find(FIND_CONSTRUCTION_SITES).isNotEmpty()) return
        val flags = mainRoom.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_YELLOW }
        if (flags.isNotEmpty()) return

        if (Memory["snap"] == null || Memory["snap"][mainRoom.name] == null){
            mc.lm.lmMessenger.log("INFO", mainRoom.name, "Snapshot not present", COLOR_RED)
            return
        }
        val d:Array<RecordOfStructurePosition> = snapshotDeserialize(Memory["snap"][mainRoom.name] as String,mainRoom.name)
        for (record in d)
            mainRoom.room.createConstructionSite(record.roomPosition,record.structureConstant)
    }
}