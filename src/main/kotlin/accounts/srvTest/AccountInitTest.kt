package accounts.srvTest

import accounts.AccountInit
import mainContext.MainContext
import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

class AccountInitTest : AccountInit ()  {
    override fun initHead(constants: Constants) {
        this.initHeadOut(constants)
    }

    override fun initBody(constants: Constants) {
        this.initBodyOut(constants)
    }

    override fun initMainRoom(mainRoom: MainRoom) {
        this.initMainRoomOut(mainRoom)
    }

    override fun initSlaveRoom(slaveRoom: SlaveRoom) {
        this.initSlaveRoomOut(slaveRoom)
    }

    override fun initMineral(mainContext: MainContext) {
        this.initMineralOut(mainContext)
    }

    override fun initTuning(mainContext: MainContext) {
        this.initTuningOut(mainContext)
    }
}