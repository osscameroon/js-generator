package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import com.osscameroon.jsgenerator.core.VariableNameStrategy;
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

@RequiredArgsConstructor
public class ConverterDefault implements Converter {
    private static final List<String> BOOLEAN_ATTRIBUTES = List.of("allowfullscreen", "async", "autofocus",
            "autoplay", "checked", "controls", "default", "defer", "disabled", "formnovalidate", "ismap", "itemscope",
            "loop", "multiple", "muted", "nomodule", "novalidate", "open", "playsinline", "readonly", "required",
            "reversed", "selected", "truespeed", "contenteditable");
    private final VariableNameStrategy variableNameStrategy;

    @Override
    @SneakyThrows
    public void convert(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
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
        visit(writer, "document", document.childNodes(), configuration);
        writer.flush();
    }

    private void visit(Writer writer, String parent, List<Node> nodes, Configuration configuration) throws IOException {
        for (final Node node : nodes) {
            if (node instanceof Element) visit(writer, parent, (Element) node, configuration);
            else if (node instanceof Comment) visit(writer, parent, (Comment) node, configuration);
            else if (node instanceof TextNode) visit(writer, parent, (TextNode) node, configuration);
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

    private void visit(Writer writer, String parent, Comment comment, Configuration configuration) throws IOException {
        final var variable = variableNameStrategy.nextName("comment");
        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        writer.write(format("\r\n%s %s = document.createComment(`%s`);\r\n", declarationKeyWord, variable, comment.getData()));
        writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
    }

    private void visit(Writer writer, String parent, TextNode textNode, Configuration configuration) throws IOException {
        final var variable = variableNameStrategy.nextName("text");
        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        writer.write(format("%s %s = document.createTextNode(`%s`);\r\n", declarationKeyWord, variable, textNode.getWholeText()));
        writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
    }

    private void visit(Writer writer, String parent, Element element, Configuration configuration) throws IOException {
        final var variable = variableNameStrategy.nextName(element.tagName());
        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        writer.write(format("\r\n%s %s = document.createElement('%s');\r\n", declarationKeyWord, variable, element.tagName()));
        visit(writer, variable, element.attributes());

        if ("script".equalsIgnoreCase(element.tagName())) {
            visitScriptNode(writer, parent, element, variable, configuration);
        } else {
            visit(writer, variable, element.childNodes(), configuration);
            writer.write(format("%s.appendChild(%s);\r\n", parent, variable));
        }

    }

    private void visitScriptNode(Writer writer, String parent, Element element, String variable, Configuration configuration) throws IOException {
        if (element.attr(element.absUrl("type")).isBlank()) {
            writer.write(format("\r\n%s.type = `text/javascript`;\r\n", variable));
        }

        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());
        final var script = ((TextNode) element.childNodes().get(0)).getWholeText()
                .replaceAll("`", "\\\\`");
        // FIXME: Will be quirky without tokenizing script code but then, it doesn't matter as it could be
        //        TypeScript or Mustache template or, Pig, etc. We may consider tokenizing those languages
        final var scriptTextVariable = variableNameStrategy.nextName("text");
        writer.write(format("\r\n" + join("\r\n", "try {",
                        "    %6$s %3$s = document.createTextNode(`%1$s`);",
                        "    %2$s.appendChild(%3$s);",
                        "    %4$s.appendChild(%2$s);",
                        "} catch (_) {",
                        "    %2$s.text = `%1$s`;",
                        "    %4$s.appendChild(%2$s);",
                        "}") + "\r\n",
                script, variable, scriptTextVariable, parent, variable, declarationKeyWord));
    }

    private String resolveDeclarationKeyWord(VariableDeclaration variableDeclaration) {

        return variableDeclaration.name().toLowerCase();
    }
}
