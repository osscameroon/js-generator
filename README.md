
<p align="center">
<img alt="GitHub" src="https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg">
</p>
<p align="center">
 <a href="/LICENSE">
        <img alt="GitHub" src="https://img.shields.io/github/license/osscameroon/js-generator?color=%2360be86&style=for-the-badge">
    </a>
    <a href="https://github.com/devicons/devicon/graphs/contributors">
        <img alt="GitHub contributors" src="https://img.shields.io/github/contributors-anon/osscameroon/js-generator?color=%2360be86&style=for-the-badge">
    </a>
 </p>

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

# Contribute

We are happy with every contribution,please have a look to our [contribution guidelines](contributing.md)


Hello World, all your contributions are welcome! Don't hesitate to open an issue on this repository and/or create a pull
request (PR). In order to create a PR, just fork first.

Thanks for your commitment, we really appreciate! 
Happy Coding! 😊🎉💯

<div align="center">
    <img src="https://forthebadge.com/images/badges/built-with-love.svg" />
    <img src="https://forthebadge.com/images/badges/built-by-developers.svg" />
</div>
