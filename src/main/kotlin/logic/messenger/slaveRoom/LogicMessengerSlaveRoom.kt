package logic.messenger.slaveRoom

import mainContext.MainContext
import screeps.api.COLOR_WHITE
import screeps.api.Game
import slaveRoom.SlaveRoom
import kotlin.math.roundToInt

class LogicMessengerSlaveRoom (val mainContext: MainContext) {
    fun showInfo(slaveRoom: SlaveRoom) {
        if ( ((Game.time.toDouble() / 1500.0).toInt() * 1500) == Game.time) {
            for (index in 4 downTo  1)
                slaveRoom.constant.profitPerTickPreviousArr[index] = slaveRoom.constant.profitPerTickPreviousArr[index-1]
            slaveRoom.constant.profitPerTickPreviousArr[0] =  slaveRoom.constant.profitUp - slaveRoom.constant.profitDown
            slaveRoom.profitClear()
        }

        if (slaveRoom.constant.profitPerTickPreviousArr[0] > this.mainContext.constants.globalConstant.showProfitWhenLessWhen * slaveRoom.source.size ) return

        var sProfitPT = "0"
        if (slaveRoom.constant.profitStart != Game.time)
            sProfitPT = (((slaveRoom.constant.profitUp - slaveRoom.constant.profitDown).toDouble() /
                    (Game.time - slaveRoom.constant.profitStart).toDouble()) * 1500.0 ).roundToInt().toString().padStart(10)

        var sProfitPerTickPrevious = ""
        for (index in 0 .. 4) sProfitPerTickPrevious += slaveRoom.constant.profitPerTickPreviousArr[index].toString().padStart(8) + ":"
        val sUp : String = slaveRoom.constant.profitUp.toString().padEnd(10)
        val sDown : String = slaveRoom.constant.profitDown.toString().padEnd(10)
        val sProfit : String = (slaveRoom.constant.profitUp - slaveRoom.constant.profitDown).toString().padEnd(10)
        val sTicks: String = (Game.time - slaveRoom.constant.profitStart).toString().padEnd(8)
        val sSources = slaveRoom.source.size.toString()

        this.mainContext.lm.lmMessenger.log("PROFIT", slaveRoom.describe,
                "Profit ----> ${slaveRoom.name} Road: ${slaveRoom.constant.roadBuild.toString().padEnd(5)} ($sProfitPT per. 1500 ticks) ticks: $sTicks  + $sUp  - $sDown  $sProfit ($sProfitPerTickPrevious sources: $sSources)", COLOR_WHITE)
    }
}