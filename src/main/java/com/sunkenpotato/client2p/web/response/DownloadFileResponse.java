package com.sunkenpotato.client2p.web.response;

public enum DownloadFileResponse {
    OK,
    NOT_FOUND,
    SESSION_EXPIRED,
    SERVER_FS_ERROR,
    UNKNOWN,
    EMPTY_BODY,
    CONNECTION_FAILURE
}
