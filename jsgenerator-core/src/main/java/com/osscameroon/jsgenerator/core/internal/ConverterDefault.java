package com.osscameroon.jsgenerator.core.internal;

import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.jsoup.parser.Parser.xmlParser;

//TODO: Think about the user will use this library , if needed provide 4 explicit methods for these 4 cases
// code html to code js
// code html to file js
// file html to code js
// file html to file js


public class ConverterDefault implements Converter {
    private static final List<String> BOOLEAN_ATTRIBUTES = List.of("allowfullscreen", "async", "autofocus",
            "autoplay", "checked", "controls", "default", "defer", "disabled", "formnovalidate", "ismap", "itemscope",
            "loop", "multiple", "muted", "nomodule", "novalidate", "open", "playsinline", "readonly", "required",
            "reversed", "selected", "truespeed", "contenteditable");

    private static Element resolveClosestNonSelfClosingAncestor(Node element) {
        // NOTE: Fix issue #41 by looking up closest non-self-closing parent/ancestor to append current element to
        var ancestor = (Element) element.parent();

        //noinspection ConstantConditions
        while (ancestor.tag().isSelfClosing()) {
            ancestor = ancestor.parent();
        }

        return ancestor;
    }

    /*
    * TODO: There is some issue related to encoding, should we not set utf8 encoding here instead of setting it as we do inside ConverterTest ?
    *  Make sure that every input has utf8 encoding. If not, we set this encoding.
    *
    *  java define encoding of InputStream
    * https://stackoverflow.com/questions/3043710/java-inputstream-encoding-charset
    *
    * */
    @Override
    public void convert(InputStream inputStream, OutputStream outputStream, Configuration configuration) throws IOException {
        final var stringBuilder = new StringBuilder();
        final var scanner = new Scanner(inputStream);

        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }

        final var content = stringBuilder.toString();

        // NOTE: There is nothing to do
        if (content.isBlank()) return;

        final var variableNameStrategy = configuration.getVariableNameStrategy();
        final var document = Jsoup.parse(content, xmlParser());
        final var writer = new OutputStreamWriter(outputStream);

        final var selector = configuration.getTargetElementSelector();


        final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement") : null;


        // NOTE: We need a variable to keep track of ancestors name.
        //       Following issue#41, elements that follow self-closing that should be added
        //       to their real parent and not the JSoup-inferred parent (which happen to just
        //       be their previous self-closing sibling)

        Map<Element, String> variables = new HashMap<Element, String>();

        /*
         * if configuration.isQuerySelectorAdded() is true then variable = variableNameStrategy.nextName("targetElement")
         * if configuration.isQuerySelectorAdded() is false then variable = null
         *
         * In case, variable is null, we'll get a NullPointerException from Map.of(document, variable).
         *
         * Getting a NullPointerException here is normal because the query selector is not added.
         * In order to handle this situation,
         * we do nothing inside this catch but based on this null value,
         * we'll change the flow of this program inside the visiting methods.
         * */
        try {
            variables = new HashMap<Element, String>(Map.of(document, variable));
        } catch (NullPointerException e) {

            /*
             * We do nothing in case the query selector is not added
             * */

        }

        final var keyword = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        final var lineSeparator = System.lineSeparator();

        if (configuration.isQuerySelectorAdded()) {
            writer.write("%s %s = document.querySelector(`%s`);%s%s".formatted(keyword, variable, selector,lineSeparator,lineSeparator));
        }

