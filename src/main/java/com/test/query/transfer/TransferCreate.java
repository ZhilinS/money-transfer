package com.test.query.transfer;

import com.test.db.Session;
import com.test.http.req.ReqOperation;
import com.test.model.Transfer;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public final class TransferCreate {

    private final Session session;

    public TransferCreate(final Session session) {
        this.session = session;
    }

    public Integer created(
        final ReqOperation req
    ) {
        return this.session.retrieve(
            ctx -> ctx.transactionResult(
                configuration -> {
                    DSL.using(configuration)
                        .insertInto(
                            DSL.table(DSL.name("transfer"))
                        )
                        .columns(
                            DSL.field(DSL.name("first")),
                            DSL.field(DSL.name("second")),
                            DSL.field(DSL.name("amount")),
                            DSL.field(DSL.name("type")),
                            DSL.field(DSL.name("status"))
                        )
                        .values(
                            req.from(),
                            req.to(),
                            req.amount(),
                            req.transfer(),
                            Transfer.Status.PENDING
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
