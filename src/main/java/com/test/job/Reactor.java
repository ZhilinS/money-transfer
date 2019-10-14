package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.OperationReq;
import com.test.model.Operation;
import com.test.query.OperationInsert;
import com.test.query.OperationUpdate;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final static Striped<Lock> LOCKS = Striped.lazyWeakLock(10);

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
        final Lock firstLock;
        final Lock secondLock;
        if (req.from() < req.to()) {
            firstLock = Reactor.LOCKS.get(req.from());
            secondLock = Reactor.LOCKS.get(req.to());
        } else {
            firstLock = Reactor.LOCKS.get(req.to());
            secondLock = Reactor.LOCKS.get(req.from());
        }
        firstLock.lock();
        secondLock.lock();
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
            log.info("JOB FINISHED");
        } finally {
            firstLock.unlock();
            secondLock.unlock();
            log.info("UNLOCKED: {}", Thread.currentThread().getId());
        }
    }
}
