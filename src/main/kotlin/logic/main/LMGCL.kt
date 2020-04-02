package logic.main

import mainContext.MainContext
import screeps.api.Game

class LMGCL (val mainContext: MainContext){
    fun calculate() {
        //Initialization
        if (mainContext.constants.globalConstant.gclFromTick == 0) {
            mainContext.constants.globalConstant.gclFromTick = Game.time
            mainContext.constants.globalConstant.gcl = Game.gcl.progress
        }

        if (Game.time - mainContext.constants.globalConstant.gclFromTick >= mainContext.constants.globalConstant.gclPeriod) {
            val sArray:Int = mainContext.constants.globalConstant.gclArray.size
            mainContext.constants.globalConstant.gclArray[sArray] = Game.gcl.progress-mainContext.constants.globalConstant.gcl
            mainContext.constants.globalConstant.gclFromTick = Game.time
            mainContext.constants.globalConstant.gcl = Game.gcl.progress
        }
    }
}