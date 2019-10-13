/*
 * Copyright (C) 2018, SEMRUSH CY LTD or it's affiliates
 */
package com.test.http.routes.get;

import com.google.gson.Gson;
import com.test.query.AccountSingle;
import spark.Request;
import spark.Response;
import spark.Route;

public final class AccountGet implements Route {

    private final AccountSingle account;

    public AccountGet(final AccountSingle account) {
        this.account = account;
    }

    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        return new Gson().toJson(
            this.account.apply(
                Integer.valueOf(request.params(":id"))
            )
        );
    }
}
