module com.osscameroon.jsgenerator.desktop {

    requires com.osscameroon.jsgenerator.core;
    requires lombok;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;

    opens com.osscameroon.jsgenerator.desktop to javafx.fxml;
    exports com.osscameroon.jsgenerator.desktop;
}