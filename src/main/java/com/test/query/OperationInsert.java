package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import com.test.model.Operation;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

public final class OperationInsert {

    private final static String INSERT = "" +
        "INSERT INTO operation(from_acc, to_acc, amount, status) " +
        "values(%d, %d, %f, '%s')";

    private final Session session;

    public OperationInsert(final Session session) {
        this.session = session;
    }

    public void exec(
        final Account from,
        final Account to,
        final double amount,
        final Operation.Status status
    ) {
        this.session.execute(
            ctx ->
//                ctx.insertInto(
//                DSL.table(DSL.name("account"))
//            )
                ctx.execute(
                String.format(
                    OperationInsert.INSERT,
                    from.id(),
                    to.id(),
                    amount,
                    status
                )
            )
        );
    }
}
