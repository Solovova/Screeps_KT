import mainContext.MainContext
import screeps.api.*
import kotlin.math.roundToInt

var mainContextGlob : MainContext? = null

@Suppress("unused")
fun loop() {

    Memory["account"] = ""
    if (Game.rooms["E54N37"] != null)  Memory["account"] = "main"
    if (Game.rooms["W5N3"] != null)    Memory["account"] = "test"
    if (Game.rooms["W8N3"] != null)    Memory["account"] = "test2"

    val cpuStart = Game.cpu.getUsed()

    // Initialisation and protect mainContext
    if (mainContextGlob == null) {
//        for (creep in Game.creeps.values) {
//            creep.suicide()}

        mainContextGlob = MainContext()
    }

    val protectedMainContext = mainContextGlob ?: return

    protectedMainContext.lm.lmMessenger.log("HEAD", "", "Current game tick is ${Game.time} _________________________________________", COLOR_WHITE)

    // Start tick functions
    var cpuStartMCStart = Game.cpu.getUsed()
    protectedMainContext.run()
    cpuStartMCStart = Game.cpu.getUsed() - cpuStartMCStart

    console.log("Construction sites: ${Game.constructionSites.size}")

    console.log("CPU: ${(Game.cpu.getUsed() - cpuStart).roundToInt()}   Creep: ${Memory["CPUCreep"]} McStart: ${cpuStartMCStart.roundToInt()}")
}