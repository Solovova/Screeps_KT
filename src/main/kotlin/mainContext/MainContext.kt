package mainContext

import mainContext.tasks.Tasks
import battleGroup.BattleGroupContainer
import mainContext.constants.Constants
import logic.LogicMain
import mainContext.dataclass.MineralDataRecord
import mainContext.mainRoomCollecror.MainRoomCollector
import screeps.api.*
import screeps.utils.toMap
import kotlin.random.Random

class MainContext {
    val lm: LogicMain = LogicMain(this)
    //Data
    var flags:List<Flag> = listOf()
    val messengerMap: MutableMap<String, String> = mutableMapOf()
    val mineralData: MutableMap<ResourceConstant, MineralDataRecord> = mutableMapOf()

    val constants: Constants = Constants(this)
    val tasks: Tasks = Tasks(this)
    var mainRoomCollector: MainRoomCollector = MainRoomCollector(this, arrayOf())

    val battleGroupContainer: BattleGroupContainer = BattleGroupContainer(this)

    init {
        //lm.lmProduction.lmMarket.showSellOrdersRealPrice("XLH2O".unsafeCast<ResourceConstant>())

    }

    fun run() {
        flags = Game.flags.toMap().values.toList()

        this.mainRoomCollector = MainRoomCollector(this, this.constants.mainRoomsInit)

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.fillCash()
            } catch (e: Exception) {
                this.lm.lmMessenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }

        //Tests
//        val mrTest = mainRoomCollector.rooms["W3N1"]
//        if (mrTest!=null) {
//            println("Test lab ${mrTest.upgradeLab?.id}  index: ${mrTest.upgradeLabIndexSorted}")
//        }
        //


        lm.lmGCL.calculate()

        lm.lmNuker.lmNukerNeedMineral.fill()
        lm.lmProduction.lmMineralFillData.fill()

        this.constants.accountInit.initTuning(this)
        lm.lmProduction.lmMineralFillProduction.fill()



        this.mainRoomCollector.creepsCalculate()
        lm.balanceUpgrader.setNeedUpgrader()
        lm.balanceBuilderWall.setNeedBuilder()

        this.mainRoomCollector.creepsCalculateProfit()

        for (room in this.mainRoomCollector.rooms.values) {
            try {
                room.runInStartOfTick()
            } catch (e: Exception) {
                this.lm.lmMessenger.log("ERROR", "Room in start of tick", room.name, COLOR_RED)
            }
        }


        this.battleGroupContainer.runInStartOfTick()

        //Not every tick
        this.mainRoomCollector.runNotEveryTick()

        if (Game.time % 10 == 0) {
            lm.lmProduction.lmMarket.sellBuy()
        }

        if (this.setNextTickRun()) {

            this.tasks.deleteTaskDiedCreep()
            this.battleGroupContainer.runNotEveryTick()
            lm.lmProduction.lmMarket.deleteEmptyOffers()
        }

        lm.lmProduction.labRoomsReactionBalance.balancing()

        //End of tick
        //ToDo rewrite
//        for (room in mainRoomCollector.rooms.values) {
//            if (room.constant.autoDefenceArea == 0) {
//                this.logicDefence.mainRoomDefence.mainRoomDefenceArea.getArea(room)
//                break
//            }
//        }

        lm.lmDirectControl.runs()
        this.lm.lmProduction.lmLabMainRoomRun.run()
        this.battleGroupContainer.runInEndOfTick()
        this.mainRoomCollector.runInEndOfTick()
        lm.lmTerminal.transactions()
        this.tasks.toMemory()
        this.constants.toMemory()

        lm.lmMessenger.show()
    }

    private fun setNextTickRun(): Boolean {
        if (this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext > Game.time) return false
        this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext = Game.time + Random.nextInt(this.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                this.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        this.lm.lmMessenger.log("TEST", "Main context", "Main room not every tick run. Next tick: ${this.constants.globalConstant.roomRunNotEveryTickNextTickRunMainContext}", COLOR_GREEN)
        return true
    }

    fun getNumRoomWithTerminal():Int {
        return this.mainRoomCollector.rooms.values.filter {
            Game.rooms[it.name] != null
                    && it.structureTerminal[0] != null }.size
    }
}