package com.test.model;

public class Account {

    private final int id;
    private final String name;
    private final double balance;

    public Account(
        final String name,
        final double balance
    ) {
        this(0, name, balance);
    }

    public Account(
        final Account old,
        final double balance
    ) {
        this(old.id(), old.name(), balance);
    }

    public Account(
        final int id,
        final String name,
        final double balance
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

    public double balance() {
        return this.balance;
    }
}
