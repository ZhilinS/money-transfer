package com.test.http.req;

import com.test.model.Transfer;

public interface ReqOperation {

    Integer account();

    Integer from();

    Integer to();

    double amount();

    Type type();

    Transfer.Type transfer();

    enum Type {
        SINGLE,
        MULTIPLE
    }
}
