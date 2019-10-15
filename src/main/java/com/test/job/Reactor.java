package com.test.job;

import com.test.http.req.ReqTransfer;
import com.test.model.Transfer;
import com.test.query.transfer.TransferCreate;
import com.test.query.transfer.TransferUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Reactor {

    private final TransferCreate transfer;
    private final TransferUpdate update;
    private final Locker locker;

    public Reactor(
        final TransferCreate transfer,
        final TransferUpdate update,
        final Locker locker
    ) {
        this.transfer = transfer;
        this.update = update;
        this.locker = locker;
    }

    public void process(
        final ReqTransfer req,
        final Job job
    ) {
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
        } finally {
            this.locker.unlock(req);
        }
    }
}
