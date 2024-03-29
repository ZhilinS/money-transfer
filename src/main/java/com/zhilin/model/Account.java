package com.zhilin.model;

public class Account {

    private final int id;
    private final String name;
    private final float balance;

    public Account(
        final String name,
        final float balance
    ) {
        this(0, name, balance);
    }

    public Account(
        final Account old,
        final float balance
    ) {
        this(old.id(), old.name(), balance);
    }

    public Account(
        final int id,
        final String name,
        final float balance
    ) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    public int id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public float balance() {
        return this.balance;
    }
}
