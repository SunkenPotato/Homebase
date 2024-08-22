package com.sunkenpotato.client2p.web;

import com.sunkenpotato.client2p.internal.FileItem;

public class FileUploadResponse {
    private final FileItem fileItem;
    private final int statusCode;

    private FileUploadResponse(FileItem fileItem, int statusCode) {
        this.fileItem = fileItem;
        this.statusCode = statusCode;
    }

    private FileUploadResponse(FileItem fileItem) {
        this(fileItem, 201);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static FileUploadResponse fromCode(int statusCode) {
        return new FileUploadResponse(null, statusCode);
    }

    public static FileUploadResponse okResponse(FileItem fileItem) {
        return new FileUploadResponse(fileItem);
    }

    public boolean wasSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    public FileItem getFileItem() {
        if (fileItem == null) throw new NullPointerException("fileItem is null");

        return fileItem;
    }

}
