package com.zhilin.http.req;

import com.zhilin.model.Transfer;

public class ReqDeposit implements ReqOperation {

    private final Integer account;
    private final float amount;

    public ReqDeposit(
        final Integer account,
        final float amount
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
        return null;
    }

    @Override
    public Integer to() {
        return this.account;
    }

    @Override
    public float amount() {
        return this.amount;
    }

    @Override
    public Type type() {
        return Type.SINGLE;
    }

    @Override
    public Transfer.Type transfer() {
        return Transfer.Type.DEPOSIT;
    }
}
