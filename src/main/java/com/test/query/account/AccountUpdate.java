package com.test.query.account;

import com.test.db.Session;
import com.test.model.Account;
import org.cactoos.Proc;
import org.jooq.impl.DSL;

public final class AccountUpdate implements Proc<Account> {

    private final Session session;

    public AccountUpdate(final Session session) {
        this.session = session;
    }

    @Override
    public void exec(final Account input) throws Exception {
        this.session.execute(
            ctx -> ctx.update(
                DSL.table(DSL.name("account"))
            )
            .set(
                DSL.field(DSL.name("balance")),
                input.balance()
            )
            .where(
                DSL.field(DSL.name("id")).eq(input.id())
            )
            .execute()
        );
    }
}
