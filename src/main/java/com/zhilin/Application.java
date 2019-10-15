package com.zhilin;

import com.zhilin.db.Connect;
import com.zhilin.db.Session;
import com.zhilin.http.Router;
import com.zhilin.http.routes.Accounts;
import com.zhilin.http.routes.Transfers;
import com.zhilin.job.Locker;
import com.zhilin.job.LockerWrap;
import com.zhilin.job.Reactor;
import com.zhilin.query.account.AccountsOf;
import com.zhilin.query.transfer.TransferOf;
import com.zhilin.query.transfer.TransferUpdate;
import com.zhilin.query.transfer.TransferCreate;
import com.zhilin.query.account.AccountCreate;
import com.zhilin.query.account.AccountOf;
import com.zhilin.query.account.AccountUpdate;
import com.zhilin.query.transfer.TransfersOf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final Connect connect = new Connect("jdbc:sqlite:memory:prod", 1);
        connect.init();
        final Session session = connect.session();
        new Router(
            8080,
            new Accounts(
                new AccountOf(session),
                new AccountsOf(session),
                new AccountUpdate(session),
                new AccountCreate(session),
                new Reactor(
                    new TransferCreate(session),
                    new TransferUpdate(session),
                    new LockerWrap(
                        new Locker()
                    )
                )
            ),
            new Transfers(
                new TransferOf(session),
                new TransfersOf(session)
            )
        ).init();
    }
}
