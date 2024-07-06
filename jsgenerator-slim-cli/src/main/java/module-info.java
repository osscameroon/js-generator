module com.osscameroon.jsgenerator.cli {
    exports com.osscameroon.jsgenerator.cli;
    opens com.osscameroon.jsgenerator.cli;

    requires com.osscameroon.jsgenerator.core;

    requires spring.boot;

    requires spring.boot.autoconfigure;
    requires spring.context;
    requires info.picocli;

    requires spring.beans;
}
