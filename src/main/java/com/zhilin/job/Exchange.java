package com.zhilin.job;

import com.zhilin.exception.LimitsExceededException;
import com.zhilin.http.req.ReqOperation;
import com.zhilin.model.Account;
import com.zhilin.query.account.AccountOf;
import com.zhilin.query.account.AccountUpdate;
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
