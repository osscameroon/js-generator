module com.osscameroon.jsgenerator.test.core {
    exports com.osscameroon.jsgenerator.test.core;
    opens com.osscameroon.jsgenerator.test.core;

    requires com.osscameroon.jsgenerator.core;

    requires org.assertj.core;
    requires org.junit.jupiter.api;
    requires org.mockito;
    requires org.mockito.junit.jupiter;
    requires org.junit.jupiter.params;

    requires org.slf4j;
    requires spring.core;

    requires org.junitpioneer;
}
