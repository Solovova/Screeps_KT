package logic.production

import logic.production.lab.LMLab
import logic.production.mineral.LMMineral
import mainContext.MainContext

class LMProduction(val mainContext: MainContext) {
    val lmLab: LMLab = LMLab(mainContext)
    val lmMineral: LMMineral = LMMineral(mainContext)
}