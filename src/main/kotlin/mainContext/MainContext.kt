package mainContext

import mainContext.tasks.Tasks
import battleGroup.BattleGroupContainer
import logic.develop.LMDevelopCPUUse
import mainContext.constants.Constants
import logic.main.LM
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.MainRoomCollector
import screeps.api.*
import screeps.utils.toMap
import kotlin.random.Random

class MainContext {
    var cpuStartMC = Game.cpu.getUsed()
    val lmDevelopCPUUse : LMDevelopCPUUse = LMDevelopCPUUse()
    val lm: LM = LM(this)
    //Data
    var flags:List<Flag> = listOf()
    val messengerMap: MutableMap<String, String> = mutableMapOf()
    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()

    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())

    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    init {
        lm.lmProduction.lmMarket.showBuyOrdersRealPrice("energy".unsafeCast<ResourceConstant>(), 10)
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"Init")
    }

    fun run() {
        flags = Game.flags.toMap().values.toList()

        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"Flag")
        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"mainRoomCollector ")

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.fillCash()
            } catch (e: Exception) {
                this.lm.lmMessenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"fillCash ")

        lm.lmGCL.calculate()
        lm.lmProduction.lmMineralFillData.fill()
        this.constants.accountInit.initTuning(this)
        lm.lmProduction.lmMineralFillProduction.fill()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"Step 1 ")


        lm.lmDefence.lmMainRoomUpgradeWall.calculate()

        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"lmMainRoomUpgradeWall ")

        this.mainRoomCollector.creepsCalculate()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"creepsCalculate")
        this.mainRoomCollector.creepsCalculateProfit()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"creepsCalculateProfit")

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.runInStartOfTick()
            } catch (e: Exception) {
                this.lm.lmMessenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"runInStartOfTick")


        this.battleGroupContainer.runInStartOfTick()

        //Not every tick
        this.mainRoomCollector.runNotEveryTick()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"runNotEveryTick")

        if (Game.time % 10 == 0) {
            lm.lmProduction.lmMarket.sellBuy()
        }
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"sellBuy")

        if (this.setNextTickRun()) {

            this.tasks.deleteTaskDiedCreep()
            this.battleGroupContainer.runNotEveryTick()
            lm.lmProduction.lmMarket.deleteEmptyOffers()
        }

        //End of tick
        //ToDo rewrite
//        for (room in mainRoomCollector.rooms.values) {
//            if (room.constant.autoDefenceArea == 0) {
//                this.logicDefence.mainRoomDefence.mainRoomDefenceArea.getArea(room)
//                break
//            }
//        }

        lm.lmDirectControl.runs()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"lmDirectControl")
        this.lm.lmProduction.lmLabMainRoomRun.run()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"lmLabMainRoomRun")
        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"mainRoomCollector.runInEndOfTick")
        lm.lmTerminal.transactions()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"transactions")
        this.tasks.toMemory()
        this.constants.toMemory()
        cpuStartMC = lmDevelopCPUUse.cutoff(cpuStartMC,"toMemory")
        var cpuStartMCStart = Game.cpu.getUsed()
        lm.lmMessenger.show()
        cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart
        console.log("Show info CPU: $cpuStartMCStart")
    }

    private fun setNextTickRun(): Boolean {
        if (this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext > Game.time) return false
        this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext = Game.time + Random.nextInt(this.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                this.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        this.lm.lmMessenger.log("TEST", "Main context", "Main room not every tick run. Next tick: ${this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext}", COLOR_GREEN)
        return true
    }

    fun getNumRoomWithTerminal():Int {
        var result = 0
        for (mainRoom in this.mainRoomCollector.rooms.values) {
            if (Game.rooms[mainRoom.name] != null && mainRoom.structureTerminal[0] != null) {
                result++
            }
        }
        return result
    }
}