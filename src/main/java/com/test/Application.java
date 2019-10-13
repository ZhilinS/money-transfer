package com.test;

import com.test.db.Connect;
import com.test.db.Session;
import com.test.http.Router;
import com.test.init.Tables;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final Connect connect = new Connect("jdbc:sqlite:memory:one");
        final Session session = connect.session();
        new Tables(session).init();
        new Router(
            new AccountSingle(session),
            new AccountInsert(session)
        ).init();
    }
}
