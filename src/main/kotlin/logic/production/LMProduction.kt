package logic.production

import logic.production.lab.LMLabFunc
import logic.production.lab.LMLabMainRoomGetLabSorted
import logic.production.lab.LMLabMainRoomRun
import logic.production.lab.LMLabReactionBalance
import logic.production.market.LMMarket
import logic.production.mineral.LMMineralFillData
import logic.production.mineral.LMMineralFillProduction
import logic.production.mineral.LMMineralHarvest
import logic.production.mineral.LMMineralSetGlobalConstant
import mainContext.MainContext

class LMProduction(val mc: MainContext) {
    val lmLabMainRoomRun: LMLabMainRoomRun = LMLabMainRoomRun(mc)
    val lmLabMainRoomGetLabSorted: LMLabMainRoomGetLabSorted = LMLabMainRoomGetLabSorted()

    val lmMineralSetGlobalConstant: LMMineralSetGlobalConstant = LMMineralSetGlobalConstant()
    val lmMineralFillData: LMMineralFillData = LMMineralFillData(mc)
    val lmMineralFillProduction: LMMineralFillProduction = LMMineralFillProduction(mc)

    val lmMarket: LMMarket = LMMarket(mc)

    val labFunc: LMLabFunc = LMLabFunc(mc)
    val labRoomsReactionBalance: LMLabReactionBalance = LMLabReactionBalance(mc)
    val mineralHarvest: LMMineralHarvest = LMMineralHarvest(mc)
}