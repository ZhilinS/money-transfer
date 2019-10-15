package com.test;

import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.Accounts;
import com.test.job.Reactor;
import com.test.query.OperationUpdate;
import com.test.query.Transfer;
import com.test.query.account.AccountInsert;
import com.test.query.account.AccountSingle;
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
                new AccountSingle(session),
                new AccountUpdate(session),
                new AccountInsert(session),
                new Reactor(
                    new Transfer(session),
                    new OperationUpdate(session)
                )
            )
        ).init();
    }
}
