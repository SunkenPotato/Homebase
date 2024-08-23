package com.sunkenpotato.client2p.controller;

import atlantafx.base.controls.PasswordTextField;
import com.sunkenpotato.client2p.web.response.CreateUserResponse;
import com.sunkenpotato.client2p.web.RequestFactory;
import com.sunkenpotato.client2p.i18n.Text;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.sunkenpotato.client2p.controller.LoginController.changeScene;

public class ACController {
    @FXML
    private Label usernameLabel, passwordLabel, confirmPasswordLabel, infoText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordTextField passwordField, confirmPasswordField;
    @FXML
    private Button returnButton, createAccountButton;

    private final String REGEX = "^[a-zA-Z0-9_]+$";

    private final RequestFactory REQUEST_FACTORY = RequestFactory.getInstance();

    private final String WARNING_STYLE = "-fx-text-fill: -color-warning-fg";
    private final String OK_STYLE = "-fx-text-fill: -color-success-fg";
    private final String ERROR_STYLE = "-fx-text-fill: -color-danger-fg";

    private final Text tooShortText = Text.translatable("text.warning.password_too_short");
    private final Text notEqualText = Text.translatable("text.warning.password_not_equal");
    private final Text usernameTooShort = Text.translatable("text.warning.username_short");
    private final Text usernameIncompatible = Text.translatable("text.warning.username_incompatible");
    private final Text okText = Text.translatable("text.labels.register_ok");
    private final Text userExistsText = Text.translatable("text.response.user_exists");
    private final Text serverErrorText = Text.translatable("text.response.server_error");
    private final Text serverDownText = Text.translatable("text.response.server_offline");
    private final Text userCreatedText = Text.translatable("text.response.user_created");

    @FXML
    private void initialize() {
        Text usernameText = Text.translatable("text.labels.username");
        Text passwordText = Text.translatable("text.labels.password");
        Text confirmPasswordText = Text.translatable("text.labels.confirm_password");
        Text returnButtonText = Text.translatable("text.button.return_to_login");
        Text createAccountButtonText = Text.translatable("text.button.create_account");

        usernameLabel.setText(usernameText.getTranslated());
        passwordLabel.setText(passwordText.getTranslated());
        confirmPasswordLabel.setText(confirmPasswordText.getTranslated());
        returnButton.setText(returnButtonText.getTranslated());
        createAccountButton.setText(createAccountButtonText.getTranslated());

        passwordField.passwordProperty().addListener(this::verifyPasswords);
        confirmPasswordField.passwordProperty().addListener(this::verifyPasswords1);
    }

    private void verifyPasswords(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        doChecks(observable.getValue(), confirmPasswordField);
    }

    private void verifyPasswords1(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        doChecks(observable.getValue(), passwordField);
    }

    private void doChecks(String newValue, PasswordTextField passwordField) {
        boolean areEqual = newValue.equals(passwordField.getPassword());
        boolean isLengthSatisfied = satisfiesLength(newValue);

        if (usernameField.getText().length() < 4) {
            setButtonState(true);
            infoText.setText(usernameTooShort.getTranslated());
        }
        else if (!usernameField.getText().matches(REGEX)) {
            setButtonState(true);
            infoText.setText(usernameIncompatible.getTranslated());
        }
        else if (!isLengthSatisfied) {
            setButtonState(true);
            infoText.setText(tooShortText.getTranslated());
        }
        else if (!areEqual) {
            setButtonState(true);
            infoText.setText(notEqualText.getTranslated());
        }
        else {
            setButtonState(false);
            infoText.setText(okText.getTranslated());
        }
    }

    private void setButtonState(boolean disabled) {
        createAccountButton.setDisable(disabled);
        infoText.setStyle(disabled ? WARNING_STYLE : OK_STYLE);
    }

    private boolean satisfiesLength(String newValue) {
        return newValue.length() >= 8;
    }

    @FXML
    private void backToLoginScreen() throws IOException {
        changeScene("fxml/login-view.fxml", 400, 600, "css/login-view.css");
    }

    @FXML
    private void createAccount() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getPassword();

        CreateUserResponse response = REQUEST_FACTORY.createUserRequest(username, password);

        if (!response.wasSuccessful()) {
            if (response == CreateUserResponse.USER_EXISTS) {

                infoText.setText(userExistsText.getTranslated());
                infoText.setStyle(ERROR_STYLE);
            } else if (response == CreateUserResponse.SERVER_ERROR) {

                infoText.setText(serverErrorText.getTranslated());
                infoText.setStyle(ERROR_STYLE);
            } else if (response == CreateUserResponse.SERVER_DOWN) {

                infoText.setText(serverDownText.getTranslated());
                infoText.setStyle(ERROR_STYLE);
            } else if (response == CreateUserResponse.UNKNOWN) {
                System.exit(1);
            }
        } else {
            infoText.setText(userCreatedText.getTranslated());
            infoText.setStyle(OK_STYLE);
        }
    }
}
