package com.test.query.transfer;

import com.test.db.Session;
import com.test.model.Transfer;
import org.cactoos.Func;
import org.jooq.impl.DSL;

public class TransferOf implements Func<Integer, Transfer> {

    private final Session session;

    public TransferOf(final Session session) {
        this.session = session;
    }

    @Override
    public Transfer apply(final Integer id) {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("transfer")
                .where(
                    DSL.field(DSL.name("first")).eq(id)
                        .or(DSL.field(DSL.name("second")).eq(id))
                )
                .fetchOneInto(Transfer.class)
        );
    }
}
