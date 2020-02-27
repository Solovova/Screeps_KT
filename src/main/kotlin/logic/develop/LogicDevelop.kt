package logic.develop

import mainRoom.MainRoom
import screeps.api.TERRAIN_MASK_WALL
import screeps.api.TERRAIN_WALL

class LogicDevelop {
    val matrix = Array(50) { Array(50) { 0 } }

    fun runMainRoom(mainRoom: MainRoom) {
        this.fillMatrix(mainRoom)
        this.outMatrix()
    }

    private fun fillMatrix(mainRoom: MainRoom) {
        val terrain = mainRoom.room.getTerrain()
        for (x in 0..49) {
            for (y in 0..49) {
                matrix[x][y] = if (terrain.get(x,y) == TERRAIN_MASK_WALL) { 1 } else { 0}
            }
        }
    }

    private fun outMatrix() {
        console.log("start_____________________________________________________")
        for (x in 0..49) {
            console.log(this.matrix[x].toString())
        }
        console.log("end_____________________________________________________")
    }


}