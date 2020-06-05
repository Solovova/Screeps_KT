package mainContext.tasks

import mainContext.MainContext
import mainContext.dataclass.TypeOfTask
import screeps.api.*
import screeps.api.Memory
import screeps.utils.unsafe.delete



//1. End of tasks
//2. Assign new tasks
//3. Do tasks

class  Tasks(private val parent: MainContext) {
    // Все держим в памяти, в конце тика записываем в Memory если пропал объект восстанавливаем из памяти
    val tasks: MutableMap<String, CreepTask> = mutableMapOf() //id of creepsRole

    init {
        this.fromMemory()
    }

    fun add(idCreep: String, task: CreepTask) {
        if (task.posObject0!= null) parent.lm.lmMessenger.log("TASK", task.posObject0.roomName, "New task: $idCreep ${task.type}", COLOR_CYAN)
        tasks[idCreep] = task
        try {
            Game.getObjectById<Creep>(idCreep)?.say(task.type.toString())
        }catch (e: Exception) {
            parent.lm.lmMessenger.log("ERROR", idCreep , "Task say", COLOR_RED)
        }
    }

    fun toMemory() {
        val dTasks: dynamic = object {}
        for (task in tasks) dTasks[task.key] = task.value.toMemory()
        val d: dynamic = object {}
        d["tasks"] = dTasks
        delete(Memory["task"])
        Memory["task"] = d
    }

    private fun fromMemory() {
        try {
            tasks.clear()
            val d: dynamic = Memory["task"] ?: return
            val dTasks = d["tasks"] ?: return
            for (key in js("Object").keys(dTasks).unsafeCast<Array<String>>()) tasks[key] = CreepTask(dTasks[key])
        } catch (e: Exception) {
            delete(Memory["task"])
        }
    }

    fun isTaskForCreep(creep: Creep): Boolean {
        return (this.tasks[creep.id] != null)
    }

    fun deleteTask(key: String) {
        parent.lm.lmMessenger.log("TASK", "", "Delete task: $key ", COLOR_CYAN)
        this.tasks.remove(key)
    }

    fun getEnergyCaringTo(id: String): Int {
        var result = 0
        for (task in this.tasks) {
            if (task.value.type != TypeOfTask.TransferTo) continue
            if (task.value.idObject0 != id) continue
            val creep: Creep = Game.getObjectById(task.key) ?: continue
            result += creep.store[RESOURCE_ENERGY] ?: 0
        }
        return result
    }

    fun getSourceHarvestNum(id: String): Int {
        var result = 0
        for (task in this.tasks) {
            if (task.value.type != TypeOfTask.Harvest) continue
            if (task.value.idObject0 != id) continue
            result += 1
        }
        return result
    }

    fun deleteTaskDiedCreep() {
        val idForDelete: MutableList<String> = mutableListOf()
        for (task in this.tasks)
            if (Game.getObjectById<Creep>(task.key) == null) idForDelete.add(task.key)

        for (key in idForDelete) this.tasks.remove(key)
    }
}