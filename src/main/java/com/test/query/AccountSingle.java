package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import org.cactoos.Scalar;

public final class AccountSingle implements Scalar<Account> {

    private final Session session;
    private final int id;

    public AccountSingle(
        final Session session,
        final int id
    ) {
        this.session = session;
        this.id = id;
    }

    @Override
    public Account value() {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("account")
                .where(
                    String.format("id = %d", this.id)
                )
                .fetchOneInto(Account.class)
        );
    }
}
