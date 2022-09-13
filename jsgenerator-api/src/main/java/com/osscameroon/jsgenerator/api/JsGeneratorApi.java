package com.osscameroon;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class JsGeneratorApi {
    public static void main(String[] args) {
        try (final var context = run(JsGeneratorApi.class, args)) {
            System.out.println(123);
        }
    }
}
