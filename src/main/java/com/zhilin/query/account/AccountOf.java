package com.zhilin.query.account;

import com.zhilin.db.Session;
import com.zhilin.model.Account;
import org.cactoos.Func;
import org.jooq.impl.DSL;

public final class AccountOf implements Func<Integer, Account> {

    private final Session session;

    public AccountOf(final Session session) {
        this.session = session;
    }

    @Override
    public Account apply(final Integer id) throws Exception {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("account")
                .where(
                    DSL.field(DSL.name("id")).eq(id)
                )
                .fetchOneInto(Account.class)
        );
    }
}
