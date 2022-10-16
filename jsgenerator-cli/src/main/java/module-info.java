/**
 * com.osscameroon.jsgenerator.cli
 * <p>
 * Author: Salathiel @t salathiel@genese.name
 *
 * @since Sep 04, 2022 @t 20:49:24
 */
module com.osscameroon.jsgenerator.cli {
    exports com.osscameroon.jsgenerator.cli;
    opens com.osscameroon.jsgenerator.cli;

    requires com.osscameroon.jsgenerator.core;

    requires spring.boot;

    requires spring.boot.autoconfigure;
    requires spring.context;
    requires info.picocli;
    requires lombok;

    requires spring.beans;
}
