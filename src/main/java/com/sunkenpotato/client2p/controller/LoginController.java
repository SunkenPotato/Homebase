package com.sunkenpotato.client2p.controller;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.PasswordTextField;
import com.sunkenpotato.client2p.MainApplication;
import com.sunkenpotato.client2p.internal.FileItem;
import com.sunkenpotato.client2p.web.AuthorizationToken;
import com.sunkenpotato.client2p.web.LoginResponse;
import com.sunkenpotato.client2p.web.RequestFactory;
import com.sunkenpotato.client2p.i18n.Text;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LoginController {
    @FXML
    private Label usernameLabel, passwordLabel, infoText;
    @FXML
    private CustomTextField usernameField;
    @FXML
    private PasswordTextField passwordField;
    @FXML
    private Button loginButton, accountCreationButton;

    private final static RequestFactory REQUEST_FACTORY = RequestFactory.getInstance();

    private final Text usernameText = Text.translatable("text.labels.username");
    private final Text passwordText = Text.translatable("text.labels.password");
    private final Text loginText = Text.translatable("text.button.login");
    private final Text accountCreationText = Text.translatable("text.button.create_account");


    @FXML
    public void initialize() {
        usernameLabel.setText(usernameText.getTranslated());
        passwordLabel.setText(passwordText.getTranslated());
        loginButton.setText(loginText.getTranslated());
        accountCreationButton.setText(accountCreationText.getTranslated());
    }

    @FXML
    private void login() throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getPassword();

        System.out.println(password);

        LoginResponse response = null;

        try {
            response = REQUEST_FACTORY.loginRequest(username, password);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Text errorText = Text.translatable("text.response.server_offline");
            infoText.setText(errorText.getTranslated());
            return;
        }

        if (!response.wasSuccessful()) {
            if (response.getStatusCode() == 404) {
                Text unknownUserText = Text.translatable("text.response.unknown_user");
                infoText.setText(unknownUserText.getTranslated());
            }
            else if (response.getStatusCode() == 403) {
                Text unauthorizedUserText = Text.translatable("text.response.bad_password");
                infoText.setText(unauthorizedUserText.getTranslated());
            }
            else {
                Text errorText = Text.translatable("text.response.unexpected_error");
                infoText.setText(errorText.getTranslated());
            }
        } else {
            MainApplication.AUTHORIZATION_TOKEN.setToken(response.getToken());
            MainApplication.USERNAME = username;
            changeScene("fxml/main-view.fxml", 900, 600);
        }

    }

    @FXML
    private void createAccount() throws IOException {
        changeScene("fxml/create-account.fxml", 400, 600, "css/create-account.css");
    }

    public static void changeScene(String fxml, int width, int height, String... stylesheets) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        for (String stylesheet : stylesheets) {
            scene.getStylesheets().add(MainApplication.class.getResource(stylesheet).toExternalForm());
        }
        scene.getStylesheets().add(MainApplication.class.getResource("css/global.css").toExternalForm());
        MainApplication.MAIN_STAGE.setScene(scene);
        MainApplication.MAIN_STAGE.show();
    }


}
