package com.sunkenpotato.homebase;

import atlantafx.base.theme.PrimerDark;
import com.google.gson.Gson;
import com.sunkenpotato.homebase.config.Config;
import com.sunkenpotato.homebase.controller.ConfigController;
import com.sunkenpotato.homebase.i18n.Text;
import com.sunkenpotato.homebase.i18n.Translator;
import com.sunkenpotato.homebase.web.AuthorizationToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class MainApplication extends Application {

    public static final AuthorizationToken AUTHORIZATION_TOKEN = new AuthorizationToken("");
    public static final Config SETTINGS = Config.INSTANCE;

    public static final Properties APPLICATION_PROPERTIES = new Properties();
    public static Logger LOGGER = LogManager.getLogger("Homebase");
    public static Translator APPLICATION_TRANSLATOR;
    public static Stage MAIN_STAGE;
    public static final Gson GSON = new Gson();

    static {
        Configurator.initialize(null, MainApplication.class.getResource("log4j.xml").toExternalForm());
    }

    @Override
    public void start(Stage stage) throws IOException {
        LOGGER.info("Launching application");

        LOGGER.info("Adding shutdown hook");
        Runtime.getRuntime().addShutdownHook(new Thread(this::renameLogs));

        LOGGER.info("Setting stylesheet");
        setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        LOGGER.info("Initializing Translator");
        APPLICATION_TRANSLATOR = Translator.fromLanguage(Text.Language.valueOf(Config.INSTANCE.get(ConfigController.languageKey).get().toUpperCase()));

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        scene.getStylesheets().add(MainApplication.class.getResource("css/login-view.css").toExternalForm());
        scene.getStylesheets().add(MainApplication.class.getResource("css/global.css").toExternalForm());
        stage.setTitle(APPLICATION_PROPERTIES.getProperty("app.title"));
        stage.setScene(scene);
        LOGGER.info("Showing login scene");
        stage.show();
        stage.resizableProperty().setValue(false);
        MAIN_STAGE = stage;
    }

    private void renameLogs() {
        Calendar calendar = Calendar.getInstance();
        File logFile = new File("logs/latest.log");
        String fileName = "logs/Homebase-" + new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(calendar.getTime()) + ".log";
        File destinationFile = new File(fileName);

        LOGGER = null;

        logFile.renameTo(destinationFile);
    }

    public static void main(String[] args) {
        launch();
    }
}