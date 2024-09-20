package com.sunkenpotato.homebase.web;

import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.homebase.MainApplication;

public interface Deserializable<T> {
    default T fromJSON(String json) {
        TypeToken<T> typeToken = new TypeToken<>(){};
        return MainApplication.GSON.fromJson(json, typeToken);
    }
}
