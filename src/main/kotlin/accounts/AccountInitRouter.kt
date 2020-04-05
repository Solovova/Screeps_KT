package accounts

import accounts.srvMain.AccountInitMain
import accounts.srvScreepPlus.AccountInitScreepPlus
import accounts.srvTest.AccountInitTest
import accounts.srvTest2.AccountInitTest2
import screeps.api.Memory
import screeps.api.get

class AccountInitRouter {
    fun instance(): AccountInit {
        if (Memory["account"] == "main") return AccountInitMain()
        if (Memory["account"] == "test") return AccountInitTest()
        if (Memory["account"] == "test2") return AccountInitTest2()
        if (Memory["account"] == "ScreepPlus") return AccountInitScreepPlus()


        return AccountInitMain()
    }
}