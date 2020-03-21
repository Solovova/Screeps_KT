import mainContext.MainContext
import mainContext.messenger
import screeps.api.*
import kotlin.math.roundToInt

var mainContextGlob : MainContext? = null

@Suppress("unused")
fun loop() {

    Memory["account"] = ""
    if (Game.rooms["E54N37"] != null)  Memory["account"] = "main"
    if (Game.rooms["W5N3"] != null)    Memory["account"] = "test"

    val cpuStart = Game.cpu.getUsed()

    // Initialisation and protect mainContext
    if (mainContextGlob == null) {
        //Game.market.createOrder(ORDER_BUY,RESOURCE_KEANIUM,0.038,100000,"E54N39")
        //Game.market.changeOrderPrice("5e72724b3265835947152333",0.04)
        //Game.market.createOrder(ORDER_BUY, RESOURCE_HYDROGEN,0.014,200000,"E52N38")
        //Game.market.cancelOrder("5e766ac39ae3874f2271f49b")
        mainContextGlob = MainContext()
    }

    val protectedMainContext = mainContextGlob ?: return



    protectedMainContext.messenger("HEAD", "", "Current game tick is ${Game.time} _________________________________________", COLOR_WHITE)

    // Start tick functions
    var cpuStartMCStart = Game.cpu.getUsed()
    protectedMainContext.runInStartOfTick()
    cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart

    var cpuStartMCNotEvery = Game.cpu.getUsed()
    protectedMainContext.runNotEveryTick()
    cpuStartMCNotEvery = Game.cpu.getUsed() - cpuStartMCNotEvery
    // Testing functions
    testingFunctions(protectedMainContext)

    // End tick functions
    var cpuStartMCEnd = Game.cpu.getUsed()
    protectedMainContext.runInEndOfTick()
    cpuStartMCEnd = Game.cpu.getUsed() - cpuStartMCEnd

    console.log("Construction sites: ${Game.constructionSites.size}")

    console.log("CPU: ${(Game.cpu.getUsed() - cpuStart).roundToInt()}   Creep: ${Memory["CPUCreep"]} McStart: ${cpuStartMCStart.roundToInt()} McNotEvery: ${cpuStartMCNotEvery.roundToInt()} McEnd: ${(cpuStartMCEnd - Game.cpu.getUsed() + cpuStart).roundToInt()}")
}