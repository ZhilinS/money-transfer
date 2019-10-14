package com.test.http.routes;

import com.google.gson.Gson;
import com.test.http.OperationRes;
import com.test.http.req.OperationReq;
import com.test.job.Reactor;
import com.test.job.TransactionJob;
import com.test.model.Account;
import com.test.model.Operation;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import com.test.query.AccountUpdate;
import spark.Route;

public final class Accounts {

    private final AccountSingle account;
    private final AccountInsert insert;
    private final AccountUpdate update;
    private final Reactor reactor;

    public Accounts(
        final AccountSingle account,
        final AccountInsert insert,
        final AccountUpdate update,
        final Reactor reactor
    ) {
        this.account = account;
        this.insert = insert;
        this.update = update;
        this.reactor = reactor;
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

//    public Route withdraw() {
//        return (request, response) -> {
//
//        };
//    }

    public Route transfer() {
        return (request, response) -> {
            final OperationReq req = new Gson()
                .fromJson(
                    request.body(),
                    OperationReq.class
                );
            this.reactor.process(
                req,
                new TransactionJob(
                    this.update,
                    this.account,
                    req
                )
            );
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
