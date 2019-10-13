/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test.http.routes.post;

import com.google.gson.Gson;
import com.test.model.Account;
import com.test.query.AccountInsert;
import spark.Request;
import spark.Response;
import spark.Route;

public final class AccountPost implements Route {

    private final AccountInsert insert;

    public AccountPost(final AccountInsert insert) {
        this.insert = insert;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        final Account created = new Gson().fromJson(
            request.body(),
            Account.class
        );
        this.insert.exec(created);
        response.status(200);
        response.type("application/json");
        return response;
    }
}
