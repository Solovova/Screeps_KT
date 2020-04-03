package mainContext

import mainContext.tasks.Tasks
import battleGroup.BattleGroupContainer
import mainContext.constants.Constants
import logic.main.LM
import mainContext.mainRoomCollecror.MainRoomCollector
import screeps.api.*
import kotlin.random.Random

class MainContext {
    val lm: LM = LM(this)

    val messengerMap: MutableMap<String, String> = mutableMapOf()

    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()
    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())


    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    init {

        //this.marketShowBuyOrdersRealPrice("energy".unsafeCast<ResourceConstant>())
    }

    fun run() {
        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)

        lm.lmDefence.lmMainRoomUpgradeWall.calculate()

        this.mainRoomCollector.creepsCalculate()
        this.mainRoomCollector.creepsCalculateProfit()

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.runInStartOfTick()
            } catch (e: Exception) {
                this.lm.lmMessenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }

        lm.lmGCL.calculate()
        lm.lmProduction.lmMineralFillData.fill()
        lm.lmProduction.lmMineralFillProduction.fill()

        this.battleGroupContainer.runInStartOfTick()

        //Not every tick
        this.mainRoomCollector.runNotEveryTick()

        if (Game.time % 10 == 0) {
            this.mineralSellBuy()
        }

        if (this.setNextTickRun()) {

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
        this.lm.lmProduction.lmLabMainRoomRun.run()

        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        lm.lmTerminal.transactions()
        this.tasks.toMemory()
        this.constants.toMemory()

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
}