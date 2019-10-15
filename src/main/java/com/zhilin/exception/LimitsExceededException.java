package com.zhilin.exception;

import com.zhilin.model.Account;

public class LimitsExceededException extends RuntimeException{

    private final Account account;
    private final double amount;

    public LimitsExceededException(
        final Account account,
        final double amount
    ) {
        super();
        this.account = account;
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return String.format(
            "Cannot withdraw from the account %d[%s]. Current: %f. Needed: %f",
            this.account.id(),
            this.account.name(),
            this.account.balance(),
            -this.amount
        );
    }
}
