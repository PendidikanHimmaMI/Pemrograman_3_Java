module pendidikan.pemrograman_3_java {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires org.kordamp.ikonli.javafx;

    opens himma.pendidikan to javafx.fxml;
    opens himma.pendidikan.controller;
    opens himma.pendidikan.controller.event;
    exports himma.pendidikan;
    exports himma.pendidikan.controller;
    exports himma.pendidikan.service.impl;
    exports himma.pendidikan.controller.event;
}