package com.zhilin.http.req;

import com.zhilin.model.Transfer;

public interface ReqOperation {

    Integer account();

    Integer from();

    Integer to();

    float amount();

    Type type();

    Transfer.Type transfer();

    enum Type {
        SINGLE,
        MULTIPLE
    }
}
