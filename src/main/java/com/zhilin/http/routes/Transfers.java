package com.zhilin.http.routes;

import com.google.gson.Gson;
import com.zhilin.query.transfer.TransferOf;
import com.zhilin.query.transfer.TransfersOf;
import spark.Route;

public class Transfers {

    private final TransferOf particular;
    private final TransfersOf all;

    public Transfers(
        final TransferOf particular,
        final TransfersOf all
    ) {
        this.particular = particular;
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
                this.particular.apply(
                    Integer.valueOf(request.params(":id"))
                )
            );
        };
    }
}
