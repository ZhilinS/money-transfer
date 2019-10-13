package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import org.cactoos.Func;

public final class AccountSingle implements Func<Integer, Account> {

    private final Session session;

    public AccountSingle(final Session session) {
        this.session = session;
    }

    @Override
    public Account apply(final Integer id) throws Exception {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("account")
                .where(
                    String.format("id = %d", id)
                )
                .fetchOneInto(Account.class)
        );
    }
}
