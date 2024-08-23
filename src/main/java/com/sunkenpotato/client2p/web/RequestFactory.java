package com.sunkenpotato.client2p.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.client2p.MainApplication;
import com.sunkenpotato.client2p.internal.FileItem;
import com.sunkenpotato.client2p.web.response.CreateUserResponse;
import com.sunkenpotato.client2p.web.response.FileUploadResponse;
import com.sunkenpotato.client2p.web.response.ListFileResponse;
import com.sunkenpotato.client2p.web.response.LoginResponse;
import javafx.scene.control.Alert;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

public class RequestFactory {

    private final String BASE_URL;
    private final OkHttpClient httpClient;
    private final Gson gson = new Gson();

    private static final MediaType APPLICATION_JSON = MediaType.parse("application/json");

    private final TypeToken<List<FileItem>> FILE_LIST_TYPE_TOKEN = new TypeToken<>() {};

    private static final RequestFactory INSTANCE = new RequestFactory(MainApplication.APPLICATION_PROPERTIES.getProperty("url"));

    private RequestFactory(String url) {
        this.BASE_URL = url;
        this.httpClient = new OkHttpClient();
    }

    public static RequestFactory getInstance() {
        return INSTANCE;
    }

    // okhttp so much cleaner ong ong
    @SuppressWarnings("DataFlowIssue")
    public LoginResponse loginRequest(String username, String password) throws IOException {

        UserData body = new UserData(username, password);

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/login")
                .post(RequestBody.create(body.toJSON(), APPLICATION_JSON))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return switch (response.code()) {
                case 200:
                    yield LoginResponse.okResponse(response.body().string());
                case 403:
                    yield LoginResponse.fromCode(403);
                case 404:
                    yield LoginResponse.fromCode(404);
                default:
                    yield LoginResponse.fromCode(response.code());
            };
        } catch (ConnectException e) {
            return LoginResponse.fromCode(1);
        }
    }

    public CreateUserResponse createUserRequest(String username, String password) throws IOException {
        UserData jsonData = new UserData(username, password);

        Request request = new Request.Builder()
                .url(BASE_URL + "/user/create")
                .post(RequestBody.create(jsonData.toJSON(), APPLICATION_JSON))
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return switch (response.code()) {
                case 201:
                    yield CreateUserResponse.OK;
                case 400:
                    yield CreateUserResponse.INVALID_FORM_1;
                case 422:
                    yield CreateUserResponse.INVALID_FORM_2;
                case 409:
                    yield CreateUserResponse.USER_EXISTS;
                case 500:
                    yield CreateUserResponse.SERVER_ERROR;
                default:
                    yield CreateUserResponse.UNKNOWN;
            };
        } catch (ConnectException e) {
            return CreateUserResponse.SERVER_DOWN;
        }
    }

    // IT WORKED
    // note to self:
    // never use apache httpcomponents
    @SuppressWarnings("DataFlowIssue")
    public ListFileResponse listFiles() throws IOException, ConnectException {

        Request request = new Request.Builder()
                .url(BASE_URL + "/file/list_files/" + MainApplication.USERNAME)
                .get()
                .addHeader("Authorization", MainApplication.AUTHORIZATION_TOKEN.getToken())
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return switch (response.code()) {
                case 200 -> {
                    List<FileItem> items = gson.fromJson(response.body().string(), FILE_LIST_TYPE_TOKEN);
                    yield ListFileResponse.okResponse(items);
                }
                case 403 -> {
                    showSessionExpired();
                    System.exit(0);
                    yield null;
                }
                case 404 -> ListFileResponse.fromCode(404);
                default -> ListFileResponse.fromCode(response.code());
            };
        } catch (ConnectException e) {
            throw new ConnectException(e.getMessage());
        }
    }

    @SuppressWarnings("DataFlowIssue")
    // cannot happen ^
    public FileUploadResponse uploadFile(String name, boolean isProtected, File file) throws IOException {
        FileUpload jsonData = new FileUpload(name, isProtected, MainApplication.USERNAME);

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("application/octet-stream")))
                .addFormDataPart("json_data", jsonData.toJSON())
                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + "/file/upload")
                .method("PUT", body)
                .addHeader("Authorization", MainApplication.AUTHORIZATION_TOKEN.getToken())
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            return switch (response.code()) {
                case 400 -> FileUploadResponse.fromCode(400);
                case 404 -> FileUploadResponse.fromCode(404);
                case 403 -> FileUploadResponse.fromCode(403);
                case 500 -> FileUploadResponse.fromCode(500);
                case 201 -> {
                    FileItem responseItem = gson.fromJson(response.body().string(), FileItem.class);
                    yield FileUploadResponse.okResponse(responseItem);
                }
                default -> FileUploadResponse.fromCode(response.code());
            };
        } catch (ConnectException e) {
            return FileUploadResponse.fromCode(1);
        }
    }

    public

    public void showSessionExpired() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Session Expired");
        alert.setHeaderText(null);
        alert.setContentText("Session expired. Please close the application and login again.");
        alert.showAndWait();
    }
}
