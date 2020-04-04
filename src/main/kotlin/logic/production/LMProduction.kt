package logic.production

import logic.production.lab.LMLabMainRoomGetLabSorted
import logic.production.lab.LMLabMainRoomRun
import logic.production.market.LMMarket
import logic.production.mineral.LMMineralFillData
import logic.production.mineral.LMMineralFillProduction
import logic.production.mineral.LMMineralSetGlobalConstant
import mainContext.MainContext

class LMProduction(val mainContext: MainContext) {
    val lmLabMainRoomRun: LMLabMainRoomRun = LMLabMainRoomRun(mainContext)
    val lmLabMainRoomGetLabSorted: LMLabMainRoomGetLabSorted = LMLabMainRoomGetLabSorted()

    val lmMineralSetGlobalConstant: LMMineralSetGlobalConstant = LMMineralSetGlobalConstant()
    val lmMineralFillData: LMMineralFillData = LMMineralFillData(mainContext)
    val lmMineralFillProduction: LMMineralFillProduction = LMMineralFillProduction(mainContext)

    val lmMarket: LMMarket = LMMarket(mainContext)
}