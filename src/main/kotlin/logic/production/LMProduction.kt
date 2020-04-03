package logic.production

import logic.production.lab.LMLabMainRoomRun
import logic.production.mineral.LMMineral
import mainContext.MainContext

class LMProduction(val mainContext: MainContext) {
    val lmLabMainRoomRun: LMLabMainRoomRun = LMLabMainRoomRun(mainContext)
    //old
    val lmMineral: LMMineral = LMMineral(mainContext)
}