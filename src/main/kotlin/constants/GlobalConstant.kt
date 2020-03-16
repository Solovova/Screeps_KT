package constants


import screeps.api.*

class GlobalConstant(val constants: Constants) {
    val dataCacheCarrierAuto: MutableMap<String, CacheCarrier> = mutableMapOf() //cashed
    val roomRunNotEveryTickTicksPauseMin: Int = 300
    val roomRunNotEveryTickTicksPauseMax: Int = 400
    var roomRunNotEveryTickNextTickRunMainContext: Int = 0
    val buttleGroupList: MutableList<String> = MutableList(0){""}
    val sentMaxMineralQuantity: Int = 10000


    //Market
    val marketMinCreditForOpenBuyOrder: Double = 100000.0
    val marketBuyPriceEnergy = 0.052

    //INFO
    val showProfitWhenLessWhen: Int = 6000

    //CreepUpgrades
    //if in room set it more priority
    val creepUpgradableParts: MutableMap<Int, Map<BodyPartConstant,ResourceConstant>> = mutableMapOf()
    val labReactionComponent: MutableMap<ResourceConstant,Array<ResourceConstant>> = mutableMapOf()

    init {
        constants.mainContext.logicUpgrade.setGlobalConstants(this)
        constants.mainContext.logicLab.setGlobalConstants(this)
    }


    fun toDynamic(): dynamic {
        val result: dynamic = object {}
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
        //dataCacheCarrierAuto
        if (d["dataCacheCarrierAuto"] != null)
            for (recordKey in js("Object").keys(d["dataCacheCarrierAuto"]).unsafeCast<Array<String>>())
                dataCacheCarrierAuto[recordKey] = CacheCarrier.initFromDynamic(d["dataCacheCarrierAuto"][recordKey])
    }
}