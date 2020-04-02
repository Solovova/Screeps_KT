package logic.develop

import mainContext.MainContext
import mainRoom.MainRoom
import screeps.api.Source
import screeps.api.TERRAIN_MASK_WALL
import screeps.api.TERRAIN_WALL
import screeps.api.structures.StructureController

class LMDevelop(val mainContext: MainContext) {
    private val matrix = Array(50) { Array(50) { 0 } }

    fun runMainRoom(mainRoom: MainRoom) {
        this.fillMatrix(mainRoom)
        this.outMatrix(mainRoom)
    }

    private fun fillMatrix(mainRoom: MainRoom) {
        val terrain = mainRoom.room.getTerrain()
        for (x in 0..49) {
            for (y in 0..49) {
                matrix[x][y] = if (terrain.get(x, y) == TERRAIN_MASK_WALL) {
                    1
                } else {
                    0
                }
            }
        }
    }

    private fun outMatrix(mainRoom: MainRoom) {
        console.log("start_____________________________________________________")
        for (x in 0..49) {
            console.log("|" + this.matrix[x].toString() + "|")
        }

        for (source in mainRoom.source.values) {
            console.log("|source|" + source.pos.x + "," + source.pos.y + "|")
        }

        val sc = mainRoom.structureController[0]
        if (sc != null) {
            console.log("|controller|" + sc.pos.x + "," + sc.pos.y + "|")
        }

        console.log("|mineral|" + mainRoom.mineral.pos.x + "," + mainRoom.mineral.pos.y + "|")

        console.log("end_____________________________________________________")
    }


}