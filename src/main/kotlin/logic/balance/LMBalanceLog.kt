package logic.balance

import mainContext.MainContext
import screeps.api.Game

class LMBalanceLog(val mainContext: MainContext) {
    var addedNew: Boolean = false
    fun show() {
        var numShow = 10
        var index = mainContext.constants.globalConstant.balanceNeedEnergy.size - 1
        var result = " "

        while (true) {
            if (index < 0 || numShow < 0) break
            result += "( ${mainContext.constants.globalConstant.balanceQtyUpgrader[index]} ) ${mainContext.constants.globalConstant.balanceNeedEnergy[index]} , "
            index--
            numShow--
        }
        mainContext.lm.lmMessenger.log("INFO", "Glob", result)
    }

    fun saveLog(qtyUpgrader: Int, needMineral: Int) {
        if (Game.time % mainContext.constants.globalConstant.balancePeriod != 0) return
        var addedNew = true

        if (mainContext.constants.globalConstant.balanceNeedEnergy.size >= mainContext.constants.globalConstant.balanceMaxSize) {
            mainContext.constants.globalConstant.balanceNeedEnergy =
                    mainContext.constants.globalConstant.balanceNeedEnergy
                            .drop(mainContext.constants.globalConstant.balanceNeedEnergy.size - mainContext.constants.globalConstant.balanceMaxSize + 1).toTypedArray()
        }

        if (mainContext.constants.globalConstant.balanceQtyUpgrader.size >= mainContext.constants.globalConstant.balanceMaxSize) {
            mainContext.constants.globalConstant.balanceQtyUpgrader =
                    mainContext.constants.globalConstant.balanceQtyUpgrader
                            .drop(mainContext.constants.globalConstant.balanceQtyUpgrader.size - mainContext.constants.globalConstant.balanceMaxSize + 1).toTypedArray()
        }

        val sizeBalanceQtyUpgrader: Int = mainContext.constants.globalConstant.balanceQtyUpgrader.size
        mainContext.constants.globalConstant.balanceQtyUpgrader[sizeBalanceQtyUpgrader] = qtyUpgrader

        val sizeBalanceNeedEnergy: Int = mainContext.constants.globalConstant.balanceNeedEnergy.size
        mainContext.constants.globalConstant.balanceNeedEnergy[sizeBalanceNeedEnergy] = needMineral
    }

    fun getUpgrader(qtyUpgraderMin: Int, qtyUpgraderMax: Int): Int {

        if (mainContext.constants.globalConstant.balanceNeedEnergy.size >= 2) {
            val oneUpgraderUse = 20500
            val qtyUpgraderNow = mainContext.constants.globalConstant.balanceQtyUpgraderNow
            val last0Energy = mainContext.constants.globalConstant.balanceNeedEnergy[mainContext.constants.globalConstant.balanceNeedEnergy.size - 1]
            val last1Energy = mainContext.constants.globalConstant.balanceNeedEnergy[mainContext.constants.globalConstant.balanceNeedEnergy.size - 2]
            var strPrediction = "Prediction "
            val energyDynamic = last1Energy - last0Energy

            strPrediction += "now: $qtyUpgraderNow "
            strPrediction += "min: $qtyUpgraderMin "
            strPrediction += "energy dynamic: $energyDynamic "


            var qtyPrediction = qtyUpgraderNow

            if (qtyPrediction <= qtyUpgraderMin) {
                qtyPrediction = qtyUpgraderMin
            } else {
                if (last0Energy > 0) {
                    if (energyDynamic < 0) {
                        qtyPrediction -= if (-energyDynamic > oneUpgraderUse * 3) {
                            2
                        } else {
                            1
                        }
                    }
                } else {
                    if (energyDynamic > 0) {
                        qtyPrediction += if (energyDynamic > oneUpgraderUse * 3) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }

            if (qtyPrediction > qtyUpgraderMax) qtyPrediction = qtyUpgraderMax

            strPrediction += "prediction: $qtyPrediction "

            mainContext.lm.lmMessenger.log("INFO", "Glob", strPrediction)

            if (addedNew) {
                addedNew = false
                //ToDo
                //mainContext.constants.globalConstant.balanceQtyUpgraderNow = strPrediction
            }
        }

        var result: Int = mainContext.constants.globalConstant.balanceQtyUpgraderNow

        if (result == -1) {
            result = mainContext.constants.globalConstant.balanceQtyUpgraderDefault

        }

        mainContext.constants.globalConstant.balanceQtyUpgraderNow = result
        return result
    }
}