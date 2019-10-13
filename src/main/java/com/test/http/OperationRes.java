package com.test.http;

import com.test.model.Operation;

public class OperationRes {

    private final int from;
    private final double amount;
    private final Operation.Status status;

    public OperationRes(
        final int from,
        final double amount,
        final Operation.Status status
    ) {
        this.from = from;
        this.amount = amount;
        this.status = status;
    }

    public int from() {
        return this.from;
    }

    public double amount() {
        return this.amount;
    }

    public Operation.Status status() {
        return this.status;
    }
}
