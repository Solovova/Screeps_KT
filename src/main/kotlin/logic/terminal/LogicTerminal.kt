package logic.terminal

import mainContext.MainContext
import mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap
import kotlin.math.max
import kotlin.math.min

class LogicTerminal(val mainContext: MainContext) {
    fun doAllTransaction() {
        this.terminalSentEnergy()
        this.terminalSentEnergyFrom3To2()
        this.terminalSentMineral()
        this.terminalSentEnergyOverflow()
        this.terminalSentEnergyLevelMax()
    }

    private fun terminalSentEnergy() {
        for (roomFrom in mainContext.mainRoomCollector.rooms.values) {
            if (roomFrom.constant.sentEnergyToRoom == "") continue
            val roomTo: MainRoom = mainContext.mainRoomCollector.rooms[roomFrom.constant.sentEnergyToRoom] ?: continue
            if (roomTo.getResourceInTerminal() < 20000 && roomFrom.getResourceInTerminal() > 30000) {
                val terminalFrom: StructureTerminal = roomFrom.structureTerminal[0] ?: continue
                val terminalTo: StructureTerminal = roomTo.structureTerminal[0] ?: continue
                if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0)
                    terminalFrom.send(RESOURCE_ENERGY, 20000, roomTo.name)
            }
        }
    }

    private fun terminalSentMineral() {
        //Logic
        //needResource term, haveResource term, haveResourceInRoom term+storage
        //needResource > haveResource && needResource <= haveResourceInRoom -> wait
        //needResource < haveResource && needResource <100 -> needResource = 100


        for (roomTo in mainContext.mainRoomCollector.rooms.values) {
            val terminalTo = roomTo.structureTerminal[0] ?: continue
            if (terminalTo.cooldown != 0) continue
            for (needResourceRecord in roomTo.needMineral) {
                val needResource = needResourceRecord.key
                var needResourceQuantity = needResourceRecord.value - roomTo.getResource(needResourceRecord.key)
                //if (needResource == "GH2O".unsafeCast<ResourceConstant>())
                //   console.log("TEST Room ${roomTo.name} need: ${needResourceRecord.value} have: ${roomTo.getResource(needResourceRecord.key)} trans: $needResourceQuantity")


                if (needResourceQuantity <= 0) continue
                needResourceQuantity = max(needResourceQuantity, 100)

                val canMineralTakeTerminal = roomTo.constant.mineralAllMaxTerminal - (terminalTo.store.toMap().map { it.value }.sum()
                        - roomTo.getResourceInTerminal(RESOURCE_ENERGY))



                for (roomFrom in mainContext.mainRoomCollector.rooms.values) {
                    if (roomFrom.name == roomTo.name) continue
                    val terminalFrom = roomFrom.structureTerminal[0] ?: continue
                    if (terminalFrom.cooldown != 0) continue
                    val haveResourceQuantity = roomFrom.getResourceInTerminal(needResource)
                    val haveResourceQuantityInRoom = roomFrom.getResource(needResource) -
                            (roomFrom.needMineral[needResource] ?: 0)

                    val quantityTransfer = min(min(min(
                            haveResourceQuantity, needResourceQuantity),
                            mainContext.constants.globalConstant.sentMaxMineralQuantity),
                            canMineralTakeTerminal)

                    //if (needResource == "GH2O".unsafeCast<ResourceConstant>())
                    //    console.log("TEST Room ${roomFrom.name} transfer: $quantityTransfer")

                    if (quantityTransfer < needResourceQuantity
                            && quantityTransfer < mainContext.constants.globalConstant.sentMaxMineralQuantity
                            && quantityTransfer < haveResourceQuantityInRoom) continue  //wait because not all resource transfer from storage to terminal

                    //if (needResource == "GH2O".unsafeCast<ResourceConstant>())
                    //    console.log("TEST Room ${roomFrom.name} transfer: $quantityTransfer")

                    if (quantityTransfer < 100) continue
                    //One transfer per tick

                    //if (needResource == "GH2O".unsafeCast<ResourceConstant>())
                    //   console.log("TEST Transfer Room ${roomFrom.name} transfer: $quantityTransfer roomTo: ${roomTo.name}")
                    val result = terminalFrom.send(needResource, quantityTransfer, roomTo.name)
                    //console.log(result)
                    if (result == OK) return
                }
            }
        }
    }

    private fun terminalSentEnergyOverflow() {
        val sentQuantity = 5000
        val emergencyMineralQuantity = 30000

        //Emergency to
        var mainRoomTo: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() < emergencyMineralQuantity
        }.minBy { it.getResource() }

        var mainRoomFrom: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.getResource() > it.constant.energyExcessSent
        }.maxBy { it.getResource() - it.constant.energyExcessSent }

        if (mainRoomTo != null) {
            if (mainRoomFrom == null) {
                mainRoomFrom = mainContext.mainRoomCollector.rooms.values.filter {
                    it.constant.levelOfRoom >= 2
                            && it.getResource() > emergencyMineralQuantity
                }.maxBy { it.getResource() }
            }
        } else {
            mainRoomTo = mainContext.mainRoomCollector.rooms.values.filter {
                it.constant.levelOfRoom >= 2
                        && it.getResource() < it.constant.energyExcessSent
            }.minBy { it.getResource() - it.constant.energyExcessSent }
        }

        if (mainRoomFrom != null && mainRoomTo != null) {
            val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
            val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return
            if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
                val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
                if (result == OK)
                    mainContext.logicMessenger.messenger("INFO", mainRoomFrom.name,
                            "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}", COLOR_GREEN)
            }
        }
    }

    private fun terminalSentEnergyFrom3To2() {
        val sentQuantity = 5000
        val energyMinQuantityIn2 = 120000
        val energyMinQuantityIn3 = 80000

        //Emergency to
        val mainRoomTo: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.constant.levelOfRoom == 2
                    && it.getResource() < energyMinQuantityIn2
        }.minBy { it.getResource() }

        val mainRoomFrom: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom == 3
                    && it.getResource() > energyMinQuantityIn3
        }.maxBy { it.getResource() - energyMinQuantityIn3 }



        if (mainRoomFrom != null && mainRoomTo != null) {
            val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
            val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return
            if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
                val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
                if (result == OK)
                    mainContext.logicMessenger.messenger("INFO", mainRoomFrom.name,
                            "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}", COLOR_GREEN)
            }
        }
    }

    private fun terminalSentEnergyLevelMax() {
        val sentQuantity = 5000
        val energyMinQuantityTo = 120000
        val energyMinQuantityFrom = 140000

        //Emergency to
        val mainRoomTo: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.constant.levelOfRoom == 3
                    && it.getResource() < energyMinQuantityTo
        }.minBy { it.getResource() }

        val mainRoomFrom: MainRoom? = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom == 3
                    && it.getResource() > energyMinQuantityFrom
        }.maxBy { it.getResource() - energyMinQuantityFrom }



        if (mainRoomFrom != null && mainRoomTo != null) {
            val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
            val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return
            if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
                val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
                if (result == OK)
                    mainContext.logicMessenger.messenger("INFO", mainRoomFrom.name,
                            "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}", COLOR_GREEN)
            }
        }
    }
}