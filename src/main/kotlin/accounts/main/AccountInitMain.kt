package accounts.main

import accounts.AccountInit
import constants.Constants
import mainRoom.MainRoom
import slaveRoom.SlaveRoom

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