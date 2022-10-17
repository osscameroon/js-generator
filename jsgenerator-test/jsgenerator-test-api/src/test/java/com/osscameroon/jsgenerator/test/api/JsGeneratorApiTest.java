package com.osscameroon.jsgenerator.test.api;

import com.osscameroon.jsgenerator.api.JsGeneratorApi;
import com.osscameroon.jsgenerator.api.rest.ConvertController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = MOCK, classes = {
        DispatcherServletAutoConfiguration.class,
        JsGeneratorApi.class,
})
public class JsGeneratorApiTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private ConvertController convertController;

    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void contextProperlyConfigured() {
        assertThat(mockMvc).isNotNull();
        assertThat(convertController).isNotNull();
    }

    @Test
    public void actuatorPublicEndpoint() throws Exception {
        // GET /actuator            :: public
        mockMvc.perform(get("/actuator"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"));
        // GET /actuator/health     :: public
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"));
        // GET /actuator/metrics    :: secured
        mockMvc.perform(get("/actuator/beans"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/actuator/mappings"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "ACTUATOR")
    public void actuatorSecuredEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/beans"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"));
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"));
        mockMvc.perform(get("/actuator/mappings"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.spring-boot.actuator.v3+json"));
    }
}