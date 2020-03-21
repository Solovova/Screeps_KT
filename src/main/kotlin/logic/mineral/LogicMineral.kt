package logic.mineral

import mainContext.MainContext

class LogicMineral(val mainContext: MainContext) {
    fun runInStartOfTick() {
        this.mineralDataFill(mainContext)
    }
}