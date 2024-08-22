package com.sunkenpotato.client2p;

import atlantafx.base.theme.PrimerDark;
import com.google.gson.Gson;
import com.sunkenpotato.client2p.web.AuthorizationToken;
import com.sunkenpotato.client2p.i18n.Text;
import com.sunkenpotato.client2p.i18n.Translator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class MainApplication extends Application {

    public static final Properties APPLICATION_PROPERTIES = new Properties();
    public static String USERNAME;
    public static String PASSWORD;
    public static Text.Language APPLICATION_LANGUAGE = Text.Language.ENGLISH;
    public static final Translator APPLICATION_TRANSLATOR = Translator.fromLanguage(APPLICATION_LANGUAGE);
    public final static AuthorizationToken AUTHORIZATION_TOKEN = new AuthorizationToken("");
    public static Stage MAIN_STAGE;
    public static final Gson GSON = new Gson();
    public static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    static {

        try {
            APPLICATION_PROPERTIES.load(MainApplication.class.getResourceAsStream("config/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);
        scene.getStylesheets().add(MainApplication.class.getResource("css/login-view.css").toExternalForm());
        scene.getStylesheets().add(MainApplication.class.getResource("css/global.css").toExternalForm());
        stage.setTitle(APPLICATION_PROPERTIES.getProperty("app.title"));
        stage.setScene(scene);
        stage.show();
        MAIN_STAGE = stage;
    }

    public static void main(String[] args) {
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.wire", "DEBUG");

        launch();
    }
}