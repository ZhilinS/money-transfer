package com.test.query;

import com.test.db.Session;
import com.test.model.Account;
import com.test.model.Operation;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public final class OperationInsert {

    private final Session session;

    public OperationInsert(final Session session) {
        this.session = session;
    }

    public Integer exec(
        final Account from,
        final Account to,
        final double amount,
        final Operation.Status status
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
                            from.id(),
                            to.id(),
                            amount,
                            status
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
//                ctx.insertInto(
//                DSL.table(DSL.name("operation"))
//            )
//                .columns(
//                    DSL.field(DSL.name("from_acc")),
//                    DSL.field(DSL.name("to_acc")),
//                    DSL.field(DSL.name("amount")),
//                    DSL.field(DSL.name("status"))
//                )
//                .values(
//                    from.id(),
//                    to.id(),
//                    amount,
//                    status
//                )
//                .execute()
        );
    }
}
