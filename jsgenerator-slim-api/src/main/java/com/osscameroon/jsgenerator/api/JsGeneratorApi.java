package com.osscameroon.jsgenerator.api;

import com.osscameroon.jsgenerator.api.rest.ConvertController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;

@RestController
@EnableWebSecurity
@EnableMethodSecurity
@SpringBootApplication
public class JsGeneratorApi {
    public final static String ACTUATOR_ROLE = "ACTUATOR";

    @Bean
    public SecurityFilterChain securityFilterChain(
            @Value("${management.endpoints.web.base-path:/actuator}") final String actuatorBasePath,
            final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .authorizeRequests()
                .antMatchers(actuatorBasePath).permitAll()
                .requestMatchers(toAnyEndpoint().excluding(HealthEndpoint.class).excludingLinks()).hasAnyRole(ACTUATOR_ROLE)
                .antMatchers(ConvertController.MAPPING).permitAll()
                .and().httpBasic()
                .and().build();
    }

    public static void main(String[] args) {
        //noinspection resource
        run(JsGeneratorApi.class, args);
    }
}
