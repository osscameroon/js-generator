package com.osscameroon.jsgenerator.app;

import com.osscameroon.jsgenerator.app.internal.ConverterDefault;
import com.osscameroon.jsgenerator.app.internal.TypeBasedNameGenerationStrategy;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

/**
 * ConverterTest
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 23:50:25
 */
@RunWith(MockitoJUnitRunner.class)
public class ConverterTest {
    private Converter converter;
    @Mock
    private Flow flow;

    @Before
    public void before() {
        converter = new ConverterDefault(new TypeBasedNameGenerationStrategy());
        reset(flow);
    }

    @Test
    public void produceValidCodeWhenGivenCDATA() {
        final var token = randomUUID().toString();
        final var converted = convert(format("<![CDATA[ < %s > & ]]>", token));

        assertThat(converted).containsExactly(format("let text_000 = document.createTextNode(` < %s > & `);", token),
            "document.appendChild(text_000);");
    }

    @Test
    public void produceValidCodeWhenGivenPlainText() {
        final var token = randomUUID().toString();
        final var converted = convert(token);

        assertThat(converted).containsExactly(format("let text_000 = document.createTextNode(`%s`);", token),
            "document.appendChild(text_000);");
    }

    @Test
    public void produceValidCodeWhenGivenComment() {
        final var token = randomUUID().toString();
        final var converted = convert(format("<!-- %s -->", token));

        assertThat(converted).containsExactly(
            format("let comment_000 = document.createComment(` %s `);", token),
            "document.appendChild(comment_000);");
    }

    @Test
    public void produceValidCodeWhenGivenScript() {
        final var token = randomUUID().toString();
        final var converted = convert(format("<script>console.log(`%s`)</script>", token));

        assertThat(converted).containsExactly(
            "let script_000 = document.createElement('script');",
            "script_000.type = `text/javascript`;",
            "try {",
            format("let text_000 = document.createTextNode(`console.log(\\`%s\\`)`);", token),
            "script_000.appendChild(text_000);",
            "document.appendChild(script_000);",
            "} catch (_) {",
            format("script_000.text = `console.log(\\`%s\\`)`;", token),
            "document.appendChild(script_000);",
            "}");
    }

    @Test
    public void produceValidCodeWhenGivenTagWithAttributes() {
        assertThat(convert("<div id=\"id-value\"></div>")).containsExactly(
            "let div_000 = document.createElement('div');",
            "div_000.setAttribute(`id`, `id-value`);",
            "document.appendChild(div_000);");

        assertThat(convert("<details open></details>")).containsExactly(
            "let details_000 = document.createElement('details');",
            "details_000.setAttribute(`open`, `true`);",
            "document.appendChild(details_000);");

        assertThat(convert("<p class></p>")).containsExactly(
            "let p_000 = document.createElement('p');",
            "p_000.setAttribute(`class`, ``);",
            "document.appendChild(p_000);");
    }

    @Test
    public void produceValidCodeWhenGivenMultipleNodeAtRoot() {
        final var converted = convert("<!-- Here comes a DIV element... --><div></div>");

        assertThat(converted).containsExactly(
            "let comment_000 = document.createComment(` Here comes a DIV element... `);",
            "document.appendChild(comment_000);",
            "let div_000 = document.createElement('div');",
            "document.appendChild(div_000);");
    }

    @Test
    public void produceValidCodeWithIncrementVariableNameWhenGivenMultipleNodeWithSameTagNames() {
        final var converted = convert("<div></div><div></div>");

        assertThat(converted).containsExactly(
            "let div_000 = document.createElement('div');",
            "document.appendChild(div_000);",
            "let div_001 = document.createElement('div');",
            "document.appendChild(div_001);");
    }

    @Test
    public void produceValidCodeWhenGivenNestedNodes() {
        final var converted = convert("<div>A <strong>...</strong></div><div><p>Well, case!</div>");

        assertThat(converted).containsExactly(
            "let div_000 = document.createElement('div');",
            "let text_000 = document.createTextNode(`A `);",
            "div_000.appendChild(text_000);",
            "let strong_000 = document.createElement('strong');",
            "let text_001 = document.createTextNode(`...`);",
            "strong_000.appendChild(text_001);",
            "div_000.appendChild(strong_000);",
            "document.appendChild(div_000);",
            "let div_001 = document.createElement('div');",
            "let p_000 = document.createElement('p');",
            "let text_002 = document.createTextNode(`Well, case!`);",
            "p_000.appendChild(text_002);",
            "div_001.appendChild(p_000);",
            "document.appendChild(div_001);");
    }

