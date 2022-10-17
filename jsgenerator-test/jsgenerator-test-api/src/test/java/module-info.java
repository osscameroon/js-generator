module com.osscameroon.jsgenerator.test.api {
    requires com.osscameroon.jsgenerator.api;

    requires spring.beans;
    requires spring.boot.autoconfigure;
    requires spring.boot.test;
    requires spring.boot.test.autoconfigure;
    requires spring.security.test;
    requires spring.test;
    requires spring.web;

    requires org.assertj.core;
    requires org.junit.jupiter.api;
}