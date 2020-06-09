import mainContext.MainContext
import screeps.api.*
import kotlin.math.roundToInt

var mainContextGlob : MainContext? = null

@Suppress("unused")
fun loop() {
    if (Memory["account"] == "" || Memory["account"] == null) {
        //Memory["account"] = ""
        if (Game.rooms["E54N37"] != null)  Memory["account"] = "main"
        if (Game.rooms["W5N7"] != null)  Memory["account"] = "main2"
        ///if (Game.rooms["W8N3"] != null)    Memory["account"] = "test2"
        if (Game.rooms["W5N3"] != null)    Memory["account"] = "test"
    }



    // Initialisation and protect mainContext
    if (mainContextGlob == null) {
        mainContextGlob = MainContext()
    }

    val protectedMainContext = mainContextGlob ?: return

    //println("Current game tick is ${Game.time} _________________________________________")
    protectedMainContext.lm.lmMessenger.log("HEAD", "", "Current game tick is ${Game.time} _________________________________________", COLOR_WHITE)

    // Start tick functions
    var cpuStart = Game.cpu.getUsed()
    protectedMainContext.run()
    cpuStart = Game.cpu.getUsed() - cpuStart

    console.log("Construction sites: ${Game.constructionSites.size}")

    console.log("CPU: ${(cpuStart).roundToInt()}   Creep: ${Memory["CPUCreep"]}")
}