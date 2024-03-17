package com.osscameroon.jsgenerator.test.core;

import com.osscameroon.jsgenerator.core.Configuration;
import com.osscameroon.jsgenerator.core.Converter;
import com.osscameroon.jsgenerator.core.VariableDeclaration;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;


/*
*
* TODO: Every test should be also validated on JSFiddle (https://jsfiddle.net/) and CodePen (https://codepen.io/).
*  The goal is to see if the js output matches the html code by pasting each code then see how they are rendered.
*  "document.querySelector(`:root > body`);" must be added into JS output to see the results.
*  To do so, just copy and paste the result from the method "printConverted(String[])".
*
* */
@ExtendWith(MockitoExtension.class)
class ConverterTest {
    private static final Logger logger = getLogger(ConverterTest.class);
    private Converter converter;

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
    public void before() {
        converter = Converter.of();
    }

    /**
     * The goal is to show how precise is our conversion compared to other websites such as
     * <a href="https://www.html-code-generator.com/html/html-code-convert-to-javascript">HTML Code Generator</a>
     * and <a href="https://wtools.io/html-to-javascript-converter">WTOOLS</a>.
     *
     *
     <br>
     <br>
     *
     *
     * Result of HTML conversion to JS code from
     * <a href="https://www.html-code-generator.com/html/html-code-convert-to-javascript">HTML Code Generator</a>:
     *
     *
     *
     *
     * <pre>
     *     {@code
     *
            let code = "";
            code += "<h1>HTML To JavaScript</h1>\n";
            code += "<ol>\n";
            code += "\t<li>First item</li>\n";
            code += "\t<li>Second item</li>\n";
            code += "\t<li>Third item</li>\n";
            code += "\t<li>Fourth item</li>\n";
            code += "</ol>\n";
     *
     *
     *     }
     * </pre>

     *
     *
     *
     *3 possible results of HTML conversion to JS code from  <a href="https://wtools.io/html-to-javascript-converter">WTOOLS</a>:
     *
     *
     * <pre>
     *     {@code
     *
     *     document.write('<h1>HTML To JavaScript</h1>');
     *     document.write('<ol>');
     *     document.write('    <li>First item</li>');
     *     document.write('    <li>Second item</li>');
     *     document.write('    <li>Third item</li>');
     *     document.write('    <li>Fourth item</li>');
     *     document.write('</ol>');
     *
     *
     *     }
     * </pre>
     *
     *-----------------------------------------------------
     *
     * <pre>
     *     {@code
     *
     *     document.writeln('<h1>HTML To JavaScript</h1>');
     *     document.writeln('<ol>');
     *     document.writeln('    <li>First item</li>');
     *     document.writeln('    <li>Second item</li>');
     *     document.writeln('    <li>Third item</li>');
     *     document.writeln('    <li>Fourth item</li>');
     *     document.writeln('</ol>');
     *
     *
     *     }
     * </pre>


     -----------------------------------------------------

     * <pre>
     *     {@code
     *
            var variable = '' +
            '<h1>HTML To JavaScript</h1>' +
            '<ol>' +
            '    <li>First item</li>' +
            '    <li>Second item</li>' +
            '    <li>Third item</li>' +
            '    <li>Fourth item</li>' +
            '</ol>' +
            '';
     *
     *
     *     }
     * </pre>
     *
     *
     * Our results use let,var and const as variable declarations, we also generate js variable for each element.
     *
     *
     * */
    //TODO: Remove this method and put that in comparison issue
    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void comparisonBetweenJsGeneratorAndOtherConverters(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert(
                """
                        <h1>HTML To JavaScript</h1>
                        <ol>
                            <li>First item</li>
                            <li>Second item</li>
                            <li>Third item</li>
                            <li>Fourth item</li>
                        </ol>
                         """,
                new Configuration(variableDeclaration, querySelectorAdded));

        printConverted(converted);

        if (querySelectorAdded) {

            assertThat(converted).containsExactly( "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s h1_001 = document.createElement('h1');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                    "h1_001.appendChild(text_001);",
                    "targetElement_001.appendChild(h1_001);",
                    "%s ol_001 = document.createElement('ol');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_002);",
                    "%s li_001 = document.createElement('li');".formatted(keyword),
                    "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                    "li_001.appendChild(text_003);",
                    "ol_001.appendChild(li_001);",
                    "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_004);",
                    "%s li_002 = document.createElement('li');".formatted(keyword),
                    "%s text_005 = document.createTextNode(`Second item`);".formatted(keyword),
                    "li_002.appendChild(text_005);",
                    "ol_001.appendChild(li_002);",
                    "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_006);",
                    "%s li_003 = document.createElement('li');".formatted(keyword),
                    "%s text_007 = document.createTextNode(`Third item`);".formatted(keyword),
                    "li_003.appendChild(text_007);",
                    "ol_001.appendChild(li_003);",
                    "%s text_008 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_008);",
                    "%s li_004 = document.createElement('li');".formatted(keyword),
                    "%s text_009 = document.createTextNode(`Fourth item`);".formatted(keyword),
                    "li_004.appendChild(text_009);",
                    "ol_001.appendChild(li_004);",
                    "targetElement_001.appendChild(ol_001);"
            );
        } else {
            assertThat(converted).containsExactly(
                    "%s h1_001 = document.createElement('h1');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                    "h1_001.appendChild(text_001);",
                    "%s ol_001 = document.createElement('ol');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_002);",
                    "%s li_001 = document.createElement('li');".formatted(keyword),
                    "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                    "li_001.appendChild(text_003);",
                    "ol_001.appendChild(li_001);",
                    "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_004);",
                    "%s li_002 = document.createElement('li');".formatted(keyword),
                    "%s text_005 = document.createTextNode(`Second item`);".formatted(keyword),
                    "li_002.appendChild(text_005);",
                    "ol_001.appendChild(li_002);",
                    "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_006);",
                    "%s li_003 = document.createElement('li');".formatted(keyword),
                    "%s text_007 = document.createTextNode(`Third item`);".formatted(keyword),
                    "li_003.appendChild(text_007);",
                    "ol_001.appendChild(li_003);",
                    "%s text_008 = document.createTextNode(`    `);".formatted(keyword),
                    "ol_001.appendChild(text_008);",
                    "%s li_004 = document.createElement('li');".formatted(keyword),
                    "%s text_009 = document.createTextNode(`Fourth item`);".formatted(keyword),
                    "li_004.appendChild(text_009);",
                    "ol_001.appendChild(li_004);"
            );
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void comparisonBetweenJsGeneratorAndOtherConvertersWithComment(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded,final boolean commentConversionModeActivated) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert(
                """
                        <h1>HTML To JavaScript</h1>
                        <ol>
                            <li>First item</li>
                            <!--Second item-->
                            <li>Second item</li>
                            <li>Third item</li>
                            <!--Fourth item-->
                            <li>Fourth item</li>
                        </ol>
                         """,
                new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated));

        printConverted(converted);

        if (querySelectorAdded) {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly( "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                        "h1_001.appendChild(text_001);",
                        "targetElement_001.appendChild(h1_001);",
                        "%s ol_001 = document.createElement('ol');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_002);",
                        "%s li_001 = document.createElement('li');".formatted(keyword),
                        "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                        "li_001.appendChild(text_003);",
                        "ol_001.appendChild(li_001);",
                        "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_004);",
                        "%s comment_001 = document.createComment(`Second item`);".formatted(keyword),
                        "ol_001.appendChild(comment_001);",
                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_005);",
                        "%s li_002 = document.createElement('li');".formatted(keyword),
                        "%s text_006 = document.createTextNode(`Second item`);".formatted(keyword),
                        "li_002.appendChild(text_006);",
                        "ol_001.appendChild(li_002);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_007);",
                        "%s li_003 = document.createElement('li');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`Third item`);".formatted(keyword),
                        "li_003.appendChild(text_008);",
                        "ol_001.appendChild(li_003);",
                        "%s text_009 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_009);",
                        "%s comment_002 = document.createComment(`Fourth item`);".formatted(keyword),
                        "ol_001.appendChild(comment_002);",
                        "%s text_010 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_010);",
                        "%s li_004 = document.createElement('li');".formatted(keyword),
                        "%s text_011 = document.createTextNode(`Fourth item`);".formatted(keyword),
                        "li_004.appendChild(text_011);",
                        "ol_001.appendChild(li_004);",
                        "targetElement_001.appendChild(ol_001);"
                );
            }else{
                assertThat(converted).containsExactly( "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                        "h1_001.appendChild(text_001);",
                        "targetElement_001.appendChild(h1_001);",
                        "%s ol_001 = document.createElement('ol');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_002);",
                        "%s li_001 = document.createElement('li');".formatted(keyword),
                        "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                        "li_001.appendChild(text_003);",
                        "ol_001.appendChild(li_001);",
                        "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_004);",
                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_005);",
                        "%s li_002 = document.createElement('li');".formatted(keyword),
                        "%s text_006 = document.createTextNode(`Second item`);".formatted(keyword),
                        "li_002.appendChild(text_006);",
                        "ol_001.appendChild(li_002);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_007);",
                        "%s li_003 = document.createElement('li');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`Third item`);".formatted(keyword),
                        "li_003.appendChild(text_008);",
                        "ol_001.appendChild(li_003);",
                        "%s text_009 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_009);",
                        "%s text_010 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_010);",
                        "%s li_004 = document.createElement('li');".formatted(keyword),
                        "%s text_011 = document.createTextNode(`Fourth item`);".formatted(keyword),
                        "li_004.appendChild(text_011);",
                        "ol_001.appendChild(li_004);",
                        "targetElement_001.appendChild(ol_001);"
                );
            }
        } else {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                        "h1_001.appendChild(text_001);",
                        "%s ol_001 = document.createElement('ol');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_002);",
                        "%s li_001 = document.createElement('li');".formatted(keyword),
                        "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                        "li_001.appendChild(text_003);",
                        "ol_001.appendChild(li_001);",
                        "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_004);",
                        "%s comment_001 = document.createComment(`Second item`);".formatted(keyword),
                        "ol_001.appendChild(comment_001);",
                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_005);",
                        "%s li_002 = document.createElement('li');".formatted(keyword),
                        "%s text_006 = document.createTextNode(`Second item`);".formatted(keyword),
                        "li_002.appendChild(text_006);",
                        "ol_001.appendChild(li_002);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_007);",
                        "%s li_003 = document.createElement('li');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`Third item`);".formatted(keyword),
                        "li_003.appendChild(text_008);",
                        "ol_001.appendChild(li_003);",
                        "%s text_009 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_009);",
                        "%s comment_002 = document.createComment(`Fourth item`);".formatted(keyword),
                        "ol_001.appendChild(comment_002);",
                        "%s text_010 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_010);",
                        "%s li_004 = document.createElement('li');".formatted(keyword),
                        "%s text_011 = document.createTextNode(`Fourth item`);".formatted(keyword),
                        "li_004.appendChild(text_011);",
                        "ol_001.appendChild(li_004);"
                );
            }else{
                assertThat(converted).containsExactly(
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`HTML To JavaScript`);".formatted(keyword),
                        "h1_001.appendChild(text_001);",
                        "%s ol_001 = document.createElement('ol');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_002);",
                        "%s li_001 = document.createElement('li');".formatted(keyword),
                        "%s text_003 = document.createTextNode(`First item`);".formatted(keyword),
                        "li_001.appendChild(text_003);",
                        "ol_001.appendChild(li_001);",
                        "%s text_004 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_004);",
                        "%s text_005 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_005);",
                        "%s li_002 = document.createElement('li');".formatted(keyword),
                        "%s text_006 = document.createTextNode(`Second item`);".formatted(keyword),
                        "li_002.appendChild(text_006);",
                        "ol_001.appendChild(li_002);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_007);",
                        "%s li_003 = document.createElement('li');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`Third item`);".formatted(keyword),
                        "li_003.appendChild(text_008);",
                        "ol_001.appendChild(li_003);",
                        "%s text_009 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_009);",
                        "%s text_010 = document.createTextNode(`    `);".formatted(keyword),
                        "ol_001.appendChild(text_010);",
                        "%s li_004 = document.createElement('li');".formatted(keyword),
                        "%s text_011 = document.createTextNode(`Fourth item`);".formatted(keyword),
                        "li_004.appendChild(text_011);",
                        "ol_001.appendChild(li_004);"
                );
            }
        }
    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void issue145WithCustomTagJavaScriptIdentifiers(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
                        <AngularText>Angular</AngularText>
                        <web-component>Web Component</web-component>""",
                new Configuration(variableDeclaration, querySelectorAdded));

        printConverted(converted);

        if (querySelectorAdded) {
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                    "custom_angulartext_001.appendChild(text_001);",
                    "targetElement_001.appendChild(custom_angulartext_001);",
                    "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                    "custom_web_component_001.appendChild(text_002);",
                    "targetElement_001.appendChild(custom_web_component_001);");
        } else {
            assertThat(converted).containsExactly(
                    "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                    "custom_angulartext_001.appendChild(text_001);",
                    "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                    "custom_web_component_001.appendChild(text_002);");
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void issue145WithCustomTagJavaScriptIdentifiersWithComment(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
                        <!--Angular Comment-->
                        <AngularText>Angular</AngularText>
                        <!--Web Component-->
                        <web-component>Web Component</web-component><!--Custom Tags-->""",
                new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated));

        printConverted(converted);

        if (querySelectorAdded) {
            if (commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(`Angular Comment`);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                        "custom_angulartext_001.appendChild(text_001);",
                        "targetElement_001.appendChild(custom_angulartext_001);",
                        "%s comment_002 = document.createComment(`Web Component`);".formatted(keyword),
                        "targetElement_001.appendChild(comment_002);",
                        "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                        "custom_web_component_001.appendChild(text_002);",
                        "targetElement_001.appendChild(custom_web_component_001);",
                        "%s comment_003 = document.createComment(`Custom Tags`);".formatted(keyword),
                        "targetElement_001.appendChild(comment_003);");
            }else{
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                        "custom_angulartext_001.appendChild(text_001);",
                        "targetElement_001.appendChild(custom_angulartext_001);",
                        "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                        "custom_web_component_001.appendChild(text_002);",
                        "targetElement_001.appendChild(custom_web_component_001);");
            }
        } else {
            if (commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(`Angular Comment`);".formatted(keyword),
                        "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                        "custom_angulartext_001.appendChild(text_001);",
                        "%s comment_002 = document.createComment(`Web Component`);".formatted(keyword),
                        "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                        "custom_web_component_001.appendChild(text_002);",
                        "%s comment_003 = document.createComment(`Custom Tags`);".formatted(keyword));
            }else{
                assertThat(converted).containsExactly(
                        "%s custom_angulartext_001 = document.createElement('AngularText');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Angular`);".formatted(keyword),
                        "custom_angulartext_001.appendChild(text_001);",
                        "%s custom_web_component_001 = document.createElement('web-component');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`Web Component`);".formatted(keyword),
                        "custom_web_component_001.appendChild(text_002);");
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void issue41WithSelfClosingTags(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
                        <input type="text">
                        <span>Hello</span>
                        <!-- Hello -->
                        Bye!
                        <script>// no-comment</script>""",
                new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated));

        printConverted(converted);

        if (querySelectorAdded) {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s input_001 = document.createElement('input');".formatted(keyword),
                        "input_001.setAttribute(`type`, `text`);",
                        "targetElement_001.appendChild(input_001);",
                        "%s span_001 = document.createElement('span');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Hello`);".formatted(keyword),
                        "span_001.appendChild(text_001);",
                        "targetElement_001.appendChild(span_001);",
                        "%s comment_001 = document.createComment(` Hello `);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s text_002 = document.createTextNode(`Bye!`);".formatted(keyword),
                        "targetElement_001.appendChild(text_002);",
                        "%s script_001 = document.createElement('script');".formatted(keyword),
                        "script_001.type = `text/javascript`;",
                        "try {",
                        "%s text_003 = document.createTextNode(`// no-comment`);".formatted(keyword),
                        "script_001.appendChild(text_003);",
                        "targetElement_001.appendChild(script_001);",
                        "} catch (_) {",
                        "script_001.text = `// no-comment`;",
                        "targetElement_001.appendChild(script_001);",
                        "}");
            }else{
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s input_001 = document.createElement('input');".formatted(keyword),
                        "input_001.setAttribute(`type`, `text`);",
                        "targetElement_001.appendChild(input_001);",
                        "%s span_001 = document.createElement('span');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Hello`);".formatted(keyword),
                        "span_001.appendChild(text_001);",
                        "targetElement_001.appendChild(span_001);",
                        "%s text_002 = document.createTextNode(`Bye!`);".formatted(keyword),
                        "targetElement_001.appendChild(text_002);",
                        "%s script_001 = document.createElement('script');".formatted(keyword),
                        "script_001.type = `text/javascript`;",
                        "try {",
                        "%s text_003 = document.createTextNode(`// no-comment`);".formatted(keyword),
                        "script_001.appendChild(text_003);",
                        "targetElement_001.appendChild(script_001);",
                        "} catch (_) {",
                        "script_001.text = `// no-comment`;",
                        "targetElement_001.appendChild(script_001);",
                        "}");
            }
        } else {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s input_001 = document.createElement('input');".formatted(keyword),
                        "input_001.setAttribute(`type`, `text`);",
                        "%s span_001 = document.createElement('span');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Hello`);".formatted(keyword),
                        "span_001.appendChild(text_001);",
                        "%s comment_001 = document.createComment(` Hello `);".formatted(keyword),
                        "%s text_002 = document.createTextNode(`Bye!`);".formatted(keyword),
                        "%s script_001 = document.createElement('script');".formatted(keyword),
                        "script_001.type = `text/javascript`;",
                        "try {",
                        "%s text_003 = document.createTextNode(`// no-comment`);".formatted(keyword),
                        "script_001.appendChild(text_003);",
                        "} catch (_) {",
                        "script_001.text = `// no-comment`;",
                        "}");
            }else{
                assertThat(converted).containsExactly(
                        "%s input_001 = document.createElement('input');".formatted(keyword),
                        "input_001.setAttribute(`type`, `text`);",
                        "%s span_001 = document.createElement('span');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`Hello`);".formatted(keyword),
                        "span_001.appendChild(text_001);",
                        "%s text_002 = document.createTextNode(`Bye!`);".formatted(keyword),
                        "%s script_001 = document.createElement('script');".formatted(keyword),
                        "script_001.type = `text/javascript`;",
                        "try {",
                        "%s text_003 = document.createTextNode(`// no-comment`);".formatted(keyword),
                        "script_001.appendChild(text_003);",
                        "} catch (_) {",
                        "script_001.text = `// no-comment`;",
                        "}");
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenCDATA(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<![CDATA[ < %s > & ]]>".formatted(token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s text_001 = document.createTextNode(` < %s > & `);".formatted(keyword, token),
                        "targetElement_001.appendChild(text_001);");
        } else {assertThat(converted).containsExactly("%s text_001 = document.createTextNode(` < %s > & `);".formatted(keyword, token));}
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenPlainText(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert(token, configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s text_001 = document.createTextNode(`%s`);".formatted(keyword, token),
                    "targetElement_001.appendChild(text_001);");
        } else {assertThat(converted).containsExactly("%s text_001 = document.createTextNode(`%s`);".formatted(keyword, token));}
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void produceValidCodeWhenGivenPlainTextWithComment(VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated);
        final var converted = convert("<!-- %s --> %s".formatted(token,token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly("%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` %s `);".formatted(keyword, token),
                        "targetElement_001.appendChild(comment_001);",
                        "%s text_001 = document.createTextNode(` %s`);".formatted(keyword, token),
                        "targetElement_001.appendChild(text_001);");

            }else{
                assertThat(converted).containsExactly("%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s text_001 = document.createTextNode(` %s`);".formatted(keyword, token),
                        "targetElement_001.appendChild(text_001);");
            }
        } else {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` %s `);".formatted(keyword, token),
                        "%s text_001 = document.createTextNode(` %s`);".formatted(keyword, token));
            }else{assertThat(converted).containsExactly("%s text_001 = document.createTextNode(` %s`);".formatted(keyword, token));}
        }
    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void produceValidCodeWhenGivenComment(VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded,commentConversionModeActivated);
        final var converted = convert("<!-- %s -->".formatted(token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` %s `);".formatted(keyword, token),
                        "targetElement_001.appendChild(comment_001);");
            }else{
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword));
            }
        } else {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly("%s comment_001 = document.createComment(` %s `);".formatted(keyword, token));
            }else{
                assertThat(converted).containsExactly();
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenScript(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<script>console.log(`%s`)</script>".formatted(token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s script_001 = document.createElement('script');".formatted(keyword),
                    "script_001.type = `text/javascript`;",
                    "try {",
                    "%s text_001 = document.createTextNode(`console.log(\\`%s\\`)`);".formatted(keyword, token),
                    "script_001.appendChild(text_001);",
                    "targetElement_001.appendChild(script_001);",
                    "} catch (_) {",
                    "script_001.text = `console.log(\\`%s\\`)`;".formatted(token),
                    "targetElement_001.appendChild(script_001);",
                    "}");
        } else {
            assertThat(converted).containsExactly(
                    "%s script_001 = document.createElement('script');".formatted(keyword),
                    "script_001.type = `text/javascript`;",
                    "try {",
                    "%s text_001 = document.createTextNode(`console.log(\\`%s\\`)`);".formatted(keyword, token),
                    "script_001.appendChild(text_001);",
                    "} catch (_) {",
                    "script_001.text = `console.log(\\`%s\\`)`;".formatted(token),
                    "}");
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenTagWithAttributes(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);

        if (querySelectorAdded) {

            String input = "<div id=\"id-value\"></div>";
            var configuration = new Configuration(variableDeclaration, true);
            var converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "div_001.setAttribute(`id`, `id-value`);",
                    "targetElement_001.appendChild(div_001);");

            input = "<details open></details>";
            configuration = new Configuration(variableDeclaration, true);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s details_001 = document.createElement('details');".formatted(keyword),
                    "details_001.setAttribute(`open`, `true`);",
                    "targetElement_001.appendChild(details_001);");

            input = "<p class></p>";
            configuration = new Configuration(variableDeclaration, true);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "p_001.setAttribute(`class`, ``);",
                    "targetElement_001.appendChild(p_001);");

            input = "<p class=\"class-value\"></p>";
            configuration = new Configuration(variableDeclaration, true);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "p_001.setAttribute(`class`, `class-value`);",
                    "targetElement_001.appendChild(p_001);");

        } else {

            String input = "<div id=\"id-value\"></div>";
            var configuration = new Configuration(variableDeclaration, false);
            var converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "div_001.setAttribute(`id`, `id-value`);");

            input = "<details open></details>";
            configuration = new Configuration(variableDeclaration, false);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s details_001 = document.createElement('details');".formatted(keyword),
                    "details_001.setAttribute(`open`, `true`);");

            input = "<p class></p>";
            configuration = new Configuration(variableDeclaration, false);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "p_001.setAttribute(`class`, ``);");

            input = "<p class=\"class-value\"></p>";
            configuration = new Configuration(variableDeclaration, false);
            converted = convert(input, configuration);

            printConverted(converted);
            assertThat(converted).containsExactly(
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "p_001.setAttribute(`class`, `class-value`);");
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void produceValidCodeWhenGivenTagWithAttributesWithComment(VariableDeclaration variableDeclaration, final boolean querySelectorAdded,final boolean commentConversionModeActivated) throws IOException {
        final var keyword = keyword(variableDeclaration);

        if (querySelectorAdded) {

            String input = "<!-- Div with id --><div id=\"id-value\"></div>";
            var configuration = new Configuration(variableDeclaration, true,commentConversionModeActivated);
            var converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` Div with id `);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `id-value`);",
                        "targetElement_001.appendChild(div_001);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `id-value`);",
                        "targetElement_001.appendChild(div_001);");

            }

            input = "<!-- Details tag with open attribute --><details open></details>";
            configuration = new Configuration(variableDeclaration, true,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` Details tag with open attribute `);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s details_001 = document.createElement('details');".formatted(keyword),
                        "details_001.setAttribute(`open`, `true`);",
                        "targetElement_001.appendChild(details_001);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s details_001 = document.createElement('details');".formatted(keyword),
                        "details_001.setAttribute(`open`, `true`);",
                        "targetElement_001.appendChild(details_001);");

            }

            input = "<!-- Paragraph tag with empty class attribute --><p class></p>";
            configuration = new Configuration(variableDeclaration, true,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` Paragraph tag with empty class attribute `);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, ``);",
                        "targetElement_001.appendChild(p_001);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, ``);",
                        "targetElement_001.appendChild(p_001);");
            }

            input = "<!-- Paragraph tag with class attribute --><p class=\"class-value\"></p>";
            configuration = new Configuration(variableDeclaration, true,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` Paragraph tag with class attribute `);".formatted(keyword),
                        "targetElement_001.appendChild(comment_001);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, `class-value`);",
                        "targetElement_001.appendChild(p_001);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, `class-value`);",
                        "targetElement_001.appendChild(p_001);");
            }

        } else {

            String input = "<!-- Div with id --><div id=\"id-value\"></div>";
            var configuration = new Configuration(variableDeclaration, false,commentConversionModeActivated);
            var converted = convert(input, configuration);

            if(commentConversionModeActivated){
                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` Div with id `);".formatted(keyword),
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `id-value`);");

            }else{
                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `id-value`);");
            }

            input = "<!-- Details tag with open attribute --><details open></details>";
            configuration = new Configuration(variableDeclaration, false,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` Details tag with open attribute `);".formatted(keyword),
                        "%s details_001 = document.createElement('details');".formatted(keyword),
                        "details_001.setAttribute(`open`, `true`);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s details_001 = document.createElement('details');".formatted(keyword),
                        "details_001.setAttribute(`open`, `true`);");
            }


            input = "<!-- Paragraph tag with empty class attribute --><p class></p>";
            configuration = new Configuration(variableDeclaration, false,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` Paragraph tag with empty class attribute `);".formatted(keyword),
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, ``);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, ``);");
            }

            input = "<!-- Paragraph tag with class attribute --><p class=\"class-value\"></p>";
            configuration = new Configuration(variableDeclaration, false,commentConversionModeActivated);
            converted = convert(input, configuration);

            if(commentConversionModeActivated){

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` Paragraph tag with class attribute `);".formatted(keyword),
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, `class-value`);");

            }else{

                printConverted(converted);
                assertThat(converted).containsExactly(
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "p_001.setAttribute(`class`, `class-value`);");
            }

        }
    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void produceValidCodeWhenGivenMultipleNodeAtRoot(VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated);
        final var converted = convert("<!-- %s --><div></div>".formatted(token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s comment_001 = document.createComment(` %s `);".formatted(keyword, token),
                        "targetElement_001.appendChild(comment_001);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "targetElement_001.appendChild(div_001);");

            }else{
                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "targetElement_001.appendChild(div_001);");
            }
        } else {
            if(commentConversionModeActivated){
                assertThat(converted).containsExactly(
                        "%s comment_001 = document.createComment(` %s `);".formatted(keyword, token),
                        "%s div_001 = document.createElement('div');".formatted(keyword));
            }else{
                assertThat(converted).containsExactly("%s div_001 = document.createElement('div');".formatted(keyword));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWithIncrementVariableNameWhenGivenMultipleNodeWithSameTagNames(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<div></div><div></div>", configuration);

        printConverted(converted);

        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "targetElement_001.appendChild(div_001);",
                    "%s div_002 = document.createElement('div');".formatted(keyword),
                    "targetElement_001.appendChild(div_002);");

        } else {

            assertThat(converted).containsExactly(
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "%s div_002 = document.createElement('div');".formatted(keyword));
        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenNestedNodes(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<div>A <strong>...</strong></div><div><p>Well, case!</p></div>", configuration);

        printConverted(converted);

        if (querySelectorAdded) {
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`A `);".formatted(keyword),
                    "div_001.appendChild(text_001);",
                    "%s strong_001 = document.createElement('strong');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`...`);".formatted(keyword),
                    "strong_001.appendChild(text_002);",
                    "div_001.appendChild(strong_001);",
                    "targetElement_001.appendChild(div_001);",
                    "%s div_002 = document.createElement('div');".formatted(keyword),
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "%s text_003 = document.createTextNode(`Well, case!`);".formatted(keyword),
                    "p_001.appendChild(text_003);",
                    "div_002.appendChild(p_001);",
                    "targetElement_001.appendChild(div_002);");

        } else {

            assertThat(converted).containsExactly(
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`A `);".formatted(keyword),
                    "div_001.appendChild(text_001);",
                    "%s strong_001 = document.createElement('strong');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`...`);".formatted(keyword),
                    "strong_001.appendChild(text_002);",
                    "div_001.appendChild(strong_001);",
                    "%s div_002 = document.createElement('div');".formatted(keyword),
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "%s text_003 = document.createTextNode(`Well, case!`);".formatted(keyword),
                    "p_001.appendChild(text_003);",
                    "div_002.appendChild(p_001);");

        }
    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    void produceValidCodeWhenGivenPathToAFile(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        //noinspection resource,ConstantConditions
        final var input = getClass().getClassLoader()
                .getResourceAsStream("htmlFilesInput/sample.html")
                .readAllBytes();
        final var converted = convert(new String(input, UTF_8), configuration);

        printConverted(converted);


        // TODO: This doesn't make sense and deserve an issue to fix:
        //       html > body > html > body > ...
        //       Same would be true for doctype child of body

        if (querySelectorAdded) {

            //TODO: `:root > body` please, where did you get it ? from Jsoup ?
            assertThat(converted).containsExactly(
                    "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s html_001 = document.createElement('html');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                    "html_001.appendChild(text_001);",
                    "%s head_001 = document.createElement('head');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_002);",
                    "%s meta_001 = document.createElement('meta');".formatted(keyword),
                    "meta_001.setAttribute(`charset`, `utf-8`);",
                    "head_001.appendChild(meta_001);",
                    "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_003);",
                    "%s title_001 = document.createElement('title');".formatted(keyword),
                    "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                    "title_001.appendChild(text_004);",
                    "head_001.appendChild(title_001);",
                    "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_005);",
                    "%s link_001 = document.createElement('link');".formatted(keyword),
                    "link_001.setAttribute(`rel`, `stylesheet`);",
                    "link_001.setAttribute(`href`, ``);",
                    "head_001.appendChild(link_001);",
                    "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                    "head_001.appendChild(text_006);",
                    "html_001.appendChild(head_001);",
                    "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                    "html_001.appendChild(text_007);",
                    "%s body_001 = document.createElement('body');".formatted(keyword),
                    "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                    "body_001.appendChild(text_008);",
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "div_001.setAttribute(`id`, `container`);",
                    "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_009);",
                    "%s div_002 = document.createElement('div');".formatted(keyword),
                    "div_002.setAttribute(`id`, `header`);",
                    "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                    "div_002.appendChild(text_010);",
                    "%s h1_001 = document.createElement('h1');".formatted(keyword),
                    "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                    "h1_001.appendChild(text_011);",
                    "div_002.appendChild(h1_001);",
                    "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                    "div_002.appendChild(text_012);",
                    "%s img_001 = document.createElement('img');".formatted(keyword),
                    "img_001.setAttribute(`src`, `kanye.jpg`);",
                    "img_001.setAttribute(`alt`, `kanye`);",
                    "div_002.appendChild(img_001);",
                    "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                    "div_002.appendChild(text_013);",
                    "div_001.appendChild(div_002);",
                    "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_014);",
                    "%s div_003 = document.createElement('div');".formatted(keyword),
                    "div_003.setAttribute(`id`, `main`);",
                    "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_015);",
                    "%s h2_001 = document.createElement('h2');".formatted(keyword),
                    "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                    "h2_001.appendChild(text_016);",
                    "div_003.appendChild(h2_001);",
                    "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_017);",
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                    "p_001.appendChild(text_018);",
                    "div_003.appendChild(p_001);",
                    "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_019);",
                    "%s img_002 = document.createElement('img');".formatted(keyword),
                    "img_002.setAttribute(`src`, ``);",
                    "img_002.setAttribute(`alt`, ``);",
                    "div_003.appendChild(img_002);",
                    "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                    "div_003.appendChild(text_020);",
                    "div_001.appendChild(div_003);",
                    "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_021);",
                    "%s div_004 = document.createElement('div');".formatted(keyword),
                    "div_004.setAttribute(`id`, `footer`);",
                    "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                    "div_004.appendChild(text_022);",
                    "%s p_002 = document.createElement('p');".formatted(keyword),
                    "%s text_023 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                    "p_002.appendChild(text_023);",
                    "div_004.appendChild(p_002);",
                    "%s text_024 = document.createTextNode(`            `);".formatted(keyword),
                    "div_004.appendChild(text_024);",
                    "div_001.appendChild(div_004);",
                    "%s text_025 = document.createTextNode(`        `);".formatted(keyword),
                    "div_001.appendChild(text_025);",
                    "body_001.appendChild(div_001);",
                    "%s text_026 = document.createTextNode(`    `);".formatted(keyword),
                    "body_001.appendChild(text_026);",
                    "html_001.appendChild(body_001);",
                    "targetElement_001.appendChild(html_001);");

        } else {

            assertThat(converted).containsExactly(

                    "%s html_001 = document.createElement('html');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                    "html_001.appendChild(text_001);",
                    "%s head_001 = document.createElement('head');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_002);",
                    "%s meta_001 = document.createElement('meta');".formatted(keyword),
                    "meta_001.setAttribute(`charset`, `utf-8`);",
                    "head_001.appendChild(meta_001);",
                    "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_003);",
                    "%s title_001 = document.createElement('title');".formatted(keyword),
                    "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                    "title_001.appendChild(text_004);",
                    "head_001.appendChild(title_001);",
                    "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                    "head_001.appendChild(text_005);",
                    "%s link_001 = document.createElement('link');".formatted(keyword),
                    "link_001.setAttribute(`rel`, `stylesheet`);",
                    "link_001.setAttribute(`href`, ``);",
                    "head_001.appendChild(link_001);",
                    "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                    "head_001.appendChild(text_006);",
                    "html_001.appendChild(head_001);",
                    "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                    "html_001.appendChild(text_007);",
                    "%s body_001 = document.createElement('body');".formatted(keyword),
                    "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                    "body_001.appendChild(text_008);",
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "div_001.setAttribute(`id`, `container`);",
                    "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_009);",
                    "%s div_002 = document.createElement('div');".formatted(keyword),
                    "div_002.setAttribute(`id`, `header`);",
                    "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                    "div_002.appendChild(text_010);",
                    "%s h1_001 = document.createElement('h1');".formatted(keyword),
                    "%s text_011 = document.createTextNode(`Sample`);".formatted(keyword),
                    "h1_001.appendChild(text_011);",
                    "div_002.appendChild(h1_001);",
                    "%s text_012 = document.createTextNode(`                `);".formatted(keyword),
                    "div_002.appendChild(text_012);",
                    "%s img_001 = document.createElement('img');".formatted(keyword),
                    "img_001.setAttribute(`src`, `kanye.jpg`);",
                    "img_001.setAttribute(`alt`, `kanye`);",
                    "div_002.appendChild(img_001);",
                    "%s text_013 = document.createTextNode(`            `);".formatted(keyword),
                    "div_002.appendChild(text_013);",
                    "div_001.appendChild(div_002);",
                    "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_014);",
                    "%s div_003 = document.createElement('div');".formatted(keyword),
                    "div_003.setAttribute(`id`, `main`);",
                    "%s text_015 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_015);",
                    "%s h2_001 = document.createElement('h2');".formatted(keyword),
                    "%s text_016 = document.createTextNode(`Main`);".formatted(keyword),
                    "h2_001.appendChild(text_016);",
                    "div_003.appendChild(h2_001);",
                    "%s text_017 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_017);",
                    "%s p_001 = document.createElement('p');".formatted(keyword),
                    "%s text_018 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                    "p_001.appendChild(text_018);",
                    "div_003.appendChild(p_001);",
                    "%s text_019 = document.createTextNode(`                `);".formatted(keyword),
                    "div_003.appendChild(text_019);",
                    "%s img_002 = document.createElement('img');".formatted(keyword),
                    "img_002.setAttribute(`src`, ``);",
                    "img_002.setAttribute(`alt`, ``);",
                    "div_003.appendChild(img_002);",
                    "%s text_020 = document.createTextNode(`            `);".formatted(keyword),
                    "div_003.appendChild(text_020);",
                    "div_001.appendChild(div_003);",
                    "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_021);",
                    "%s div_004 = document.createElement('div');".formatted(keyword),
                    "div_004.setAttribute(`id`, `footer`);",
                    "%s text_022 = document.createTextNode(`                `);".formatted(keyword),
                    "div_004.appendChild(text_022);",
                    "%s p_002 = document.createElement('p');".formatted(keyword),
                    "%s text_023 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                    "p_002.appendChild(text_023);",
                    "div_004.appendChild(p_002);",
                    "%s text_024 = document.createTextNode(`            `);".formatted(keyword),
                    "div_004.appendChild(text_024);",
                    "div_001.appendChild(div_004);",
                    "%s text_025 = document.createTextNode(`        `);".formatted(keyword),
                    "div_001.appendChild(text_025);",
                    "body_001.appendChild(div_001);",
                    "%s text_026 = document.createTextNode(`    `);".formatted(keyword),
                    "body_001.appendChild(text_026);",
                    "html_001.appendChild(body_001);");
        }

    }

    //The way we define encoding utf8 is very important to solve the issue we get at Rest API Level
    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAddedAndCommentConversionModeActivated")
    void produceValidCodeWhenGivenPathToAFileWithComment(VariableDeclaration variableDeclaration, final boolean querySelectorAdded, final boolean commentConversionModeActivated) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded, commentConversionModeActivated);
        //noinspection resource,ConstantConditions
        final var input = getClass().getClassLoader()
                .getResourceAsStream("htmlFilesInput/sampleWithComment.html")
                .readAllBytes();
        final var converted = convert(new String(input, UTF_8), configuration);

        printConverted(converted);


        // TODO: This doesn't make sense and deserve an issue to fix:
        //       html > body > html > body > ...
        //       Same would be true for doctype child of body

        if (querySelectorAdded) {

            if(commentConversionModeActivated){

                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s html_001 = document.createElement('html');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_001);",
                        "%s head_001 = document.createElement('head');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_002);",
                        "%s meta_001 = document.createElement('meta');".formatted(keyword),
                        "meta_001.setAttribute(`charset`, `utf-8`);",
                        "head_001.appendChild(meta_001);",
                        "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_003);",
                        "%s title_001 = document.createElement('title');".formatted(keyword),
                        "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                        "title_001.appendChild(text_004);",
                        "head_001.appendChild(title_001);",
                        "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_005);",
                        "%s link_001 = document.createElement('link');".formatted(keyword),
                        "link_001.setAttribute(`rel`, `stylesheet`);",
                        "link_001.setAttribute(`href`, ``);",
                        "head_001.appendChild(link_001);",
                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                        "head_001.appendChild(text_006);",
                        "html_001.appendChild(head_001);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_007);",
                        "%s body_001 = document.createElement('body');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                        "body_001.appendChild(text_008);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `container`);",
                        "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_009);",
                        "%s div_002 = document.createElement('div');".formatted(keyword),
                        "div_002.setAttribute(`id`, `header`);",
                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_010);",
                        "%s comment_001 = document.createComment(` Sample H1 `);".formatted(keyword),
                        "div_002.appendChild(comment_001);",
                        "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_011);",
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_012 = document.createTextNode(`Sample`);".formatted(keyword),
                        "h1_001.appendChild(text_012);",
                        "div_002.appendChild(h1_001);",
                        "%s text_013 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_013);",
                        "%s img_001 = document.createElement('img');".formatted(keyword),
                        "img_001.setAttribute(`src`, `kanye.jpg`);",
                        "img_001.setAttribute(`alt`, `kanye`);",
                        "div_002.appendChild(img_001);",
                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                        "div_002.appendChild(text_014);",
                        "div_001.appendChild(div_002);",
                        "%s text_015 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_015);",
                        "%s div_003 = document.createElement('div');".formatted(keyword),
                        "div_003.setAttribute(`id`, `main`);",
                        "%s text_016 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_016);",
                        "%s h2_001 = document.createElement('h2');".formatted(keyword),
                        "%s text_017 = document.createTextNode(`Main`);".formatted(keyword),
                        "h2_001.appendChild(text_017);",
                        "div_003.appendChild(h2_001);",
                        "%s text_018 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_018);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "%s text_019 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                        "p_001.appendChild(text_019);",
                        "div_003.appendChild(p_001);",
                        "%s text_020 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_020);",
                        "%s img_002 = document.createElement('img');".formatted(keyword),
                        "img_002.setAttribute(`src`, ``);",
                        "img_002.setAttribute(`alt`, ``);",
                        "div_003.appendChild(img_002);",
                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                        "div_003.appendChild(text_021);",
                        "div_001.appendChild(div_003);",
                        "%s text_022 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_022);",
                        "%s div_004 = document.createElement('div');".formatted(keyword),
                        "div_004.setAttribute(`id`, `footer`);",
                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_023);",
                        "%s comment_002 = document.createComment(` Copyright `);".formatted(keyword),
                        "div_004.appendChild(comment_002);",
                        "%s text_024 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_024);",
                        "%s p_002 = document.createElement('p');".formatted(keyword),
                        "%s text_025 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                        "p_002.appendChild(text_025);",
                        "div_004.appendChild(p_002);",
                        "%s text_026 = document.createTextNode(`            `);".formatted(keyword),
                        "div_004.appendChild(text_026);",
                        "div_001.appendChild(div_004);",
                        "%s text_027 = document.createTextNode(`        `);".formatted(keyword),
                        "div_001.appendChild(text_027);",
                        "body_001.appendChild(div_001);",
                        "%s text_028 = document.createTextNode(`    `);".formatted(keyword),
                        "body_001.appendChild(text_028);",
                        "html_001.appendChild(body_001);",
                        "targetElement_001.appendChild(html_001);");


            }else{

                assertThat(converted).containsExactly(
                        "%s targetElement_001 = document.querySelector(`:root > body`);".formatted(keyword),
                        "%s html_001 = document.createElement('html');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_001);",
                        "%s head_001 = document.createElement('head');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_002);",
                        "%s meta_001 = document.createElement('meta');".formatted(keyword),
                        "meta_001.setAttribute(`charset`, `utf-8`);",
                        "head_001.appendChild(meta_001);",
                        "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_003);",
                        "%s title_001 = document.createElement('title');".formatted(keyword),
                        "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                        "title_001.appendChild(text_004);",
                        "head_001.appendChild(title_001);",
                        "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_005);",
                        "%s link_001 = document.createElement('link');".formatted(keyword),
                        "link_001.setAttribute(`rel`, `stylesheet`);",
                        "link_001.setAttribute(`href`, ``);",
                        "head_001.appendChild(link_001);",
                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                        "head_001.appendChild(text_006);",
                        "html_001.appendChild(head_001);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_007);",
                        "%s body_001 = document.createElement('body');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                        "body_001.appendChild(text_008);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `container`);",
                        "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_009);",
                        "%s div_002 = document.createElement('div');".formatted(keyword),
                        "div_002.setAttribute(`id`, `header`);",
                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_010);",
                        "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_011);",
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_012 = document.createTextNode(`Sample`);".formatted(keyword),
                        "h1_001.appendChild(text_012);",
                        "div_002.appendChild(h1_001);",
                        "%s text_013 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_013);",
                        "%s img_001 = document.createElement('img');".formatted(keyword),
                        "img_001.setAttribute(`src`, `kanye.jpg`);",
                        "img_001.setAttribute(`alt`, `kanye`);",
                        "div_002.appendChild(img_001);",
                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                        "div_002.appendChild(text_014);",
                        "div_001.appendChild(div_002);",
                        "%s text_015 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_015);",
                        "%s div_003 = document.createElement('div');".formatted(keyword),
                        "div_003.setAttribute(`id`, `main`);",
                        "%s text_016 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_016);",
                        "%s h2_001 = document.createElement('h2');".formatted(keyword),
                        "%s text_017 = document.createTextNode(`Main`);".formatted(keyword),
                        "h2_001.appendChild(text_017);",
                        "div_003.appendChild(h2_001);",
                        "%s text_018 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_018);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "%s text_019 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                        "p_001.appendChild(text_019);",
                        "div_003.appendChild(p_001);",
                        "%s text_020 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_020);",
                        "%s img_002 = document.createElement('img');".formatted(keyword),
                        "img_002.setAttribute(`src`, ``);",
                        "img_002.setAttribute(`alt`, ``);",
                        "div_003.appendChild(img_002);",
                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                        "div_003.appendChild(text_021);",
                        "div_001.appendChild(div_003);",
                        "%s text_022 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_022);",
                        "%s div_004 = document.createElement('div');".formatted(keyword),
                        "div_004.setAttribute(`id`, `footer`);",
                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_023);",
                        "%s text_024 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_024);",
                        "%s p_002 = document.createElement('p');".formatted(keyword),
                        "%s text_025 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                        "p_002.appendChild(text_025);",
                        "div_004.appendChild(p_002);",
                        "%s text_026 = document.createTextNode(`            `);".formatted(keyword),
                        "div_004.appendChild(text_026);",
                        "div_001.appendChild(div_004);",
                        "%s text_027 = document.createTextNode(`        `);".formatted(keyword),
                        "div_001.appendChild(text_027);",
                        "body_001.appendChild(div_001);",
                        "%s text_028 = document.createTextNode(`    `);".formatted(keyword),
                        "body_001.appendChild(text_028);",
                        "html_001.appendChild(body_001);",
                        "targetElement_001.appendChild(html_001);");


            }


        } else {

            if(commentConversionModeActivated){

                assertThat(converted).containsExactly(
                        "%s html_001 = document.createElement('html');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_001);",
                        "%s head_001 = document.createElement('head');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_002);",
                        "%s meta_001 = document.createElement('meta');".formatted(keyword),
                        "meta_001.setAttribute(`charset`, `utf-8`);",
                        "head_001.appendChild(meta_001);",
                        "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_003);",
                        "%s title_001 = document.createElement('title');".formatted(keyword),
                        "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                        "title_001.appendChild(text_004);",
                        "head_001.appendChild(title_001);",
                        "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_005);",
                        "%s link_001 = document.createElement('link');".formatted(keyword),
                        "link_001.setAttribute(`rel`, `stylesheet`);",
                        "link_001.setAttribute(`href`, ``);",
                        "head_001.appendChild(link_001);",
                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                        "head_001.appendChild(text_006);",
                        "html_001.appendChild(head_001);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_007);",
                        "%s body_001 = document.createElement('body');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                        "body_001.appendChild(text_008);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `container`);",
                        "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_009);",
                        "%s div_002 = document.createElement('div');".formatted(keyword),
                        "div_002.setAttribute(`id`, `header`);",
                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_010);",
                        "%s comment_001 = document.createComment(` Sample H1 `);".formatted(keyword),
                        "div_002.appendChild(comment_001);",
                        "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_011);",
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_012 = document.createTextNode(`Sample`);".formatted(keyword),
                        "h1_001.appendChild(text_012);",
                        "div_002.appendChild(h1_001);",
                        "%s text_013 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_013);",
                        "%s img_001 = document.createElement('img');".formatted(keyword),
                        "img_001.setAttribute(`src`, `kanye.jpg`);",
                        "img_001.setAttribute(`alt`, `kanye`);",
                        "div_002.appendChild(img_001);",
                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                        "div_002.appendChild(text_014);",
                        "div_001.appendChild(div_002);",
                        "%s text_015 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_015);",
                        "%s div_003 = document.createElement('div');".formatted(keyword),
                        "div_003.setAttribute(`id`, `main`);",
                        "%s text_016 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_016);",
                        "%s h2_001 = document.createElement('h2');".formatted(keyword),
                        "%s text_017 = document.createTextNode(`Main`);".formatted(keyword),
                        "h2_001.appendChild(text_017);",
                        "div_003.appendChild(h2_001);",
                        "%s text_018 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_018);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "%s text_019 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                        "p_001.appendChild(text_019);",
                        "div_003.appendChild(p_001);",
                        "%s text_020 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_020);",
                        "%s img_002 = document.createElement('img');".formatted(keyword),
                        "img_002.setAttribute(`src`, ``);",
                        "img_002.setAttribute(`alt`, ``);",
                        "div_003.appendChild(img_002);",
                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                        "div_003.appendChild(text_021);",
                        "div_001.appendChild(div_003);",
                        "%s text_022 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_022);",
                        "%s div_004 = document.createElement('div');".formatted(keyword),
                        "div_004.setAttribute(`id`, `footer`);",
                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_023);",
                        "%s comment_002 = document.createComment(` Copyright `);".formatted(keyword),
                        "div_004.appendChild(comment_002);",
                        "%s text_024 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_024);",
                        "%s p_002 = document.createElement('p');".formatted(keyword),
                        "%s text_025 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                        "p_002.appendChild(text_025);",
                        "div_004.appendChild(p_002);",
                        "%s text_026 = document.createTextNode(`            `);".formatted(keyword),
                        "div_004.appendChild(text_026);",
                        "div_001.appendChild(div_004);",
                        "%s text_027 = document.createTextNode(`        `);".formatted(keyword),
                        "div_001.appendChild(text_027);",
                        "body_001.appendChild(div_001);",
                        "%s text_028 = document.createTextNode(`    `);".formatted(keyword),
                        "body_001.appendChild(text_028);",
                        "html_001.appendChild(body_001);");


            }else{

                assertThat(converted).containsExactly("%s html_001 = document.createElement('html');".formatted(keyword),
                        "%s text_001 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_001);",
                        "%s head_001 = document.createElement('head');".formatted(keyword),
                        "%s text_002 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_002);",
                        "%s meta_001 = document.createElement('meta');".formatted(keyword),
                        "meta_001.setAttribute(`charset`, `utf-8`);",
                        "head_001.appendChild(meta_001);",
                        "%s text_003 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_003);",
                        "%s title_001 = document.createElement('title');".formatted(keyword),
                        "%s text_004 = document.createTextNode(`Sample`);".formatted(keyword),
                        "title_001.appendChild(text_004);",
                        "head_001.appendChild(title_001);",
                        "%s text_005 = document.createTextNode(`        `);".formatted(keyword),
                        "head_001.appendChild(text_005);",
                        "%s link_001 = document.createElement('link');".formatted(keyword),
                        "link_001.setAttribute(`rel`, `stylesheet`);",
                        "link_001.setAttribute(`href`, ``);",
                        "head_001.appendChild(link_001);",
                        "%s text_006 = document.createTextNode(`    `);".formatted(keyword),
                        "head_001.appendChild(text_006);",
                        "html_001.appendChild(head_001);",
                        "%s text_007 = document.createTextNode(`    `);".formatted(keyword),
                        "html_001.appendChild(text_007);",
                        "%s body_001 = document.createElement('body');".formatted(keyword),
                        "%s text_008 = document.createTextNode(`        `);".formatted(keyword),
                        "body_001.appendChild(text_008);",
                        "%s div_001 = document.createElement('div');".formatted(keyword),
                        "div_001.setAttribute(`id`, `container`);",
                        "%s text_009 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_009);",
                        "%s div_002 = document.createElement('div');".formatted(keyword),
                        "div_002.setAttribute(`id`, `header`);",
                        "%s text_010 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_010);",
                        "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_011);",
                        "%s h1_001 = document.createElement('h1');".formatted(keyword),
                        "%s text_012 = document.createTextNode(`Sample`);".formatted(keyword),
                        "h1_001.appendChild(text_012);",
                        "div_002.appendChild(h1_001);",
                        "%s text_013 = document.createTextNode(`                `);".formatted(keyword),
                        "div_002.appendChild(text_013);",
                        "%s img_001 = document.createElement('img');".formatted(keyword),
                        "img_001.setAttribute(`src`, `kanye.jpg`);",
                        "img_001.setAttribute(`alt`, `kanye`);",
                        "div_002.appendChild(img_001);",
                        "%s text_014 = document.createTextNode(`            `);".formatted(keyword),
                        "div_002.appendChild(text_014);",
                        "div_001.appendChild(div_002);",
                        "%s text_015 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_015);",
                        "%s div_003 = document.createElement('div');".formatted(keyword),
                        "div_003.setAttribute(`id`, `main`);",
                        "%s text_016 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_016);",
                        "%s h2_001 = document.createElement('h2');".formatted(keyword),
                        "%s text_017 = document.createTextNode(`Main`);".formatted(keyword),
                        "h2_001.appendChild(text_017);",
                        "div_003.appendChild(h2_001);",
                        "%s text_018 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_018);",
                        "%s p_001 = document.createElement('p');".formatted(keyword),
                        "%s text_019 = document.createTextNode(`This is the main content.`);".formatted(keyword),
                        "p_001.appendChild(text_019);",
                        "div_003.appendChild(p_001);",
                        "%s text_020 = document.createTextNode(`                `);".formatted(keyword),
                        "div_003.appendChild(text_020);",
                        "%s img_002 = document.createElement('img');".formatted(keyword),
                        "img_002.setAttribute(`src`, ``);",
                        "img_002.setAttribute(`alt`, ``);",
                        "div_003.appendChild(img_002);",
                        "%s text_021 = document.createTextNode(`            `);".formatted(keyword),
                        "div_003.appendChild(text_021);",
                        "div_001.appendChild(div_003);",
                        "%s text_022 = document.createTextNode(`            `);".formatted(keyword),
                        "div_001.appendChild(text_022);",
                        "%s div_004 = document.createElement('div');".formatted(keyword),
                        "div_004.setAttribute(`id`, `footer`);",
                        "%s text_023 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_023);",
                        "%s text_024 = document.createTextNode(`                `);".formatted(keyword),
                        "div_004.appendChild(text_024);",
                        "%s p_002 = document.createElement('p');".formatted(keyword),
                        "%s text_025 = document.createTextNode(`Copyright © 2019`);".formatted(keyword),
                        "p_002.appendChild(text_025);",
                        "div_004.appendChild(p_002);",
                        "%s text_026 = document.createTextNode(`            `);".formatted(keyword),
                        "div_004.appendChild(text_026);",
                        "div_001.appendChild(div_004);",
                        "%s text_027 = document.createTextNode(`        `);".formatted(keyword),
                        "div_001.appendChild(text_027);",
                        "body_001.appendChild(div_001);",
                        "%s text_028 = document.createTextNode(`    `);".formatted(keyword),
                        "body_001.appendChild(text_028);",
                        "html_001.appendChild(body_001);");

            }

        }

    }


    /**
     * A helper method to work with language-native String and array of data structures.
     *
     * @param input         The input HTML string
     * @param configuration The object related to variable declaration (let, const or var) and query selector
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

    private void printConverted(String[] s) {


        String convertedString = String.join("\n", s);

        String space = """
                                
                -------------------------------------------------------------------
                                
                                
                """;

        logger.info(space + convertedString + space);
    }
}