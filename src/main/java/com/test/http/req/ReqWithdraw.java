package com.test.http.req;

import com.test.model.Transfer;

public class ReqWithdraw implements ReqOperation {

    private final Integer account;
    private final double amount;

    public ReqWithdraw(
        final Integer account,
        final double amount
    ) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public Integer account() {
        return this.account;
    }

    @Override
    public Integer from() {
        return this.account;
    }

    @Override
    public Integer to() {
        return null;
    }

    @Override
    public double amount() {
        return -this.amount;
    }

    @Override
    public Type type() {
        return Type.SINGLE;
    }

    @Override
    public Transfer.Type transfer() {
        return Transfer.Type.WITHDRAW;
    }
}
