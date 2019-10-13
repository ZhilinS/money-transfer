package com.test.http;

import com.test.http.routes.get.AccountGet;
import com.test.http.routes.post.AccountPost;
import lombok.extern.slf4j.Slf4j;
import static spark.Spark.*;

@Slf4j
public final class Router {

    private final AccountGet accountGet;
    private final AccountPost accountPost;

    public Router(
        final AccountGet accountGet,
        final AccountPost accountPost
    ) {
        this.accountGet = accountGet;
        this.accountPost = accountPost;
    }

    public void init() {
        port(8080);
        path("/api", () -> {
            path("/account", () -> {
                get("/:id", this.accountGet);
                post("", this.accountPost);
            });
        });
    }
}
