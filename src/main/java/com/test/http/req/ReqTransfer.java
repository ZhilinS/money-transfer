package com.test.http.req;

import com.test.model.Transfer;

public class ReqTransfer implements ReqOperation {

    private final Integer from;
    private final Integer to;
    private final double amount;

    public ReqTransfer(
        final Integer from,
        final Integer to,
        final double amount
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
    public double amount() {
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
