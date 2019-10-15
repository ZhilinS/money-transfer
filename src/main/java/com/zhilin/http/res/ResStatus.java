package com.zhilin.http.res;

public class ResStatus {

    private final Status status;

    public ResStatus(
        final Status status
    ) {
        this.status = status;
    }

    public enum Status {
        SUCCESS,
        ERROR
    }
}
