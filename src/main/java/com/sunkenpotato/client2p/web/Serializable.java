package com.sunkenpotato.client2p.web;

import com.sunkenpotato.client2p.MainApplication;

public interface Serializable {
    default String toJSON() {
        return MainApplication.GSON.toJson(this);
    }
}
