package com.osscameroon.jsgenerator.api;

import com.osscameroon.jsgenerator.core.autoconfigure.JsGeneratorCoreAutoconfigure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest.toAnyEndpoint;

/*
* After migrating from Spring Boot 2.7.3 to 3.3.1 and deleting lombok, we got these 2 issues described below:
*
* Issue 1: We added "com.osscameroon.jsgenerator.core" to be scanned
*
***************************
APPLICATION FAILED TO START
***************************
Description:
Parameter 0 of constructor in com.osscameroon.jsgenerator.api.rest.ConvertController required a bean of type 'com.osscameroon.jsgenerator.core.OutputStreamResolver' that could not be found.
Action:
Consider defining a bean of type 'com.osscameroon.jsgenerator.core.OutputStreamResolver' in your configuration.
*
*
* Issue 2: API tests were failing then we add "proxyBeanMethods = false"
*
* org.springframework.beans.factory.BeanDefinitionStoreException: Could not enhance configuration class [com.osscameroon.jsgenerator.api.JsGeneratorApi].
* Consider declaring @Configuration(proxyBeanMethods=false) without inter-bean references between @Bean methods on the configuration class,
* avoiding the need for CGLIB enhancement.
*
* https://github.com/osscameroon/js-generator/actions/runs/10111080853/job/27962309332#step:4:11924
* */
@EnableWebSecurity
@EnableMethodSecurity
@Import(JsGeneratorCoreAutoconfigure.class)
@SpringBootApplication(proxyBeanMethods = false)//, scanBasePackageClasses = {Converter.class, JsGeneratorApi.class})
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
