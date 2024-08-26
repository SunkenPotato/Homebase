package com.sunkenpotato.client2p.i18n;

import com.sunkenpotato.client2p.MainApplication;

import java.util.Optional;

public class Text {

    private final String key;

    private Text(String key) {
        this.key = key;
    }

    // TODO: implement lol
    public static Text translatable(String key) {
        return new Text(key);
    }

    public String getTranslated() {
        return translateDefault(key);
    }

    private String translateDefault(String key) {
        Optional<String> translated = MainApplication.APPLICATION_TRANSLATOR.translateKey(key);
        if (translated.isEmpty()) {
            MainApplication.LOGGER.warn("Key {} not found in {}.", key, MainApplication.APPLICATION_LANGUAGE.value);
            return "Key not found";
        }

        return MainApplication.APPLICATION_TRANSLATOR.translateKey(key).get();
    }

    private String translateTo(String key, Language language) {
        return "";
    }

    // TODO: prob move somewhere else
    public enum Language {
        ENGLISH("en_us.json");

        String value;
        Language(String s) {
            value = s;
        }

        public String getValue() {
            return value;
        }
    }

}
