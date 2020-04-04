package mainContext.mainRoomCollecror.mainRoom

import mainContext.dataclass.RecordOfStructurePosition
import screeps.api.*

fun MainRoom.doSnapShot() {
    val structures = this.room.find(FIND_STRUCTURES)
    if (Memory["snap"] == null) Memory["snap"] = object {}
    Memory["snap"][this.name] = mc.lm.lmBuilding.lmBuildingSnapShot.snapshotSerialize(structures)
}

fun MainRoom.restoreSnapShot(){
    if (this.room.find(FIND_CONSTRUCTION_SITES).isNotEmpty()) return
    val flags = this.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_YELLOW }
    if (flags.isNotEmpty()) return

    if (Memory["snap"] == null || Memory["snap"][this.name] == null){
        mc.lm.lmMessenger.log("INFO", this.name, "Snapshot not present", COLOR_RED)
        return
    }
    val d:Array<RecordOfStructurePosition> = mc.lm.lmBuilding.lmBuildingSnapShot.snapshotDeserialize(Memory["snap"][this.name] as String,this.name)
    for (record in d)
        this.room.createConstructionSite(record.roomPosition,record.structureConstant)
}

fun MainRoom.directControl() {
    val flagsRedGreen = this.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_GREEN }
    if (flagsRedGreen.isNotEmpty()) this.restoreSnapShot()
    for (flag in flagsRedGreen) flag.remove()

    val flagsRedRed = this.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_RED }
    if (flagsRedRed.isNotEmpty()) this.doSnapShot()
    for (flag in flagsRedRed) flag.remove()

    val flagsRedGrey = this.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_GREY }
    if (flagsRedGrey.isNotEmpty()) mc.lm.lmDevelop.runMainRoom(this)
    for (flag in flagsRedGrey) flag.remove()

    val flagsRedBlue = this.room.find(FIND_FLAGS).filter { it.color == COLOR_RED && it.secondaryColor == COLOR_BLUE }
    if (flagsRedBlue.isNotEmpty()) {
        mc.lm.lmDefence.lmMainRoomDefenceArea.calculate(this)
    }
    for (flag in flagsRedBlue) flag.remove()
}