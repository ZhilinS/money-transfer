package com.test.job;

import com.test.http.req.ReqOperation;

public final class LockerWrap {

    private final Locker locker;

    public LockerWrap(final Locker locker) {
        this.locker = locker;
    }

    public void lock(final ReqOperation req) {
        if (req.type().equals(ReqOperation.Type.SINGLE)) {
            this.locker.lock(req.account());
        } else {
            this.locker.lock(Math.min(req.from(), req.to()));
            this.locker.lock(Math.max(req.from(), req.to()));
        }
    }

    public void unlock(final ReqOperation req) {
        if (req.type().equals(ReqOperation.Type.SINGLE)) {
            this.locker.unlock(req.account());
        } else {
            this.locker.unlock(req.from());
            this.locker.unlock(req.to());
        }
    }
}
