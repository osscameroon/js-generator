
![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/osscameroon/js-generator/branch/main/graph/badge.svg?token=QJEBIRY8JJ)](https://codecov.io/gh/osscameroon/js-generator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Contributors](https://img.shields.io/github/contributors-anon/osscameroon/js-generator)](https://github.com/osscameroon/js-generator/graphs/contributors)


# table of content
- [about the project](#about-the-project)
- [Getting started](#getting-started)
- [tools](#tools)
- [contribute](#contribute)


# About the project

Translating from HTML to JS.

> This project is different from the
> [JavaScript Generator Object](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Generator).
 
The goal is to generate JS  from HTML  following the JavaScript DOM structure. Sometimes, we forget how to write
JavaScript to build dynamic web apps. Even if we know JS, it happens that we don't always have enough time to generate
JS from a big HTML code. Thus, the goal of this project is helping developers gaining time by producing JS code as
Output based on HTML as Input. This project will be very useful for beginners learning HTML and JavaScript. Also, it
will help more experienced developers whenever they want to use JS instead of HTML.

<del>The project is based on [jsoup  library, a Java HTML parser](https://jsoup.org/) /
[jsoup GitHub Repository](https://github.com/jhy/jsoup/). It's all about using
[Nodes](https://github.com/jhy/jsoup/blob/master/src/main/java/org/jsoup/nodes/Node.java)
(not Node JS) to generate JavaScript.</del>


## Getting started

**CLI**

jsgenerator has several options that can be used in a console here is an example of use below

```shell
$ jsgenerator --tty --inline '<div>I am a <strong>tea pot</strong>'

let div_000 = document.createElement('div');
let text_000 = document.createTextNode(`I am a `);
div_000.appendChild(text_000);

let strong_000 = document.createElement('strong');
let text_001 = document.createTextNode(`tea pot`);
strong_000.appendChild(text_001);
div_000.appendChild(strong_000);
document.appendChild(div_000);
```


```text
Usage: jsgenerator [-htV] [-e=<extension>] [--inline-pattern=<inlinePattern>]
                   [--path-pattern=<pathPattern>]
                   [--stdin-pattern=<stdinPattern>] [-i=<inlineContents>...]...
                   [--] [<paths>...]
Translating files, stdin or inline from HTML to JS
      [<paths>...]        file paths to translate content, parsed as HTML
  -e, --ext=<extension>   output files' extension
  -h, --help              Show this help message and exit.
  -i, --inline=<inlineContents>...
                          args as HTML content, not files
      --inline-pattern=<inlinePattern>
                          Pattern for inline output filename
      --path-pattern=<pathPattern>
                          pattern for path-based output filenames
      --stdin-pattern=<stdinPattern>
                          pattern for stdin output filenames
  -t, --tty               output to stdin, not files
  -V, --version           Print version information and exit.
```

**WEB**

> ***TODO:** Not yet implemented.*

**DESKTOP**

> ***TODO:** Not yet implemented.*

**CODE API**

> ***TODO:** Not yet implemented.*
> See [Wiki](https://github.com/osscameroon/js-generator/wiki).

# Tools

+ Java 11
+ Maven 3
+ Spring 5.3.22
+ Spring Boot 2.7.3

# Why is the js generator useful:

1- It will be really useful for beginners learning html, css and Js .

2- More experienced devs will like it too. [@sherlockwisdom](https://github.com/sherlockwisdom) shared why he needed such tool:
<details>
 <summary>Comment</summary>
😂 Yes it's hard to say why it's important. I was working on an Android based app, but was building it with Vanilla JavaScript. So I needed a quick way to turn bootstrap code into Vanilla Js objects so that I could do what ReactJS does now 🤣. This was ~4 years back. Not sure of its relevance now, but they could be some. 😅 Sorry if I rather made things not easy for you to explain.
</details>
This is an example of html to js converter (https://wtools.io/html-to-javascript-converter), very useful in applications where the code must be dynamically generated.

Why are we building another converter if there are other available converters ? First, we want to use let or var instead of document.write as you might see how some converters do. Secondly, the user is able to convert pure HTML 5 tags or custom tags. Thirdly, we are not only building a web app but a family of Apps / APIs: Maven & Gradle Library, REST APIs, Web & Desktop Apps and Command Line Interface.

# Contribute

We are happy with every contribution,please have a look to our [contribution guidelines](CONTRIBUTING.md)


Hello World, all your contributions are welcome! Don't hesitate to open an issue on this repository and/or create a pull
request (PR). In order to create a PR, just fork first.

Thanks for your commitment, we really appreciate! 
Happy Coding! 😊🎉💯

<div align="center">
    <img src="https://forthebadge.com/images/badges/built-with-love.svg" />
    <img src="https://forthebadge.com/images/badges/built-by-developers.svg" />
</div>
