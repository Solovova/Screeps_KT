package accounts

import accounts.srvMain.AccountInitMain
import accounts.srvTest.AccountInitTest
import accounts.srvTest2.AccountInitTest2
import screeps.api.Memory
import screeps.api.get

class AccountInitRouter {
    fun instance(): AccountInit {
        if (Memory["account"] == "main") return AccountInitMain()
        if (Memory["account"] == "test") return AccountInitTest()
        if (Memory["account"] == "test2") return AccountInitTest2()
        return AccountInitMain()
    }
}