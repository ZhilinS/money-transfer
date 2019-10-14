package com.test.query;

import com.test.db.Session;
import com.test.http.req.OperationReq;
import com.test.model.Operation;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public final class Transfer {

    private final Session session;

    public Transfer(final Session session) {
        this.session = session;
    }

    public Integer created(
        final OperationReq req
    ) {
        return this.session.retrieve(
            ctx -> ctx.transactionResult(
                configuration -> {
                    DSL.using(configuration)
                        .insertInto(
                            DSL.table(DSL.name("operation"))
                        )
                        .columns(
                            DSL.field(DSL.name("from_acc")),
                            DSL.field(DSL.name("to_acc")),
                            DSL.field(DSL.name("amount")),
                            DSL.field(DSL.name("status"))
                        )
                        .values(
                            req.from(),
                            req.to(),
                            req.amount(),
                            Operation.Status.PENDING
                        )
                        .execute();
                    return DSL.using(configuration)
                        .select(
                            DSL.field(
                                "last_insert_rowid()",
                                SQLDataType.INTEGER
                            )
                        )
                        .fetchOneInto(Integer.class);
                }
            )
        );
    }
}
