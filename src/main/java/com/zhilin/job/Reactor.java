package com.zhilin.job;

import com.zhilin.http.req.ReqOperation;
import com.zhilin.model.Transfer;
import com.zhilin.query.transfer.TransferCreate;
import com.zhilin.query.transfer.TransferUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final TransferCreate transfer;
    private final TransferUpdate update;
    private final LockerWrap locker;

    public Reactor(
        final TransferCreate transfer,
        final TransferUpdate update,
        final LockerWrap locker
    ) {
        this.transfer = transfer;
        this.update = update;
        this.locker = locker;
    }

    public void process(
        final ReqOperation req,
        final Job job
    ) throws Exception {
        final Integer id = this.transfer.created(req);
        this.locker.lock(req);
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
            throw exception;
        } finally {
            this.locker.unlock(req);
        }
    }
}
