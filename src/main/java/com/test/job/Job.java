package com.test.job;

import com.test.http.req.OperationReq;
import com.test.model.Account;
import com.test.query.account.AccountSingle;
import com.test.query.account.AccountUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Job {

    private final OperationReq req;
    private final AccountUpdate update;
    private final AccountSingle accounts;

    public Job(
        final OperationReq req,
        final AccountUpdate update,
        final AccountSingle accounts
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
