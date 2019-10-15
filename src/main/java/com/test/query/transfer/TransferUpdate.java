package com.test.query.transfer;

import com.test.db.Session;
import com.test.model.Transfer;
import org.cactoos.BiProc;
import org.jooq.impl.DSL;

public final class TransferUpdate implements BiProc<Transfer.Status, Integer> {

    private final Session session;

    public TransferUpdate(final Session session) {
        this.session = session;
    }

    @Override
    public void exec(final Transfer.Status status, final Integer id) {
        this.session.execute(
            ctx -> ctx.update(
                DSL.table(DSL.name("transfer"))
            )
                .set(
                    DSL.field(DSL.name("status")),
                    status
                )
                .where(
                    DSL.field(DSL.name("id"))
                        .eq(id)
                )
                .execute()
        );
    }
}
