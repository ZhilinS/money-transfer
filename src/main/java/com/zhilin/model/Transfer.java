package com.zhilin.model;

public class Transfer {

    private final int id;
    private final Integer first;
    private final Integer second;
    private final double amount;
    private final Type type;
    private final Status status;

    public Transfer(
        final int id,
        final Integer first,
        final Integer second,
        final double amount,
        final Type type,
        final Status status
    ) {
        this.id = id;
        this.first = first;
        this.second = second;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    public int id() {
        return this.id;
    }

    public double amount() {
        return this.amount;
    }

    public Type type() {
        return this.type;
    }

    public Status status() {
        return this.status;
    }

    public enum Status {
        PENDING,
        ERROR,
        COMPLETED
    }

    public enum Type {
        WITHDRAW,
        DEPOSIT,
        TRANSFER
    }
}
