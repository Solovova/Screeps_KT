package logic.mineral

import mainContext.MainContext

class LogicMineral {
    fun runInStartOfTick(mainContext: MainContext) {
        this.mineralDataFill(mainContext)
    }
}