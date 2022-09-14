package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.VariableNameStrategy;
import com.osscameroon.jsgenerator.core.Converter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.jsoup.parser.Parser.xmlParser;

/**
 * ConverterDefault
 *
 * @author Salathiel @t salathiel@genese.name
 * @since Sep 02, 2022 @t 23:25:27
 */
@RequiredArgsConstructor
public class ConverterDefault implements Converter {
    private static final List<String> BOOLEAN_ATTRIBUTES = List.of("allowfullscreen", "async", "autofocus",
        "autoplay", "checked", "controls", "default", "defer", "disabled", "formnovalidate", "ismap", "itemscope",
        "loop", "multiple", "muted", "nomodule", "novalidate", "open", "playsinline", "readonly", "required",
        "reversed", "selected", "truespeed", "contenteditable");
    private final VariableNameStrategy variableNameStrategy;

    @Override
    @SneakyThrows
    public void convert(InputStream inputStream, OutputStream outputStream) {
        final var stringBuilder = new StringBuilder();
        final var scanner = new Scanner(inputStream);

        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }

        final var content = stringBuilder.toString();

        // NOTE: There is nothing to do
        if (content.isBlank()) return;

        final var document = Jsoup.parse(content, xmlParser());
        final var writer = new OutputStreamWriter(outputStream);
        visit(writer, "document", document.childNodes());
        writer.flush();
    }

    private void visit(Writer writer, String parent, List<Node> nodes) throws IOException {
        for (final Node node : nodes) {
            if (node instanceof Element) visit(writer, parent, (Element) node);
            else if (node instanceof Comment) visit(writer, parent, (Comment) node);
            else if (node instanceof TextNode) visit(writer, parent, (TextNode) node);
        }
    }

    private void visit(Writer writer, String parent, Attributes attributes) throws IOException {
        for (final Attribute attribute : attributes) {
            // FIXME: Should we special handle aria-* and data-* attributes ?
            // NOTE: Account for boolean attributes like open, required, disabled, contenteditable...
            final var value = attribute.hasDeclaredValue() ? attribute.getValue()
                : BOOLEAN_ATTRIBUTES.contains(attribute.getKey()) ? "true"
                : "";
            writer.write(format("%s.setAttribute(`%s`, `%s`);\r\n", parent, attribute.getKey(), value));
        }
    }

    private void visit(Writer writer, String parent, Comment comment) throws IOException {
        final var variable = variableNameStrategy.nextName("comment");

        writer.write(format("\r\nlet %s = document.createComment(`%s`);\r\n", variable, comment.getData()));
        writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
    }

    private void visit(Writer writer, String parent, TextNode textNode) throws IOException {
        final var variable = variableNameStrategy.nextName("text");

        writer.write(format("let %s = document.createTextNode(`%s`);\r\n", variable, textNode.getWholeText()));
        writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
    }

    private void visit(Writer writer, String parent, Element element) throws IOException {
        final var variable = variableNameStrategy.nextName(element.tagName());

        writer.write(format("\r\nlet %s = document.createElement('%s');\r\n", variable, element.tagName()));
        visit(writer, variable, element.attributes());

        if ("script".equalsIgnoreCase(element.tagName())) {
            visitScriptNode(writer, parent, element, variable);
        } else {
            visit(writer, variable, element.childNodes());
            writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
        }

    }

    private void visitScriptNode(Writer writer, String parent, Element element, String variable) throws IOException {
        if (element.attr(element.absUrl("type")).isBlank()) {
            writer.write(format("\r\n%s.type = `text/javascript`;\r\n", variable));
        }

        final var script = ((TextNode) element.childNodes().get(0)).getWholeText()
            .replaceAll("`", "\\\\`");
        // FIXME: Will be quirky without tokenizing script code but then, it doesn't matter as it could be
        //        TypeScript or Mustache template or, Pig, etc. We may consider tokenizing those languages
        final var scriptTextVariable = variableNameStrategy.nextName("text");
        writer.write(format("\r\n" + join("\r\n", "try {",
                "    let %3$s = document.createTextNode(`%1$s`);",
                "    %2$s.appendChild(%3$s);",
                "    %4$s.appendChild(%2$s);",
                "} catch (_) {",
                "    %2$s.text = `%1$s`;",
                "    %4$s.appendChild(%2$s);",
                "}") + "\r\n",
            script, variable, scriptTextVariable, parent, variable));
    }
}
