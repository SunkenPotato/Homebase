package com.sunkenpotato.client2p.controller;

import com.sunkenpotato.client2p.MainApplication;
import com.sunkenpotato.client2p.i18n.Text;
import com.sunkenpotato.client2p.internal.FileItem;
import com.sunkenpotato.client2p.web.ListFileResponse;
import com.sunkenpotato.client2p.web.RequestFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

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




}
