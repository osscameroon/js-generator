package com.osscameroon.jsgenerator.test.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.osscameroon.jsgenerator.api.JsGeneratorApi;
import com.osscameroon.jsgenerator.api.rest.ConvertController;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import org.hamcrest.CustomMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
import java.util.stream.Stream;

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
class JsGeneratorApiTest {
    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED = new ClassPathResource("jsFilesOutput/querySelectorAdded/sample.js");
    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED = new ClassPathResource("jsFilesOutput/querySelectorNotAdded/sample.js");

    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED_AND_COMMENT_CONVERSION_MODE_ACTIVATED = new ClassPathResource("jsFilesOutput/querySelectorAdded/commentConversionModeActivated/sample.js");

    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED_AND_COMMENT_CONVERSION_MODE_NOT_ACTIVATED = new ClassPathResource("jsFilesOutput/querySelectorAdded/commentConversionModeNotActivated/sample.js");

    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED_AND_COMMENT_CONVERSION_MODE_ACTIVATED = new ClassPathResource("jsFilesOutput/querySelectorNotAdded/commentConversionModeActivated/sample.js");

    private static final Resource SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED_AND_COMMENT_CONVERSION_MODE_NOT_ACTIVATED = new ClassPathResource("jsFilesOutput/querySelectorNotAdded/commentConversionModeNotActivated/sample.js");
    private static final Resource SAMPLE_INPUT = new ClassPathResource("htmlFilesInput/sample.html");
    private static final Resource SAMPLE_INPUT_WITH_COMMENT = new ClassPathResource("htmlFilesInput/sampleWithComment.html");
    @Autowired
    private WebApplicationContext webApplicationContext;
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

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

    private static Stream<Arguments> provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated() {
        return Stream.of(
                Arguments.of(VariableDeclaration.LET, true, true),
                Arguments.of(VariableDeclaration.LET, false, true),
                Arguments.of(VariableDeclaration.VAR, true, true),
                Arguments.of(VariableDeclaration.VAR, false, true),
                Arguments.of(VariableDeclaration.CONST, true, true),
                Arguments.of(VariableDeclaration.CONST, false, true),
                Arguments.of(VariableDeclaration.LET, true, false),
                Arguments.of(VariableDeclaration.LET, false, false),
                Arguments.of(VariableDeclaration.VAR, true, false),
                Arguments.of(VariableDeclaration.VAR, false, false),
                Arguments.of(VariableDeclaration.CONST, true, false),
                Arguments.of(VariableDeclaration.CONST, false, false)
        );
    }

    private static Stream<Arguments> provideVariableDeclarationsAndQuerySelectorAdded() {
        return Stream.of(
                Arguments.of(VariableDeclaration.LET, true),
                Arguments.of(VariableDeclaration.LET, false),
                Arguments.of(VariableDeclaration.VAR, true),
                Arguments.of(VariableDeclaration.VAR, false),
                Arguments.of(VariableDeclaration.CONST, true),
                Arguments.of(VariableDeclaration.CONST, false)
        );
    }


