package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.OperationReq;
import com.test.model.Operation;
import com.test.query.OperationUpdate;
import com.test.query.Transfer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final static Striped<Lock> LOCKS = Striped.lazyWeakLock(10);
    private final static ConcurrentHashMap<Integer, Lock> REQUEST_LOCKS =
        new ConcurrentHashMap<>(10);

    private final Transfer transfer;
    private final OperationUpdate update;

    public Reactor(
        final Transfer transfer,
        final OperationUpdate update
    ) {
        this.transfer = transfer;
        this.update = update;
    }

    public void process(
        final OperationReq req,
        final Job job
    ) {
        final Integer transaction = this.transfer.created(req);
        this.lock(req);
        try {
            job.start();
            this.update.exec(
                Operation.Status.COMPLETED,
                transaction
            );
        } catch (final Exception exception) {
            this.update.exec(
                Operation.Status.ERROR,
                transaction
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

    private void ordered(final int from, final int to) {
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
