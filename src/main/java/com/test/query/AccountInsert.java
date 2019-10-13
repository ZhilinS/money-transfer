/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import org.cactoos.Proc;

public final class AccountInsert implements Proc<Account> {

    private final static String INSERT = "INSERT INTO account(name, balance) values('%s', %f)";

    private final Session session;

    public AccountInsert(
        final Session session
    ) {
        this.session = session;
    }

    @Override
    public void exec(final Account account) {
        this.session.execute(
            ctx -> ctx.execute(
                String.format(
                    AccountInsert.INSERT,
                    account.name(),
                    account.balance()
                )
            )
        );
    }
}
