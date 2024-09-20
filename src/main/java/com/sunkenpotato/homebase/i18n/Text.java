package com.sunkenpotato.homebase.i18n;

import com.sunkenpotato.homebase.MainApplication;

import java.util.ArrayList;
import java.util.List;
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
            MainApplication.LOGGER.warn("Key {} not found in {}.", key, MainApplication.APPLICATION_TRANSLATOR.getLanguage());
            return "N/A";
        }

        return MainApplication.APPLICATION_TRANSLATOR.translateKey(key).get();
    }

    private String translateTo(String key, Language language) {
        return "";
    }

    // TODO: prob move somewhere else
    public enum Language {
        ENGLISH("en_us.json", "English"),
        GERMAN("de_de.json", "German");

        final String file;
        final String display;

        Language(String s, String display) {
            file = s;
            this.display = display;
        }

        public static List<String> getDisplayNames() {
            List<String> displayNames = new ArrayList<>();
            for (Language language : Language.values()) {
                displayNames.add(language.getDisplay());
            }

            return displayNames;
        }

        public String getFile() {
            return file;
        }

        public String getDisplay() {
            return display;
        }
    }

}
