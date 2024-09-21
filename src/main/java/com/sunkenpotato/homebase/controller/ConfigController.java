package com.sunkenpotato.homebase.controller;

import com.sunkenpotato.homebase.config.Config;
import com.sunkenpotato.homebase.i18n.Text;
import com.sunkenpotato.homebase.i18n.Translator;
import com.sunkenpotato.homebase.web.RequestFactory;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import static com.sunkenpotato.homebase.MainApplication.LOGGER;
import static com.sunkenpotato.homebase.MainApplication.SETTINGS;

public class ConfigController {
    public static final String serverAddressKey = "server.address";
    public static final String savePathKey = "save.path";
    public static final String languageKey = "language";
    private final Text languageLabelText = Text.translatable("text.config.label.language");

    @FXML
    private TextField serverTextField, savePathDescriptor;
    @FXML
    private Label serverLabel, savePathLabel, languageLabel;

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
    private ComboBox<String> languageSelector;

    @FXML
    private void initialize() {
        ControllerHandler.setTitle(serverLabel, "Homebase - Settings");

        serverLabel.setText(serverLabelText.getTranslated());
        applyButton.setText(applyButtonText.getTranslated());
        cancelButton.setText(cancelButtonText.getTranslated());
        okButton.setText(okButtonText.getTranslated());
        savePathLabel.setText(savePathLabelText.getTranslated());
        savePathButton.setText(savePathButtonText.getTranslated());
        languageLabel.setText(languageLabelText.getTranslated());

        fillFields();

        resetServerPathToDefault.setText(resetServerPathText.getTranslated());
        resetSavePathToDefault.setText(resetSavePathText.getTranslated());

        // make sure scene and window are locked n loaded
        applyButton.sceneProperty().addListener((observable, oldValue, newValue) ->
                newValue.windowProperty().addListener(((observable1, oldValue1, newValue1) ->
                        newValue1.setOnCloseRequest(event -> applySettings()))));
    }

    private void fillFields() {
        String serverFieldValue = Config.INSTANCE.get(serverAddressKey).orElseThrow();
        String savePathFieldValue = Config.INSTANCE.get(savePathKey).orElseThrow();

        if (serverFieldValue.isEmpty())
            serverTextField.setText(Config.INSTANCE.getDefault(serverAddressKey).orElseThrow());
        else
            serverTextField.setText(serverFieldValue);

        if (savePathFieldValue.isEmpty())
            savePathDescriptor.setText(Config.INSTANCE.getDefault(savePathKey).orElseThrow());
        else
            savePathDescriptor.setText(savePathFieldValue);

        languageSelector.setItems(FXCollections.observableList(Text.Language.getDisplayNames()));
        languageSelector.setValue(Translator.getInstance().getLanguage().getDisplay());
        languageSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
            Text changeText = Text.translatable("text.warning.changes_detected");
            Text languageChangeText = Text.translatable("text.warning.language_change");
            MainController.showAlert(changeText, languageChangeText, Alert.AlertType.WARNING);
        });
    }

    @FXML
    private void applySettings() {
        String serverAddress = serverTextField.getText();
        String savePath = savePathDescriptor.getText();
        String language = languageSelector.getValue();

        SETTINGS.set(serverAddressKey, serverAddress);
        SETTINGS.set(savePathKey, savePath);
        SETTINGS.set(languageKey, language);

        Translator.getInstance().setLanguage(Text.Language.valueOf(language.toUpperCase()));
        RequestFactory.getInstance().setBASE_URL(serverAddress);

        SETTINGS.save();

        LOGGER.info("Saving settings");
    }

    @FXML
    private void okSettings() {
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
        serverTextField.setText(SETTINGS.getDefault(serverAddressKey).orElseThrow());
    }

    @FXML
    private void resetSavePath() {
        savePathDescriptor.setText(SETTINGS.getDefault(savePathKey).orElseThrow());
    }
}
