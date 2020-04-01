package mainContext

import Tasks
import battleGroup.BattleGroupContainer
import constants.Constants
import logic.building.LogicBuilding
import logic.defence.LogicDefence
import logic.develop.LogicDevelop
import logic.lab.LogicLab
import logic.main.LogicMain
import logic.messenger.LogicMessenger
import logic.mineral.LogicMineral
import logic.terminal.LogicTerminal
import logic.upgrade.LogicUpgrade
import mainRoomCollector.MainRoomCollector
import screeps.api.*
import kotlin.random.Random

class MainContext {
    val logicLab: LogicLab = LogicLab()
    val logicUpgrade: LogicUpgrade = LogicUpgrade()
    val logicBuilding: LogicBuilding = LogicBuilding()
    private val logicTerminal: LogicTerminal = LogicTerminal(this)
    val logicMineral: LogicMineral = LogicMineral(this)
    val logicMessenger: LogicMessenger = LogicMessenger(this)
    val logicDevelop: LogicDevelop = LogicDevelop()
    val logicDefence: LogicDefence = LogicDefence(this)

    val logicMain: LogicMain = LogicMain(this)

    val messengerMap: MutableMap<String, String> = mutableMapOf()


    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()
    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())


    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    fun run() {
        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)

        this.mainRoomCollector.creepsCalculate()
        this.mainRoomCollector.creepsCalculateProfit()

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.runInStartOfTick()
            } catch (e: Exception) {
                this.logicMessenger.messenger("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }

        logicMain.runInStartOfTick()
        logicMineral.runInStartOfTick()

        this.battleGroupContainer.runInStartOfTick()

        //Not every tick
        this.mainRoomCollector.runNotEveryTick()

        if (this.setNextTickRun()) {
            this.mineralSellBuy()
            this.tasks.deleteTaskDiedCreep()
            this.battleGroupContainer.runNotEveryTick()
            this.marketDeleteEmptyOffers()
        }

        //End of tick
        //ToDo rewrite
//        for (room in mainRoomCollector.rooms.values) {
//            if (room.constant.autoDefenceArea == 0) {
//                this.logicDefence.mainRoomDefence.mainRoomDefenceArea.getArea(room)
//                break
//            }
//        }

        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        logicTerminal.doAllTransaction()
        this.tasks.toMemory()
        this.constants.toMemory()

        var cpuStartMCStart = Game.cpu.getUsed()
        logicMessenger.showInfo()
        cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart
        console.log("Show info CPU: $cpuStartMCStart")

    }

    private fun setNextTickRun(): Boolean {
        if (this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext > Game.time) return false
        this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext = Game.time + Random.nextInt(this.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                this.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        this.logicMessenger.messenger("TEST", "Main context", "Main room not every tick run. Next tick: ${this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext}", COLOR_GREEN)
        return true
    }
}