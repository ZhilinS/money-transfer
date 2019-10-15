package com.zhilin.query.account;

import com.zhilin.db.Session;
import com.zhilin.model.Account;
import java.util.Collection;
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
