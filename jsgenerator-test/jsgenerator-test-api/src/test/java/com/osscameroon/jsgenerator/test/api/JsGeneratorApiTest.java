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
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
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
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream()))
                            .characterEncoding(UTF_8))
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
                                    "files", SAMPLE_INPUT.getFilename(), MULTIPART_FORM_DATA_VALUE, SAMPLE_INPUT.getInputStream())).characterEncoding(UTF_8))
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

    /*
    * We got an issue related to the encoding-decoding process of copyright character ©, the test method named "convertUploadedFilesContentWithComment" was failing.
    * https://github.com/osscameroon/js-generator/issues/238
    * Finally this test helped us to understand that there was no issue when the output is just code but issue occurred when output was a js file.
    * */
    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void convertInlineContentWithNonASCIICharactersWithComment(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws Exception {
        final var keyword = keyword(variableDeclaration);
        final var extension = randomUUID().toString();
        final var prefix = randomUUID().toString();
        final var input = """
<!DOCTYPE html>

<html>
    <head>
        <meta charset="utf-8">
        <title>Sample</title>
        <link rel="stylesheet" href="">
    </head>
    <body>
        <div id="container">
            <div id="header">
                <!-- Sample H1 -->
                <h1>Sample</h1>
                <img src="kanye.jpg" alt="kanye">
            </div>
            <div id="main">
                <h2>Main</h2>
                <p>This is the main content.</p>
                <img src="" alt="">
            </div>
            <div id="footer">
                <!-- Copyright -->
                <p>Ã – string çöntäining nön äsçii çhäräçtérs couldn't Copyright © 2019</p>
            </div>
        </div>
    </body>
</html>                         
""";

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
                                        "%s html_000 = document.createElement('html');".formatted(keyword),
                                        "%s text_000 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_000);",
                                        "%s head_000 = document.createElement('head');".formatted(keyword),
                                        "%s text_001 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_001);",
                                        "%s meta_000 = document.createElement('meta');".formatted(keyword),
                                        "meta_000.setAttribute(`charset`, `utf-8`);",
                                        "head_000.appendChild(meta_000);",
                                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_002);",
                                        "%s title_000 = document.createElement('title');".formatted(keyword),
                                        "%s text_003 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "title_000.appendChild(text_003);",
                                        "head_000.appendChild(title_000);",
                                        "%s text_004 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_004);",
                                        "%s link_000 = document.createElement('link');".formatted(keyword),
                                        "link_000.setAttribute(`rel`, `stylesheet`);",
                                        "link_000.setAttribute(`href`, ``);",
                                        "head_000.appendChild(link_000);",
                                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                                        "head_000.appendChild(text_005);",
                                        "html_000.appendChild(head_000);",
                                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_006);",
                                        "%s body_000 = document.createElement('body');".formatted(keyword),
                                        "%s text_007 = document.createTextNode(`        `);".formatted(keyword),
                                        "body_000.appendChild(text_007);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`id`, `container`);",
                                        "%s text_008 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_008);",
                                        "%s div_001 = document.createElement('div');".formatted(keyword),
                                        "div_001.setAttribute(`id`, `header`);",
                                        "%s text_009 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_009);",
                                        "%s comment_000 = document.createComment(` Sample H1 `);".formatted(keyword),
                                        "div_001.appendChild(comment_000);",
                                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_010);",
                                        "%s h1_000 = document.createElement('h1');".formatted(keyword),
                                        "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "h1_000.appendChild(text_011);",
                                        "div_001.appendChild(h1_000);",
                                        "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_012);",
                                        "%s img_000 = document.createElement('img');".formatted(keyword),
                                        "img_000.setAttribute(`src`, `kanye.jpg`);",
                                        "img_000.setAttribute(`alt`, `kanye`);",
                                        "div_001.appendChild(img_000);",
                                        "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_001.appendChild(text_013);",
                                        "div_000.appendChild(div_001);",
                                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_014);",
                                        "%s div_002 = document.createElement('div');".formatted(keyword),
                                        "div_002.setAttribute(`id`, `main`);",
                                        "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_015);",
                                        "%s h2_000 = document.createElement('h2');".formatted(keyword),
                                        "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                                        "h2_000.appendChild(text_016);",
                                        "div_002.appendChild(h2_000);",
                                        "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_017);",
                                        "%s p_000 = document.createElement('p');".formatted(keyword),
                                        "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                                        "p_000.appendChild(text_018);",
                                        "div_002.appendChild(p_000);",
                                        "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_019);",
                                        "%s img_001 = document.createElement('img');".formatted(keyword),
                                        "img_001.setAttribute(`src`, ``);",
                                        "img_001.setAttribute(`alt`, ``);",
                                        "div_002.appendChild(img_001);",
                                        "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_002.appendChild(text_020);",
                                        "div_000.appendChild(div_002);",
                                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_021);",
                                        "%s div_003 = document.createElement('div');".formatted(keyword),
                                        "div_003.setAttribute(`id`, `footer`);",
                                        "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_022);",
                                        "%s comment_001 = document.createComment(` Copyright `);".formatted(keyword),
                                        "div_003.appendChild(comment_001);",
                                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_023);",
                                        "%s p_001 = document.createElement('p');".formatted(keyword),
                                        "%s text_024 = document.createTextNode(`Ã – string çöntäining nön äsçii çhäräçtérs couldn't Copyright © 2019`);".formatted(keyword),
                                        "p_001.appendChild(text_024);",
                                        "div_003.appendChild(p_001);",
                                        "%s text_025 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_003.appendChild(text_025);",
                                        "div_000.appendChild(div_003);",
                                        "%s text_026 = document.createTextNode(`        `);".formatted(keyword),
                                        "div_000.appendChild(text_026);",
                                        "body_000.appendChild(div_000);",
                                        "%s text_027 = document.createTextNode(`    `);".formatted(keyword),
                                        "body_000.appendChild(text_027);",
                                        "html_000.appendChild(body_000);",
                                        "targetElement_000.appendChild(html_000);"
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
                                        "%s html_000 = document.createElement('html');".formatted(keyword),
                                        "%s text_000 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_000);",
                                        "%s head_000 = document.createElement('head');".formatted(keyword),
                                        "%s text_001 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_001);",
                                        "%s meta_000 = document.createElement('meta');".formatted(keyword),
                                        "meta_000.setAttribute(`charset`, `utf-8`);",
                                        "head_000.appendChild(meta_000);",
                                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_002);",
                                        "%s title_000 = document.createElement('title');".formatted(keyword),
                                        "%s text_003 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "title_000.appendChild(text_003);",
                                        "head_000.appendChild(title_000);",
                                        "%s text_004 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_004);",
                                        "%s link_000 = document.createElement('link');".formatted(keyword),
                                        "link_000.setAttribute(`rel`, `stylesheet`);",
                                        "link_000.setAttribute(`href`, ``);",
                                        "head_000.appendChild(link_000);",
                                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                                        "head_000.appendChild(text_005);",
                                        "html_000.appendChild(head_000);",
                                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_006);",
                                        "%s body_000 = document.createElement('body');".formatted(keyword),
                                        "%s text_007 = document.createTextNode(`        `);".formatted(keyword),
                                        "body_000.appendChild(text_007);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`id`, `container`);",
                                        "%s text_008 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_008);",
                                        "%s div_001 = document.createElement('div');".formatted(keyword),
                                        "div_001.setAttribute(`id`, `header`);",
                                        "%s text_009 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_009);",
                                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_010);",
                                        "%s h1_000 = document.createElement('h1');".formatted(keyword),
                                        "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "h1_000.appendChild(text_011);",
                                        "div_001.appendChild(h1_000);",
                                        "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_012);",
                                        "%s img_000 = document.createElement('img');".formatted(keyword),
                                        "img_000.setAttribute(`src`, `kanye.jpg`);",
                                        "img_000.setAttribute(`alt`, `kanye`);",
                                        "div_001.appendChild(img_000);",
                                        "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_001.appendChild(text_013);",
                                        "div_000.appendChild(div_001);",
                                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_014);",
                                        "%s div_002 = document.createElement('div');".formatted(keyword),
                                        "div_002.setAttribute(`id`, `main`);",
                                        "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_015);",
                                        "%s h2_000 = document.createElement('h2');".formatted(keyword),
                                        "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                                        "h2_000.appendChild(text_016);",
                                        "div_002.appendChild(h2_000);",
                                        "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_017);",
                                        "%s p_000 = document.createElement('p');".formatted(keyword),
                                        "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                                        "p_000.appendChild(text_018);",
                                        "div_002.appendChild(p_000);",
                                        "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_019);",
                                        "%s img_001 = document.createElement('img');".formatted(keyword),
                                        "img_001.setAttribute(`src`, ``);",
                                        "img_001.setAttribute(`alt`, ``);",
                                        "div_002.appendChild(img_001);",
                                        "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_002.appendChild(text_020);",
                                        "div_000.appendChild(div_002);",
                                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_021);",
                                        "%s div_003 = document.createElement('div');".formatted(keyword),
                                        "div_003.setAttribute(`id`, `footer`);",
                                        "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_022);",
                                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_023);",
                                        "%s p_001 = document.createElement('p');".formatted(keyword),
                                        "%s text_024 = document.createTextNode(`Ã – string çöntäining nön äsçii çhäräçtérs couldn't Copyright © 2019`);".formatted(keyword),
                                        "p_001.appendChild(text_024);",
                                        "div_003.appendChild(p_001);",
                                        "%s text_025 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_003.appendChild(text_025);",
                                        "div_000.appendChild(div_003);",
                                        "%s text_026 = document.createTextNode(`        `);".formatted(keyword),
                                        "div_000.appendChild(text_026);",
                                        "body_000.appendChild(div_000);",
                                        "%s text_027 = document.createTextNode(`    `);".formatted(keyword),
                                        "body_000.appendChild(text_027);",
                                        "html_000.appendChild(body_000);",
                                        "targetElement_000.appendChild(html_000);"
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
                                        "%s html_000 = document.createElement('html');".formatted(keyword),
                                        "%s text_000 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_000);",
                                        "%s head_000 = document.createElement('head');".formatted(keyword),
                                        "%s text_001 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_001);",
                                        "%s meta_000 = document.createElement('meta');".formatted(keyword),
                                        "meta_000.setAttribute(`charset`, `utf-8`);",
                                        "head_000.appendChild(meta_000);",
                                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_002);",
                                        "%s title_000 = document.createElement('title');".formatted(keyword),
                                        "%s text_003 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "title_000.appendChild(text_003);",
                                        "head_000.appendChild(title_000);",
                                        "%s text_004 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_004);",
                                        "%s link_000 = document.createElement('link');".formatted(keyword),
                                        "link_000.setAttribute(`rel`, `stylesheet`);",
                                        "link_000.setAttribute(`href`, ``);",
                                        "head_000.appendChild(link_000);",
                                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                                        "head_000.appendChild(text_005);",
                                        "html_000.appendChild(head_000);",
                                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_006);",
                                        "%s body_000 = document.createElement('body');".formatted(keyword),
                                        "%s text_007 = document.createTextNode(`        `);".formatted(keyword),
                                        "body_000.appendChild(text_007);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`id`, `container`);",
                                        "%s text_008 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_008);",
                                        "%s div_001 = document.createElement('div');".formatted(keyword),
                                        "div_001.setAttribute(`id`, `header`);",
                                        "%s text_009 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_009);",
                                        "%s comment_000 = document.createComment(` Sample H1 `);".formatted(keyword),
                                        "div_001.appendChild(comment_000);",
                                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_010);",
                                        "%s h1_000 = document.createElement('h1');".formatted(keyword),
                                        "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "h1_000.appendChild(text_011);",
                                        "div_001.appendChild(h1_000);",
                                        "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_012);",
                                        "%s img_000 = document.createElement('img');".formatted(keyword),
                                        "img_000.setAttribute(`src`, `kanye.jpg`);",
                                        "img_000.setAttribute(`alt`, `kanye`);",
                                        "div_001.appendChild(img_000);",
                                        "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_001.appendChild(text_013);",
                                        "div_000.appendChild(div_001);",
                                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_014);",
                                        "%s div_002 = document.createElement('div');".formatted(keyword),
                                        "div_002.setAttribute(`id`, `main`);",
                                        "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_015);",
                                        "%s h2_000 = document.createElement('h2');".formatted(keyword),
                                        "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                                        "h2_000.appendChild(text_016);",
                                        "div_002.appendChild(h2_000);",
                                        "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_017);",
                                        "%s p_000 = document.createElement('p');".formatted(keyword),
                                        "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                                        "p_000.appendChild(text_018);",
                                        "div_002.appendChild(p_000);",
                                        "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_019);",
                                        "%s img_001 = document.createElement('img');".formatted(keyword),
                                        "img_001.setAttribute(`src`, ``);",
                                        "img_001.setAttribute(`alt`, ``);",
                                        "div_002.appendChild(img_001);",
                                        "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_002.appendChild(text_020);",
                                        "div_000.appendChild(div_002);",
                                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_021);",
                                        "%s div_003 = document.createElement('div');".formatted(keyword),
                                        "div_003.setAttribute(`id`, `footer`);",
                                        "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_022);",
                                        "%s comment_001 = document.createComment(` Copyright `);".formatted(keyword),
                                        "div_003.appendChild(comment_001);",
                                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_023);",
                                        "%s p_001 = document.createElement('p');".formatted(keyword),
                                        "%s text_024 = document.createTextNode(`Ã – string çöntäining nön äsçii çhäräçtérs couldn't Copyright © 2019`);".formatted(keyword),
                                        "p_001.appendChild(text_024);",
                                        "div_003.appendChild(p_001);",
                                        "%s text_025 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_003.appendChild(text_025);",
                                        "div_000.appendChild(div_003);",
                                        "%s text_026 = document.createTextNode(`        `);".formatted(keyword),
                                        "div_000.appendChild(text_026);",
                                        "body_000.appendChild(div_000);",
                                        "%s text_027 = document.createTextNode(`    `);".formatted(keyword),
                                        "body_000.appendChild(text_027);",
                                        "html_000.appendChild(body_000);"
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
                                        "%s html_000 = document.createElement('html');".formatted(keyword),
                                        "%s text_000 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_000);",
                                        "%s head_000 = document.createElement('head');".formatted(keyword),
                                        "%s text_001 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_001);",
                                        "%s meta_000 = document.createElement('meta');".formatted(keyword),
                                        "meta_000.setAttribute(`charset`, `utf-8`);",
                                        "head_000.appendChild(meta_000);",
                                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_002);",
                                        "%s title_000 = document.createElement('title');".formatted(keyword),
                                        "%s text_003 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "title_000.appendChild(text_003);",
                                        "head_000.appendChild(title_000);",
                                        "%s text_004 = document.createTextNode(`        `);".formatted(keyword),
                                        "head_000.appendChild(text_004);",
                                        "%s link_000 = document.createElement('link');".formatted(keyword),
                                        "link_000.setAttribute(`rel`, `stylesheet`);",
                                        "link_000.setAttribute(`href`, ``);",
                                        "head_000.appendChild(link_000);",
                                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                                        "head_000.appendChild(text_005);",
                                        "html_000.appendChild(head_000);",
                                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                                        "html_000.appendChild(text_006);",
                                        "%s body_000 = document.createElement('body');".formatted(keyword),
                                        "%s text_007 = document.createTextNode(`        `);".formatted(keyword),
                                        "body_000.appendChild(text_007);",
                                        "%s div_000 = document.createElement('div');".formatted(keyword),
                                        "div_000.setAttribute(`id`, `container`);",
                                        "%s text_008 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_008);",
                                        "%s div_001 = document.createElement('div');".formatted(keyword),
                                        "div_001.setAttribute(`id`, `header`);",
                                        "%s text_009 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_009);",
                                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_010);",
                                        "%s h1_000 = document.createElement('h1');".formatted(keyword),
                                        "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                                        "h1_000.appendChild(text_011);",
                                        "div_001.appendChild(h1_000);",
                                        "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_001.appendChild(text_012);",
                                        "%s img_000 = document.createElement('img');".formatted(keyword),
                                        "img_000.setAttribute(`src`, `kanye.jpg`);",
                                        "img_000.setAttribute(`alt`, `kanye`);",
                                        "div_001.appendChild(img_000);",
                                        "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_001.appendChild(text_013);",
                                        "div_000.appendChild(div_001);",
                                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_014);",
                                        "%s div_002 = document.createElement('div');".formatted(keyword),
                                        "div_002.setAttribute(`id`, `main`);",
                                        "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_015);",
                                        "%s h2_000 = document.createElement('h2');".formatted(keyword),
                                        "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                                        "h2_000.appendChild(text_016);",
                                        "div_002.appendChild(h2_000);",
                                        "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_017);",
                                        "%s p_000 = document.createElement('p');".formatted(keyword),
                                        "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                                        "p_000.appendChild(text_018);",
                                        "div_002.appendChild(p_000);",
                                        "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_002.appendChild(text_019);",
                                        "%s img_001 = document.createElement('img');".formatted(keyword),
                                        "img_001.setAttribute(`src`, ``);",
                                        "img_001.setAttribute(`alt`, ``);",
                                        "div_002.appendChild(img_001);",
                                        "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_002.appendChild(text_020);",
                                        "div_000.appendChild(div_002);",
                                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_000.appendChild(text_021);",
                                        "%s div_003 = document.createElement('div');".formatted(keyword),
                                        "div_003.setAttribute(`id`, `footer`);",
                                        "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_022);",
                                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                                        "div_003.appendChild(text_023);",
                                        "%s p_001 = document.createElement('p');".formatted(keyword),
                                        "%s text_024 = document.createTextNode(`Ã – string çöntäining nön äsçii çhäräçtérs couldn't Copyright © 2019`);".formatted(keyword),
                                        "p_001.appendChild(text_024);",
                                        "div_003.appendChild(p_001);",
                                        "%s text_025 = document.createTextNode(`            `);".formatted(keyword),
                                        "div_003.appendChild(text_025);",
                                        "div_000.appendChild(div_003);",
                                        "%s text_026 = document.createTextNode(`        `);".formatted(keyword),
                                        "div_000.appendChild(text_026);",
                                        "body_000.appendChild(div_000);",
                                        "%s text_027 = document.createTextNode(`    `);".formatted(keyword),
                                        "body_000.appendChild(text_027);",
                                        "html_000.appendChild(body_100);"
                                })));

            }
        }
    }

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
                                )).getBytes()))
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
                                )).getBytes()))
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