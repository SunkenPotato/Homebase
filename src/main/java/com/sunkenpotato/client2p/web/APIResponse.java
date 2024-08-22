package com.sunkenpotato.client2p.web;

public sealed abstract class APIResponse permits LoginResponse {
    public final int statusCode;

    public APIResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}

