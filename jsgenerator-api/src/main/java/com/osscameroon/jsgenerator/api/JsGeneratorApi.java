package com.osscameroon.jsgenerator.api;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class JsGeneratorApi {
    public static void main(String[] args) {
        //noinspection resource
        run(JsGeneratorApi.class, args);
    }
}
