package slaveRoom

import accounts.constantSlaveRoomInit
import constants.CacheCarrier
import constants.SlaveRoomConstant
import mainContext.getCacheRecordRoom
import mainRoom.MainRoom
import mainRoom.QueueSpawnRecord
import screeps.api.*
import screeps.api.structures.*
import screeps.utils.toMap
import kotlin.random.Random

external val STRUCTURE_INVADER_CORE: BuildableStructureConstant

class SlaveRoom(val parent: MainRoom, val name: String, val describe: String, val constant: SlaveRoomConstant) {
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
        constantSlaveRoomInit(this)
        if (this.constant.model == 0 && this.room != null) {
            if (this.source.size == 3) this.constant.model = 2
        }
    }

    private fun getTimeDeath(fRole: Int): Int {
        return when (fRole) {
            106 -> parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)?.timeForDeath
                    ?: 0
            108 -> parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)?.timeForDeath
                    ?: 0
            121 -> parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)?.timeForDeath
                    ?: 0
            123 -> parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)?.timeForDeath
                    ?: 0
            125 -> parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer2", this.parent, this)?.timeForDeath
                    ?: 0

            else -> 0
        }
    }

    fun buildQueue(queue: MutableList<QueueSpawnRecord>, priority: Int) {
        val fPriorityOfRole = arrayOf(10, 11, 15, 4, 0, 1, 2, 3, 5, 7, 9, 6, 8, 20, 22, 24, 21, 23, 25, 26, 27)
        for (fRole in fPriorityOfRole) {
            var fNeed = this.need[0][fRole]
            if (priority >= 1) fNeed += this.need[1][fRole]
            if (priority >= 2) fNeed += this.need[2][fRole]
            while (this.haveForQueue[fRole] < fNeed) {
                this.haveForQueue[fRole]++
                queue.add(QueueSpawnRecord(fRole + 100, this.parent.name, this.name, this.getTimeDeath(fRole + 100)))
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
                if (this.parent.room.energyCapacityAvailable >= 1500) result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
                else result = arrayOf(MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, MOVE, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY, CARRY)
            }

            103 -> {
                result = arrayOf(CLAIM, CLAIM, MOVE, MOVE)
            }

            104 -> {
                result = arrayOf(MOVE)
            }

            105, 107 -> {
                result = arrayOf(MOVE, MOVE, MOVE, WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY)
            }

            106 -> {
                val carrierAuto: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)
                if (carrierAuto == null) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            108 -> {
                val carrierAuto: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)
                if (carrierAuto == null) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
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
                val carrierAuto: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)
                if (carrierAuto == null) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer0", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            123 -> {
                val carrierAuto: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)
                if (carrierAuto == null) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer1", COLOR_RED)
                    result = arrayOf()
                } else {
                    result = carrierAuto.needBody
                }
            }

            125 -> {
                val carrierAuto: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer2", this.parent, this)
                if (carrierAuto == null) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("ERROR", this.name, "Auto not exists slaveContainer2", COLOR_RED)
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
            0 -> {
                if (this.room != null && this.constant.model == 0) {
                    val towerInvader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_TOWER }
                    if (towerInvader != null && towerInvader.isActive()) {
                        this.constant.roomHostileType = 4
                        this.constant.roomHostileNum = 5
                        this.constant.roomHostile = true
                    } else {
                        val invader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_INVADER_CORE }
                        if (invader != null) {
                            this.constant.roomHostileType = 3
                            this.constant.roomHostileNum = 1
                            this.constant.roomHostile = true
                        } else {
                            val hostileCreeps = this.room.find(FIND_HOSTILE_CREEPS)
                            this.constant.roomHostile = hostileCreeps.isNotEmpty()
                            var typeAttack = 2 //ranged
                            for (hostileCreep in hostileCreeps)
                                if (hostileCreep.body.firstOrNull { it.type == ATTACK } != null) {
                                    typeAttack = 1
                                    break
                                }
                            this.constant.roomHostileType = typeAttack
                            this.constant.roomHostileNum = hostileCreeps.size
                        }
                    }
                }

                //5 Defender
                if (this.constant.roomHostile) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("INFO", this.name, "Attacked tpe: ${this.constant.roomHostileType} num:${this.constant.roomHostileNum}", COLOR_RED)
                    if (this.constant.roomHostileNum > 2) {
                        if (this.room == null) this.need[0][4] = 1
                    } else {
                        if (this.constant.roomHostileType == 2) this.need[1][10] = this.constant.roomHostileNum
                        else this.need[1][11] = this.constant.roomHostileNum
                    }
                    return
                }

                //Reservation not my
                val protectedStructureController: StructureController? = this.structureController[0]
                if (protectedStructureController != null) {
                    val reservation = protectedStructureController.reservation
                    if (reservation != null && reservation.username == "Invader") {
                        this.need[0][4] = 1
                        return
                    }
                }


                //
                if (this.room != null) {
                    if (this.room.find(FIND_STRUCTURES).any { it.structureType == STRUCTURE_POWER_BANK }) return
                    if (this.room.find(FIND_HOSTILE_CREEPS).isNotEmpty()) return
                }


                //1 Explorer 104
                if (this.room == null) {
                    this.need[0][4] = 1
                }

                //2 Reserve 103
                if (protectedStructureController != null) {
                    val reservation = protectedStructureController.reservation
                    if (reservation != null && reservation.ticksToEnd < 2200) {
                        this.need[0][3] = 1
                    }
                    if (reservation == null) this.need[0][3] = 1
                }

                //3 Harvester 105
                if (this.source[0] != null) this.need[0][5] = 1
                if (this.source[1] != null) this.need[0][7] = 1

                //4 Carrier
                val carrierAuto0: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)
                if (carrierAuto0 != null) {
                    if (this.need[1][6] == 0) this.need[1][6] = carrierAuto0.needCarriers
                }

                val carrierAuto1: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)
                if (carrierAuto1 != null) {
                    if (this.need[1][8] == 0) this.need[1][8] = carrierAuto1.needCarriers
                }

                //4 Builder 109
                if (this.constructionSite.size > 2 && this.need[1][9] == 0 &&
                        this.structureContainerNearSource.size == this.source.size) this.need[1][9] = 2

            }
            2 -> {
                if (this.room != null && this.constant.model == 2) {
                    val towerInvader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_TOWER }
                    if (towerInvader != null ) {
                        this.constant.roomHostileType = 4
                        this.constant.roomHostileNum = 5
                        this.constant.roomHostile = true
                    } else {
                        val invader = this.room.find(FIND_HOSTILE_STRUCTURES).firstOrNull { it.structureType == STRUCTURE_INVADER_CORE }
                        if (invader != null) {
                            this.constant.roomHostileType = 3
                            this.constant.roomHostileNum = 1
                            this.constant.roomHostile = true
                        } else {
                            val hostileCreeps = this.room.find(FIND_HOSTILE_CREEPS).filter { it.name.startsWith("invader") }
                            this.constant.roomHostile = hostileCreeps.isNotEmpty()
                            var typeAttack = 2 //ranged
                            for (hostileCreep in hostileCreeps)
                                if (hostileCreep.body.firstOrNull { it.type == ATTACK } != null) {
                                    typeAttack = 1
                                    break
                                }
                            this.constant.roomHostileType = typeAttack
                            this.constant.roomHostileNum = hostileCreeps.size
                        }
                    }
                }

                //5 Defender
                if (this.constant.roomHostile) {
                    parent.mainRoomCollector.mainContext.lm.lmMessenger.log("INFO", this.name, "Attacked type: ${this.constant.roomHostileType} num:${this.constant.roomHostileNum}", COLOR_RED)
                    if (this.constant.roomHostileNum > 1) {
                        if (this.room == null) this.need[0][4] = 1
                    }
//                    else {
//                        if (this.constant.roomHostileType == 2) this.need[1][10] = 1
//                        else this.need[1][11] = 1
//                    }

                    return
                }


                this.need[0][4] = 1
                this.need[1][15] = 1

                if (this.source.containsKey(0) && this.rescueFlag.containsKey(0)) this.need[1][20] = 1
                val carrierAuto0: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer0", this.parent, this)
                //if (this.name == "E56N34") console.log("null")
                if (carrierAuto0 != null) {
                    //if (this.name == "E56N34") console.log("${carrierAuto0.needCarriers}")
                    if (this.need[1][21] == 0) this.need[1][21] = carrierAuto0.needCarriers
                }

                if (this.source.containsKey(1) && this.rescueFlag.containsKey(1)) this.need[1][22] = 1
                val carrierAuto1: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer1", this.parent, this)
                if (carrierAuto1 != null) {
                    if (this.need[1][23] == 0) this.need[1][23] = carrierAuto1.needCarriers
                }

                if (this.source.containsKey(2) && this.rescueFlag.containsKey(2)) this.need[1][24] = 1
                val carrierAuto2: CacheCarrier? = parent.mainRoomCollector.mainContext.getCacheRecordRoom("slaveContainer2", this.parent, this)
                if (carrierAuto2 != null) {
                    if (this.need[1][25] == 0) this.need[1][25] = carrierAuto2.needCarriers
                }

                //Mineral
                val mineral = this.mineral[0]
                if (mineral != null) {
                    if (mineral.mineralAmount > 0) {
                        if (parent.getResource(mineral.mineralType) < parent.constant.mineralMaxInRoom)
                            this.need[1][26] = 1
                        else parent.mainRoomCollector.mainContext.lm.lmMessenger.log("INFO", this.name, "Mineral full in parent room", COLOR_RED)
                    }

                    this.need[1][27] = this.have[26]
                }
            }
        }
    }

    fun runNotEveryTick() {
        if (!this.setNextTickRun()) return
        if (this.constant.model == 1) this.building()
        if ((this.constant.model == 0 || this.constant.model == 2) && this.constant.autoBuildRoad)
            this.constant.roadBuild = this.buildWaysInRoom()

        this.restoreSnapShot()
        this.needCleanerCalculate()
    }

    private fun setNextTickRun(): Boolean {
        if (this.constant.roomRunNotEveryTickNextTickRun > Game.time) return false
        this.constant.roomRunNotEveryTickNextTickRun = Game.time + Random.nextInt(parent.mainRoomCollector.mainContext.constants.globalConstant.roomRunNotEveryTickTicksPauseMin,
                parent.mainRoomCollector.mainContext.constants.globalConstant.roomRunNotEveryTickTicksPauseMax)
        parent.mainRoomCollector.mainContext.lm.lmMessenger.log("TEST", this.name, "Slave room not every tick run. Next tick: ${this.constant.roomRunNotEveryTickNextTickRun}", COLOR_GREEN)
        return true
    }

    fun runInStartOfTick() {
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

    private fun needClean(store: StoreDefinition?, resource: ResourceConstant): Boolean {
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



