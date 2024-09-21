package com.sunkenpotato.homebase.controller;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.PasswordTextField;
import com.sunkenpotato.homebase.MainApplication;
import com.sunkenpotato.homebase.i18n.Text;
import com.sunkenpotato.homebase.web.response.LoginResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static com.sunkenpotato.homebase.controller.ControllerHandler.*;

public class LoginController {
    @FXML
    private Label usernameLabel, passwordLabel, infoText;
    @FXML
    private CustomTextField usernameField;
    @FXML
    private PasswordTextField passwordField;
    @FXML
    private Button loginButton, accountCreationButton;


    private final Text usernameText = Text.translatable("text.labels.username");
    private final Text passwordText = Text.translatable("text.labels.password");
    private final Text loginText = Text.translatable("text.button.login");
    private final Text accountCreationText = Text.translatable("text.button.create_account");
    private final Text serverOfflineText = Text.translatable("text.response.server_offline");

    @FXML
    public void initialize() {
        setTitle(usernameLabel, "Homebase - Login");
        usernameLabel.setText(usernameText.getTranslated());
        passwordLabel.setText(passwordText.getTranslated());
        loginButton.setText(loginText.getTranslated());
        accountCreationButton.setText(accountCreationText.getTranslated());
    }

    @FXML
    private void login() throws IOException {
        final String username = usernameField.getText();
        final String password = passwordField.getPassword();

        LoginResponse response;

        response = REQUEST_FACTORY.loginRequest(username, password);

        if (response.failed()) {
            if (response.getStatusCode() == 404) {
                Text unknownUserText = Text.translatable("text.response.unknown_user");
                infoText.setText(unknownUserText.getTranslated());
            }
            else if (response.getStatusCode() == 403) {
                Text unauthorizedUserText = Text.translatable("text.response.bad_password");
                infoText.setText(unauthorizedUserText.getTranslated());
            }
            else if (response.getStatusCode() == 1) {
                infoText.setText(serverOfflineText.getTranslated());
            }
            else {
                Text errorText = Text.translatable("text.response.unexpected_error");
                infoText.setText(errorText.getTranslated());
            }
        } else {
            MainApplication.AUTHORIZATION_TOKEN.setToken(response.getToken());
            changeScene("fxml/main-view.fxml", MainApplication.MAIN_STAGE, 900, 600);
        }
    }

    @FXML
    private void createAccount() throws IOException {
        changeScene("fxml/create-account.fxml", MainApplication.MAIN_STAGE, 400, 600, "css/create-account.css");
    }

    @FXML
    private void openConfig() throws IOException {
        Stage configStage = new Stage();
        changeScene("fxml/config-view.fxml", configStage, 600, 450);
    }

}
