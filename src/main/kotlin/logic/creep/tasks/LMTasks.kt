package logic.creep.tasks

import mainContext.MainContext

class LMTasks (val mainContext: MainContext) {
    val lmTasksSlaveRoomGoToRoom: LMTasksSlaveRoomGoToRoom = LMTasksSlaveRoomGoToRoom(mainContext)
    val lmTasksLabFiller: LMTasksLabFiller = LMTasksLabFiller(mainContext)
    val lmTasksLogist:LMTasksLogist = LMTasksLogist(mainContext)
}