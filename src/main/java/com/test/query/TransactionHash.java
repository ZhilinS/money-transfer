package com.test.query;

import com.test.model.Account;

public class TransactionHash {

    private final Account from;
    private final Account to;

    public TransactionHash(
        final Account from,
        final Account to
    ) {
        this.from = from;
        this.to = to;
    }

    public Account from() {
        return this.from;
    }

    public Account to() {
        return this.to;
    }
}
