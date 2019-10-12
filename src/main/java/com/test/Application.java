/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test;

import com.test.db.Connect;
import com.test.db.Session;
import com.test.init.Tables;
import com.test.model.Account;
import com.test.query.AccountSingle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {

    public static void main(String[] args) {
        final Connect connect = new Connect("jdbc:sqlite:memory:one");
        final Session session = connect.session();
        new Tables(session).init();
        final Account acc = new AccountSingle(session, 1).value();
        System.out.println(acc);
    }
}
