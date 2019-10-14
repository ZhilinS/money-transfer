package com.test.job;

import com.test.model.Account;
import com.test.query.AccountUpdate;

public final class TransactionJob {

    private final Account from;
    private final Account to;
    private final double amount;

    public TransactionJob(
        final Account from,
        final Account to,
        final double amount
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public void exchange(final AccountUpdate update) throws Exception {
        update.exec(
            new Account(
                this.from.id(),
                this.from.name(),
                this.from.balance() - this.amount
            )
        );
        update.exec(
            new Account(
                this.to.id(),
                this.to.name(),
                this.to.balance() + this.amount
            )
        );
    }
}
