package mainContext

import screeps.api.*
import mainContext.dataclass.slaveRoom

fun MainContext.directControlTaskClearInRoom(nameSlaveRoom: String) {
    try {
        for (creep in Game.creeps.values)
            if (creep.memory.slaveRoom == nameSlaveRoom) tasks.deleteTask(creep.id)

    }catch (e: Exception) {
        lm.lmMessenger.log("ERROR", "Direct control","", COLOR_RED)
    }
    lm.lmMessenger.log("TEST", "Direct control"," Erase all tasks in slave room: $nameSlaveRoom", COLOR_RED)
}