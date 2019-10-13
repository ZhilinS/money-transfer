package com.test.http;

import com.google.gson.Gson;
import com.test.model.Account;
import com.test.query.AccountInsert;
import com.test.query.AccountSingle;
import lombok.extern.slf4j.Slf4j;
import static spark.Spark.*;

@Slf4j
public final class Router {

    static {
        port(8080);
    }

    private final AccountSingle account;
    private final AccountInsert insert;

    public Router(final AccountSingle account, final AccountInsert insert) {
        this.account = account;
        this.insert = insert;
    }

    public void init() {
        path("/api", () -> {
            path("/account", () -> {
                get(
                    "/:id",
                    (req, res) -> new Gson().toJson(
                        this.account.apply(
                            Integer.valueOf(req.params(":id"))
                        )
                    )
                );
                post(
                    "",
                    (req, res) -> {
                        final Account created = new Gson().fromJson(
                            req.body(),
                            Account.class
                        );
                        this.insert.exec(created);
                        res.status(200);
                        res.type("application/json");
                        return res;
                    }
                );
            });
        });
    }
}
