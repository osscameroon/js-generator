module com.osscameroon.jsgenerator.test.api {
    requires com.osscameroon.jsgenerator.api;
    requires com.osscameroon.jsgenerator.core;

    requires com.fasterxml.jackson.databind;
    requires lombok;
    requires org.hamcrest;
    requires org.junit.jupiter.params;
    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot.test;
    requires spring.core;
    requires spring.security.test;
    requires spring.test;
    requires spring.web;

    requires org.assertj.core;
    requires org.junit.jupiter.api;

    requires org.apache.tomcat.embed.core;
}