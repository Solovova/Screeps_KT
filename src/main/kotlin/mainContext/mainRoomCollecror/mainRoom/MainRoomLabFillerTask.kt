package mainContext.mainRoomCollecror.mainRoom

import mainContext.dataclass.LabFillerTask
import mainContext.dataclass.TypeOfTask
import mainContext.tasks.CreepTask
import screeps.api.*
import screeps.api.structures.StructureLab
import screeps.utils.toMap
import kotlin.math.min

fun MainRoom.setLabFillerTask(creep: Creep) {
    val terminal = this.structureTerminal[0] ?: return
    val lab0 = this.structureLabSort[0] ?: return
    val listTasks: MutableList<LabFillerTask> = mutableListOf()
    val lab1 = this.structureLabSort[1] ?: return
    val sourceLab = arrayOf(lab0, lab1)

    if (creep.store.toMap().map { it.value }.sum() != 0) {
        val resTransfer = creep.store.toMap().filter { it.value != 0 }.toList().firstOrNull()
        if (resTransfer != null) {
            mc.tasks.add(creep.id, CreepTask(TypeOfTask.TransferTo, terminal.id, terminal.pos,
                    resource = resTransfer.first, quantity = resTransfer.second))
            return
        }
    }

    //fill energy to Lab2
    val lab2 = this.structureLabSort[2] ?: return
    val needComponent = min(lab2.store.getFreeCapacity(RESOURCE_ENERGY) ?: 0 , this.getResourceInTerminal(RESOURCE_ENERGY))
    if (needComponent >= creep.store.getCapacity()) {
        listTasks.add(listTasks.size,
                LabFillerTask(terminal, lab2, RESOURCE_ENERGY, needComponent, needComponent + 20000))
    }

    if (this.creepNeedUpgradeID != "") {
        val resourceForUpgrade = this.creepNeedUpgradeResource.unsafeCast<ResourceConstant>()
        val resourceForUpgradeQuantity = this.creepNeedUpgradeResourceQuantity
        val sourceLab2: Pair<ResourceConstant,Int> = lab2.store.toMap()
                .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                .firstOrNull() ?: Pair(resourceForUpgrade, 0)

        if (sourceLab2.first != resourceForUpgrade && sourceLab2.second != 0) {
            listTasks.add(listTasks.size,
                    LabFillerTask(lab2, terminal, sourceLab2.first, min(sourceLab2.second, creep.store.getCapacity()), min(sourceLab2.second, creep.store.getCapacity()) + 20000))
        }else{
            val needResourceForUpgradeQuantity = resourceForUpgradeQuantity - sourceLab2.second
            if (needResourceForUpgradeQuantity > 0 )
            listTasks.add(listTasks.size,
                    LabFillerTask(terminal, lab2, resourceForUpgrade, min(needResourceForUpgradeQuantity, creep.store.getCapacity()), min(needResourceForUpgradeQuantity, creep.store.getCapacity()) + 20000))
        }
    }


    if (this.constant.reactionActive != "") {
        val reaction = this.constant.reactionActive.unsafeCast<ResourceConstant>()
        if (this.structureLabSort.size !in arrayOf(3, 6, 10)) return
        val reactionComponent = mc.constants.globalConstant.labReactionComponent[reaction]
                ?: return
        if (reactionComponent.size != 2) return

        for (ind in 0..1) {
            val sourceLabInd: Pair<ResourceConstant,Int> = sourceLab[ind].store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(reactionComponent[ind], 0)
            if (sourceLabInd.second != 0
                    && sourceLabInd.first != reactionComponent[ind]) {
                val takeComponent = sourceLabInd.second
                listTasks.add(listTasks.size,
                        LabFillerTask(sourceLab[ind], terminal, sourceLabInd.first, takeComponent, takeComponent + 10000))
            } else {
                val needComponent = min(sourceLab[ind].store.getFreeCapacity(reactionComponent[ind]) ?: 0, this.getResourceInTerminal(reactionComponent[ind]))
                if (needComponent >= creep.store.getCapacity()) {
                    listTasks.add(listTasks.size,
                            LabFillerTask(terminal, sourceLab[ind], reactionComponent[ind], needComponent, needComponent))
                }
            }
        }

        for (ind in 2 until this.structureLabSort.size) {
            val lab = this.structureLabSort[ind] ?: continue
            val sourceLab: Pair<ResourceConstant,Int> = lab.store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(reaction, 0)
            if (sourceLab.second != 0
                    && sourceLab.first != reaction
                    && !(this.creepNeedUpgradeID != "" && ind == 2 && this.creepNeedUpgradeResource.unsafeCast<ResourceConstant>() == sourceLab.first)) {
                val takeComponent = sourceLab.second
                listTasks.add(listTasks.size,
                        LabFillerTask(lab, terminal, sourceLab.first, takeComponent, takeComponent + 10000))
            } else {
                val haveProduction = sourceLab.second
                if (haveProduction >= creep.store.getCapacity() &&
                        !(this.creepNeedUpgradeID != "" && ind == 2 && this.creepNeedUpgradeResource.unsafeCast<ResourceConstant>() == sourceLab.first)) {
                    listTasks.add(listTasks.size,
                            LabFillerTask(lab, terminal, reaction, haveProduction, haveProduction + 5000))
                }
            }
        }
    } else {
        for (ind in 0..1) {
            val sourceLabInd: Pair<ResourceConstant,Int> = sourceLab[ind].store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(RESOURCE_CATALYST, 0)
            if (sourceLabInd.second != 0) {
                val takeComponent = sourceLabInd.second
                listTasks.add(listTasks.size,
                        LabFillerTask(sourceLab[ind], terminal, sourceLabInd.first, takeComponent, takeComponent + 10000))
            }
        }
        for (ind in 2 until this.structureLabSort.size) {
            val lab = this.structureLabSort[ind] ?: continue
            val sourceLabInd: Pair<ResourceConstant,Int> = lab.store.toMap()
                    .filter { it.key != RESOURCE_ENERGY && it.value != 0 }.toList()
                    .firstOrNull() ?: Pair(RESOURCE_CATALYST, 0)
            if (sourceLabInd.second != 0 && !(this.creepNeedUpgradeID != "" && ind == 2 && this.creepNeedUpgradeResource.unsafeCast<ResourceConstant>() == sourceLabInd.first)) {
                val takeComponent = sourceLabInd.second
                listTasks.add(listTasks.size,
                        LabFillerTask(lab, terminal, sourceLabInd.first, takeComponent, takeComponent + 10000))
            }
        }
    }

    if (listTasks.isNotEmpty()) {
        val tmpTask: LabFillerTask? = listTasks.toMutableList().maxBy { it.priority }
        if (tmpTask != null) {
            //console.log("${tmpTask.StructureFrom}  ${tmpTask.StructureTo} ${tmpTask.quantity} ${tmpTask.resource}")
            mc.tasks.add(creep.id, CreepTask(TypeOfTask.Transport, tmpTask.StructureFrom.id, tmpTask.StructureFrom.pos,
                    tmpTask.StructureTo.id, tmpTask.StructureTo.pos, tmpTask.resource, min(creep.store.getCapacity(), tmpTask.quantity)))}
    }
}