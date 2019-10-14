package com.test.job;

import com.test.http.req.OperationReq;
import com.test.model.Account;
import com.test.query.account.AccountSingle;
import com.test.query.account.AccountUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransactionJob {

    private final AccountUpdate update;
    private final AccountSingle accounts;
    private final OperationReq req;

    public TransactionJob(
        final AccountUpdate update,
        final AccountSingle accounts,
        final OperationReq req
    ) {
        this.update = update;
        this.accounts = accounts;
        this.req = req;
    }

    public void start(
        final Started started,
        final Updated completed,
        final Updated error
    ) {
        final int transaction = started.get();
        try {
            final Account from = this.accounts.apply(
                req.from()
            );
            final Account to = this.accounts.apply(
                req.to()
            );
            this.update.exec(
                new Account(
                    from.id(),
                    from.name(),
                    from.balance() - this.req.amount()
                )
            );
            this.update.exec(
                new Account(
                    to.id(),
                    to.name(),
                    to.balance() + this.req.amount()
                )
            );
            completed.set(transaction);
        } catch (final Exception exception) {
            log.error("Exception while processing transaction");
            error.set(transaction);
        }
    }
}
