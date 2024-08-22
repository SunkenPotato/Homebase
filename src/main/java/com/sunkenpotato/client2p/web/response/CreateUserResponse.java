package com.sunkenpotato.client2p.web.response;

public enum CreateUserResponse {
    OK(201),
    USER_EXISTS(409),
    INVALID_FORM_1(400),
    INVALID_FORM_2(422),
    SERVER_ERROR(500),
    UNKNOWN(-1),
    SERVER_DOWN(1);

    private final int statusCode;
    CreateUserResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean wasSuccessful() {
        return statusCode == 201;
    }
}