package com.test.http.routes;

import com.google.gson.Gson;
import com.test.http.req.OperationReq;
import com.test.job.Job;
import com.test.job.Reactor;
import com.test.model.Account;
import com.test.query.account.AccountInsert;
import com.test.query.account.AccountSingle;
import com.test.query.account.AccountUpdate;
import org.eclipse.jetty.http.HttpStatus;
import spark.Route;

public final class Accounts {

    private final AccountSingle account;
    private final AccountUpdate update;
    private final AccountInsert insert;
    private final Reactor reactor;

    public Accounts(
        final AccountSingle account,
        final AccountUpdate update,
        final AccountInsert insert,
        final Reactor reactor
    ) {
        this.account = account;
        this.update = update;
        this.insert = insert;
        this.reactor = reactor;
    }

    public Route single() {
        return (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                this.account.apply(
                    Integer.valueOf(request.params(":id"))
                )
            );
        };
    }

    public Route insert() {
        return (request, response) -> {
            this.insert.exec(
                new Gson()
                    .fromJson(
                        request.body(),
                        Account.class
                    )
            );
            response.status(HttpStatus.OK_200);
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
            this.reactor.process(
                req,
                new Job(
                    req,
                    this.update,
                    this.account
                )
            );
            response.status(HttpStatus.OK_200);
            return response;
        };
    }
}
