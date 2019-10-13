package com.test.query;

import com.test.db.Session;
import com.test.model.Operation;
import java.util.Collection;
import java.util.List;

public class OperationsOf {

    private final Session session;
    private final Collection<Integer> accounts;

    public OperationsOf(
        final Session session,
        final Collection<Integer> accounts
    ) {
        this.session = session;
        this.accounts = accounts;
    }

    public List<Operation> all() {
        return this.session.retrieve(
            ctx -> ctx.select()
            .from("operation")
            .fetchInto(Operation.class)
        );
    }
}
