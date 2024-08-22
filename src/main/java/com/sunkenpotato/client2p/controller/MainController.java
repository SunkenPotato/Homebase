package com.sunkenpotato.client2p.controller;

import com.sunkenpotato.client2p.MainApplication;
import com.sunkenpotato.client2p.i18n.Text;
import com.sunkenpotato.client2p.internal.FileItem;
import com.sunkenpotato.client2p.web.response.FileUploadResponse;
import com.sunkenpotato.client2p.web.response.ListFileResponse;
import com.sunkenpotato.client2p.web.RequestFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class MainController {

    @FXML
    private Button uploadButton, deleteButton, downloadButton;
    @FXML
    private TableView<FileItem> fileTable;
    @FXML
    private TableColumn<FileItem, String> nameColumn, typeColumn, sizeColumn, ownerColumn;
    @FXML
    private TableColumn<FileItem, Boolean> protectedColumn;

    private final RequestFactory REQUEST_FACTORY = RequestFactory.getInstance();

    private final Text serverOfflineText = Text.translatable("text.response.server_offline");
    private final Text errorText = Text.translatable("text.errors.error");
    private final Text unexpectedErrorText = Text.translatable("text.response.unexpected_error");

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("owner"));
        protectedColumn.setCellValueFactory(new PropertyValueFactory<>("protected"));

        fileTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                deleteButton.setDisable(true);
                downloadButton.setDisable(true);
            } else {
                deleteButton.setDisable(false);
                downloadButton.setDisable(false);
            }
        });

        populateTable();
    }

    @FXML
    private void uploadFile() {

        Dialog<File> dialog = new Dialog<>();
        dialog.setTitle("Upload File");
        dialog.initOwner(fileTable.getScene().getWindow());

        // Request file from user
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");

        Button chooseFileButton = new Button("Choose File");
        // Array because it has to be effectively final
        File[] fileArray = new File[1];
        chooseFileButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(fileTable.getScene().getWindow());
            if (file != null) {
                fileArray[0] = file;
                dialog.setResult(file);
            }
        });

        CheckBox isProtected = new CheckBox("Protected");

        VBox content = new VBox(10, chooseFileButton, isProtected);
        dialog.getDialogPane().setContent(content);
        content.setPadding(new Insets(20));

        dialog.getDialogPane().getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<File> result = dialog.showAndWait();
        if (result.isEmpty())
            return;

        File uploadedFile = result.get();

        FileUploadResponse response;
        try {
            response = REQUEST_FACTORY.uploadFile(uploadedFile.getName(), isProtected.isSelected(), uploadedFile);
        } catch (ExecutionException | IOException | InterruptedException e) {
            System.out.println("Called.");
            e.printStackTrace();
            showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            return;
        }

        if (response.failed()) {
            switch (response.getStatusCode()) {
                case 404 -> MainApplication.LOGGER.error("User was not found.");
                case 403 -> REQUEST_FACTORY.showSessionExpired();
                case 500 -> MainApplication.LOGGER.error("Server-side error occurred. Please contact the developers.");
                case 400 -> MainApplication.LOGGER.error("Bad request");
                default -> showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            }
            return;
        }

        fileTable.getItems().add(response.getFileItem());
        fileTable.refresh();

    }


    private void populateTable() {
        ListFileResponse LFR;

        try {
            LFR = REQUEST_FACTORY.listFiles();
        } catch (ExecutionException | InterruptedException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(serverOfflineText.getTranslated());
            alert.showAndWait();
            return;
        }

        ObservableList<FileItem> osList = FXCollections.observableList(LFR.getFiles());
        osList.add(new FileItem(true, "MainApplication.java", "Java Source", "piro2", 2048));
        fileTable.setItems(osList);
    }

    private void showAlert(Text title, Text content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title.getTranslated());
        alert.setContentText(content.getTranslated());
        alert.showAndWait();
    }




}
