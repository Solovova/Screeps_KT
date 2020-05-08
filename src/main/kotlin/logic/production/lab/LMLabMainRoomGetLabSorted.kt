package logic.production.lab

import screeps.api.structures.StructureLab

class LMLabMainRoomGetLabSorted {
    private fun getLabSortedByArrays(structureLab: Map<String, StructureLab>,
                                     arrDx: Array<Int>, arrDy: Array<Int>,
                                     minX: Int, minY: Int): Map<Int, StructureLab> {
        val result: MutableMap<Int, StructureLab> = mutableMapOf()
        for (ind in arrDx.indices) {
            val tmpLab: StructureLab? = structureLab.values.firstOrNull {
                it.pos.x == (minX + arrDx[ind]) && it.pos.y == (minY + arrDy[ind])
            }
            if (tmpLab != null) result[ind] = tmpLab
        }
        return result.toMap()
    }

    fun getLabSort(structureLab: Map<String, StructureLab>): Map<Int, StructureLab> {
        val emptyResult: MutableMap<Int, StructureLab> = mutableMapOf()

        val minX: Int = structureLab.values.minBy { it.pos.x }?.pos?.x
                ?: return emptyResult
        val minY: Int = structureLab.values.minBy { it.pos.y }?.pos?.y
                ?: return emptyResult

        var tmpResult: Map<Int, StructureLab>

        if (structureLab.size == 10) {
            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(1, 2, 2, 3, 3, 3, 0, 0, 0, 1),
                    arrayOf(2, 1, 0, 0, 1, 2, 1, 2, 3, 3), minX, minY)
            if (tmpResult.size == 10) {
                return tmpResult
            }

            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(1, 2, 3, 3, 3, 2, 0, 0, 1, 2),
                    arrayOf(1, 2, 1, 2, 3, 3, 1, 0, 0, 0), minX, minY)
            if (tmpResult.size == 10) {
                return tmpResult
            }
        }

        if (structureLab.size == 6) {
            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(0, 1, 1, 2, 2, 2),
                    arrayOf(2, 1, 0, 0, 1, 2), minX, minY)
            if (tmpResult.size == 6) {
                return tmpResult
            }

            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(0, 1, 2, 2, 2, 1),
                    arrayOf(0, 1, 0, 1, 2, 2), minX, minY)
            if (tmpResult.size == 6) {
                return tmpResult
            }
        }

        if (structureLab.size == 3) {
            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(0, 1, 1),
                    arrayOf(2, 1, 0), minX, minY)
            if (tmpResult.size == 3) {
                return tmpResult
            }

            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(0, 1, 2),
                    arrayOf(0, 1, 0), minX, minY)
            if (tmpResult.size == 3) {
                return tmpResult
            }
        }

        if (structureLab.size == 1) {
            tmpResult = this.getLabSortedByArrays(structureLab,
                    arrayOf(0),
                    arrayOf(0), minX, minY)
            if (tmpResult.size == 1) {
                return tmpResult
            }
        }

        return emptyResult
    }
}