package com.test.query.transfer;

import com.test.db.Session;
import com.test.model.Transfer;
import java.util.List;

public final class OperationsOf {

    private final Session session;

    public OperationsOf(final Session session) {
        this.session = session;
    }

    public List<Transfer> all() {
        return this.session.retrieve(
            ctx -> ctx.select()
                .from("transfer")
                .fetchInto(Transfer.class)
        );
    }
}
