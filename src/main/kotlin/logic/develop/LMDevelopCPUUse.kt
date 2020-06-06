package logic.develop

import screeps.api.Game

class LMDevelopCPUUse {
    fun cutoff(oldCPU:Double , text: String):Double {
        val newCpu = Game.cpu.getUsed()
        //println("Cutoff $text ${newCpu - oldCPU}")
        return newCpu
    }
}