package logic.main

import logic.creep.LMCreep
import logic.defence.LMDefence
import logic.develop.LMDevelop
import logic.messenger.LMMessenger
import logic.production.LMProduction
import logic.terminal.LMTerminal
import mainContext.MainContext

class LM(val mainContext: MainContext) {
    val lmGCL: LMGCL = LMGCL(mainContext)
    val lmTerminal: LMTerminal = LMTerminal(mainContext)
    val lmProduction: LMProduction = LMProduction(mainContext)
    val lmDefence: LMDefence = LMDefence(mainContext)
    val lmMessenger: LMMessenger = LMMessenger(mainContext)
    val lmCreep: LMCreep = LMCreep(mainContext)
    val lmDevelop: LMDevelop = LMDevelop(mainContext)
}