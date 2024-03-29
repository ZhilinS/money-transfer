package com.zhilin.http;

import com.zhilin.http.routes.Accounts;
import com.zhilin.http.routes.Transfers;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;
import static spark.Spark.*;

@Slf4j
public final class Router {

    private final int port;
    private final Accounts accounts;
    private final Transfers transfers;

    public Router(
        final int port,
        final Accounts accounts,
        final Transfers transfers
    ) {
        this.port = port;
        this.accounts = accounts;
        this.transfers = transfers;
    }

    public void init() {
        port(this.port);
        path("/api", () -> {
            path("/account", () -> {
                get("", this.accounts.all());
                get("/:id", this.accounts.single());
                post("", this.accounts.insert());
                post("/withdraw", this.accounts.withdraw());
                post("/deposit", this.accounts.deposit());
            });
            path("/transfer", () -> {
                post("", this.accounts.transfer());
            });
            path("/log", () -> {
                get("", this.transfers.all());
                get("/:id", this.transfers.log());
            });
        });
        Spark.before((req, res) -> {
            String path = req.pathInfo();
            if (path.endsWith("/"))
                res.redirect(path.substring(0, path.length() - 1));
        });
    }
}
