package com.test.job;

import com.test.exception.LimitsExceededException;
import com.test.http.req.ReqOperation;
import com.test.http.req.ReqTransfer;
import com.test.model.Account;
import com.test.query.account.AccountOf;
import com.test.query.account.AccountUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Exchange implements Job {

    private final ReqOperation req;
    private final AccountUpdate update;
    private final AccountOf accounts;

    public Exchange(
        final ReqOperation req,
        final AccountUpdate update,
        final AccountOf accounts
    ) {
        this.req = req;
        this.update = update;
        this.accounts = accounts;
    }

    @Override
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
        if (account.balance() + amount < 0) {
            throw new LimitsExceededException(account, amount);
        }
        this.update.exec(
            new Account(
                account,
                account.balance() + amount
            )
        );
    }
}
