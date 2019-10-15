package com.test.http.routes;

import com.google.gson.Gson;
import com.test.http.req.ReqTransfer;
import com.test.job.Job;
import com.test.job.Reactor;
import com.test.model.Account;
import com.test.query.account.AccountCreate;
import com.test.query.account.AccountOf;
import com.test.query.account.AccountUpdate;
import org.eclipse.jetty.http.HttpStatus;
import spark.Route;

public final class Accounts {

    private final AccountOf account;
    private final AccountUpdate updated;
    private final AccountCreate create;
    private final Reactor reactor;

    public Accounts(
        final AccountOf account,
        final AccountUpdate updated,
        final AccountCreate create,
        final Reactor reactor
    ) {
        this.account = account;
        this.updated = updated;
        this.create = create;
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
            this.create.exec(
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
            final ReqTransfer req = new Gson()
                .fromJson(
                    request.body(),
                    ReqTransfer.class
                );
            this.reactor.process(
                req,
                new Job(
                    req,
                    this.updated,
                    this.account
                )
            );
            response.status(HttpStatus.OK_200);
            return response;
        };
    }
}
