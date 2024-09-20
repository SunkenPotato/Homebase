package com.sunkenpotato.homebase.controller;

import com.sunkenpotato.homebase.i18n.Text;
import com.sunkenpotato.homebase.internal.FileItem;
import com.sunkenpotato.homebase.web.RequestFactory;
import com.sunkenpotato.homebase.web.response.DeleteFileResponse;
import com.sunkenpotato.homebase.web.response.DownloadFileResponse;
import com.sunkenpotato.homebase.web.response.FileUploadResponse;
import com.sunkenpotato.homebase.web.response.ListFileResponse;
import javafx.beans.property.SimpleStringProperty;
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
import java.net.ConnectException;
import java.util.Optional;

import static com.sunkenpotato.homebase.MainApplication.LOGGER;

public class MainController {

    @FXML
    private Label infoText;
    @FXML
    private Button uploadButton, deleteButton, downloadButton;
    @FXML
    private TableView<FileItem> fileTable;
    @FXML
    private TableColumn<FileItem, String> nameColumn, typeColumn, sizeColumn;
    @FXML
    private TableColumn<FileItem, String> protectedColumn;

    private final RequestFactory REQUEST_FACTORY = RequestFactory.getInstance();

    private final Text serverOfflineText = Text.translatable("text.response.server_offline");
    private final Text errorText = Text.translatable("text.errors.error");
    private final Text unexpectedErrorText = Text.translatable("text.response.unexpected_error");
    private final Text fileDownloadedText = Text.translatable("text.response.file_downloaded");
    private final Text fileNotFoundText = Text.translatable("text.response.file_not_found");
    private final Text serverFSError = Text.translatable("text.response.server_fs_error");

    public static void showAlert(Text title, Text content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title.getTranslated());
        alert.setContentText(content.getTranslated());
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        initCellValueFactories();

        setColumnNames();

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

    private void setColumnNames() {
        Text nameCLText = Text.translatable("text.main.column.name");
        Text typeCLText = Text.translatable("text.main.column.type");
        Text sizeCLText = Text.translatable("text.main.column.size");
        Text protectedCLText = Text.translatable("text.main.column.protected");

        nameColumn.setText(nameCLText.getTranslated());
        typeColumn.setText(typeCLText.getTranslated());
        sizeColumn.setText(sizeCLText.getTranslated());
        protectedColumn.setText(protectedCLText.getTranslated());

    }

    private void initCellValueFactories() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType()));
        protectedColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDisplayProtected()));
        sizeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDisplaySize()));

    }

    @FXML
    private void downloadFile() throws IOException {
        FileItem fileItem = fileTable.getSelectionModel().getSelectedItem();
        if (fileItem == null)
            return;

        DownloadFileResponse response = REQUEST_FACTORY.downloadFile(fileItem);
        switch (response) {
            // TODO: set this to a custom path/.properties path
            case OK -> infoText.setText(fileDownloadedText.getTranslated() + " Downloads");
            case NOT_FOUND -> setInfoTextWarn(fileNotFoundText);
            case CONNECTION_FAILURE -> showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            case UNKNOWN -> showAlert(errorText, unexpectedErrorText, Alert.AlertType.ERROR);
            case SERVER_FS_ERROR -> setInfoTextWarn(serverFSError);
            case SESSION_EXPIRED, EMPTY_BODY -> {}
        }
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
        chooseFileButton.setOnAction(event -> {
            File file = fileChooser.showOpenDialog(fileTable.getScene().getWindow());
            if (file != null)
                dialog.setResult(file);
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
        } catch (IOException e) {
            LOGGER.error(e);
            showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            return;
        }

        if (response.failed()) {
            switch (response.getStatusCode()) {
                case 404 -> System.err.println("User was not found.");
                case 403 -> REQUEST_FACTORY.showSessionExpired();
                case 500 -> System.err.println("Server-side error occurred. Please contact the developers.");
                case 400 -> System.err.println("Bad request");
                default -> showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            }
            return;
        }

        fileTable.getItems().add(response.getFileItem());
        fileTable.refresh();
    }

    private void setInfoTextWarn(Text text) {
        infoText.setText(text.getTranslated());
        infoText.setStyle("-fx-text-fill: -color-warning-fg");
    }

    private void populateTable() {
        ListFileResponse LFR;

        try {
            LFR = REQUEST_FACTORY.listFiles();
        } catch (ConnectException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(serverOfflineText.getTranslated());
            alert.showAndWait();
            return;
        } catch (IOException e) {
            throw new RuntimeException(e); // what exception :(
        }

        ObservableList<FileItem> osList = FXCollections.observableList(LFR.getFiles());
        fileTable.setItems(osList);
    }

    @FXML
    private void deleteFile() throws IOException {
        FileItem fileItem = fileTable.getSelectionModel().getSelectedItem();
        if (fileItem == null)
            return;

        DeleteFileResponse response = REQUEST_FACTORY.deleteFile(fileItem);
        switch (response) {
            case OK ->  {
                fileTable.getItems().remove(fileItem);
                infoText.setText(fileItem.name + " Deleted");
            }
            case NOT_FOUND -> infoText.setText(fileItem.name + " Not Found");
            case FORBIDDEN -> REQUEST_FACTORY.showSessionExpired();
            case CONNECTION_ERROR -> showAlert(errorText, serverOfflineText, Alert.AlertType.ERROR);
            case UNKNOWN -> showAlert(errorText, unexpectedErrorText, Alert.AlertType.ERROR);
        }
    }
}
