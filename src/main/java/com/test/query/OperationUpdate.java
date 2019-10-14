package com.test.query;

import com.test.db.Session;
import com.test.model.Operation;
import org.cactoos.BiProc;
import org.jooq.impl.DSL;

public final class OperationUpdate implements BiProc<Operation.Status, Integer> {

    private final Session session;

    public OperationUpdate(final Session session) {
        this.session = session;
    }

    @Override
    public void exec(final Operation.Status status, final Integer id) throws Exception {
        this.session.execute(
            ctx -> ctx.update(
                DSL.table(DSL.name("operation"))
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
