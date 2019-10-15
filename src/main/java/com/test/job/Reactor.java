package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.ReqTransfer;
import com.test.model.Transfer;
import com.test.query.transfer.TransferUpdate;
import com.test.query.transfer.TransferCreate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final static Striped<Lock> LOCKS = Striped.lazyWeakLock(10);
    private final static ConcurrentHashMap<Integer, Lock> REQUEST_LOCKS =
        new ConcurrentHashMap<>(10);

    private final TransferCreate transfer;
    private final TransferUpdate update;

    public Reactor(
        final TransferCreate transfer,
        final TransferUpdate update
    ) {
        this.transfer = transfer;
        this.update = update;
    }

    public void process(
        final ReqTransfer req,
        final Job job
    ) {
        final Integer id = this.transfer.created(req);
        this.lock(req);
        try {
            job.start();
            this.update.exec(
                Transfer.Status.COMPLETED,
                id
            );
        } catch (final Exception exception) {
            this.update.exec(
                Transfer.Status.ERROR,
                id
            );
        } finally {
            this.unlock(req);
        }
    }

    private void lock(ReqTransfer req) {
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

    private void unlock(final ReqTransfer req) {
        Reactor.REQUEST_LOCKS.get(req.from()).unlock();
        Reactor.REQUEST_LOCKS.get(req.to()).unlock();
    }
}
