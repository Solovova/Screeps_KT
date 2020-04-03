package logic.production.mineral

import mainContext.constants.GlobalConstant
import mainContext.dataclass.REACTIONS
import screeps.api.ResourceConstant

class LMMineralSetGlobalConstant {
    fun setConstant(globalConstant: GlobalConstant) {
        for (key0 in js("Object").keys(REACTIONS).unsafeCast<Array<ResourceConstant>>())
            for (key1 in js("Object").keys(REACTIONS[key0]).unsafeCast<Array<ResourceConstant>>())
                globalConstant.labReactionComponent[REACTIONS[key0][key1].unsafeCast<ResourceConstant>()] = arrayOf(key0, key1)
    }
}