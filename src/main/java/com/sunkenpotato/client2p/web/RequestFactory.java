package com.sunkenpotato.client2p.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sunkenpotato.client2p.MainApplication;
import com.sunkenpotato.client2p.internal.FileItem;
import javafx.scene.control.Alert;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.http.*;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RequestFactory {

    private final String BASE_URL;
    private final CloseableHttpAsyncClient httpClient;
    private final Gson gson = new Gson();

    private final TypeToken<List<FileItem>> FILE_LIST_TYPE_TOKEN = new TypeToken<>() {};

    private static final RequestFactory INSTANCE = new RequestFactory(MainApplication.APPLICATION_PROPERTIES.getProperty("url"));

    private RequestFactory(String url) {
        this.BASE_URL = url;
        this.httpClient = HttpAsyncClients.createDefault();
        httpClient.start();
    }

    public static RequestFactory getInstance() {
        return INSTANCE;
    }

    // TODO: add body lol
    public LoginResponse loginRequest(String username, String password) throws ExecutionException, InterruptedException {

        SimpleHttpRequest request = SimpleHttpRequest.create(Method.POST, URI.create(BASE_URL + "/user/login"));

        String body = "{" +
                "\"username\":\"" + username + "\"," +
                "\"password\":\"" + password + "\"" +
                "}";

        request.setBody(body, ContentType.APPLICATION_JSON);

        SimpleHttpResponse response = httpClient.execute(request, null).get();

        return switch (response.getCode()) {
            case 200:
                yield LoginResponse.okResponse(response.getBodyText());
            case 403:
                yield LoginResponse.fromCode(403);
            case 404:
                yield LoginResponse.fromCode(404);
            default:
                yield LoginResponse.fromCode(response.getCode());
        };
    }

    public CreateUserResponse createUserRequest(String username, String password) {
        SimpleHttpRequest request = SimpleHttpRequest.create(Method.POST, URI.create(BASE_URL + "/user/create"));
        // I know it's ugly lol
        String sb = "{" +
                "\"username\":\"" +
                username + "\"," +
                "\"password\":\"" +
                password +
                "\"" +
                "}";

        request.setBody(sb, ContentType.APPLICATION_JSON);
        SimpleHttpResponse response = null;
        try {
            response = httpClient.execute(request, null).get();
        } catch (InterruptedException | ExecutionException e) {
            return CreateUserResponse.SERVER_DOWN;
        }

        return switch (response.getCode()) {
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
    }

    public ListFileResponse listFiles() throws ExecutionException, InterruptedException {
        SimpleHttpRequest request = SimpleHttpRequest.create(Method.GET, URI.create(BASE_URL + "/file/list_files/" + MainApplication.USERNAME));
        request.setHeader("Authorization", MainApplication.AUTHORIZATION_TOKEN.getToken());
        SimpleHttpResponse response = httpClient.execute(request, null).get();
        String responseBody = response.getBodyText();

        return switch (response.getCode()) {
            case 200 -> {
                List<FileItem> items = gson.fromJson(responseBody, FILE_LIST_TYPE_TOKEN);
                System.out.println(items);
                yield ListFileResponse.okResponse(items);
            }
            case 403 -> {
                showSessionExpired();
                System.exit(0);
                yield null;
            }
            case 404 -> ListFileResponse.fromCode(404);
            default -> ListFileResponse.fromCode(response.getCode());
        };
    }

    public FileUploadResponse uploadFile(String name, boolean isProtected, File file) throws ExecutionException, InterruptedException {

        HttpPut request = new HttpPut(URI.create(BASE_URL + "/file/delete/" + MainApplication.USERNAME));
        request.setHeader("Authorization", MainApplication.AUTHORIZATION_TOKEN.getToken());
        request.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.toString());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.DEFAULT_BINARY, file.getName());

        FileUploadJSON jsonData = new FileUploadJSON(name, isProtected, MainApplication.USERNAME);
        builder.addTextBody("json_data", jsonData.toString());

        HttpEntity entity = builder.build();
        request.setEntity(entity);

        SimpleHttpResponse response = httpClient.execute(SimpleRequestBuilder.copy(request).build(), null).get();

        return switch(response.getCode()) {
            case 400 -> FileUploadResponse.fromCode(400);
            case 404 -> FileUploadResponse.fromCode(404);
            case 403 -> FileUploadResponse.fromCode(403);
            case 500 -> FileUploadResponse.fromCode(500);
            case 200 -> {
                FileItem responseItem = gson.fromJson(response.getBodyText(), FileItem.class);
            }
        }

    }

    public void showSessionExpired() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Session Expired");
        alert.setHeaderText(null);
        alert.setContentText("Session expired. Please close the application and login again.");
        alert.showAndWait();
    }

    private class FileUploadJSON {

        private final String filename, username;
        private final boolean isProtected;

        public FileUploadJSON(String filename, boolean isProtected, String username) {
            this.filename = filename;
            this.isProtected = isProtected;
            this.username = username;
        }

        @Override
        public String toString() {
            return "{" +
                    "filename: " + filename + ","
                    + "protected: " + isProtected + ","
                    + "username: " + username + "}";
        }
    }
}
