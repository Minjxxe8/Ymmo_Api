package com.ymmo.ymmoapi.exception;

public class ResponseException extends RuntimeException {
    int httpCode;

    public ResponseException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
