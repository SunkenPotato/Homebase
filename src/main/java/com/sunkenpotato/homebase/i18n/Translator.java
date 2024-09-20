package com.sunkenpotato.homebase.i18n;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.homebase.MainApplication;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

public class Translator {

    public static final String LANG_DIR = "lang/";

    private static final TypeToken<Map<String, String>> typeToken = new TypeToken<>() {};

    private final Gson gson = new Gson();
    private Map<String, String> languageMap;
    private static final boolean hasBeenInitialized = false;
    private Text.Language language;

    private Translator(Text.Language language) {
        setLanguage(language);
    }

    public static Translator getInstance() {
        return MainApplication.APPLICATION_TRANSLATOR;
    }

    public static Translator fromLanguage(Text.Language language) {

        if (!hasBeenInitialized)
            return new Translator(language);
        throw new IllegalStateException("Translator has already been initialized");
    }

    public Text.Language getLanguage() {
        return language;
    }

    public void setLanguage(Text.Language language) {
        String path = LANG_DIR + language.getFile();
        InputStream fileStream = MainApplication.class.getResourceAsStream(path);
        if (fileStream == null) {
            throw new IllegalArgumentException("Language file not found: " + language.getFile());
        }

        languageMap = gson.fromJson(new InputStreamReader(fileStream), typeToken);

        this.language = language;
    }

    public Optional<String> translateKey(String key) {
        String translated = languageMap.get(key);
        if (translated == null) {
            return Optional.empty();
        }
        return Optional.of(translated);
    }
}
