package com.test.job;

import com.test.http.req.ReqTransfer;
import com.test.model.Account;
import com.test.query.account.AccountOf;
import com.test.query.account.AccountUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Job {

    private final ReqTransfer req;
    private final AccountUpdate update;
    private final AccountOf accounts;

    public Job(
        final ReqTransfer req,
        final AccountUpdate update,
        final AccountOf accounts
    ) {
        this.req = req;
        this.update = update;
        this.accounts = accounts;
    }

    public void start() throws Exception {
        this.change(req.from(), -req.amount());
        this.change(req.to(), req.amount());
    }

    private void change(
        final int id,
        final double amount
    ) throws Exception {
        final Account account = this.accounts.apply(
            id
        );
        this.update.exec(
            new Account(
                account,
                account.balance() + amount
            )
        );
    }
}
