module com.osscameroon.jsgenerator.desktop {
    exports com.osscameroon.jsgenerator.desktop.autoconfigure;
    exports com.osscameroon.jsgenerator.desktop.controller;

    opens com.osscameroon.jsgenerator.desktop.controller to javafx.fxml, spring.beans;
    opens com.osscameroon.jsgenerator.desktop.autoconfigure to javafx.fxml, spring.beans;

    requires com.osscameroon.jsgenerator.core;

    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.core;
}
