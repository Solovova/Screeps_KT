package mainContext

import Tasks
import battleGroup.BattleGroupContainer
import constants.Constants
import logic.building.LogicBuilding
import logic.defence.LogicDefence
import logic.develop.LogicDevelop
import logic.lab.LogicLab
import logic.market.LogicMarket
import logic.messenger.LogicMessenger
import logic.mineral.LogicMineral
import logic.terminal.LogicTerminal
import logic.upgrade.LogicUpgrade
import mainRoomCollector.MainRoomCollector
import mainRoomCollector.infoShow
import screeps.api.*
import kotlin.random.Random

class MainContext {
    val logicLab: LogicLab = LogicLab()
    val logicUpgrade: LogicUpgrade = LogicUpgrade()
    val logicBuilding: LogicBuilding = LogicBuilding()
    val logicTerminal: LogicTerminal = LogicTerminal()
    val logicMineral: LogicMineral = LogicMineral(this)
    val logicMarket: LogicMarket = LogicMarket()
    val logicMessenger: LogicMessenger = LogicMessenger()
    val logicDevelop: LogicDevelop = LogicDevelop()
    val logicDefence: LogicDefence = LogicDefence(this)


    val messengerMap: MutableMap<String, String> = mutableMapOf()
    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()
    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())


    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    fun runInStartOfTick() {
        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)
        this.mainRoomCollector.runInStartOfTick()

        logicMineral.runInStartOfTick()
        LogicLab().runInStartOfTick(this)

        this.battleGroupContainer.runInStartOfTick()

    }

    fun runNotEveryTick() {
        this.mainRoomCollector.runNotEveryTick()

        this.mineralSellBuy()
        if (!this.setNextTickRun()) return
        this.tasks.deleteTaskDiedCreep()
        this.battleGroupContainer.runNotEveryTick()
        this.marketDeleteEmptyOffers()
    }

    fun runInEndOfTick() {
        //ToDo rewrite
        for (room in mainRoomCollector.rooms.values) {
            if (room.constant.autoDefenceArea == 0) {
                this.logicDefence.mainRoomDefence.mainRoomDefenceArea.getArea(room)
                break
            }
        }

        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        this.tasks.toMemory()

        var cpuStartMCStart = Game.cpu.getUsed()
        this.constants.toMemory()
        cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart
        console.log("Constants to memory: $cpuStartMCStart")

        this.mineralInfoShow()
        this.messengerShow()
        this.mainRoomCollector.infoShow()
    }

    private fun setNextTickRun(): Boolean {
        if (this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext > Game.time) return false
        this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext = Game.time + Random.nextInt(this.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                this.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        this.messenger("TEST", "Main context", "Main room not every tick run. Next tick: ${this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext}", COLOR_GREEN)
        return true
    }
}