    @Test
    public void produceValidCodeWhenGivenPathToAFile() throws IOException {
        final var outputStream = new ByteArrayOutputStream();
        doReturn(outputStream).when(flow).getOutputStream();
        doReturn(getClass().getClassLoader().getResourceAsStream(
            "htmlFilesInput/sample.html")).when(flow).getInputStream();
        new ConverterDefault(new TypeBasedNameGenerationStrategy()).convert(flow);

        assertThat(outputAsStrippedLines(outputStream)).containsExactly(
            "let html_000 = document.createElement('html');",
            "let text_000 = document.createTextNode(`    `);",
            "html_000.appendChild(text_000);",
            "let head_000 = document.createElement('head');",
            "let text_001 = document.createTextNode(`        `);",
            "head_000.appendChild(text_001);",
            "let meta_000 = document.createElement('meta');",
            "meta_000.setAttribute(`charset`, `utf-8`);",
            "let text_002 = document.createTextNode(`        `);",
            "meta_000.appendChild(text_002);",
            "let title_000 = document.createElement('title');",
            "let text_003 = document.createTextNode(`Sample`);",
            "title_000.appendChild(text_003);",
            "meta_000.appendChild(title_000);",
            "let text_004 = document.createTextNode(`        `);",
            "meta_000.appendChild(text_004);",
            "let link_000 = document.createElement('link');",
            "link_000.setAttribute(`rel`, `stylesheet`);",
            "link_000.setAttribute(`href`, ``);",
            "let text_005 = document.createTextNode(`    `);",
            "link_000.appendChild(text_005);",
            "meta_000.appendChild(link_000);",
            "head_000.appendChild(meta_000);",
            "html_000.appendChild(head_000);",
            "let text_006 = document.createTextNode(`    `);",
            "html_000.appendChild(text_006);",
            "let body_000 = document.createElement('body');",
            "let text_007 = document.createTextNode(`        `);",
            "body_000.appendChild(text_007);",
            "let div_000 = document.createElement('div');",
            "div_000.setAttribute(`id`, `container`);",
            "let text_008 = document.createTextNode(`            `);",
            "div_000.appendChild(text_008);",
            "let div_001 = document.createElement('div');",
            "div_001.setAttribute(`id`, `header`);",
            "let text_009 = document.createTextNode(`                `);",
            "div_001.appendChild(text_009);",
            "let h1_000 = document.createElement('h1');",
            "let text_010 = document.createTextNode(`Sample`);",
            "h1_000.appendChild(text_010);",
            "div_001.appendChild(h1_000);",
            "let text_011 = document.createTextNode(`                `);",
            "div_001.appendChild(text_011);",
            "let img_000 = document.createElement('img');",
            "img_000.setAttribute(`src`, `kanye.jpg`);",
            "img_000.setAttribute(`alt`, `kanye`);",
            "let text_012 = document.createTextNode(`            `);",
            "img_000.appendChild(text_012);",
            "div_001.appendChild(img_000);",
            "div_000.appendChild(div_001);",
            "let text_013 = document.createTextNode(`            `);",
            "div_000.appendChild(text_013);",
            "let div_002 = document.createElement('div');",
            "div_002.setAttribute(`id`, `main`);",
            "let text_014 = document.createTextNode(`                `);",
            "div_002.appendChild(text_014);",
            "let h2_000 = document.createElement('h2');",
            "let text_015 = document.createTextNode(`Main`);",
            "h2_000.appendChild(text_015);",
            "div_002.appendChild(h2_000);",
            "let text_016 = document.createTextNode(`                `);",
            "div_002.appendChild(text_016);",
            "let p_000 = document.createElement('p');",
            "let text_017 = document.createTextNode(`This is the main content.`);",
            "p_000.appendChild(text_017);",
            "div_002.appendChild(p_000);",
            "let text_018 = document.createTextNode(`                `);",
            "div_002.appendChild(text_018);",
            "let img_001 = document.createElement('img');",
            "img_001.setAttribute(`src`, ``);",
            "img_001.setAttribute(`alt`, ``);",
            "let text_019 = document.createTextNode(`            `);",
            "img_001.appendChild(text_019);",
            "div_002.appendChild(img_001);",
            "div_000.appendChild(div_002);",
            "let text_020 = document.createTextNode(`            `);",
            "div_000.appendChild(text_020);",
            "let div_003 = document.createElement('div');",
            "div_003.setAttribute(`id`, `footer`);",
            "let text_021 = document.createTextNode(`                `);",
            "div_003.appendChild(text_021);",
            "let p_001 = document.createElement('p');",
            "let text_022 = document.createTextNode(`Copyright © 2019`);",
            "p_001.appendChild(text_022);",
            "div_003.appendChild(p_001);",
            "let text_023 = document.createTextNode(`            `);",
            "div_003.appendChild(text_023);",
            "div_000.appendChild(div_003);",
            "let text_024 = document.createTextNode(`        `);",
            "div_000.appendChild(text_024);",
            "body_000.appendChild(div_000);",
            "let text_025 = document.createTextNode(`    `);",
            "body_000.appendChild(text_025);",
            "html_000.appendChild(body_000);",
            "document.appendChild(html_000);");
    }

    /**
     * A helper method to work with language-native String and array of data structures.
     *
     * @param input The inout HTML string
     * @return Lines of output JS code
     */
    @NonNull
    private String[] convert(@NonNull String input) {
        final var inputStream = new ByteArrayInputStream(input.getBytes(UTF_8));
        final var outputStream = new ByteArrayOutputStream();

        converter.convert(inputStream, outputStream);

        return outputAsStrippedLines(outputStream);
    }

    private String[] outputAsStrippedLines(OutputStream outputStream) {
        return outputStream
            .toString()
            .lines()
            .map(String::strip)
            .filter(line -> !line.isEmpty())
            .toArray(String[]::new);
    }
}
