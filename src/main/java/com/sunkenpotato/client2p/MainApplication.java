package com.sunkenpotato.client2p;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import com.google.gson.Gson;
import com.sunkenpotato.client2p.internal.AppSettings;
import com.sunkenpotato.client2p.web.AuthorizationToken;
import com.sunkenpotato.client2p.i18n.Text;
import com.sunkenpotato.client2p.i18n.Translator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.util.Properties;

public class MainApplication extends Application {

    public static final Logger LOGGER = LogManager.getLogger("Homebase");

    public static final Properties APPLICATION_PROPERTIES = new Properties();
    public static String USERNAME;
    public static String PASSWORD;
    public static Text.Language APPLICATION_LANGUAGE = Text.Language.ENGLISH;
    public static final Translator APPLICATION_TRANSLATOR = Translator.fromLanguage(APPLICATION_LANGUAGE);
    public final static AuthorizationToken AUTHORIZATION_TOKEN = new AuthorizationToken("");
    public static Stage MAIN_STAGE;
    public static final Gson GSON = new Gson();
    public static final AppSettings SETTINGS = AppSettings.INSTANCE;

    @Override
    public void start(Stage stage) throws IOException {

        Configurator.initialize(null, this.getClass().getResource("log4j.xml").toExternalForm());

        LOGGER.info("Launching application");
        LOGGER.info("Setting stylesheet");
        setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

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

    public static void main(String[] args) {
        launch();
    }
}