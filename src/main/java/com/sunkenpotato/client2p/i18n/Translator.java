package com.sunkenpotato.client2p.i18n;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.client2p.MainApplication;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class Translator {

    public static final String LANG_DIR = "lang/";

    private static final TypeToken<Map<String, String>> typeToken = new TypeToken<>() {};

    private final Gson gson = new Gson();
    private Map<String, String> languageMap;
    private final Text.Language language;

    private Translator(Text.Language language) {
        String path = LANG_DIR + language.getValue();
        System.out.print(path);
        InputStream fileStream = MainApplication.class.getResourceAsStream(path);
        if (fileStream == null) {
            throw new IllegalArgumentException("Language file not found: " + language.getValue());
        }

        languageMap = gson.fromJson(new InputStreamReader(fileStream), typeToken);

        this.language = language;
    }

    public static Translator fromLanguage(Text.Language language) {
        return new Translator(language);
    }

    public Optional<String> translateKey(String key) {
        String translated = languageMap.get(key);
        if (translated == null) {
            return Optional.empty();
        }
        return Optional.of(translated);
    }
}
