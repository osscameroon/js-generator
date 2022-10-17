package com.osscameroon.jsgenerator.test.api;

import com.osscameroon.jsgenerator.api.JsGeneratorApi;
import com.osscameroon.jsgenerator.api.rest.ConvertController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@SpringBootTest(webEnvironment = MOCK, classes = {
        DispatcherServletAutoConfiguration.class,
        MockMvcAutoConfiguration.class,
        JsGeneratorApi.class,
})
public class JsGeneratorApiTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ConvertController convertController;

    @Test
    public void contextProperlyConfigured() {
        assertThat(mockMvc).isNotNull();
        assertThat(convertController).isNotNull();
    }
}