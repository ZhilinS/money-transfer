package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import java.util.Collection;
import java.util.List;
import org.cactoos.Scalar;

public final class AccountsOf implements Scalar<Collection<Account>> {

    private final Session session;

    public AccountsOf(final Session session) {
        this.session = session;
    }

    @Override
    public Collection<Account> value() {
        return this.session.retrieve(
            ctx -> ctx.select()
                .from("account")
                .fetch()
                .into(Account.class)
        );
    }
}
