package logic.messenger

import RESOURCES_ALL
import mainContext.MainContext
import mainContext.MineralDataRecord
import toSecDigit
import kotlin.math.min

class LMMessengerMineral(val mainContext: MainContext) {
    class MineralInfo(private val numRows: Int = 5, val widthColumn: Int = 15, val numColumnInRow: Int = 10, val mainContext: MainContext) {
        var info = Array(numRows) { "" }

        fun addColumn(dataIn: Array<String>) {
            for (ind in 0 until min(dataIn.size, numRows))
                info[ind] += dataIn[ind].padEnd(widthColumn)
        }

        fun show() {
            for (bigRow in 0..((info[0].length - 1) / (widthColumn * numColumnInRow)))
                for (ind in 0 until numRows) {
                    val startIndex = bigRow * (widthColumn * numColumnInRow)
                    val endIndex = min((bigRow + 1) * (widthColumn * numColumnInRow), this.info[ind].length)

                    val tInfo = this.info[ind].subSequence(startIndex until endIndex) as String
                    console.log("<font color=orange> $tInfo </font>")
                }
        }
    }

    fun showInfo() {
        val mineralInfo = MineralInfo(numRows = 5, mainContext = mainContext)
        mineralInfo.addColumn(arrayOf(
                "Res:",
                "Quantity:",
                "Balance:",
                "Need:"))
        for (res in RESOURCES_ALL) {
            val mineralDataRecord: MineralDataRecord? = mainContext.mineralData[res]
            if (mineralDataRecord != null) {
                val strQuantity = if (mineralDataRecord.quantity == 0) "" else mineralDataRecord.quantity.toString().toSecDigit()
                //val strMinPrice = if (mineralDataRecord.priceMin == 0.0) "" else mineralDataRecord.priceMin.toString()
                //val strMaxPrice = if (mineralDataRecord.priceMax == 0.0) "" else mineralDataRecord.priceMax.toString()
                //val strProduceUp = if (mineralDataRecord.quantityUp == 0) "" else mineralDataRecord.quantityUp.toString()
                //val strProduceDown = if (mineralDataRecord.quantityDown == 0) "" else mineralDataRecord.quantityDown.toString()
                val strBalance = if (mineralDataRecord.quantityUp - mineralDataRecord.quantityDown == 0) ""
                else (mineralDataRecord.quantityUp - mineralDataRecord.quantityDown).toString()

                val strNeed = if (mineralDataRecord.need > mineralDataRecord.quantity) (-mineralDataRecord.need + mineralDataRecord.quantity).toString()
                else ""


                mineralInfo.addColumn(arrayOf(
                        "$res",
                        strQuantity,
                        strBalance,
                        strNeed))
            }
        }
        mineralInfo.show()
    }
}