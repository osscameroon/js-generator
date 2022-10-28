package com.osscameroon.jsgenerator.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.osscameroon.jsgenerator.api.JsGeneratorApi;
import com.osscameroon.jsgenerator.api.rest.ConvertController;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import org.hamcrest.CustomMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.osscameroon.jsgenerator.test.api.helper.MultipartResultMatcher.withMultipart;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(webEnvironment = MOCK)
public class JsGeneratorApiTest {
    private static final Resource SAMPLE_OUTPUT = new ClassPathResource("htmlFilesOutput/sample.js");
    private static final Resource SAMPLE = new ClassPathResource("htmlFilesInput/sample.html");
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        objectMapper = JsonMapper.builder().build();
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
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

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void convertInlineContent(final VariableDeclaration variableDeclaration) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();
        final var content = randomUUID().toString();

        mockMvc.perform(post(ConvertController.MAPPING)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(of(
                                "contents", List.of("<div contenteditable>%s</div>".formatted(content)),
                                "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                "variableDeclaration", variableDeclaration,
                                "extension", ".%s".formatted(extension)
                        ))))
                .andExpectAll(
                        status().isOk(),
                        header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                        jsonPath("$").isArray(),
                        jsonPath("$.length()").value(1),
                        jsonPath("$.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                        jsonPath("$.[0].content").value(new Match(new String[]{
                                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                                "%s div_000 = document.createElement('div');".formatted(keyword),
                                "div_000.setAttribute(`contenteditable`, `true`);",
                                "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, content),
                                "div_000.appendChild(text_000);",
                                "targetElement_000.appendChild(div_000);",
                        })));
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void convertUploadedFilesContent(final VariableDeclaration variableDeclaration) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();

        mockMvc.perform(multipart(ConvertController.MAPPING)
                        .file(new MockMultipartFile(
                                "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                "variableDeclaration", variableDeclaration,
                                "extension", ".%s".formatted(extension)
                        )).getBytes(UTF_8)))
                        .file(new MockMultipartFile(
                                "files", SAMPLE.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE.getInputStream()))
                        .file(new MockMultipartFile(
                                "files", SAMPLE.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE.getInputStream())))
                .andExpectAll(
                        status().isOk(),
                        withMultipart().size(2),
                        withMultipart().nth(0).exists(),
                        withMultipart().nth(1).exists(),
                        withMultipart().nth(0)
                                .map(JsGeneratorApiTest::toArray)
                                .passContent(lines -> assertThat(lines).containsExactly(
                                        toArray(fileContent(SAMPLE_OUTPUT).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                        content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                        header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));
    }

    @Test
    public void convertBadRequests() throws Exception {
        mockMvc.perform(multipart(ConvertController.MAPPING))
                .andExpectAll(status().isBadRequest());
        mockMvc.perform(post(ConvertController.MAPPING)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(of("extension", ".js"))))
                .andExpectAll(status().isBadRequest());
    }

    private static String keyword(final VariableDeclaration variableDeclaration) {
        return variableDeclaration.name().toLowerCase();
    }

    private static String[] toArray(String content) {
        return content
                .lines()
                .map(String::strip)
                .filter(line -> !line.isEmpty())
                .toArray(String[]::new);
    }

    private static String fileContent(final Resource resource) throws IOException {
        return new BufferedReader(new InputStreamReader(resource.getInputStream(), UTF_8))
                .lines().collect(joining("\r\n"));
    }

    public static class Application extends JsGeneratorApi {
    }

    private static final class Match extends CustomMatcher<String> {
        private final String[] lines;

        public Match(final String[] lines) {
            super("jsjenerator-matcher");
            this.lines = lines;
        }

        @Override
        public boolean matches(Object actual) {
            assertThat(toArray((String) actual)).containsExactly(lines);
            return true;
        }
    }
}