package com.osscameroon.jsgenerator.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;

@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication(proxyBeanMethods = false)
public class JsGeneratorApi {
    public final static String ACTUATOR_ROLE = "ACTUATOR";

    @Bean
    public SecurityFilterChain securityFilterChain(
            @Value("${management.endpoints.web.base-path:/actuator}") final String actuatorBasePath,
            final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(spec -> spec
                        .requestMatchers(toAnyEndpoint().excluding(HealthEndpoint.class).excludingLinks()).authenticated()
                        .requestMatchers(HttpMethod.GET, actuatorBasePath).permitAll()
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    public static void main(String[] args) {
        //noinspection resource
        run(JsGeneratorApi.class, args);
    }
}
