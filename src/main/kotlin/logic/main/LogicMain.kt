package logic.main

import mainContext.MainContext

class LogicMain(val mainContext: MainContext) {
    private val logicMainGCL: LogicMainGCL = LogicMainGCL(mainContext)
    fun runInit() {

    }

    fun runInStartOfTick() {
        logicMainGCL.gclCalculate()
    }

    fun runInEndOfTick(){

    }
}