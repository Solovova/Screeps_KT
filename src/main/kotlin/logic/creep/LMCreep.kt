package logic.creep

import logic.creep.tasks.LMTasks
import logic.creep.upgrade.LMUpgrade
import mainContext.MainContext

class LMCreep (val mainContext: MainContext) {
    val lmTask:LMTasks = LMTasks(mainContext)
    val lmUpgrade: LMUpgrade = LMUpgrade(mainContext)
}