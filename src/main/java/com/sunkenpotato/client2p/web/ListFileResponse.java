package com.sunkenpotato.client2p.web;

import com.sunkenpotato.client2p.internal.FileItem;

import java.util.List;

public class ListFileResponse {
    private final List<FileItem> files;
    private final int statusCode;

    private ListFileResponse(int statusCode, List<FileItem> files) {
        this.statusCode = statusCode;
        this.files = files;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean wasSuccessful() {
        return statusCode == 200;
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
