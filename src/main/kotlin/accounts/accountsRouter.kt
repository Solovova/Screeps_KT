package accounts

import accounts.serverMain.*
import accounts.serverTest.*
import accounts.serverTest2.*
import constants.Constants
import mainContext.MainContext
import mainContext.MineralDataRecord
import mainRoom.MainRoom
import screeps.api.Memory
import screeps.api.ResourceConstant
import screeps.api.get
import slaveRoom.SlaveRoom

fun Constants.initHead() {
    if (Memory["account"] == "main") this.initMainHead()
    if (Memory["account"] == "test") this.initTestHead()
    if (Memory["account"] == "test2") this.initTest2Head()
}

fun Constants.initBody() {
    if (Memory["account"] == "main") this.initMainBody()
    if (Memory["account"] == "test") this.initTestBody()
    if (Memory["account"] == "test2") this.initTest2Body()

}

fun constantMainRoomInit(mainRoom: MainRoom) {
    if (Memory["account"] == "main") constantMainRoomInitMain(mainRoom)
    if (Memory["account"] == "test") constantMainRoomInitTest(mainRoom)
    if (Memory["account"] == "test2") constantMainRoomInitTest2(mainRoom)
}

fun constantSlaveRoomInit(slaveRoom: SlaveRoom) {
    if (Memory["account"] == "main") constantSlaveRoomInitMain(slaveRoom)
    if (Memory["account"] == "test") constantSlaveRoomInitTest(slaveRoom)
    if (Memory["account"] == "test2") constantSlaveRoomInitTest2(slaveRoom)


}

fun initMineralData(mainContext: MainContext) {
    if (Memory["account"] == "main") initMineralMain(mainContext)
    if (Memory["account"] == "test") initMineralTest(mainContext)
    if (Memory["account"] == "test2") initMineralTest2(mainContext)

}

