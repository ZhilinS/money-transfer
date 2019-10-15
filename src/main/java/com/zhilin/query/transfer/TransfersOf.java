package com.zhilin.query.transfer;

import com.zhilin.db.Session;
import com.zhilin.model.Transfer;
import java.util.List;
import org.cactoos.Scalar;

public final class TransfersOf implements Scalar<List<Transfer>> {

    private final Session session;

    public TransfersOf(final Session session) {
        this.session = session;
    }

    @Override
    public List<Transfer> value() {
        return this.session.retrieve(
            ctx -> ctx.selectFrom("transfer")
                .fetchInto(Transfer.class)
        );
    }
}
