package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.OperationReq;
import com.test.model.Operation;
import com.test.query.OperationInsert;
import com.test.query.OperationUpdate;
import java.util.concurrent.locks.Lock;

public final class Reactor {

    private final Striped<Lock> locks = Striped.lazyWeakLock(10);

    private final OperationUpdate operation;
    private final OperationInsert transaction;

    public Reactor(
        final OperationUpdate operation,
        final OperationInsert transaction
    ) {
        this.operation = operation;
        this.transaction = transaction;
    }

    public void process(
        final OperationReq req,
        final TransactionJob job
    ) {
        final Lock fromLock = locks.get(req.from());
        final Lock toLock = locks.get(req.to());
        fromLock.lock();
        toLock.lock();
        try {
            job.exchange(
                () -> this.transaction.exec(
                    req.from(),
                    req.to(),
                    req.amount(),
                    Operation.Status.PENDING
                ),
                id -> this.operation.exec(
                    Operation.Status.COMPLETED,
                    id
                ),
                id -> this.operation.exec(
                    Operation.Status.ERROR,
                    id
                )
            );
        } finally {
            fromLock.unlock();
            toLock.unlock();
        }
    }
}
