package com.test.job;

import com.google.common.util.concurrent.Striped;
import com.test.http.req.ReqTransfer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public final class Locker {

    private final static int STRIPES = 10;

    private final Striped<Lock> locks;
    private final ConcurrentHashMap<Integer, Lock> requests;

    public Locker() {
        this(
            Striped.lazyWeakLock(Locker.STRIPES),
            new ConcurrentHashMap<>(Locker.STRIPES)
        );
    }

    public Locker(
        final Striped<Lock> locks,
        final ConcurrentHashMap<Integer, Lock> requests
    ) {
        this.locks = locks;
        this.requests = requests;
    }

    public void lock(final ReqTransfer req) {
        if (req.from() < req.to()) {
            this.ordered(req.from(), req.to());
        } else {
            this.ordered(req.to(), req.from());
        }
    }

    public void unlock(final ReqTransfer req) {
        this.requests.get(req.from()).unlock();
        this.requests.get(req.to()).unlock();
    }

    private void ordered(final int from, final int to) {
        final Lock first = this.locks.get(from);
        final Lock second = this.locks.get(to);
        this.requests.putIfAbsent(from, first);
        this.requests.putIfAbsent(to, second);
        first.lock();
        second.lock();
    }
}
