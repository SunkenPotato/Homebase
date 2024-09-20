package com.sunkenpotato.homebase.web.response;

import com.sunkenpotato.homebase.internal.FileItem;

import java.util.List;

public non-sealed class ListFileResponse extends APIResponse {
    private final List<FileItem> files;
    private final int statusCode;

    private ListFileResponse(int statusCode, List<FileItem> files) {
        super(statusCode);
        this.statusCode = statusCode;
        this.files = files;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    short getOkStatus() {
        return 200;
    }

    public static ListFileResponse fromCode(int statusCode) {
        return new ListFileResponse(statusCode, null);
    }

    public static ListFileResponse okResponse(List<FileItem> items) {
        return new ListFileResponse(200, items);
    }

    public List<FileItem> getFiles() {
        if (files == null) throw new NullPointerException("File Items are empty!");

        return files;
    }

}
