package com.test.query.account;

import com.test.db.Session;
import com.test.model.Account;
import org.cactoos.Proc;
import org.jooq.impl.DSL;

public final class AccountCreate implements Proc<Account> {

    private final Session session;

    public AccountCreate(final Session session) {
        this.session = session;
    }

    @Override
    public void exec(final Account account) {
        this.session.execute(
            ctx -> ctx.insertInto(
                DSL.table(DSL.name("account"))
            )
                .columns(
                    DSL.field(DSL.name("name")),
                    DSL.field(DSL.name("balance"))
                )
            .values(
                account.name(),
                account.balance()
            )
            .execute()
        );
    }
}
