package com.test.query;

import com.test.db.Session;
import com.test.model.Operation;
import java.util.List;

public final class OperationsOf {

    private final Session session;

    public OperationsOf(final Session session) {
        this.session = session;
    }

    public List<Operation> all() {
        return this.session.retrieve(
            ctx -> ctx.select()
                .from("operation")
                .fetchInto(Operation.class)
        );
    }
}
