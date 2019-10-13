package com.test.http;

import com.test.http.routes.get.AccountGet;
import com.test.http.routes.post.AccountPost;
import lombok.extern.slf4j.Slf4j;
import static spark.Spark.*;

@Slf4j
public final class Router {

    private final int port;
    private final AccountGet accountGet;
    private final AccountPost accountPost;

    public Router(
        final int port,
        final AccountGet accountGet,
        final AccountPost accountPost
    ) {
        this.port = port;
        this.accountGet = accountGet;
        this.accountPost = accountPost;
    }

    public void init() {
        port(this.port);
        path("/api", () -> {
            path("/account", () -> {
                get("/:id", this.accountGet);
                post("", this.accountPost);
            });
        });
    }
}
