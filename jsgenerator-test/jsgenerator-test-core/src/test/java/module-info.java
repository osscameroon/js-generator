/**
 * com.osscameroon.jsgenerator.test.core
 * <p>
 * Author: Salathiel @t salathiel@genese.name
 *
 * @since Sep 04, 2022 @t 20:18:26
 */
module com.osscameroon.jsgenerator.test.core {
    exports com.osscameroon.jsgenerator.test.core;
    opens com.osscameroon.jsgenerator.test.core;

    requires com.osscameroon.jsgenerator.core;

    requires org.assertj.core;
    requires org.junit.jupiter.api;
    requires lombok;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.junit.jupiter.params;
}
