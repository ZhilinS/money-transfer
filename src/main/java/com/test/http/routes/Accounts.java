package com.test.http.routes;

import com.google.gson.Gson;
import com.test.http.OperationRes;
import com.test.http.req.OperationReq;
import com.test.model.Account;
import com.test.model.Operation;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import com.test.query.OperationInsert;
import spark.Route;

public final class Accounts {

    private final AccountSingle account;
    private final AccountInsert insert;
    private final OperationInsert transaction;
//    private final Reactor reactor;

    public Accounts(
        final AccountSingle account,
        final AccountInsert insert,
        final OperationInsert transaction
    ) {
        this.account = account;
        this.insert = insert;
        this.transaction = transaction;
    }

    public Route get() {
        return (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                this.account.apply(
                    Integer.valueOf(request.params(":id"))
                )
            );
        };
    }

    public Route post() {
        return (request, response) -> {
            this.insert.exec(
                new Gson()
                    .fromJson(
                        request.body(),
                        Account.class
                    )
            );
            response.status(200);
            response.type("application/json");
            return response;
        };
    }

    public Route transfer() {
        return (request, response) -> {
            final OperationReq req = new Gson()
                .fromJson(
                    request.body(),
                    OperationReq.class
                );
            final Account from = this.account.apply(
                req.from()
            );
            final Account to = this.account.apply(
                req.to()
            );
            this.transaction.exec(
                from,
                to,
                req.amount(),
                Operation.Status.PENDING
            );
//            this.reactor.exec(
//                new TransactionJob(
//                    from,
//                    to,
//                    req.amount()
//                )
//            );
            //todo: Error handling
            return new Gson().toJson(
                new OperationRes(
                    req.from(),
                    req.amount(),
                    Operation.Status.PENDING
                )
            );
        };
    }
}
