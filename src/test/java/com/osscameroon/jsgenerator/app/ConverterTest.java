package com.osscameroon.jsgenerator.app;

import com.osscameroon.jsgenerator.app.internal.ConverterDefault;
import com.osscameroon.jsgenerator.app.internal.TypeBasedNameGenerationStrategy;
import lombok.NonNull;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

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
public class ConverterTest {
    private Converter converter;

    @Before
    public void before() {
        converter = new ConverterDefault(new TypeBasedNameGenerationStrategy());
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

        return outputStream
            .toString(UTF_8)
            .lines()
            .map(String::strip)
            .filter(line -> !line.isEmpty())
            .toArray(String[]::new);
    }
}
