package com.zhilin.query.transfer;

import com.zhilin.db.Session;
import com.zhilin.model.Transfer;
import java.util.List;
import org.cactoos.Func;
import org.jooq.impl.DSL;

public class TransferOf implements Func<Integer, List<Transfer>> {

    private final Session session;

    public TransferOf(final Session session) {
        this.session = session;
    }

    @Override
    public List<Transfer> apply(final Integer id) {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("transfer")
                .where(
                    DSL.field(DSL.name("first")).eq(id)
                        .or(DSL.field(DSL.name("second")).eq(id))
                )
                .fetchInto(Transfer.class)
        );
    }
}
