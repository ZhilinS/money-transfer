package com.zhilin.job;

import com.google.common.util.concurrent.Striped;
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

    public void lock(final int id) {
        this.lockSingle(id);
    }

    public void unlock(final int id) {
        this.requests.get(id).unlock();
    }

    private void lockSingle(final int id) {
        final Lock lock = this.locks.get(id);
        this.requests.putIfAbsent(id, lock);
        lock.lock();
    }
}
