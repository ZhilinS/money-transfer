package com.test.http;

public class OperationRes {

    private final int from;
    private final double amount;

    public OperationRes(
        final int from,
        final double amount
    ) {
        this.from = from;
        this.amount = amount;
    }

    public int from() {
        return this.from;
    }

    public double amount() {
        return this.amount;
    }
}
