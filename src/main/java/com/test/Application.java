package com.test;

import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.Accounts;
import com.test.job.Locker;
import com.test.job.LockerWrap;
import com.test.job.Reactor;
import com.test.query.transfer.TransferUpdate;
import com.test.query.transfer.TransferCreate;
import com.test.query.account.AccountCreate;
import com.test.query.account.AccountOf;
import com.test.query.account.AccountUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final Connect connect = new Connect("jdbc:sqlite:memory:one", 1);
        connect.init();
        final Session session = connect.session();
        new Router(
            8080,
            new Accounts(
                new AccountOf(session),
                new AccountUpdate(session),
                new AccountCreate(session),
                new Reactor(
                    new TransferCreate(session),
                    new TransferUpdate(session),
                    new LockerWrap(
                        new Locker()
                    )
                )
            )
        ).init();
    }
}
