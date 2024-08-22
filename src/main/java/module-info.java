module com.sunkenpotato.client2p {
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires com.google.gson;
    requires org.apache.commons.io;
    requires org.kordamp.ikonli.javafx;
    requires java.net.http;

    opens com.sunkenpotato.client2p to javafx.fxml;
    exports com.sunkenpotato.client2p;
    exports com.sunkenpotato.client2p.controller;
    exports com.sunkenpotato.client2p.internal;
    opens com.sunkenpotato.client2p.controller to javafx.fxml;
    exports com.sunkenpotato.client2p.web;
    exports com.sunkenpotato.client2p.i18n;
}