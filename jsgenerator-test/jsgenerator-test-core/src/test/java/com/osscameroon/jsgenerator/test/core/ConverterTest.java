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

@ExtendWith(MockitoExtension.class)
public class ConverterTest {
    private static final Logger logger = getLogger(ConverterTest.class);
    private Converter converter;

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

    /*
    * The goal is to show how precise is our conversion compared to other websites such as:
    *
    * https://www.html-code-generator.com/html/html-code-convert-to-javascript
    *
    * https://wtools.io/html-to-javascript-converter
    *
    * TODO: Add javadoc with the results coming from these 2 sites
    * */
    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void trying1(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
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


//        let targetElement_000 = document.querySelector(`:root > body`);
//        let h1_000 = document.createElement('h1');
//        let text_000 = document.createTextNode(`HTML To JavaScript`);
//        h1_000.appendChild(text_000);
//        targetElement_000.appendChild(h1_000);
//        let text_001 = document.createTextNode(`                                             `);
//        targetElement_000.appendChild(text_001);
//        let ol_000 = document.createElement('ol');
//        let text_002 = document.createTextNode(`                                                 `);
//        ol_000.appendChild(text_002);
//        let li_000 = document.createElement('li');
//        let text_003 = document.createTextNode(`First item`);
//        li_000.appendChild(text_003);
//        ol_000.appendChild(li_000);
//        let text_004 = document.createTextNode(`                                                 `);
//        ol_000.appendChild(text_004);
//        let li_001 = document.createElement('li');
//        let text_005 = document.createTextNode(`Second item`);
//        li_001.appendChild(text_005);
//        ol_000.appendChild(li_001);
//        let text_006 = document.createTextNode(`                                                 `);
//        ol_000.appendChild(text_006);
//        let li_002 = document.createElement('li');
//        let text_007 = document.createTextNode(`Third item`);
//        li_002.appendChild(text_007);
//        ol_000.appendChild(li_002);
//        let text_008 = document.createTextNode(`                                                 `);
//        ol_000.appendChild(text_008);
//        let li_003 = document.createElement('li');
//        let text_009 = document.createTextNode(`Fourth item`);
//        li_003.appendChild(text_009);
//        ol_000.appendChild(li_003);
//        let text_010 = document.createTextNode(`                                             `);
//        ol_000.appendChild(text_010);
//        targetElement_000.appendChild(ol_000);


        if (querySelectorAdded) {

//            assertThat(converted).containsExactly(
//                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
//                    "%s custom_angulartext_000 = document.createElement('AngularText');".formatted(keyword),
//                    "%s text_000 = document.createTextNode(`Angular`);".formatted(keyword),
//                    "custom_angulartext_000.appendChild(text_000);",
//                    "targetElement_000.appendChild(custom_angulartext_000);",
//                    "%s custom_web_component_000 = document.createElement('web-component');".formatted(keyword),
//                    "%s text_001 = document.createTextNode(`Web Component`);".formatted(keyword),
//                    "custom_web_component_000.appendChild(text_001);",
//                    "targetElement_000.appendChild(custom_web_component_000);");

        } else {

//            assertThat(converted).containsExactly(
//                    "%s custom_angulartext_000 = document.createElement('AngularText');".formatted(keyword),
//                    "%s text_000 = document.createTextNode(`Angular`);".formatted(keyword),
//                    "custom_angulartext_000.appendChild(text_000);",
//
//                    "%s custom_web_component_000 = document.createElement('web-component');".formatted(keyword),
//                    "%s text_001 = document.createTextNode(`Web Component`);".formatted(keyword),
//                    "custom_web_component_000.appendChild(text_001);");


        }


    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void issue145WithCustomTagJavaScriptIdentifiers(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
                        <AngularText>Angular</AngularText>
                        <web-component>Web Component</web-component>""",
                new Configuration(variableDeclaration, querySelectorAdded));

        printConverted(converted);


        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s custom_angulartext_000 = document.createElement('AngularText');".formatted(keyword),
                    "%s text_000 = document.createTextNode(`Angular`);".formatted(keyword),
                    "custom_angulartext_000.appendChild(text_000);",
                    "targetElement_000.appendChild(custom_angulartext_000);",
                    "%s custom_web_component_000 = document.createElement('web-component');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`Web Component`);".formatted(keyword),
                    "custom_web_component_000.appendChild(text_001);",
                    "targetElement_000.appendChild(custom_web_component_000);");

        } else {

            assertThat(converted).containsExactly(
                    "%s custom_angulartext_000 = document.createElement('AngularText');".formatted(keyword),
                    "%s text_000 = document.createTextNode(`Angular`);".formatted(keyword),
                    "custom_angulartext_000.appendChild(text_000);",

                    "%s custom_web_component_000 = document.createElement('web-component');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`Web Component`);".formatted(keyword),
                    "custom_web_component_000.appendChild(text_001);");


        }


    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void issue41WithSelfClosingTags(final VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var converted = convert("""
                        <input type="text">
                        <span>Hello</span>
                        <!-- Hello -->
                        Bye!
                        <script>// no-comment</script>""",
                new Configuration(variableDeclaration, querySelectorAdded));

        printConverted(converted);


        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s input_000 = document.createElement('input');".formatted(keyword),
                    "input_000.setAttribute(`type`, `text`);",
                    "targetElement_000.appendChild(input_000);",
                    "%s span_000 = document.createElement('span');".formatted(keyword),
                    "%s text_000 = document.createTextNode(`Hello`);".formatted(keyword),
                    "span_000.appendChild(text_000);",
                    "targetElement_000.appendChild(span_000);",
                    "%s comment_000 = document.createComment(` Hello `);".formatted(keyword),
                    "targetElement_000.appendChild(comment_000);",
                    "%s text_001 = document.createTextNode(`Bye!`);".formatted(keyword),
                    "targetElement_000.appendChild(text_001);",

                    "%s script_000 = document.createElement('script');".formatted(keyword),
                    "script_000.type = `text/javascript`;",
                    "try {",
                    "%s text_002 = document.createTextNode(`// no-comment`);".formatted(keyword),
                    "script_000.appendChild(text_002);",
                    "targetElement_000.appendChild(script_000);",
                    "} catch (_) {",
                    "script_000.text = `// no-comment`;",
                    "targetElement_000.appendChild(script_000);",
                    "}");

        } else {

            assertThat(converted).containsExactly(
                    "%s input_000 = document.createElement('input');".formatted(keyword),
                    "input_000.setAttribute(`type`, `text`);",

                    "%s span_000 = document.createElement('span');".formatted(keyword),
                    "%s text_000 = document.createTextNode(`Hello`);".formatted(keyword),
                    "span_000.appendChild(text_000);",

                    "%s comment_000 = document.createComment(` Hello `);".formatted(keyword),

                    "%s text_001 = document.createTextNode(`Bye!`);".formatted(keyword),


                    "%s script_000 = document.createElement('script');".formatted(keyword),
                    "script_000.type = `text/javascript`;",
                    "try {",
                    "%s text_002 = document.createTextNode(`// no-comment`);".formatted(keyword),
                    "script_000.appendChild(text_002);",

                    "} catch (_) {",
                    "script_000.text = `// no-comment`;",

                    "}");


        }

    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenCDATA(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<![CDATA[ < %s > & ]]>".formatted(token), configuration);


        printConverted(converted);

        if (querySelectorAdded) {
            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s text_000 = document.createTextNode(` < %s > & `);".formatted(keyword, token),
                    "targetElement_000.appendChild(text_000);");

        } else {

            assertThat(converted).containsExactly(
                    "%s text_000 = document.createTextNode(` < %s > & `);".formatted(keyword, token)
            );

        }


    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenPlainText(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert(token, configuration);


        printConverted(converted);

        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, token),
                    "targetElement_000.appendChild(text_000);");

        } else {

            assertThat(converted).containsExactly(
                    "%s text_000 = document.createTextNode(`%s`);".formatted(keyword, token));


        }

    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenComment(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<!-- %s -->".formatted(token), configuration);

        printConverted(converted);


        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token),
                    "targetElement_000.appendChild(comment_000);");

        } else {

            assertThat(converted).containsExactly(

                    "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token));

        }


    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenScript(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<script>console.log(`%s`)</script>".formatted(token), configuration);

        printConverted(converted);

        if (querySelectorAdded) {

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

        } else {

            assertThat(converted).containsExactly(

                    "%s script_000 = document.createElement('script');".formatted(keyword),
                    "script_000.type = `text/javascript`;",
                    "try {",
                    "%s text_000 = document.createTextNode(`console.log(\\`%s\\`)`);".formatted(keyword, token),
                    "script_000.appendChild(text_000);",

                    "} catch (_) {",
                    "script_000.text = `console.log(\\`%s\\`)`;".formatted(token),

                    "}");


        }


    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenTagWithAttributes(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);

        if (querySelectorAdded) {

            printConverted(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration, true)));

            assertThat(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration, true))).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s div_000 = document.createElement('div');".formatted(keyword),
                    "div_000.setAttribute(`id`, `id-value`);",
                    "targetElement_000.appendChild(div_000);");

            printConverted(convert("<details open></details>", new Configuration(variableDeclaration, true)));


            assertThat(convert("<details open></details>", new Configuration(variableDeclaration, true))).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s details_000 = document.createElement('details');".formatted(keyword),
                    "details_000.setAttribute(`open`, `true`);",
                    "targetElement_000.appendChild(details_000);");

            printConverted(convert("<p class></p>", new Configuration(variableDeclaration, true)));


            assertThat(convert("<p class></p>", new Configuration(variableDeclaration, true))).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s p_000 = document.createElement('p');".formatted(keyword),
                    "p_000.setAttribute(`class`, ``);",
                    "targetElement_000.appendChild(p_000);");

        } else {

            printConverted(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration, false)));

            assertThat(convert("<div id=\"id-value\"></div>", new Configuration(variableDeclaration, false))).containsExactly(

                    "%s div_000 = document.createElement('div');".formatted(keyword),
                    "div_000.setAttribute(`id`, `id-value`);");

            printConverted(convert("<details open></details>", new Configuration(variableDeclaration, false)));

            assertThat(convert("<details open></details>", new Configuration(variableDeclaration, false))).containsExactly(

                    "%s details_000 = document.createElement('details');".formatted(keyword),
                    "details_000.setAttribute(`open`, `true`);");

            printConverted(convert("<p class></p>", new Configuration(variableDeclaration, false)));

            assertThat(convert("<p class></p>", new Configuration(variableDeclaration, false))).containsExactly(

                    "%s p_000 = document.createElement('p');".formatted(keyword),
                    "p_000.setAttribute(`class`, ``);");

        }
    }


    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenMultipleNodeAtRoot(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var token = randomUUID().toString();
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<!-- %s --><div></div>".formatted(token), configuration);

        printConverted(converted);


        if (querySelectorAdded) {


            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token),
                    "targetElement_000.appendChild(comment_000);",
                    "%s div_000 = document.createElement('div');".formatted(keyword),
                    "targetElement_000.appendChild(div_000);");

        } else {


            assertThat(converted).containsExactly(

                    "%s comment_000 = document.createComment(` %s `);".formatted(keyword, token),

                    "%s div_000 = document.createElement('div');".formatted(keyword));


        }


    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWithIncrementVariableNameWhenGivenMultipleNodeWithSameTagNames(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert("<div></div><div></div>", configuration);

        printConverted(converted);

        if (querySelectorAdded) {

            assertThat(converted).containsExactly(
                    "%s targetElement_000 = document.querySelector(`:root > body`);".formatted(keyword),
                    "%s div_000 = document.createElement('div');".formatted(keyword),
                    "targetElement_000.appendChild(div_000);",
                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "targetElement_000.appendChild(div_001);");


        } else {

            assertThat(converted).containsExactly(

                    "%s div_000 = document.createElement('div');".formatted(keyword),

                    "%s div_001 = document.createElement('div');".formatted(keyword));


        }


    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenNestedNodes(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        final var converted = convert(
                "<div>A <strong>...</strong></div><div><p>Well, case!</div>", configuration);

        printConverted(converted);

        if (querySelectorAdded) {

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

        } else {

            assertThat(converted).containsExactly(

                    "%s div_000 = document.createElement('div');".formatted(keyword),
                    "%s text_000 = document.createTextNode(`A `);".formatted(keyword),
                    "div_000.appendChild(text_000);",
                    "%s strong_000 = document.createElement('strong');".formatted(keyword),
                    "%s text_001 = document.createTextNode(`...`);".formatted(keyword),
                    "strong_000.appendChild(text_001);",
                    "div_000.appendChild(strong_000);",

                    "%s div_001 = document.createElement('div');".formatted(keyword),
                    "%s p_000 = document.createElement('p');".formatted(keyword),
                    "%s text_002 = document.createTextNode(`Well, case!`);".formatted(keyword),
                    "p_000.appendChild(text_002);",
                    "div_001.appendChild(p_000);");


        }


    }

    @ParameterizedTest
    @MethodSource("provideVariableDeclarationsAndQuerySelectorAdded")
    public void produceValidCodeWhenGivenPathToAFile(VariableDeclaration variableDeclaration, final boolean querySelectorAdded) throws IOException {
        final var keyword = keyword(variableDeclaration);
        final var configuration = new Configuration(variableDeclaration, querySelectorAdded);
        //noinspection resource,ConstantConditions
        final var input = getClass().getClassLoader()
                .getResourceAsStream("htmlFilesInput/sample.html")
                .readAllBytes();
        final var converted = convert(new String(input, UTF_8), configuration);

        printConverted(converted);


        // TODO: This doesn't make sense and deserve and issue to fix:
        //       html > body > html > body > ...
        //       Same would be true for doctype child of body

        if (querySelectorAdded) {

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
                    "%s h1_000 = document.createElement('h1');".formatted(keyword),
                    "%s text_010 = document.createTextNode(`Sample`);".formatted(keyword),
                    "h1_000.appendChild(text_010);",
                    "div_001.appendChild(h1_000);",
                    "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                    "div_001.appendChild(text_011);",
                    "%s img_000 = document.createElement('img');".formatted(keyword),
                    "img_000.setAttribute(`src`, `kanye.jpg`);",
                    "img_000.setAttribute(`alt`, `kanye`);",
                    "div_001.appendChild(img_000);",
                    "%s text_012 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_012);",
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
                    "div_002.appendChild(img_001);",
                    "%s text_019 = document.createTextNode(`            `);".formatted(keyword),
                    "div_002.appendChild(text_019);",
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

        } else {

            assertThat(converted).containsExactly(

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
                    "%s h1_000 = document.createElement('h1');".formatted(keyword),
                    "%s text_010 = document.createTextNode(`Sample`);".formatted(keyword),
                    "h1_000.appendChild(text_010);",
                    "div_001.appendChild(h1_000);",
                    "%s text_011 = document.createTextNode(`                `);".formatted(keyword),
                    "div_001.appendChild(text_011);",
                    "%s img_000 = document.createElement('img');".formatted(keyword),
                    "img_000.setAttribute(`src`, `kanye.jpg`);",
                    "img_000.setAttribute(`alt`, `kanye`);",
                    "div_001.appendChild(img_000);",
                    "%s text_012 = document.createTextNode(`            `);".formatted(keyword),
                    "div_001.appendChild(text_012);",
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
                    "div_002.appendChild(img_001);",
                    "%s text_019 = document.createTextNode(`            `);".formatted(keyword),
                    "div_002.appendChild(text_019);",
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
                    "html_000.appendChild(body_000);");
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
