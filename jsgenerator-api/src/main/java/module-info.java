module com.osscameroon.jsgenerator.api {
    exports com.osscameroon.jsgenerator.api.rest;
    exports com.osscameroon.jsgenerator.api;

    opens com.osscameroon.jsgenerator.api to spring.beans;
    opens com.osscameroon.jsgenerator.api.domain;
    opens com.osscameroon.jsgenerator.api.rest to spring.beans;

    requires com.osscameroon.jsgenerator.core;

    requires spring.beans;
    requires spring.boot.actuator;
    requires spring.boot;
    requires spring.boot.actuator.autoconfigure;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.security.config;
    requires spring.security.web;
    requires spring.web;

    requires lombok;
    requires org.slf4j;
    requires java.validation;
}