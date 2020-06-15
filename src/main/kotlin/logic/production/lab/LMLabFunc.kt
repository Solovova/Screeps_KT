package logic.production.lab

import mainContext.MainContext
import screeps.api.ResourceConstant

class LMLabFunc(val mc: MainContext) {
    fun getReactionCompounds(reaction: ResourceConstant):List<ResourceConstant> {
        val reactionCompounds: Array<ResourceConstant> = mc.constants.globalConstant.labReactionComponent[reaction]
                ?: return listOf()
        return reactionCompounds.toList()
    }
}