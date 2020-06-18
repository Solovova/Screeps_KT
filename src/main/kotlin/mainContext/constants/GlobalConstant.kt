package mainContext.constants


import constants.CacheCarrier
import screeps.api.*

class GlobalConstant(val constants: Constants) {
    var username: String = ""
    var nukerFill: Boolean = false
    var nukerFilInRooms: Array<String> = arrayOf()

    val dataCacheCarrierAuto: MutableMap<String, CacheCarrier> = mutableMapOf() //cashed
    val roomRunNotEveryTickTicksPauseMin: Int = 300
    val roomRunNotEveryTickTicksPauseMax: Int = 400
    var roomRunNotEveryTickNextTickRunMainContext: Int = 0
    val battleGroupList: MutableList<String> = MutableList(0){""}
    val sentMaxMineralQuantity: Int = 10000

    var gcl: Int = 0  //cashed
    var gclFromTick: Int = 0 //cashed
    var gclArray: Array<Int> = arrayOf() //cashed
    var gclArrayMaxSize: Int = 100
    var gclPeriod: Int = 1000

    //Market
    val marketMinCreditForOpenBuyOrder: Double = 100000.0
    val marketBuyPriceEnergy = 0.052 //ToDo auto calculate

    //INFO
    val showProfitWhenLessWhen: Int = 6000

    //CreepUpgrades
    //if in room set it more priority
    val creepUpgradablePartsRange:MutableMap<Int,Map<BodyPartConstant,List<Pair<Int,ResourceConstant>>>> = mutableMapOf()
    val labReactionComponent: MutableMap<ResourceConstant,Array<ResourceConstant>> = mutableMapOf()

    var defenceLimitUpgrade: Int = 17_000_000

    init {
        constants.mainContext.lm.lmProduction.lmMineralSetGlobalConstant.setConstant(this)
    }


    fun toDynamic(): dynamic {
        val result: dynamic = object {}
        result["gcl"] = this.gcl
        result["gclFromTick"] = this.gclFromTick
        result["gclArray"] = this.gclArray
        result["roomRunNotEveryTickNextTickRunMainContext"] = this.roomRunNotEveryTickNextTickRunMainContext
        //dataCacheCarrierAuto
        result["dataCacheCarrierAuto"] = object {}
        for (record in dataCacheCarrierAuto)
            result["dataCacheCarrierAuto"][record.key] = record.value.toDynamic()


        //--------------------
        return result
    }

    fun fromDynamic(d: dynamic) {
        if (d["roomRunNotEveryTickNextTickRunMainContext"] != null) this.roomRunNotEveryTickNextTickRunMainContext = d["roomRunNotEveryTickNextTickRunMainContext"] as Int

        if (d["gcl"] != null) this.gcl = d["gcl"] as Int
        if (d["gclFromTick"] != null) this.gclFromTick = d["gclFromTick"] as Int
        if (d["gclArray"] != null) this.gclArray = d["gclArray"] as Array<Int>

        //dataCacheCarrierAuto
        if (d["dataCacheCarrierAuto"] != null)
            for (recordKey in js("Object").keys(d["dataCacheCarrierAuto"]).unsafeCast<Array<String>>())
                dataCacheCarrierAuto[recordKey] = CacheCarrier.initFromDynamic(d["dataCacheCarrierAuto"][recordKey])
    }
}