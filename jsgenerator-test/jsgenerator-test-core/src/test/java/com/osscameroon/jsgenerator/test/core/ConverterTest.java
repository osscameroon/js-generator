package com.osscameroon.jsgenerator.test.core;

import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * ConverterTest
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 23:50:25
 */
@ExtendWith(MockitoExtension.class)
public class ConverterTest {

    private Converter converter;

    @BeforeEach
    public void before() {
        converter = Converter.of(VariableNameStrategy.ofTypeBased());
    }


    @Test
    public void produceValidCodeWhenGivenSelfClosingTag(){

        String selfClosingTagInputWithoutSlashHtml = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

        String selfClosingTagInputWithSlashHtml = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

        System.err.println("selfClosingTagInputWithoutSlashHtml");

        System.err.println(selfClosingTagInputWithoutSlashHtml);

        Arrays.stream(convert(selfClosingTagInputWithoutSlashHtml,new Configuration())).forEach(System.out::println);

        System.err.println("selfClosingTagInputWithSlashHtml");

        System.err.println(selfClosingTagInputWithSlashHtml);

        Arrays.stream(convert(selfClosingTagInputWithSlashHtml,new Configuration())).forEach(System.out::println);



    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenCDATA(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        final var token = randomUUID().toString();
        final var converted = convert(format("<![CDATA[ < %s > & ]]>", token), new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(format(variableDeclarationKeyWord + " text_000 = document.createTextNode(` < %s > & `);", token),
                "document.appendChild(text_000);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenPlainText(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();
        final var token = randomUUID().toString();
        final var converted = convert(token, new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(format(variableDeclarationKeyWord + " text_000 = document.createTextNode(`%s`);", token),
                "document.appendChild(text_000);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST

    public void produceValidCodeWhenGivenComment(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();
        final var token = randomUUID().toString();
        final var converted = convert(format("<!-- %s -->", token), new Configuration(variableDeclaration));


        assertThat(converted).containsExactly(
                format(variableDeclarationKeyWord + " comment_000 = document.createComment(` %s `);", token),
                "document.appendChild(comment_000);");
    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST

    public void produceValidCodeWhenGivenScript(VariableDeclaration variableDeclaration) {
        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();
        final var token = randomUUID().toString();
        final var converted = convert(format("<script>console.log(`%s`)</script>", token), new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(
                variableDeclarationKeyWord + " script_000 = document.createElement('script');",
                "script_000.type = `text/javascript`;",
                "try {",
                format(variableDeclarationKeyWord + " text_000 = document.createTextNode(`console.log(\\`%s\\`)`);", token),
                "script_000.appendChild(text_000);",
                "document.appendChild(script_000);",
                "} catch (_) {",
                format("script_000.text = `console.log(\\`%s\\`)`;", token),
                "document.appendChild(script_000);",
                "}");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenTagWithAttributes(VariableDeclaration variableDeclaration) {
        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        assertThat(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration))).containsExactly(
                variableDeclarationKeyWord + " div_000 = document.createElement('div');",
                "div_000.setAttribute(`id`, `id-value`);",
                "document.appendChild(div_000);");

        assertThat(convert("<details open></details>", new Configuration(variableDeclaration))).containsExactly(
                variableDeclarationKeyWord + " details_000 = document.createElement('details');",
                "details_000.setAttribute(`open`, `true`);",
                "document.appendChild(details_000);");

        assertThat(convert("<p class></p>", new Configuration(variableDeclaration))).containsExactly(
                variableDeclarationKeyWord + " p_000 = document.createElement('p');",
                "p_000.setAttribute(`class`, ``);",
                "document.appendChild(p_000);");
    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenMultipleNodeAtRoot(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        final var converted = convert("<!-- Here comes a DIV element... --><div></div>", new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(
                variableDeclarationKeyWord + " comment_000 = document.createComment(` Here comes a DIV element... `);",
                "document.appendChild(comment_000);",
                variableDeclarationKeyWord + " div_000 = document.createElement('div');",
                "document.appendChild(div_000);");
    }


    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWithIncrementVariableNameWhenGivenMultipleNodeWithSameTagNames(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        final var converted = convert("<div></div><div></div>", new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(
                variableDeclarationKeyWord + " div_000 = document.createElement('div');",
                "document.appendChild(div_000);",
                variableDeclarationKeyWord + " div_001 = document.createElement('div');",
                "document.appendChild(div_001);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenNestedNodes(VariableDeclaration variableDeclaration) {
        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        final var converted = convert("<div>A <strong>...</strong></div><div><p>Well, case!</div>", new Configuration(variableDeclaration));

        assertThat(converted).containsExactly(
                variableDeclarationKeyWord + " div_000 = document.createElement('div');",
                variableDeclarationKeyWord + " text_000 = document.createTextNode(`A `);",
                "div_000.appendChild(text_000);",
                variableDeclarationKeyWord + " strong_000 = document.createElement('strong');",
                variableDeclarationKeyWord + " text_001 = document.createTextNode(`...`);",
                "strong_000.appendChild(text_001);",
                "div_000.appendChild(strong_000);",
                "document.appendChild(div_000);",
                variableDeclarationKeyWord + " div_001 = document.createElement('div');",
                variableDeclarationKeyWord + " p_000 = document.createElement('p');",
                variableDeclarationKeyWord + " text_002 = document.createTextNode(`Well, case!`);",
                "p_000.appendChild(text_002);",
                "div_001.appendChild(p_000);",
                "document.appendChild(div_001);");
    }

    @ParameterizedTest
    @EnumSource(VariableDeclaration.class) // passing all variable declaration options: LET, VAR, CONST
    public void produceValidCodeWhenGivenPathToAFile(VariableDeclaration variableDeclaration) {

        final String variableDeclarationKeyWord = variableDeclaration.name().toLowerCase();

        final var outputStream = new ByteArrayOutputStream();
        final var inputStream = getClass().getClassLoader()
                .getResourceAsStream("htmlFilesInput/sample.html");


        assertThat(inputStream).isNotNull();
        Converter.of(VariableNameStrategy.ofTypeBased()).convert(inputStream, outputStream, new Configuration(variableDeclaration));

        assertThat(outputAsStrippedLines(outputStream)).containsExactly(
                variableDeclarationKeyWord + " html_000 = document.createElement('html');",
                variableDeclarationKeyWord + " text_000 = document.createTextNode(`    `);",
                "html_000.appendChild(text_000);",
                variableDeclarationKeyWord + " head_000 = document.createElement('head');",
                variableDeclarationKeyWord + " text_001 = document.createTextNode(`        `);",
                "head_000.appendChild(text_001);",
                variableDeclarationKeyWord + " meta_000 = document.createElement('meta');",
                "meta_000.setAttribute(`charset`, `utf-8`);",
                variableDeclarationKeyWord + " text_002 = document.createTextNode(`        `);",
                "meta_000.appendChild(text_002);",
                variableDeclarationKeyWord + " title_000 = document.createElement('title');",
                variableDeclarationKeyWord + " text_003 = document.createTextNode(`Sample`);",
                "title_000.appendChild(text_003);",
                "meta_000.appendChild(title_000);",
                variableDeclarationKeyWord + " text_004 = document.createTextNode(`        `);",
                "meta_000.appendChild(text_004);",
                variableDeclarationKeyWord + " link_000 = document.createElement('link');",
                "link_000.setAttribute(`rel`, `stylesheet`);",
                "link_000.setAttribute(`href`, ``);",
                variableDeclarationKeyWord + " text_005 = document.createTextNode(`    `);",
                "link_000.appendChild(text_005);",
                "meta_000.appendChild(link_000);",
                "head_000.appendChild(meta_000);",
                "html_000.appendChild(head_000);",
                variableDeclarationKeyWord + " text_006 = document.createTextNode(`    `);",
                "html_000.appendChild(text_006);",
                variableDeclarationKeyWord + " body_000 = document.createElement('body');",
                variableDeclarationKeyWord + " text_007 = document.createTextNode(`        `);",
                "body_000.appendChild(text_007);",
                variableDeclarationKeyWord + " div_000 = document.createElement('div');",
                "div_000.setAttribute(`id`, `container`);",
                variableDeclarationKeyWord + " text_008 = document.createTextNode(`            `);",
                "div_000.appendChild(text_008);",
                variableDeclarationKeyWord + " div_001 = document.createElement('div');",
                "div_001.setAttribute(`id`, `header`);",
                variableDeclarationKeyWord + " text_009 = document.createTextNode(`                `);",
                "div_001.appendChild(text_009);",
                variableDeclarationKeyWord + " h1_000 = document.createElement('h1');",
                variableDeclarationKeyWord + " text_010 = document.createTextNode(`Sample`);",
                "h1_000.appendChild(text_010);",
                "div_001.appendChild(h1_000);",
                variableDeclarationKeyWord + " text_011 = document.createTextNode(`                `);",
                "div_001.appendChild(text_011);",
                variableDeclarationKeyWord + " img_000 = document.createElement('img');",
                "img_000.setAttribute(`src`, `kanye.jpg`);",
                "img_000.setAttribute(`alt`, `kanye`);",
                variableDeclarationKeyWord + " text_012 = document.createTextNode(`            `);",
                "img_000.appendChild(text_012);",
                "div_001.appendChild(img_000);",
                "div_000.appendChild(div_001);",
                variableDeclarationKeyWord + " text_013 = document.createTextNode(`            `);",
                "div_000.appendChild(text_013);",
                variableDeclarationKeyWord + " div_002 = document.createElement('div');",
                "div_002.setAttribute(`id`, `main`);",
                variableDeclarationKeyWord + " text_014 = document.createTextNode(`                `);",
                "div_002.appendChild(text_014);",
                variableDeclarationKeyWord + " h2_000 = document.createElement('h2');",
                variableDeclarationKeyWord + " text_015 = document.createTextNode(`Main`);",
                "h2_000.appendChild(text_015);",
                "div_002.appendChild(h2_000);",
                variableDeclarationKeyWord + " text_016 = document.createTextNode(`                `);",
                "div_002.appendChild(text_016);",
                variableDeclarationKeyWord + " p_000 = document.createElement('p');",
                variableDeclarationKeyWord + " text_017 = document.createTextNode(`This is the main content.`);",
                "p_000.appendChild(text_017);",
                "div_002.appendChild(p_000);",
                variableDeclarationKeyWord + " text_018 = document.createTextNode(`                `);",
                "div_002.appendChild(text_018);",
                variableDeclarationKeyWord + " img_001 = document.createElement('img');",
                "img_001.setAttribute(`src`, ``);",
                "img_001.setAttribute(`alt`, ``);",
                variableDeclarationKeyWord + " text_019 = document.createTextNode(`            `);",
                "img_001.appendChild(text_019);",
                "div_002.appendChild(img_001);",
                "div_000.appendChild(div_002);",
                variableDeclarationKeyWord + " text_020 = document.createTextNode(`            `);",
                "div_000.appendChild(text_020);",
                variableDeclarationKeyWord + " div_003 = document.createElement('div');",
                "div_003.setAttribute(`id`, `footer`);",
                variableDeclarationKeyWord + " text_021 = document.createTextNode(`                `);",
                "div_003.appendChild(text_021);",
                variableDeclarationKeyWord + " p_001 = document.createElement('p');",
                variableDeclarationKeyWord + " text_022 = document.createTextNode(`Copyright © 2019`);",
                "p_001.appendChild(text_022);",
                "div_003.appendChild(p_001);",
                variableDeclarationKeyWord + " text_023 = document.createTextNode(`            `);",
                "div_003.appendChild(text_023);",
                "div_000.appendChild(div_003);",
                variableDeclarationKeyWord + " text_024 = document.createTextNode(`        `);",
                "div_000.appendChild(text_024);",
                "body_000.appendChild(div_000);",
                variableDeclarationKeyWord + " text_025 = document.createTextNode(`    `);",
                "body_000.appendChild(text_025);",
                "html_000.appendChild(body_000);",
                "document.appendChild(html_000);");
    }

    /**
     * A helper method to work with language-native String and array of data structures.
     *
     * @param input         The input HTML string
     * @param configuration The variable declaration used: let, const or var
     * @return Lines of output JS code
     */

    private String[] convert(@NonNull String input, Configuration configuration) {
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
}
