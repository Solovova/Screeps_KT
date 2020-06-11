package logic.terminal

import mainContext.MainContext
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import screeps.api.*
import screeps.api.structures.StructureTerminal
import screeps.utils.toMap
import kotlin.math.max
import kotlin.math.min

class LMTerminal(val mainContext: MainContext) {
    private val emergencyMineralQuantity = 30000
    private val sentQuantity = 5000

    fun transactions() {
        this.terminalSentEnergyEmergency()
        this.terminalSentEnergyForBuild()
        this.terminalSentMineral()

        this.terminalSentEnergyExcessSent()
        this.terminalSentEnergyFrom3To2()
        this.terminalSentEnergyStorageFullSent()
    }

    private fun terminalSentFromTo(mainRoomFrom: MainRoom, mainRoomTo: MainRoom, describe: String) {

        val terminalFrom: StructureTerminal = mainRoomFrom.structureTerminal[0] ?: return
        val terminalTo: StructureTerminal = mainRoomTo.structureTerminal[0] ?: return

        if (terminalFrom.cooldown == 0 && terminalTo.cooldown == 0) {
            //
            val cost = Game.market.calcTransactionCost(sentQuantity, mainRoomFrom.name, mainRoomTo.name)
            if (Memory["transCost"] == null) {
                Memory["transCost"] = 0
                Memory["transCount"] = 0
                Memory["transStartTick"] = Game.time
            }
            Memory["transCost"] = Memory["transCost"] + cost
            Memory["transCount"] = Memory["transCount"] + 1
            //
            val result = terminalFrom.send(RESOURCE_ENERGY, sentQuantity, mainRoomTo.name)
            if (result == OK) {
                mainContext.lm.lmMessenger.log("INFO", mainRoomFrom.name,
                        "Send energy $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}   $describe", COLOR_GREEN)
            } else {
                mainContext.lm.lmMessenger.log("ERROR", mainRoomFrom.name,
                        "Send energy Error: $result cost: $cost quantity: $sentQuantity from ${mainRoomFrom.name} $sentQuantity -> ${mainRoomTo.name}   $describe", COLOR_GREEN)
            }

        }
    }

    private fun terminalSentMineral() {
        for (roomTo in mainContext.mainRoomCollector.rooms.values) {
            val terminalTo = roomTo.structureTerminal[0] ?: continue
            if (terminalTo.cooldown != 0) continue
            for (needResourceRecord in roomTo.needMineral) {
                val needResource = needResourceRecord.key
                var needResourceQuantity = needResourceRecord.value - roomTo.getResource(needResourceRecord.key)

                val canMineralTakeTerminal = roomTo.constant.mineralAllMaxTerminal - (terminalTo.store.toMap().map { it.value }.sum() - roomTo.getResourceInTerminal(RESOURCE_ENERGY))

                needResourceQuantity = min(needResourceQuantity, canMineralTakeTerminal)

                if (needResourceQuantity <= 0) continue
                needResourceQuantity = max(needResourceQuantity, 100)

                //println("Test" + roomTo.name + ":" + needResource +" " +needResourceQuantity)
                val roomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values
                        .filter {
                            it.name != roomTo.name
                                    && it.structureTerminal[0] != null
                                    && it.structureTerminal[0]?.cooldown == 0
                                    && (it.getResource(needResource) - (it.needMineral[needResource]
                                    ?: 0)) > 100
                        }
                        .maxBy {
                            it.getResource(needResource) - (it.needMineral[needResource] ?: 0)
                        }
                        ?: continue


                val haveResourceQuantityInTerminal = roomFrom.getResourceInTerminal(needResource)
                val haveResourceQuantity = roomFrom.getResource(needResource) - (roomFrom.needMineral[needResource]
                        ?: 0)

                val quantityTransfer = min(min(
                        haveResourceQuantity, needResourceQuantity),
                        mainContext.constants.globalConstant.sentMaxMineralQuantity)
                //println("Test<----" + roomFrom.name + " " + quantityTransfer + " have in terminal:"+ haveResourceQuantityInTerminal)
                //wait because not all resource transfer from storage to terminal
                if (haveResourceQuantityInTerminal < quantityTransfer) continue
                if (quantityTransfer < 100) continue
                val terminalFrom: StructureTerminal = roomFrom.structureTerminal[0]
                        ?: continue
                val result = terminalFrom.send(needResource, quantityTransfer, roomTo.name)
                if (result == OK) {
                    mainContext.lm.lmMessenger.log("INFO", roomFrom.name,
                            "Send $needResource $quantityTransfer from ${roomFrom.name} -> ${roomTo.name} ", COLOR_YELLOW)
                    return
                }
            }
        }

    }

    private fun terminalSentEnergyForBuild() {

        //Build to
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() < 100000
                    && it.constructionSite.isNotEmpty()
        }.minBy { it.getResource() }
                ?: return

        //Take max room resource, but priority lvl3
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom >= 2
                    && it.getResource() > emergencyMineralQuantity
                    && it.name != mainRoomTo.name
        }.maxBy {
            it.getResource() + if (it.constant.levelOfRoom == 3) {
                1000000
            } else {
                0
            }
        }
                ?: return


        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "For building")
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


        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "Emergency")
    }

    private fun terminalSentEnergyFrom3To2() {
        val energyMinQuantityIn2 = 120000
        val energyMinQuantityIn3 = 80000

        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.constant.levelOfRoom == 2
                    && it.getResource() < energyMinQuantityIn2
        }.minBy { it.getResource() }
                ?: return

        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.constant.levelOfRoom == 3
                    && it.getResource() > energyMinQuantityIn3
        }.maxBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "From3To2")
    }

    private fun terminalSentEnergyExcessSent() {
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() > it.constant.energyExcessSent
        }.maxBy { it.getResource() }
                ?: return

        //Upgrade and less when energyUpgradeDefence
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && ((it.constant.defenceNeedUpgrade && it.getResource() < it.constant.energyUpgradeDefence)
                    || it.getResource() < it.constant.energyUpgradeLvl8Controller)
                    && it.name != mainRoomFrom.name
        }.minBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "ExcessSent")
    }

    private fun terminalSentEnergyStorageFullSent() {
        val mainRoomFrom: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() > 500_000
        }.maxBy { it.getResource() }
                ?: return

        //Upgrade and less when energyUpgradeDefence
        val mainRoomTo: MainRoom = mainContext.mainRoomCollector.rooms.values.filter {
            it.structureTerminal[0] != null
                    && it.getResource() < 450_000
                    && it.name != mainRoomFrom.name
        }.minBy { it.getResource() }
                ?: return

        this.terminalSentFromTo(mainRoomFrom, mainRoomTo, "StorageFullSent")
    }
}