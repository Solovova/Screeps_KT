package mainContext.dataclass

import screeps.api.ResourceConstant
import screeps.api.structures.Structure

data class LabFillerTask(val StructureFrom: Structure,
                         val StructureTo: Structure,
                         val resource: ResourceConstant,
                         val quantity: Int,
                         val priority: Int)