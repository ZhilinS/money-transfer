package com.test;

import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.http.routes.Accounts;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import com.test.query.OperationInsert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final Connect connect = new Connect("jdbc:sqlite:memory:one");
        connect.init();
        final Session session = connect.session();
        new Router(
            8080,
            new Accounts(
                new AccountSingle(session),
                new AccountInsert(session),
                new OperationInsert(session)
            )
        ).init();
    }
}
