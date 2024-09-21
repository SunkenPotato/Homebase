package com.sunkenpotato.homebase.web.response;

public sealed abstract class APIResponse permits FileUploadResponse, ListFileResponse, LoginResponse {
    public final int statusCode;

    public APIResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean failed() {
        return statusCode != getOkStatus();
    }

    abstract short getOkStatus();

}

