package com.sunkenpotato.homebase.internal;

import org.apache.commons.io.FileUtils;

public class FileItem {
    public final int id;
    public final boolean isProtected;
    public final long size;
    public final String route;
    public final String name;

    public FileItem(int id, boolean isProtected, long size, String route, String name) {
        this.id = id;
        this.isProtected = isProtected;
        this.size = size;
        this.route = route;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDisplaySize() {
        return FileUtils.byteCountToDisplaySize(size);
    }

    public String getType() {
        return name.split("\\.")[1].toUpperCase();
    }

    public String getDisplayProtected() {
        return this.isProtected ? "\uD83D\uDD12" : "\uD83D\uDD13";
    }

    public String getRoute() {
        return route;
    }
}
