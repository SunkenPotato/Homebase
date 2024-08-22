package com.sunkenpotato.client2p.web.response;

public final class LoginResponse extends APIResponse {
    private final String token;

    private LoginResponse(int statusCode, String token) {
        super(statusCode);
        this.token = token;
    }

    private LoginResponse(int statusCode) {
        this(statusCode, null);
    }

    public static LoginResponse fromCode(int statusCode) {
        return new LoginResponse(statusCode);
    }

    public static LoginResponse okResponse(String token) {
        return new LoginResponse(200, token);
    }

    public String getToken() {
        return token;
    }

    @Override
    short getOkStatus() {
        return 200;
    }
}
