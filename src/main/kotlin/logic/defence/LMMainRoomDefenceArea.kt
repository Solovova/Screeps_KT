package logic.defence

import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureStorage

class LMMainRoomDefenceArea() {
    private data class DataCell(val y: Int, val x: Int)
    private class FillBy(val matrix: Array<Array<Int>>, val fillList: MutableList<DataCell>,
                         val whiteList: Array<Int>, val whatFill: Int){
        fun fillAroundCell(matrix: Array<Array<Int>>,fillList: MutableList<DataCell>):Int {
            var result = 0
            val actualDataCell: DataCell = fillList[0]
            fillList.removeAt(0)
            if (matrix[actualDataCell.y][actualDataCell.x] in whiteList) {
                matrix[actualDataCell.y][actualDataCell.x] = whatFill
                result = 1
            }


            for (dx in -1 ..1)
                for (dy in -1 ..1){
                    if (dx == 0 && dy == 0) continue
                    val nx: Int = actualDataCell.x + dx
                    val ny: Int = actualDataCell.y + dy
                    if (nx < 0 || nx > 49) continue
                    if (ny < 0 || ny > 49) continue
                    if (!fillList.contains(DataCell(ny, nx)) && matrix[ny][nx] in whiteList) {
                        fillList.add(DataCell(ny, nx))
                    }
                }
            return result
        }

        fun fill() : Int {
            var result = 0
            while (fillList.size != 0)
                result += fillAroundCell(matrix,fillList)
            return result
        }
    }

    private var matrix = Array(50) { Array(50) { 0 } }

    private fun fillFromBorder(): Int {
        val fillList: MutableList<DataCell> = mutableListOf()
        for (y in 0..49)
            if (matrix[y][0] == 0) fillList.add(DataCell(y, 0))
        for (x in 1..49)
            if (matrix[0][x] == 0) fillList.add(DataCell(0, x))
        for (x in 1..49)
            if (matrix[49][x] == 0) fillList.add(DataCell(49, x))
        for (y in 1..48)
            if (matrix[y][49] == 0) fillList.add(DataCell(y, 49))
        return FillBy(this.matrix, fillList, arrayOf(0), 9).fill()
    }

    private fun fillFromCenter(mainRoom: MainRoom):Int {
        val fillList: MutableList<DataCell> = mutableListOf()
        val structureStorage: StructureStorage? = mainRoom.structureStorage[0]
        if (structureStorage != null) {
            fillList.add(DataCell(structureStorage.pos.y, structureStorage.pos.x))
            return FillBy(this.matrix, fillList, arrayOf(0, 2), 8).fill()
        }
        return 0
    }

    private fun fillStructure(mainRoom: MainRoom) {
        //0 - free
        //1 - wall
        //2 - rampart
        //3 - structure
        //9 - fill from border

        mainRoom.room.find(FIND_MY_STRUCTURES).forEach { struct ->
            if (struct.structureType == STRUCTURE_RAMPART && matrix[struct.pos.y][struct.pos.x] == 0) {
                matrix[struct.pos.y][struct.pos.x] = 2
            }
            if (struct.structureType !== STRUCTURE_CONTAINER &&
                    (struct.structureType !== STRUCTURE_RAMPART)) {
                matrix[struct.pos.y][struct.pos.x] = 3
            }
        }

        mainRoom.room.find(FIND_STRUCTURES).forEach { struct ->
            if (struct.structureType == STRUCTURE_WALL) {
                matrix[struct.pos.y][struct.pos.x] = 3
            }
        }

        mainRoom.room.find(FIND_CONSTRUCTION_SITES).forEach { cs ->
            if (cs.structureType != STRUCTURE_ROAD
                    && cs.structureType != STRUCTURE_CONTAINER
                    && cs.structureType != STRUCTURE_RAMPART)
                matrix[cs.pos.y][cs.pos.x] = 3
        }

        mainRoom.room.find(FIND_HOSTILE_STRUCTURES).forEach { struct ->
            matrix[struct.pos.y][struct.pos.x] = 3
        }

        mainRoom.room.getTerrain()
        val terrain = mainRoom.room.getTerrain()
        for (x in 0..49) {
            for (y in 0..49) {
                if (terrain.get(x, y) == TERRAIN_MASK_WALL) {
                    matrix[y][x] = 1
                }
            }
        }
    }

    private fun outMatrix() {
        console.log("start_____________________________________________________")
        for (y in 0..49) {
            var row = ""
            for(x in 0..49) {
                row += if (matrix[y][x] == 8) {
                    "*"
                }else{
                    " "
                    //"${matrix[y][x]}"
                }
            }
            console.log("|$row|")
        }
        console.log("end_____________________________________________________")
    }

    fun calculate(mainRoom: MainRoom) {
        var cpuStartMCStart = Game.cpu.getUsed()

        this.matrix = Array(50) { Array(50) { 0 } }
        fillStructure(mainRoom)
        fillFromBorder()
        val result = fillFromCenter(mainRoom)
        outMatrix()

        val matrixArea = Array(50) { Array(50) { 0 } }
        for (y in 0..49)
            for (x in 0..49)
                if (matrix[y][x] == 8) matrixArea[y][x] = 1

        mainRoom.constant.autoDefenceAreaMatrix = matrixArea
        mainRoom.constant.autoDefenceArea = if (result==0) -1 else result

//        console.log("was:" + matrixArea.joinToString { "," }.length)
//        console.log("is:" + codeMatrix(matrixArea).length)

        cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart
        console.log("Defence area: $result  cpu: $cpuStartMCStart")
    }

    private fun codeMatrix(matrixArea : Array<Array<Int>>):String {
        val list = mutableListOf<Int>()
        var count = 0
        var tmp = 0
        for (i in matrixArea)
            for (j in i ) {
                count++
                tmp = tmp*2+j
                if (count == 8) {
                    list.add(tmp)
                    tmp = 0
                    count = 0
                }
            }
        return list.joinToString { ";" }
    }
}