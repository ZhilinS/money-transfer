package com.test.job;

import com.test.model.Operation;
import com.test.query.AccountUpdate;
import com.test.query.OperationUpdate;

public final class Reactor {

    private final OperationUpdate operation;
    private final AccountUpdate account;

    public Reactor(final OperationUpdate operation, final AccountUpdate account) {
        this.operation = operation;
        this.account = account;
    }

    public void exec(final int operation, final TransactionJob job) throws Exception {
        job.exchange(this.account);
        this.operation.exec(
            Operation.Status.COMPLETED,
            operation
        );
    }
}
