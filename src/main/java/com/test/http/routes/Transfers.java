package com.test.http.routes;

import com.google.gson.Gson;
import com.test.query.transfer.TransferOf;
import com.test.query.transfer.TransfersOf;
import spark.Route;

public class Transfers {

    private final TransferOf single;
    private final TransfersOf all;

    public Transfers(
        final TransferOf single,
        final TransfersOf all
    ) {
        this.single = single;
        this.all = all;
    }

    public Route all() {
        return (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                this.all.value()
            );
        };
    }

    public Route log() {
        return (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(
                this.single.apply(
                    Integer.valueOf(request.params(":id"))
                )
            );
        };
    }
}