    @BeforeEach
    public void beforeEach() {
        objectMapper = JsonMapper.builder().build();
        mockMvc = webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void actuatorPublicEndpoint() throws Exception {
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
    void actuatorSecuredEndpoint() throws Exception {
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
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void convertInlineContent(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();
        final var content = randomUUID().toString();

        if (querySelectorAdded) {

            mockMvc.perform(post(ConvertController.MAPPING)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(of(
                                    "contents", List.of("<div contenteditable>%s</div>".formatted(content)),
                                    "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                    "variableDeclaration", variableDeclaration,
                                    "extension", ".%s".formatted(extension),
                                    "querySelectorAdded", true,
                                    "commentConversionModeActivated", true
                            ))))
                    .andExpectAll(
                            status().isOk(),
                            header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                            jsonPath("$.status").value("SUCCESS"),
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content.length()").value(1),
                            jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                            jsonPath("$.content.[0].content").value(new Match(new String[]{
                                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                                    "%s div_000 = document.createElement('div');".formatted(keyword),
                                    "div_000.setAttribute(`contenteditable`, `true`);",
                                    "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, content),
                                    "div_000.appendChild(text_000);",
                                    "targetElement_000.appendChild(div_000);"
                            })));


        } else {

            mockMvc.perform(post(ConvertController.MAPPING)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(of(
                                    "contents", List.of("<div contenteditable>%s</div>".formatted(content)),
                                    "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                    "variableDeclaration", variableDeclaration,
                                    "extension", ".%s".formatted(extension),
                                    "querySelectorAdded", false,
                                    "commentConversionModeActivated", true
                            ))))
                    .andExpectAll(
                            status().isOk(),
                            header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                            jsonPath("$.status").value("SUCCESS"),
                            jsonPath("$.content").isArray(),
                            jsonPath("$.content.length()").value(1),
                            jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                            jsonPath("$.content.[0].content").value(new Match(new String[]{

                                    "%s div_000 = document.createElement('div');".formatted(keyword),
                                    "div_000.setAttribute(`contenteditable`, `true`);",
                                    "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, content),
                                    "div_000.appendChild(text_000);"

                            })));


        }

    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void convertInlineContentWithComment(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded,final boolean commentConversionModeActivated) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();
        final var content = randomUUID().toString();
        final var input = "<!-- ContentEditable --> <div contenteditable>%s</div>".formatted(content);

        if (querySelectorAdded) {

            if(commentConversionModeActivated){

                mockMvc.perform(post(ConvertController.MAPPING)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(of(
                                        "contents", List.of(input),
                                        "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", true,
                                        "commentConversionModeActivated", true
                                ))))
                        .andExpectAll(
                                status().isOk(),
                                header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                                jsonPath("$.status").value("SUCCESS"),
                                jsonPath("$.content").isArray(),
                                jsonPath("$.content.length()").value(1),
                                jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                                jsonPath("$.content.[0].content").value(new Match(new String[]{
                                        "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                                        "%s comment_000 = document.createComment(` ContentEditable `);".formatted(keyword),
                                        "targetElement_000.appendChild(comment_000);",
                                        "%s text_000 = document.createTextNode(` `);".formatted(keyword),
                                        "targetElement_000.appendChild(text_000);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`contenteditable`, `true`);",
                                        "%s text_001 = document.createTextNode(`%s`);".formatted(keyword, content),
                                        "div_000.appendChild(text_001);",
                                        "targetElement_000.appendChild(div_000);"
                                })));



            }else{

                mockMvc.perform(post(ConvertController.MAPPING)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(of(
                                        "contents", List.of(input),
                                        "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", true,
                                        "commentConversionModeActivated", false
                                ))))
                        .andExpectAll(
                                status().isOk(),
                                header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                                jsonPath("$.status").value("SUCCESS"),
                                jsonPath("$.content").isArray(),
                                jsonPath("$.content.length()").value(1),
                                jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                                jsonPath("$.content.[0].content").value(new Match(new String[]{
                                        "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                                        "%s text_000 = document.createTextNode(` `);".formatted(keyword),
                                        "targetElement_000.appendChild(text_000);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`contenteditable`, `true`);",
                                        "%s text_001 = document.createTextNode(`%s`);".formatted(keyword, content),
                                        "div_000.appendChild(text_001);",
                                        "targetElement_000.appendChild(div_000);"
                                })));

            }


        } else {

            if(commentConversionModeActivated){
                mockMvc.perform(post(ConvertController.MAPPING)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(of(
                                        "contents", List.of(input),
                                        "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", false,
                                        "commentConversionModeActivated", true
                                ))))
                        .andExpectAll(
                                status().isOk(),
                                header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                                jsonPath("$.status").value("SUCCESS"),
                                jsonPath("$.content").isArray(),
                                jsonPath("$.content.length()").value(1),
                                jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                                jsonPath("$.content.[0].content").value(new Match(new String[]{
                                        "%s comment_000 = document.createComment(` ContentEditable `);".formatted(keyword),
                                        "%s text_000 = document.createTextNode(` `);".formatted(keyword),
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`contenteditable`, `true`);",
                                        "%s text_001 = document.createTextNode(`%s`);".formatted(keyword, content),
                                        "div_000.appendChild(text_001);"
                                })));


            }else{

                mockMvc.perform(post(ConvertController.MAPPING)
                                .header(CONTENT_TYPE, APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(of(
                                        "contents", List.of(input),
                                        "pattern", "%s.{{ index }}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", false,
                                        "commentConversionModeActivated", false
                                ))))
                        .andExpectAll(
                                status().isOk(),
                                header().string(CONTENT_TYPE, APPLICATION_JSON_VALUE),
                                jsonPath("$.status").value("SUCCESS"),
                                jsonPath("$.content").isArray(),
                                jsonPath("$.content.length()").value(1),
                                jsonPath("$.content.[0].filename").value("%s.0.%s".formatted(prefix, extension)),
                                jsonPath("$.content.[0].content").value(new Match(new String[]{
                                        "%s text_000 = document.createTextNode(` `);".formatted(keyword),
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`contenteditable`, `true`);",
                                        "%s text_001 = document.createTextNode(`%s`);".formatted(keyword, content),
                                        "div_000.appendChild(text_001);"
                                })));

            }

        }
    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void convertUploadedFilesContent(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();

        if (querySelectorAdded) {

            mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                            .file(new MockMultipartFile(
                                    "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                    "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                    "variableDeclaration", variableDeclaration,
                                    "extension", ".%s".formatted(extension),
                                    "querySelectorAdded", true,
                                    "commentConversionModeActivated", true
                            )).getBytes(UTF_8)))
                            .file(new MockMultipartFile(
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream()))
                            .file(new MockMultipartFile(
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream())))
                    .andExpectAll(
                            status().isOk(),
                            withMultipart().size(2),
                            withMultipart().nth(0).exists(),
                            withMultipart().nth(1).exists(),
                            withMultipart().nth(0)
                                    .map(JsGeneratorApiTest::toArray)
                                    .passContent(lines -> assertThat(lines).containsExactly(
                                            toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                            content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                            header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


        } else {

            mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                            .file(new MockMultipartFile(
                                    "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                    "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                    "variableDeclaration", variableDeclaration,
                                    "extension", ".%s".formatted(extension),
                                    "querySelectorAdded", false,
                                    "commentConversionModeActivated", true
                            )).getBytes(UTF_8)))
                            .file(new MockMultipartFile(
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream()))
                            .file(new MockMultipartFile(
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream())))
                    .andExpectAll(
                            status().isOk(),
                            withMultipart().size(2),
                            withMultipart().nth(0).exists(),
                            withMultipart().nth(1).exists(),
                            withMultipart().nth(0)
                                    .map(JsGeneratorApiTest::toArray)
                                    .passContent(lines -> assertThat(lines).containsExactly(
                                            toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                            content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                            header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


        }

    }

    @Disabled("Encoding Issue here with '©', we will solve that")
    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void convertUploadedFilesContentWithComment(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();

        if (querySelectorAdded) {

            if(commentConversionModeActivated){

                mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                                .file(new MockMultipartFile(
                                        "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                        "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", true,
                                        "commentConversionModeActivated", true
                                )).getBytes(UTF_8)))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream()))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream())))
                        .andExpectAll(
                                status().isOk(),
                                withMultipart().size(2),
                                withMultipart().nth(0).exists(),
                                withMultipart().nth(1).exists(),
                                withMultipart().nth(0)
                                        .map(JsGeneratorApiTest::toArray)
                                        .passContent(lines -> assertThat(lines).containsExactly(
                                                toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED_AND_COMMENT_CONVERSION_MODE_ACTIVATED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                                content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                                header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


            }else{

                mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                                .file(new MockMultipartFile(
                                        "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                        "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", true,
                                        "commentConversionModeActivated", false
                                )).getBytes(UTF_8)))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream()))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream())))
                        .andExpectAll(
                                status().isOk(),
                                withMultipart().size(2),
                                withMultipart().nth(0).exists(),
                                withMultipart().nth(1).exists(),
                                withMultipart().nth(0)
                                        .map(JsGeneratorApiTest::toArray)
                                        .passContent(lines -> assertThat(lines).containsExactly(
                                                toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_ADDED_AND_COMMENT_CONVERSION_MODE_NOT_ACTIVATED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                                content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                                header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


            }

        } else {

            if(commentConversionModeActivated){

                mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                                .file(new MockMultipartFile(
                                        "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                        "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", false,
                                        "commentConversionModeActivated", true
                                )).getBytes(UTF_8)))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream()))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream())))
                        .andExpectAll(
                                status().isOk(),
                                withMultipart().size(2),
                                withMultipart().nth(0).exists(),
                                withMultipart().nth(1).exists(),
                                withMultipart().nth(0)
                                        .map(JsGeneratorApiTest::toArray)
                                        .passContent(lines -> assertThat(lines).containsExactly(
                                                toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED_AND_COMMENT_CONVERSION_MODE_ACTIVATED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                                content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                                header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


            }else{

                mockMvc.perform(multipart(ConvertController.MAPPING + "/files")
                                .file(new MockMultipartFile(
                                        "options", "config.json", APPLICATION_JSON_VALUE, objectMapper.writeValueAsString(of(
                                        "pattern", "%s.{{ index }}{{}}{{ extension }}".formatted(prefix),
                                        "variableDeclaration", variableDeclaration,
                                        "extension", ".%s".formatted(extension),
                                        "querySelectorAdded", false,
                                        "commentConversionModeActivated", false
                                )).getBytes(UTF_8)))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream()))
                                .file(new MockMultipartFile(
                                        "files", SAMPLE_INPUT_WITH_COMMENT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT_WITH_COMMENT.getInputStream())))
                        .andExpectAll(
                                status().isOk(),
                                withMultipart().size(2),
                                withMultipart().nth(0).exists(),
                                withMultipart().nth(1).exists(),
                                withMultipart().nth(0)
                                        .map(JsGeneratorApiTest::toArray)
                                        .passContent(lines -> assertThat(lines).containsExactly(
                                                toArray(fileContent(SAMPLE_OUTPUT_QUERY_SELECTOR_NOT_ADDED_AND_COMMENT_CONVERSION_MODE_NOT_ACTIVATED).replaceAll("\\{\\{\s*keyword\s*}}", keyword)))),
                                content().contentTypeCompatibleWith(MULTIPART_FORM_DATA),
                                header().string(CONTENT_TYPE, matchesPattern("^%s;boundary=.*$".formatted(MULTIPART_FORM_DATA_VALUE))));


            }
        }
    }


    @Test
    void convertBadRequests() throws Exception {
        mockMvc.perform(multipart(ConvertController.MAPPING + "/files"))
                .andExpectAll(status().isBadRequest());
        mockMvc.perform(post(ConvertController.MAPPING)
                        .header(CONTENT_TYPE, APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(of("extension", ".js"))))
                .andExpectAll(status().isBadRequest());
    }

    @SpringBootApplication(exclude = JsGeneratorApi.class)
    public static class Application extends JsGeneratorApi {
    }

    private static final class Match extends CustomMatcher<String> {
        private final String[] lines;

        public Match(final String[] lines) {
            super("jsgenerator-matcher");
            this.lines = lines;
        }

        @Override
        public boolean matches(Object actual) {
            assertThat(toArray((String) actual)).containsExactly(lines);
            return true;
        }
    }
}