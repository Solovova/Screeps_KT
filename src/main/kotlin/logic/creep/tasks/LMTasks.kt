package logic.creep.tasks

import mainContext.MainContext

class LMTasks (val mainContext: MainContext) {
    val taskGoToRoom: TaskGoToRoom = TaskGoToRoom(mainContext)
}