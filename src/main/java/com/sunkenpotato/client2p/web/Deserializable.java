package com.sunkenpotato.client2p.web;

import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.client2p.MainApplication;

public interface Deserializable<T> {
    default T fromJSON(String json) {
        TypeToken<T> typeToken = new TypeToken<>(){};
        return MainApplication.GSON.fromJson(json, typeToken);
    }
}
