module com.sunkenpotato.homebase {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.feather;
    requires atlantafx.base;
    requires com.google.gson;
    requires org.apache.commons.io;
    requires org.kordamp.ikonli.javafx;
    requires java.net.http;
    requires okhttp3;
    requires annotations;
    requires kotlin.stdlib;
    requires org.apache.logging.log4j.core;

    opens com.sunkenpotato.homebase to javafx.fxml;
    exports com.sunkenpotato.homebase;
    exports com.sunkenpotato.homebase.controller;
    exports com.sunkenpotato.homebase.internal;
    opens com.sunkenpotato.homebase.controller to javafx.fxml;
    exports com.sunkenpotato.homebase.web;
    exports com.sunkenpotato.homebase.i18n;
    exports com.sunkenpotato.homebase.web.response;
    exports com.sunkenpotato.homebase.config;
}