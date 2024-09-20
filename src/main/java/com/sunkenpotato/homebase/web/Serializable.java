package com.sunkenpotato.homebase.web;

import com.sunkenpotato.homebase.MainApplication;

public interface Serializable {
    default String toJSON() {
        return MainApplication.GSON.toJson(this);
    }
}