        visit(writer, document.childNodes(), configuration, variables);
        writer.flush();
    }

    private void visit(Writer writer, List<Node> nodes, Configuration configuration, Map<Element, String> variables) throws IOException {
        for (final Node node : nodes) {
            if (node instanceof Element) visit(writer, (Element) node, configuration, variables);
            else if (node instanceof Comment) {
                if (configuration.isCommentConversionModeActivated()) {
                    visit(writer, (Comment) node, configuration, variables);
                }
            } else if (node instanceof TextNode) visit(writer, (TextNode) node, configuration, variables);
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

    private void visit(Writer writer, Comment comment, Configuration configuration, Map<Element, String> variables) throws IOException {
        final var variableNameStrategy = configuration.getVariableNameStrategy();
        final var variable = variableNameStrategy.nextName("comment");
        final var ancestor = resolveClosestNonSelfClosingAncestor(comment);
        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        writer.write(format("\r\n%s %s = document.createComment(`%s`);\r\n", declarationKeyWord, variable, comment.getData()));


        /*
         * Based on this ternary operation on convert method,
         *
         *         final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement"):null;

         * if configuration.isQuerySelectorAdded() is true then variables.get(ancestor) is not null
         * if configuration.isQuerySelectorAdded() is false then variables.get(ancestor) is null
         *
         * In order to not appendChild to a null element (configuration.isQuerySelectorAdded() is false), we use this condition
         * */

        if (variables.get(ancestor) != null) {
            writer.write(format("%s.appendChild(%s);\r\n", variables.get(ancestor), variable));
        }
    }

    private void visit(Writer writer, TextNode textNode, Configuration configuration, Map<Element, String> variables) throws IOException {
        final var variableNameStrategy = configuration.getVariableNameStrategy();
        final var variable = variableNameStrategy.nextName("text");
        final var ancestor = resolveClosestNonSelfClosingAncestor(textNode);
        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());

        writer.write(format("%s %s = document.createTextNode(`%s`);\r\n", declarationKeyWord, variable, textNode.getWholeText()));

        /*
         * Based on this ternary operation on convert method,
         *
         *         final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement"):null;

         * if configuration.isQuerySelectorAdded() is true then variables.get(ancestor) is not null
         * if configuration.isQuerySelectorAdded() is false then variables.get(ancestor) is null
         *
         * In order to not appendChild to a null element (configuration.isQuerySelectorAdded() is false), we use this condition
         * */

        if (variables.get(ancestor) != null) {

            writer.write(format("%s.appendChild(%s);\r\n", variables.get(ancestor), variable));

        }

    }

    private void visit(Writer writer, Element element, Configuration configuration, Map<Element, String> variables) throws IOException {
        final var declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());
        final var variableNameStrategy = configuration.getVariableNameStrategy();
        final var variable = variableNameStrategy.nextName(element.tagName());

        variables.put(element, variable);
        writer.write(format("\r\n%s %s = document.createElement('%s');\r\n", declarationKeyWord, variable, element.tagName()));
        visit(writer, variable, element.attributes());

        if ("script".equalsIgnoreCase(element.tagName())) {
            visitScriptNode(writer, element, variable, configuration, variables);
        } else {
            final var ancestor = resolveClosestNonSelfClosingAncestor(element);

            if (element.tag().isSelfClosing()) {
                // NOTE: JSoup wrongly considers the current element being visited as capable of having children.
                //       This condition ensures that we append the self-closing element to its parent,
                //       before processing its siblings, which JSoup parses as its children.

        /*
         * Based on this ternary operation on convert method,
         *
         *         final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement"):null;

         * if configuration.isQuerySelectorAdded() is true then variables.get(ancestor) is not null
         * if configuration.isQuerySelectorAdded() is false then variables.get(ancestor) is null
         *
         * In order to not appendChild to a null element (configuration.isQuerySelectorAdded() is false), we use this condition
         * */


                if (variables.get(ancestor) != null) {

                    writer.write(format("%s.appendChild(%s);\r\n", variables.get(ancestor), variable));

                }

                visit(writer, element.childNodes(), configuration, variables);
            } else {
                visit(writer, element.childNodes(), configuration, variables);


        /*
         * Based on this ternary operation on convert method,
         *
         *         final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement"):null;

         * if configuration.isQuerySelectorAdded() is true then variables.get(ancestor) is not null
         * if configuration.isQuerySelectorAdded() is false then variables.get(ancestor) is null
         *
         * In order to not appendChild to a null element (configuration.isQuerySelectorAdded() is false), we use this condition
         * */

                if (variables.get(ancestor) != null) {

                    writer.write(format("%s.appendChild(%s);\r\n", variables.get(ancestor), variable));

                }
            }
        }

    }

    private void visitScriptNode(Writer writer, Element element, String variable,
                                 Configuration configuration, Map<Element, String> variables) throws IOException {
        final var variableNameStrategy = configuration.getVariableNameStrategy();
        final var ancestor = resolveClosestNonSelfClosingAncestor(element);

        if (element.attr(element.absUrl("type")).isBlank()) {
            writer.write(format("\r\n%s.type = `text/javascript`;\r\n", variable));
        }

        String declarationKeyWord = resolveDeclarationKeyWord(configuration.getVariableDeclaration());
        final var script = ((TextNode) element.childNodes().get(0)).getWholeText()
                .replaceAll("`", "\\\\`");
        // FIXME: Will be quirky without tokenizing script code but then, it doesn't matter as it could be
        //        TypeScript or Mustache template or, Pig, etc. We may consider tokenizing those languages
        final var scriptTextVariable = variableNameStrategy.nextName("text");

        /*
         * Based on this ternary operation on convert method,
         *
         *         final var variable = configuration.isQuerySelectorAdded()
                ? variableNameStrategy.nextName("targetElement"):null;

         * if configuration.isQuerySelectorAdded() is true then variables.get(ancestor) is not null
         * if configuration.isQuerySelectorAdded() is false then variables.get(ancestor) is null
         *
         * In order to not appendChild to a null element (configuration.isQuerySelectorAdded() is false), we use this condition
         * */


        if (variables.get(ancestor) != null) {


            writer.write(format("\r\n" + join("\r\n", "try {",
                            "    %6$s %3$s = document.createTextNode(`%1$s`);",
                            "    %2$s.appendChild(%3$s);",
                            "    %4$s.appendChild(%2$s);",
                            "} catch (_) {",
                            "    %2$s.text = `%1$s`;",
                            "    %4$s.appendChild(%2$s);",
                            "}") + "\r\n",
                    script, variable, scriptTextVariable, variables.get(ancestor), variable, declarationKeyWord));


        } else {

            writer.write(format("\r\n" + join("\r\n", "try {",
                            "    %4$s %3$s = document.createTextNode(`%1$s`);",
                            "    %2$s.appendChild(%3$s);",
                            "} catch (_) {",
                            "    %2$s.text = `%1$s`;",
                            "}") + "\r\n",
                    script, variable, scriptTextVariable, declarationKeyWord));

        }
    }

    private String resolveDeclarationKeyWord(VariableDeclaration variableDeclaration) {
        return variableDeclaration.name().toLowerCase();
    }
}