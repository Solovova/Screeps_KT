package mainContext.mainRoomCollecror.mainRoom.slaveRoom

import constants.CacheCarrier
import mainContext.MainContext
import mainContext.constants.SlaveRoomConstant
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.QueueSpawnRecord
import screeps.api.*
import screeps.api.structures.*
import screeps.utils.toMap
import kotlin.random.Random

external val STRUCTURE_INVADER_CORE: BuildableStructureConstant

class SlaveRoom(val mc: MainContext, val mr: MainRoom, val name: String, val describe: String, val constant: SlaveRoomConstant) {
    val room: Room? = Game.rooms[this.name]

    val need = Array(3) { Array(100) { 0 } }
    val have = Array(100) { 0 }
    val haveForQueue = Array(100) { 0 }

    //StructureController
    private var _structureController: Map<Int, StructureController>? = null
    val structureController: Map<Int, StructureController>
        get() {
            if (this.room == null)
                _structureController = mapOf()
            else
                if (this._structureController == null)
                    _structureController = this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTROLLER }.withIndex().associate { it.index to it.value as StructureController }

            return _structureController ?: throw AssertionError("Error get StructureController")
        }

    //Source
    private var _source: Map<Int, Source>? = null
    val source: Map<Int, Source>
        get() {
            if (this.room == null)
                _source = mapOf()
            else
                if (this._source == null) {
                    _source = this.room.find(FIND_SOURCES).sortedWith(Comparator<Source> { a, b ->
                        when {
                            a.id > b.id -> 1
                            a.id < b.id -> -1
                            else -> 0
                        }
                    }).withIndex().associate { it.index to it.value }
                }


            return _source ?: throw AssertionError("Error get Source")
        }

    //ConstructionSite
    private var _constructionSite: Map<String, ConstructionSite>? = null
    val constructionSite: Map<String, ConstructionSite>
        get() {
            if (this.room == null)
                _constructionSite = mapOf()
            else
                if (this._constructionSite == null)
                    _constructionSite = this.room.find(FIND_CONSTRUCTION_SITES).associateBy { it.id }

            return _constructionSite ?: throw AssertionError("Error get ConstructionSite")
        }

    //StructureContainer //ToDo test
    private var _structureContainer: Map<String, StructureContainer>? = null
    val structureContainer: Map<String, StructureContainer>
        get() {
            if (this.room == null)
                _structureContainer = mapOf()
            else
                if (this._structureContainer == null)
                    _structureContainer = this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_CONTAINER }.associate { it.id to it as StructureContainer }

            return _structureContainer ?: throw AssertionError("Error get StructureContainer")
        }

    //StructureContainerNearSource //ToDo test
    private var _structureContainerNearSource: Map<Int, StructureContainer>? = null //id source
    val structureContainerNearSource: Map<Int, StructureContainer>
        get() {
            if (this._structureContainerNearSource == null) {
                val resultContainer = mutableMapOf<Int, StructureContainer>()
                for (sourceRec in this.source)
                    for (container in this.structureContainer.values)
                        if (!resultContainer.containsValue(container) && sourceRec.value.pos.inRangeTo(container.pos, 2))
                            resultContainer[sourceRec.key] = container
                _structureContainerNearSource = resultContainer
            }
            return _structureContainerNearSource
                    ?: throw AssertionError("Error get StructureContainerNearSource")
        }

    //StructureStorage
    private var _structureStorage: Map<Int, StructureStorage>? = null
    val structureStorage: Map<Int, StructureStorage>
        get() {
            if (this.room == null)
                _structureStorage = mapOf()
            else if (this._structureStorage == null)
                _structureStorage = this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_STORAGE }.withIndex().associate { it.index to it.value as StructureStorage }
            return _structureStorage ?: throw AssertionError("Error get StructureStorage")
        }

    //Mineral
    private var _mineral: Map<Int, Mineral>? = null
    val mineral: Map<Int, Mineral>
        get() {
            if (this.room == null)
                _mineral = mapOf()
            else if (this._mineral == null) {
                val result: MutableMap<Int, Mineral> = mutableMapOf()
                result[0] = this.room.find(FIND_MINERALS).first()
                _mineral = result.toMap()
            }
            return _mineral ?: throw AssertionError("Error get Mineral")
        }

    //StructureExtractor
    private var _structureExtractor: Map<Int, StructureExtractor>? = null
    val structureExtractor: Map<Int, StructureExtractor>
        get() {
            if (this.room == null)
                _structureExtractor = mapOf()
            else if (this._structureExtractor == null)
                _structureExtractor = this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_EXTRACTOR }.withIndex().associate { it.index to it.value as StructureExtractor }
            return _structureExtractor ?: throw AssertionError("Error get StructureExtractor")
        }

    //StructureKeeperLair
    private var _structureKeeperLair: Map<Int, StructureKeeperLair>? = null
    val structureKeeperLair: Map<Int, StructureKeeperLair>
        get() {
            if (this.room == null)
                _structureKeeperLair = mapOf()
            else
                if (this._structureKeeperLair == null) {
                    val result: MutableMap<Int, StructureKeeperLair> = mutableMapOf()
                    val tmpKeeperLairs: List<Structure> = this.room.find(FIND_STRUCTURES).filter { it.structureType == STRUCTURE_KEEPER_LAIR }
                    for (sourceRecord in this.source)
                        for (keeperLair in tmpKeeperLairs)
                            if (sourceRecord.value.pos.inRangeTo(keeperLair.pos, 6)) {
                                result[sourceRecord.key] = keeperLair as StructureKeeperLair
                                break
                            }
                    val tmpMineral = this.mineral[0]
                    if (tmpMineral != null)
                        for (keeperLair in tmpKeeperLairs)
                            if (tmpMineral.pos.inRangeTo(keeperLair.pos, 6)) {
                                result[3] = keeperLair as StructureKeeperLair
                                break
                            }
                    _structureKeeperLair = result.toMap()
                }
            return _structureKeeperLair ?: throw AssertionError("Error get StructureKeeperLair")
        }

    //RescueFlag
    private var _rescueFlag: Map<Int, Flag>? = null
    val rescueFlag: Map<Int, Flag>
        get() {
            if (this.room == null)
                _rescueFlag = mapOf()
            else
                if (this._rescueFlag == null) {
                    val result: MutableMap<Int, Flag> = mutableMapOf()
                    val tmpFlags: List<Flag> = this.room.find(FIND_FLAGS).filter { it.color == COLOR_WHITE && it.secondaryColor == COLOR_WHITE }
                    for (sourceRecord in this.source)
                        for (flag in tmpFlags)
                            if (sourceRecord.value.pos.inRangeTo(flag.pos, 8)) {
                                result[sourceRecord.key] = flag
                                break
                            }
                    val tmpMineral = this.mineral[0]
                    if (tmpMineral != null)
                        for (flag in tmpFlags)
                            if (tmpMineral.pos.inRangeTo(flag.pos, 8)) {
                                result[3] = flag
                                break
                            }
                    _rescueFlag = result.toMap()
                }
            return _rescueFlag ?: throw AssertionError("Error get RescueFlag")
        }


    init {
        mc.constants.accountInit.initSlaveRoom(this)
        if (this.constant.model == SlaveRoomType.normal && this.room != null) {
            if (this.source.size == 3) this.constant.model = SlaveRoomType.dangeon
        }
    }

    private fun getTimeDeath(fRole: Int): Int {
        return when (fRole) {
            106 -> mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)?.timeForDeath
                    ?: 0
            108 -> mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)?.timeForDeath
                    ?: 0
            121 -> mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)?.timeForDeath
                    ?: 0
            123 -> mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)?.timeForDeath
                    ?: 0
            125 -> mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer2", this.mr, this)?.timeForDeath
                    ?: 0

            else -> 0
        }
    }

    fun buildQueue(queue: MutableList<QueueSpawnRecord>, priority: Int) {
        val fPriorityOfRole = arrayOf(10, 11, 15, 4, 28, 0, 1, 2, 3, 5, 7, 9, 6, 8, 27, 20, 22, 24, 21, 23, 25, 26)
        for (fRole in fPriorityOfRole) {
            var fNeed = this.need[0][fRole]
            if (priority >= 1) fNeed += this.need[1][fRole]
            if (priority >= 2) fNeed += this.need[2][fRole]
            while (this.haveForQueue[fRole] < fNeed) {
                this.haveForQueue[fRole]++
                queue.add(QueueSpawnRecord(fRole + 100, this.mr.name, this.name, this.getTimeDeath(fRole + 100)))
            }
        }
    }

    fun getBodyRole(role: Int): Array<BodyPartConstant> {
        var result: Array<BodyPartConstant> = arrayOf()

        when (role) {
            100 -> {
                result = arrayOf(MOVE, MOVE, CLAIM)
            }

            101 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            102 -> {
                if (this.mr.room.energyCapacityAvailable >= 1500) result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
                else result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            103 -> {
                if (this.mr.room.energyCapacityAvailable < 3900) result = arrayOf(CLAIM, CLAIM, MOVE, MOVE)
                else result = arrayOf(MOVE,MOVE,MOVE,MOVE,MOVE,MOVE,CLAIM,CLAIM,CLAIM,CLAIM,CLAIM,CLAIM)

            }

            104 -> {
                result = arrayOf(MOVE)
            }

            105, 107 -> {
                result = arrayOf(MOVE, MOVE, MOVE, WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY)
            }

            106 -> {
                val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)
                if (carrierAuto == null) {
                    mc.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            108 -> {
                val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)
                if (carrierAuto == null) {
                    mc.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            109 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, WORK, WORK, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            110 -> {
                result = arrayOf(TOUGH, TOUGH, TOUGH, TOUGH, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, RANGED_ATTACK, RANGED_ATTACK, RANGED_ATTACK, RANGED_ATTACK)
            }

            111 -> {
                result = arrayOf(TOUGH, TOUGH, TOUGH, TOUGH, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK)
            }

            115 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, ATTACK, HEAL, HEAL, HEAL, HEAL, HEAL)
            }

            120, 122, 124 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            121 -> {
                val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer0", this.mr, this)
                if (carrierAuto == null) {
                    mc.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            123 -> {
                val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer1", this.mr, this)
                if (carrierAuto == null) {
                    mc.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer1", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            125 -> {
                val carrierAuto: CacheCarrier? = mc.lm.lmHarvestCacheRecordRoom.gets("slaveContainer2", this.mr, this)
                if (carrierAuto == null) {
                    mc.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer2", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            126 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            127 -> {
                result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            128 -> {
                if (this.mr.room.energyCapacityAvailable < 3900) result = arrayOf(CLAIM, CLAIM, MOVE, MOVE)
                else result = arrayOf(MOVE,MOVE,MOVE,MOVE,MOVE,MOVE,CLAIM,CLAIM,CLAIM,CLAIM,CLAIM,CLAIM)
            }
        }
        return result
    }

    fun getConstructionSite(pos: RoomPosition): ConstructionSite? {
        var tObject: ConstructionSite? = null
        var tMinRange = 1000
        for (construct in this.constructionSite.values) {
            val tTmpRange = pos.getRangeTo(construct.pos)
            if (tTmpRange < tMinRange) {
                tMinRange = tTmpRange
                tObject = construct
            }
        }
        return tObject
    }

    fun needCorrection() {
        when (this.constant.model) {
            SlaveRoomType.normal -> this.correctionNormal()
            SlaveRoomType.dangeon -> this.correctionDangeon()

            SlaveRoomType.central -> this.correctionCentral()
            SlaveRoomType.colonize -> {}
        }
    }

    fun runNotEveryTick() {
        if (!this.setNextTickRun()) return
        if (this.constant.model == SlaveRoomType.colonize) this.building()
        if ((this.constant.model in setOf(SlaveRoomType.central,SlaveRoomType.dangeon,SlaveRoomType.normal)) && this.constant.autoBuildRoad)
            this.constant.roadBuild = this.buildWaysInRoom()

        this.restoreSnapShot()
        this.needCleanerCalculate()
    }

    private fun setNextTickRun(): Boolean {
        if (this.constant.roomRunNotEveryTickNextTickRun > Game.time) return false
        this.constant.roomRunNotEveryTickNextTickRun = Game.time + Random.nextInt(mc.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                mc.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        mc.lm.lmMessenger.log("TEST", this.name, "Slave room not every tick run. Next tick: ${this.constant.roomRunNotEveryTickNextTickRun}", COLOR_GREEN)
        return true
    }

    fun runInEndOfTick() {
        this.directControl()
    }

    fun profitClear() {
        this.constant.profitUp = 0
        this.constant.profitDown = 0
        this.constant.profitStart = Game.time
        console.log("clear")
    }

    fun profitMinus(role: Int) {
        this.constant.profitDown += getBodyRole(role).sumBy { BODYPART_COST[it] ?: 0 }
    }

    fun profitPlus(put: Int) {
        this.constant.profitUp += put
    }

    private fun needClean(store: Store?, resource: ResourceConstant): Boolean {
        if (store == null) return false
        return (store[resource] ?: 0) != (store.toMap().map { it.value }.sum())
    }

    fun needCleanerCalculate() {
        var result = false
        if (!result) result = needClean(this.structureContainerNearSource[0]?.store, RESOURCE_ENERGY)
        if (!result) result = needClean(this.structureContainerNearSource[1]?.store, RESOURCE_ENERGY)
        if (!result) result = needClean(this.structureContainerNearSource[2]?.store, RESOURCE_ENERGY)
        this.constant.needCleaner = result
    }
}



