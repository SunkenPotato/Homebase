package com.sunkenpotato.client2p.controller;

import com.sunkenpotato.client2p.i18n.Text;
import com.sunkenpotato.client2p.web.RequestFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static com.sunkenpotato.client2p.MainApplication.SETTINGS;

public class ConfigController {
    @FXML
    private Label serverLabel, savePathLabel;

    @FXML
    private TextField serverTextField, savePathDescriptor;

    @FXML
    private Button applyButton, cancelButton, okButton, savePathButton, resetServerPathToDefault, resetSavePathToDefault;

    File saveDirectory = new File(System.getProperty("user.home") + "/Downloads");

    private final Text serverLabelText = Text.translatable("text.config.label.server");
    private final Text applyButtonText = Text.translatable("text.config.button.apply");
    private final Text cancelButtonText = Text.translatable("text.config.button.cancel");
    private final Text okButtonText = Text.translatable("text.config.button.ok");
    private final Text resetServerPathText = Text.translatable("text.config.button.reset_server");
    private final Text resetSavePathText = Text.translatable("text.config.button.reset_savepath");
    private final Text savePathLabelText = Text.translatable("text.config.label.savepath");
    private final Text savePathButtonText = Text.translatable("text.config.button.savepath");

    @FXML
    private void initialize() {
        serverLabel.setText(serverLabelText.getTranslated());
        applyButton.setText(applyButtonText.getTranslated());
        cancelButton.setText(cancelButtonText.getTranslated());
        okButton.setText(okButtonText.getTranslated());
        savePathLabel.setText(savePathLabelText.getTranslated());
        savePathButton.setText(savePathButtonText.getTranslated());

        serverTextField.setPromptText(SETTINGS.getServerAddress());
        savePathDescriptor.setPromptText(SETTINGS.getSavePath());

        resetServerPathToDefault.setText(resetServerPathText.getTranslated());
        resetSavePathToDefault.setText(resetSavePathText.getTranslated());
    }

    @FXML
    private void applySettings() throws IOException {
        String serverAddress = serverTextField.getText();

        SETTINGS.setServerAddress(serverAddress);
        SETTINGS.setSavePath(savePathDescriptor.getText());

        RequestFactory.getInstance().setBASE_URL(serverAddress);

        SETTINGS.syncFS();
    }

    @FXML
    private void okSettings() throws IOException {
        Stage currentStage = (Stage) serverLabel.getScene().getWindow();

        applySettings();
        currentStage.close();
    }

    @FXML
    private void cancelSettings() {
        Stage currentStage = (Stage) serverLabel.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void chooseDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose save directory");

        saveDirectory = directoryChooser.showDialog(null);
        if (saveDirectory != null)
            savePathDescriptor.setText(saveDirectory.getAbsolutePath());
    }

    @FXML
    private void resetServerPath() {
        serverTextField.setText(SETTINGS.defaultProperties.getProperty("server.address"));
    }

    @FXML
    private void resetSavePath() {
        savePathDescriptor.setText(SETTINGS.defaultProperties.getProperty("save.path"));
    }

}
