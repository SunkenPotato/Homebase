package com.sunkenpotato.client2p.web.response;

import com.sunkenpotato.client2p.internal.FileItem;

public final class FileUploadResponse extends APIResponse {
    private final FileItem fileItem;
    private final int statusCode;

    private FileUploadResponse(FileItem fileItem, int statusCode) {
        super(statusCode);
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

    @Override
    short getOkStatus() {
        return 201;
    }

    public FileItem getFileItem() {
        if (fileItem == null) throw new NullPointerException("fileItem is null");

        return fileItem;
    }

}
