package com.sunkenpotato.client2p.internal;

import com.sunkenpotato.client2p.MainApplication;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

// This is probably not programmed too well
// I might replace this w/ a library in the future
public class AppSettings {
    public static final AppSettings INSTANCE = new AppSettings();

    public final Properties defaultProperties = new Properties();

    private final File settingsFile = new File(System.getProperty("user.home") + "/.local/2P/settings.properties");
    private Properties settings = new Properties();


    private final Logger LOGGER = MainApplication.LOGGER;

    private AppSettings() {
        try {
            setup();
        } catch (IOException e) {
            LOGGER.error("Failed to set up settings", e);
        }
    }

    private void setup() throws IOException {

        defaultProperties.load(MainApplication.class.getResourceAsStream("config/default.properties"));

        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            settingsFile.createNewFile();
        }

        settings.load(new FileInputStream(settingsFile));
        if (settings.isEmpty()) {
            InputStream in = MainApplication.class.getResourceAsStream("config/default.properties");
            settings.load(in);
        }

        LOGGER.info("Settings: {}", settings);
    }

    public String getServerAddress() {
        return settings.getProperty("server.address");
    }

    public String getSavePath() {
        return expandTilde(settings.getProperty("save.path"));
    }

    public void setServerAddress(String serverAddress) {
        settings.setProperty("server.address", serverAddress);
    }

    public void setSavePath(String savePath) {
        settings.setProperty("save.path", savePath);
    }

    private String expandTilde(String path) {
        return path.replace("~", System.getProperty("user.home"));
    }

    public synchronized void syncFS() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(settingsFile, false);
        settings.store(fileOutputStream, "Homebase Settings. It is not recommended to change these manually.");
    }
}
