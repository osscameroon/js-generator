let text_000 = document.createTextNode(`![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)[![codecov](https://codecov.io/gh/osscameroon/js-generator/branch/main/graph/badge.svg?token=QJEBIRY8JJ)](https://codecov.io/gh/osscameroon/js-generator)[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)# jsgenerator : Translating from HTML to JS# Disclaimer**This project is different from the [JavaScript Generator Object](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Generator).**# AboutThe goal is to generate JS  from HTML  following the JavaScript DOM structure.Sometimes, we forget how to write JavaScript to build dynamic web apps. Even if we know JS, it happens that we don't always have enough time to generate JS from a big HTML code. Thus, the goal of this project is helping developers gaining time by producing JS code as Output based on HTML as Input. This project will be very useful for beginners learning HTML and JavaScript. Also, it will help more experienced developers whenever they want to use JS instead of HTML.The project is based on [jsoup  library, a Java HTML parser](https://jsoup.org/) / [jsoup GitHub Repository](https://github.com/jhy/jsoup/). It's all about using [Nodes](https://github.com/jhy/jsoup/blob/master/src/main/java/org/jsoup/nodes/Node.java) (not Node JS) to generate JavaScript. **Actually, there is no release yet, it's just a console program but gradually we will build a Maven/Gradle Library, a Web & Desktop App and a CLI (Command Line Interface) tool translating from HTML to JS.**# Wikihttps://github.com/osscameroon/js-generator/wiki# Project Documentationhttps://osscameroon.github.io/js-generator/# ExampleWe invite you to work on the [example branch code](https://github.com/osscameroon/js-generator/tree/example) to follow this tutorial.You are free to copy and paste into [JSFiddle](https://jsfiddle.net/) or just click on the link provided to see the result.This example gives you a big picture, our [Wiki](https://github.com/osscameroon/js-generator/wiki) provides more explanation on how things work under the hood.Let's suppose we are building an HTML page starting with this initial code [JSFiddle](https://jsfiddle.net/a23j0nxf/):```html`);
document.appendChild(text_000);

let html_000 = document.createElement('html');
let text_001 = document.createTextNode(`  `);
html_000.appendChild(text_001);

let body_000 = document.createElement('body');
let text_002 = document.createTextNode(`    `);
body_000.appendChild(text_002);

let div_000 = document.createElement('div');
div_000.setAttribute(`id`, `divtest`);
let text_003 = document.createTextNode(`    `);
div_000.appendChild(text_003);
body_000.appendChild(div_000);
let text_004 = document.createTextNode(`  `);
body_000.appendChild(text_004);
html_000.appendChild(body_000);
document.appendChild(html_000);
let text_005 = document.createTextNode(````Then, we want to add more data inside the **`div tag with id "divtest"`**. It's possible to do it manually but we don't want that. **Our goal is to make it dynamic with JavaScript. We want to create JavaScript variables that we'll use for whatever we want.**This is the HTML code we'll add to divtest [JSFiddle](https://jsfiddle.net/xyrqa05c/):```html `);
document.appendChild(text_005);

let div_001 = document.createElement('div');
let text_006 = document.createTextNode(`      `);
div_001.appendChild(text_006);

let h1_000 = document.createElement('h1');
let text_007 = document.createTextNode(`Open Source Society Cameroon`);
h1_000.appendChild(text_007);
div_001.appendChild(h1_000);
let text_008 = document.createTextNode(`        `);
div_001.appendChild(text_008);

let p_000 = document.createElement('p');
let text_009 = document.createTextNode(`jsgenerator : Translating from Html to Js`);
p_000.appendChild(text_009);
div_001.appendChild(p_000);
let text_010 = document.createTextNode(`        `);
div_001.appendChild(text_010);

let h2_000 = document.createElement('h2');
let text_011 = document.createTextNode(`About`);
h2_000.appendChild(text_011);
div_001.appendChild(h2_000);
let text_012 = document.createTextNode(`        `);
div_001.appendChild(text_012);

let p_001 = document.createElement('p');
let text_013 = document.createTextNode(`                The goal is to generate Javascript from HTML following the JavaScript DOM structure.           Sometimes, we forget how to use javascript to build dynamic web apps.           Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input.           This project will be very useful for beginners learning html and javascript.    `);
p_001.appendChild(text_013);
div_001.appendChild(p_001);
let text_014 = document.createTextNode(`          `);
div_001.appendChild(text_014);

let p_002 = document.createElement('p');
let text_015 = document.createTextNode(`                The project is based on `);
p_002.appendChild(text_015);

let a_000 = document.createElement('a');
a_000.setAttribute(`href`, `https://jsoup.org/`);
let text_016 = document.createTextNode(`jsoup library, a java html parser`);
a_000.appendChild(text_016);
p_002.appendChild(a_000);
let text_017 = document.createTextNode(` / `);
p_002.appendChild(text_017);

let a_001 = document.createElement('a');
a_001.setAttribute(`href`, `https://github.com/jhy/jsoup/`);
let text_018 = document.createTextNode(`Jsoup GitHub Repository`);
a_001.appendChild(text_018);
p_002.appendChild(a_001);
let text_019 = document.createTextNode(` .It's all about using nodes to generate javascript.          `);
p_002.appendChild(text_019);
div_001.appendChild(p_002);
let text_020 = document.createTextNode(`          `);
div_001.appendChild(text_020);

let p_003 = document.createElement('p');
let text_021 = document.createTextNode(`                Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to  `);
p_003.appendChild(text_021);

let a_002 = document.createElement('a');
a_002.setAttribute(`href`, `https://translate.google.com/`);
let text_022 = document.createTextNode(`Google Translate`);
a_002.appendChild(text_022);
p_003.appendChild(a_002);
let text_023 = document.createTextNode(`.    `);
p_003.appendChild(text_023);
div_001.appendChild(p_003);
let text_024 = document.createTextNode(`      `);
div_001.appendChild(text_024);
document.appendChild(div_001);
let text_025 = document.createTextNode(````Let's go to the [Main Class JSGenerator](https://github.com/osscameroon/js-generator/blob/example/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java), just copy and paste the HTML code into the variable named **html** then run the program.It's possible to choose between **let** or **var** to declare your JavaScript variables as you can see here. We'll use **let** for this example:```java    /*     * Just choose between VAR or LET for your variable declarations     *     * static ConvertService convertService = new     * ConvertServiceImpl(JsVariableDeclaration.VAR);     */    static ConvertService convertService = new ConvertServiceImpl(JSVariableDeclaration.LET);``````java    /**     * Converts built-in code from Html to Js and prints the result.     */    static void convertAndPrintBuiltInCodeFromHtmlToJs() {	// Use log instead of system.out.println to show steps	logger.log(Level.INFO, " **** Converting built-in code from html to js **** ");	logger.log(Level.INFO, " **** Html to convert:  **** ");	// Copy the html code into the variable named html then run the program.	String html = "";	logger.log(Level.INFO, "\n\n" + html + "\n");	logger.log(Level.INFO, " **** generated js:  **** " + "\n");	System.out.println(convertService.convert(html));    }```JS ouput we get after translating from the console:```javascriptlet h1 = document.createElement("h1");h1.appendChild(document.createTextNode("Open Source Society Cameroon "));let p = document.createElement("p");p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js "));let h2 = document.createElement("h2");h2.appendChild(document.createTextNode("About "));let p_ = document.createElement("p");p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure.           Sometimes, we forget how to use javascript to build dynamic web apps.           Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input.           This project will be very useful for beginners learning html and javascript. "));let a = document.createElement("a");a.setAttribute("href", "https://jsoup.org/");a.appendChild(document.createTextNode("jsoup library, a java html parser "));let a_ = document.createElement("a");a_.setAttribute("href", "https://github.com/jhy/jsoup/");a_.appendChild(document.createTextNode("Jsoup GitHub Repository "));let p__ = document.createElement("p");p__.appendChild(document.createTextNode("The project is based on "));p__.appendChild(a);p__.appendChild(document.createTextNode("/ "));p__.appendChild(a_);p__.appendChild(document.createTextNode(".It's all about using nodes to generate javascript. "));let a__ = document.createElement("a");a__.setAttribute("href", "https://translate.google.com/");a__.appendChild(document.createTextNode("Google Translate "));let p___ = document.createElement("p");p___.appendChild(document.createTextNode("Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to "));p___.appendChild(a__);p___.appendChild(document.createTextNode(". "));let div = document.createElement("div");div.appendChild(h1);div.appendChild(p);div.appendChild(h2);div.appendChild(p_);div.appendChild(p__);div.appendChild(p___);```It's time to link our output to our initial HTML code:```javascriptlet divtest = document.getElementById("divtest");divtest.appendChild(div);```In order to test that it works, just compare the results on https://jsfiddle.net/## Expected Result[JSFiddle](https://jsfiddle.net/pyahn1fc/)```html`);
document.appendChild(text_025);

let html_001 = document.createElement('html');
let text_026 = document.createTextNode(`  `);
html_001.appendChild(text_026);

let body_001 = document.createElement('body');
let text_027 = document.createTextNode(`    `);
body_001.appendChild(text_027);

let div_002 = document.createElement('div');
div_002.setAttribute(`id`, `divtest`);
let text_028 = document.createTextNode(`           `);
div_002.appendChild(text_028);

let div_003 = document.createElement('div');
let text_029 = document.createTextNode(`          `);
div_003.appendChild(text_029);

let h1_001 = document.createElement('h1');
let text_030 = document.createTextNode(`Open Source Society Cameroon`);
h1_001.appendChild(text_030);
div_003.appendChild(h1_001);
let text_031 = document.createTextNode(`          `);
div_003.appendChild(text_031);

let p_004 = document.createElement('p');
let text_032 = document.createTextNode(`jsgenerator : Translating from Html to Js`);
p_004.appendChild(text_032);
div_003.appendChild(p_004);
let text_033 = document.createTextNode(`          `);
div_003.appendChild(text_033);

let h2_001 = document.createElement('h2');
let text_034 = document.createTextNode(`About`);
h2_001.appendChild(text_034);
div_003.appendChild(h2_001);
let text_035 = document.createTextNode(`          `);
div_003.appendChild(text_035);

let p_005 = document.createElement('p');
let text_036 = document.createTextNode(`                The goal is to generate Javascript from HTML following the JavaScript DOM structure.                 Sometimes, we forget how to use javascript to build dynamic web apps.                 Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input.                 This project will be very useful for beginners learning html and javascript.          `);
p_005.appendChild(text_036);
div_003.appendChild(p_005);
let text_037 = document.createTextNode(`          `);
div_003.appendChild(text_037);

let p_006 = document.createElement('p');
let text_038 = document.createTextNode(`                The project is based on `);
p_006.appendChild(text_038);

let a_003 = document.createElement('a');
a_003.setAttribute(`href`, `https://jsoup.org/`);
let text_039 = document.createTextNode(`jsoup library, a java html parser`);
a_003.appendChild(text_039);
p_006.appendChild(a_003);
let text_040 = document.createTextNode(` / `);
p_006.appendChild(text_040);

let a_004 = document.createElement('a');
a_004.setAttribute(`href`, `https://github.com/jhy/jsoup/`);
let text_041 = document.createTextNode(`Jsoup GitHub Repository`);
a_004.appendChild(text_041);
p_006.appendChild(a_004);
let text_042 = document.createTextNode(` .It's all about using nodes to generate javascript.          `);
p_006.appendChild(text_042);
div_003.appendChild(p_006);
let text_043 = document.createTextNode(`          `);
div_003.appendChild(text_043);

let p_007 = document.createElement('p');
let text_044 = document.createTextNode(`                Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to  `);
p_007.appendChild(text_044);

let a_005 = document.createElement('a');
a_005.setAttribute(`href`, `https://translate.google.com/`);
let text_045 = document.createTextNode(`Google Translate`);
a_005.appendChild(text_045);
p_007.appendChild(a_005);
let text_046 = document.createTextNode(`.          `);
p_007.appendChild(text_046);
div_003.appendChild(p_007);
let text_047 = document.createTextNode(`      `);
div_003.appendChild(text_047);
div_002.appendChild(div_003);
let text_048 = document.createTextNode(`    `);
div_002.appendChild(text_048);
body_001.appendChild(div_002);
let text_049 = document.createTextNode(`  `);
body_001.appendChild(text_049);
html_001.appendChild(body_001);
document.appendChild(html_001);
let text_050 = document.createTextNode(````## Equivalent Result with the Initial HTML and JS generated code[JSFiddle](https://jsfiddle.net/yaoqt24x/)### Initial HTML Code```html`);
document.appendChild(text_050);

let html_002 = document.createElement('html');
let text_051 = document.createTextNode(`  `);
html_002.appendChild(text_051);

let body_002 = document.createElement('body');
let text_052 = document.createTextNode(`    `);
body_002.appendChild(text_052);

let div_004 = document.createElement('div');
div_004.setAttribute(`id`, `divtest`);
let text_053 = document.createTextNode(`    `);
div_004.appendChild(text_053);
body_002.appendChild(div_004);
let text_054 = document.createTextNode(`  `);
body_002.appendChild(text_054);
html_002.appendChild(body_002);
document.appendChild(html_002);
let text_055 = document.createTextNode(````### JavaScript Generated Code```javascriptlet h1 = document.createElement("h1");h1.appendChild(document.createTextNode("Open Source Society Cameroon "));let p = document.createElement("p");p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js "));let h2 = document.createElement("h2");h2.appendChild(document.createTextNode("About "));let p_ = document.createElement("p");p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure.           Sometimes, we forget how to use javascript to build dynamic web apps.           Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input.           This project will be very useful for beginners learning html and javascript. "));let a = document.createElement("a");a.setAttribute("href", "https://jsoup.org/");a.appendChild(document.createTextNode("jsoup library, a java html parser "));let a_ = document.createElement("a");a_.setAttribute("href", "https://github.com/jhy/jsoup/");a_.appendChild(document.createTextNode("Jsoup GitHub Repository "));let p__ = document.createElement("p");p__.appendChild(document.createTextNode("The project is based on "));p__.appendChild(a);p__.appendChild(document.createTextNode("/ "));p__.appendChild(a_);p__.appendChild(document.createTextNode(".It's all about using nodes to generate javascript. "));let a__ = document.createElement("a");a__.setAttribute("href", "https://translate.google.com/");a__.appendChild(document.createTextNode("Google Translate "));let p___ = document.createElement("p");p___.appendChild(document.createTextNode("Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to "));p___.appendChild(a__);p___.appendChild(document.createTextNode(". "));let div = document.createElement("div");div.appendChild(h1);div.appendChild(p);div.appendChild(h2);div.appendChild(p_);div.appendChild(p__);div.appendChild(p___);// It's time to link our output to our initial html codelet divtest = document.getElementById("divtest");divtest.appendChild(div);```We hope this example makes you better understand this project. In a real project, you might want to use the generated JS code for whatever you want, it's up to you. If not enough, the [Wiki](https://github.com/osscameroon/js-generator/wiki) provides more explanation on how things work under the hood.# Code InfoLanguage: JAVAVersion: 11Build Tool: Maven# Build1. Clone2. Build -> `mvn clean package`3. Browse through code and find [Main Class JSGenerator](https://github.com/osscameroon/js-generator/blob/example/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java)4. Paste HTML code and test... should output some JS for you...# How to ContributeHello World, all your contributions are welcome! Don't hesitate to open an issue on this repository and/or create a pull request (PR).In order to create a PR, just fork first.Thanks for your commitment, we really appreciate! Happy Coding! 😊🎉💯`);
document.appendChild(text_055);
