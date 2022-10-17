package com.osscameroon.jsgenerator.test.core;

import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ConverterTest {
    private Converter converter;

    @BeforeEach
    public void before() {
        converter = Converter.of();
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenCDATA(VariableDeclaration variableDeclaration) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert("<![CDATA[ < %s > & ]]>".formatted(token), configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s text_000 = document.createTextNode(` < %s > & `);".formatted(keyword, token),
                "targetElement_000.appendChild(text_000);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenPlainText(VariableDeclaration variableDeclaration) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert(token, configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, token),
                "targetElement_000.appendChild(text_000);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenComment(VariableDeclaration variableDeclaration) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert("<!-- %s -->".formatted(token), configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token),
                "targetElement_000.appendChild(comment_000);");
    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenScript(VariableDeclaration variableDeclaration) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert("<script>console.log(`%s`)</script>".formatted(token), configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s script_000 = document.createElement('script');".formatted(keyword),
                "script_000.type = `text/javascript`;",
                "try {",
                "%s text_000 = document.createTextNode(`console.log(\\`%s\\`)`);".formatted(keyword, token),
                "script_000.appendChild(text_000);",
                "targetElement_000.appendChild(script_000);",
                "} catch (_) {",
                "script_000.text = `console.log(\\`%s\\`)`;".formatted(token),
                "targetElement_000.appendChild(script_000);",
                "}");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenTagWithAttributes(VariableDeclaration variableDeclaration) throws IOException {
        final var keyword = keyword(variableDeclaration);

        assertThat(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration))).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s div_000 = document.createElement('div');".formatted(keyword),
                "div_000.setAttribute(`id`, `id-value`);",
                "targetElement_000.appendChild(div_000);");
        assertThat(convert("<details open></details>", new Configuration(variableDeclaration))).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s details_000 = document.createElement('details');".formatted(keyword),
                "details_000.setAttribute(`open`, `true`);",
                "targetElement_000.appendChild(details_000);");
        assertThat(convert("<p class></p>", new Configuration(variableDeclaration))).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s p_000 = document.createElement('p');".formatted(keyword),
                "p_000.setAttribute(`class`, ``);",
                "targetElement_000.appendChild(p_000);");
    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenMultipleNodeAtRoot(VariableDeclaration variableDeclaration) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert("<!-- %s --><div></div>".formatted(token), configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token),
                "targetElement_000.appendChild(comment_000);",
                "%s div_000 = document.createElement('div');".formatted(keyword),
                "targetElement_000.appendChild(div_000);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWithIncrementVariableNameWhenGivenMultipleNodeWithSameTagNames(VariableDeclaration variableDeclaration) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert("<div></div><div></div>", configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s div_000 = document.createElement('div');".formatted(keyword),
                "targetElement_000.appendChild(div_000);",
                "%s div_001 = document.createElement('div');".formatted(keyword),
                "targetElement_000.appendChild(div_001);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenNestedNodes(VariableDeclaration variableDeclaration) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        final var converted = convert(
                "<div>A <strong>...</strong></div><div><p>Well, case!</div>", configuration);

        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s div_000 = document.createElement('div');".formatted(keyword),
                "%s text_000 = document.createTextNode(`A `);".formatted(keyword),
                "div_000.appendChild(text_000);",
                "%s strong_000 = document.createElement('strong');".formatted(keyword),
                "%s text_001 = document.createTextNode(`...`);".formatted(keyword),
                "strong_000.appendChild(text_001);",
                "div_000.appendChild(strong_000);",
                "targetElement_000.appendChild(div_000);",
                "%s div_001 = document.createElement('div');".formatted(keyword),
                "%s p_000 = document.createElement('p');".formatted(keyword),
                "%s text_002 = document.createTextNode(`Well, case!`);".formatted(keyword),
                "p_000.appendChild(text_002);",
                "div_001.appendChild(p_000);",
                "targetElement_000.appendChild(div_001);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class)
    public void produceValidCodeWhenGivenPathToAFile(VariableDeclaration variableDeclaration) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration);
        //noinspection resource,ConstantConditions
        final var input = getClass().getClassLoader()
                .getResourceAsStream("htmlFilesInput/sample.html")
                .readAllBytes();
        final var converted = convert(new String(input, UTF_8), configuration);

        // TODO: This doesn't make sense and deserve and issue to fix:
        //       html > body > html > body > ...
        //       Same would be true for doctype child of body
        assertThat(converted).containsExactly(
                "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                "%s html_000 = document.createElement('html');".formatted(keyword),
                "%s text_000 = document.createTextNode(`    `);".formatted(keyword),
                "html_000.appendChild(text_000);",
                "%s head_000 = document.createElement('head');".formatted(keyword),
                "%s text_001 = document.createTextNode(`        `);".formatted(keyword),
                "head_000.appendChild(text_001);",
                "%s meta_000 = document.createElement('meta');".formatted(keyword),
                "meta_000.setAttribute(`charset`, `utf-8`);",
                "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                "meta_000.appendChild(text_002);",
                "%s title_000 = document.createElement('title');".formatted(keyword),
                "%s text_003 = document.createTextNode(`Sample`);".formatted(keyword),
                "title_000.appendChild(text_003);",
                "meta_000.appendChild(title_000);",
                "%s text_004 = document.createTextNode(`        `);".formatted(keyword),
                "meta_000.appendChild(text_004);",
                "%s link_000 = document.createElement('link');".formatted(keyword),
                "link_000.setAttribute(`rel`, `stylesheet`);",
                "link_000.setAttribute(`href`, ``);",
                "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                "link_000.appendChild(text_005);",
                "meta_000.appendChild(link_000);",
                "head_000.appendChild(meta_000);",
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
                "%s h1_000 = document.createElement('h1');".formatted(keyword),
                "%s text_010 = document.createTextNode(`Sample`);".formatted(keyword),
                "h1_000.appendChild(text_010);",
                "div_001.appendChild(h1_000);",
                "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                "div_001.appendChild(text_011);",
                "%s img_000 = document.createElement('img');".formatted(keyword),
                "img_000.setAttribute(`src`, `kanye.jpg`);",
                "img_000.setAttribute(`alt`, `kanye`);",
                "%s text_012 = document.createTextNode(`            `);".formatted(keyword),
                "img_000.appendChild(text_012);",
                "div_001.appendChild(img_000);",
                "div_000.appendChild(div_001);",
                "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                "div_000.appendChild(text_013);",
                "%s div_002 = document.createElement('div');".formatted(keyword),
                "div_002.setAttribute(`id`, `main`);",
                "%s text_014 = document.createTextNode(`                `);".formatted(keyword),
                "div_002.appendChild(text_014);",
                "%s h2_000 = document.createElement('h2');".formatted(keyword),
                "%s text_015 = document.createTextNode(`Main`);".formatted(keyword),
                "h2_000.appendChild(text_015);",
                "div_002.appendChild(h2_000);",
                "%s text_016 = document.createTextNode(`                `);".formatted(keyword),
                "div_002.appendChild(text_016);",
                "%s p_000 = document.createElement('p');".formatted(keyword),
                "%s text_017 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                "p_000.appendChild(text_017);",
                "div_002.appendChild(p_000);",
                "%s text_018 = document.createTextNode(`                `);".formatted(keyword),
                "div_002.appendChild(text_018);",
                "%s img_001 = document.createElement('img');".formatted(keyword),
                "img_001.setAttribute(`src`, ``);",
                "img_001.setAttribute(`alt`, ``);",
                "%s text_019 = document.createTextNode(`            `);".formatted(keyword),
                "img_001.appendChild(text_019);",
                "div_002.appendChild(img_001);",
                "div_000.appendChild(div_002);",
                "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                "div_000.appendChild(text_020);",
                "%s div_003 = document.createElement('div');".formatted(keyword),
                "div_003.setAttribute(`id`, `footer`);",
                "%s text_021 = document.createTextNode(`                `);".formatted(keyword),
                "div_003.appendChild(text_021);",
                "%s p_001 = document.createElement('p');".formatted(keyword),
                "%s text_022 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                "p_001.appendChild(text_022);",
                "div_003.appendChild(p_001);",
                "%s text_023 = document.createTextNode(`            `);".formatted(keyword),
                "div_003.appendChild(text_023);",
                "div_000.appendChild(div_003);",
                "%s text_024 = document.createTextNode(`        `);".formatted(keyword),
                "div_000.appendChild(text_024);",
                "body_000.appendChild(div_000);",
                "%s text_025 = document.createTextNode(`    `);".formatted(keyword),
                "body_000.appendChild(text_025);",
                "html_000.appendChild(body_000);",
                "targetElement_000.appendChild(html_000);");
    }

    /**
     * A helper method to work with language-native String and array of data structures.
     *
     * @param input         The input HTML string
     * @param configuration The variable declaration used: let, const or var
     * @return Lines of output JS code
     */
    private String[] convert(@NonNull String input, Configuration configuration) throws IOException {
        final var inputStream = new ByteArrayInputStream(input.getBytes(UTF_8));
        final var outputStream = new ByteArrayOutputStream();

        converter.convert(inputStream, outputStream, configuration);

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

    private String keyword(final VariableDeclaration variableDeclaration) {
        return variableDeclaration.name().toLowerCase();
    }
}
