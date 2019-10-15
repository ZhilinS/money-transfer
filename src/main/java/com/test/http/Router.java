package com.test.http;

import com.test.http.routes.Accounts;
import lombok.extern.slf4j.Slf4j;
import static spark.Spark.*;

@Slf4j
public final class Router {

    private final int port;
    private final Accounts accounts;

    public Router(
        final int port,
        final Accounts accounts
    ) {
        this.port = port;
        this.accounts = accounts;
    }

    public void init() {
        port(this.port);
        path("/api", () -> {
            path("/account", () -> {
                get("/:id", this.accounts.single());
                post("", this.accounts.insert());
                post("/withdraw", this.accounts.withdraw());
                post("/deposit", this.accounts.deposit());
            });
            path("/transfer", () -> {
                post("", this.accounts.transfer());
            });
        });
    }
}
