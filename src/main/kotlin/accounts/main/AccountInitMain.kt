package accounts.main

import accounts.AccountInit
import mainContext.constants.Constants
import mainContext.mainRoomCollecror.mainRoom.MainRoom
import mainContext.mainRoomCollecror.mainRoom.slaveRoom.SlaveRoom

class AccountInitMain (constants: Constants): AccountInit (constants)  {
    override fun initHead() {
        this.initHeadOut()
    }

    override fun initBody() {

    }

    override fun initMainRoom(mainRoom: MainRoom) {

    }

    override fun initSlaveRoom(slaveRoom: SlaveRoom) {

    }
}