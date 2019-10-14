package com.test.model;

public class Operation {

    private final int id;
    private final Account fromAcc;
    private final Account toAcc;
    private final double amount;
    private final Status status;

    public Operation(
        final int id,
        final Account fromAcc,
        final Account toAcc,
        final double amount,
        final Status status
    ) {
        this.id = id;
        this.fromAcc = fromAcc;
        this.toAcc = toAcc;
        this.amount = amount;
        this.status = status;
    }

    public int id() {
        return this.id;
    }

    public double amount() {
        return this.amount;
    }

    public Status status() {
        return this.status;
    }

    public enum Status {
        PENDING,
        ERROR,
        COMPLETED
    }
}
