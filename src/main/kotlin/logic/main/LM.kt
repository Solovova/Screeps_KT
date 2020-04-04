package logic.main

import logic.building.LMBuilding
import logic.creep.LMCreep
import logic.defence.LMDefence
import logic.develop.LMDevelop
import logic.harvest.LMHarvestCacheRecordRoom
import logic.harvest.LMHarvestGetCarrierAuto
import logic.harvest.LMHarvestGetWayFromPosToPos
import logic.messenger.LMMessenger
import logic.production.LMProduction
import logic.terminal.LMTerminal
import mainContext.MainContext

class LM(val mc: MainContext) {
    val lmGCL: LMGCL = LMGCL(mc)
    val lmTerminal: LMTerminal = LMTerminal(mc)
    val lmProduction: LMProduction = LMProduction(mc)
    val lmDefence: LMDefence = LMDefence(mc)
    val lmMessenger: LMMessenger = LMMessenger(mc)
    val lmCreep: LMCreep = LMCreep(mc)
    val lmDevelop: LMDevelop = LMDevelop(mc)
    val lmBuilding: LMBuilding = LMBuilding()
    val lmHarvestCacheRecordRoom: LMHarvestCacheRecordRoom = LMHarvestCacheRecordRoom(mc)
    val lmHarvestGetCarrierAuto: LMHarvestGetCarrierAuto = LMHarvestGetCarrierAuto()
    val lmHarvestGetWayFromPosToPos: LMHarvestGetWayFromPosToPos = LMHarvestGetWayFromPosToPos()
}