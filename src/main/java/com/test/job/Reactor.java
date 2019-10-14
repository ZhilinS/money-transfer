package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.OperationReq;
import com.test.model.Operation;
import com.test.query.Transfer;
import com.test.query.OperationUpdate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final static Striped<Lock> LOCKS = Striped.lazyWeakLock(10);
    private final static ConcurrentHashMap<Integer, Lock> REQUEST_LOCKS =
        new ConcurrentHashMap<>(10);

    private final OperationUpdate operation;
    private final Transfer transfer;

    public Reactor(
        final OperationUpdate operation,
        final Transfer transfer
    ) {
        this.operation = operation;
        this.transfer = transfer;
    }

    public void process(
        final OperationReq req,
        final TransactionJob job
    ) {
        this.lock(req);
        try {
            job.start(
                () -> this.transfer.created(req),
                id -> this.operation.exec(Operation.Status.COMPLETED, id),
                id -> this.operation.exec(Operation.Status.ERROR, id)
            );
        } finally {
            this.unlock(req);
        }
    }

    private void lock(OperationReq req) {
        if (req.from() < req.to()) {
            this.ordered(req.from(), req.to());
        } else {
            this.ordered(req.to(), req.from());
        }
    }

    private void ordered(final int from , final int to) {
        final Lock first = Reactor.LOCKS.get(from);
        final Lock second = Reactor.LOCKS.get(to);
        Reactor.REQUEST_LOCKS.putIfAbsent(from, first);
        Reactor.REQUEST_LOCKS.putIfAbsent(to, second);
        first.lock();
        second.lock();
    }

    private void unlock(final OperationReq req) {
        Reactor.REQUEST_LOCKS.get(req.from()).unlock();
        Reactor.REQUEST_LOCKS.get(req.to()).unlock();
    }
}
