package accounts

import accounts.srvMain.AccountInitMain
import accounts.srvMain2.AccountInitMain2
import screeps.api.Memory
import screeps.api.get

class AccountInitRouter {
    fun instance(): AccountInit {
        if (Memory["account"] == "main") return AccountInitMain()
        if (Memory["account"] == "main2") return AccountInitMain2()
        return AccountInitMain()
    }
}