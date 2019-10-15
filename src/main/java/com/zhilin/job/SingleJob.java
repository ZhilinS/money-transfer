package com.zhilin.job;

import com.zhilin.exception.LimitsExceededException;
import com.zhilin.http.req.ReqOperation;
import com.zhilin.model.Account;
import com.zhilin.query.account.AccountOf;
import com.zhilin.query.account.AccountUpdate;

public final class SingleJob implements Job {

    private final ReqOperation req;
    private final AccountUpdate update;
    private final AccountOf accounts;

    public SingleJob(
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
        final Account account = this.accounts.apply(
            this.req.account()
        );
        if (account.balance() + this.req.amount() < 0) {
            throw new LimitsExceededException(account, this.req.amount());
        }
        this.update.exec(
            new Account(
                account,
                account.balance() + this.req.amount()
            )
        );
    }
}
