package com.zhilin.http.req;

import com.zhilin.model.Transfer;

public class ReqTransfer implements ReqOperation {

    private final Integer from;
    private final Integer to;
    private final float amount;

    public ReqTransfer(
        final Integer from,
        final Integer to,
        final float amount
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public Integer account() {
        return null;
    }

    @Override
    public Integer from() {
        return this.from;
    }

    @Override
    public Integer to() {
        return this.to;
    }

    @Override
    public float amount() {
        return this.amount;
    }

    @Override
    public Type type() {
        return Type.MULTIPLE;
    }

    @Override
    public Transfer.Type transfer() {
        return Transfer.Type.TRANSFER;
    }
}
