package com.sunkenpotato.client2p.internal;

import org.apache.commons.io.FileUtils;

public class FileItem {
    public boolean isProtected;
    public String filename;
    public String type;
    public String owner;
    public String size;
    public String route;

    public FileItem(boolean isProtected, String name, String type, String owner, int size) {
        this.isProtected = isProtected;
        this.filename = name;
        this.type = type;
        this.owner = owner;
        this.size = FileUtils.byteCountToDisplaySize(size);
    }

    public String getName() {
        return filename;
    }

    public String getType() {
        return type;
    }

    public String getOwner() {
        return owner;
    }

    public String getSize() {
        return size;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public String getRoute() {
        return route;
    }
}
