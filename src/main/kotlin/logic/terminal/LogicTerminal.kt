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
        this.terminalSentEnergyEmergency()
        this.terminalSentMineral()

        this.terminalSentEnergyExcessSent()
        this.terminalSentEnergyFrom3To2()
    }

    private fun terminalSentFromTo(mainRoomFrom: MainRoom, mainRoomTo: MainRoom, describe: String) {
        val sentQuantity = 5000
        val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
        val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return
        if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
            //
            val cost = Game.market.calcTransactionCost(sentQuantity,mainRoomFrom.name,mainRoomTo.name)
            if (Memory["transCost"] == null) {
                Memory["transCost"] = 0
                Memory["transCount"] = 0
                Memory["transStartTick"] = Game.time
            }
            Memory["transCost"] = Memory["transCost"] + cost
            Memory["transCount"] = Memory["transCount"] + 1
            //
            val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
            if (result == OK)
                mainContext.logicMessenger.messenger("INFO", mainRoomFrom.name,
                        "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}   $describe", COLOR_GREEN)
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

    private fun terminalSentEnergyEmergency() {
        val emergencyMineralQuantity = 30000

        //Emergency to
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() < emergencyMineralQuantity
        }.minBy { it.getResource() }
                ?: return

        //Take max room resource, but priority lvl3
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.getResource() > emergencyMineralQuantity
        }.maxBy {
            it.getResource() + if (it.constant.levelOfRoom == 3) {
                1000000
            } else {
                0
            }
        }
                ?: return


        this.terminalSentFromTo(mainRoomFrom, mainRoomTo,"Emergency")
    }

    private fun terminalSentEnergyFrom3To2() {
        val energyMinQuantityIn2 = 120000
        val energyMinQuantityIn3 = 80000

        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.constant.levelOfRoom == 2
                    && it.getResource() < energyMinQuantityIn2
        }.minBy { it.getResource() }
                ?:return

        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom == 3
                    && it.getResource() > energyMinQuantityIn3
        }.maxBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo,"From3To2")
    }

    private fun terminalSentEnergyExcessSent() {
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() > it.constant.energyExcessSent
        }.maxBy { it.getResource()}
                ?: return

        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() < it.constant.energyExcessSent
        }.minBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo,"ExcessSent")
    }
}