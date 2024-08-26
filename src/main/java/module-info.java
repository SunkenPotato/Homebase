module com.sunkenpotato.client2p {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;
    requires com.google.gson;
    requires org.apache.commons.io;
    requires org.kordamp.ikonli.javafx;
    requires java.net.http;
    requires okhttp3;
    requires annotations;
    requires kotlin.stdlib;
    requires org.apache.logging.log4j.core;

    opens com.sunkenpotato.client2p to javafx.fxml;
    exports com.sunkenpotato.client2p;
    exports com.sunkenpotato.client2p.controller;
    exports com.sunkenpotato.client2p.internal;
    opens com.sunkenpotato.client2p.controller to javafx.fxml;
    exports com.sunkenpotato.client2p.web;
    exports com.sunkenpotato.client2p.i18n;
    exports com.sunkenpotato.client2p.web.response;
}