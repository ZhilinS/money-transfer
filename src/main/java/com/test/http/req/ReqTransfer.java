package com.test.http.req;

public class ReqTransfer {

    private final int from;
    private final int to;
    private final double amount;

    public ReqTransfer(
        final int from,
        final int to,
        final double amount
    ) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public int from() {
        return this.from;
    }

    public int to() {
        return this.to;
    }

    public double amount() {
        return this.amount;
    }
}
