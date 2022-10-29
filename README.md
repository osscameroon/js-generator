
![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/osscameroon/js-generator/branch/main/graph/badge.svg?token=QJEBIRY8JJ)](https://codecov.io/gh/osscameroon/js-generator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Contributors](https://img.shields.io/github/contributors-anon/osscameroon/js-generator)](https://github.com/osscameroon/js-generator/graphs/contributors)
![Follow](https://img.shields.io/twitter/follow/osscameroon?style=social)


# Table of Contents
- [About](#about)
- [Getting Started](#getting-started)
- [Clients](#clients)
- [Stack](#stack)
- [Contribute](#contribute)

![From Html to Js through Java](illustrations/html_java_js.png)

# About

Translating from HTML to JS.

> This project is different from the
> [JavaScript Generator Object](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Generator).
 
The goal is to generate JS  from HTML  following the [Document Object Model](https://www.w3schools.com/js/js_htmldom.asp) structure. Sometimes, we forget how to write
JavaScript to build dynamic web apps. Even if we know JS, it happens that we don't always have enough time to generate
JS from a big HTML code. Thus, the goal of this project is helping developers gaining time by producing JS code as
Output based on HTML as Input. This project will be very useful for beginners learning HTML and JavaScript. Also, it
will help more experienced developers whenever they want to use JS instead of HTML, very useful in applications where the code must be dynamically generated.

[Sherlock Wisdom](https://github.com/sherlockwisdom) shared why he needed such tool:

> 😂 Yes it's hard to say why it's important. I was working on an Android based app, but was building it with Vanilla JavaScript. So I needed a quick way to turn bootstrap code into Vanilla Js objects so that I could do what ReactJS does now 🤣. This was ~4 years back. Not sure of its relevance now, but they could be some. 😅 Sorry if I rather made things not easy for you to explain.

We would like to give credit to [jsoup](https://jsoup.org/) / [jsoup GitHub Repository](https://github.com/jhy/jsoup/) as the main library to help us handle HTML tokenization and traversing.

![How does it work in a nutshell ?](illustrations/jsgenerator_intro.png)

## Getting Started

**CLI**

jsgenerator has several options that can be used in a console here is an example of use below

```shell
$ jsgenerator --tty --inline '<div>I am a <strong>tea pot</strong></div>'

let div_000 = document.createElement('div');
let text_000 = document.createTextNode(`I am a `);
div_000.appendChild(text_000);

let strong_000 = document.createElement('strong');
let text_001 = document.createTextNode(`tea pot`);
strong_000.appendChild(text_001);
div_000.appendChild(strong_000);
document.appendChild(div_000);
```

## Clients

**CLI**
```text
Usage: jsgenerator [-htV] [-e=<extension>] [--inline-pattern=<inlinePattern>]
                   [-k=<variableDeclaration>] [--path-pattern=<pathPattern>]
                   [-s=<targetElementSelector>]
                   [--stdin-pattern=<stdinPattern>]
                   [--variable-name-generation-strategy=<builtinVariableNameStra
                   tegy>] [-i=<inlineContents>...]... [<paths>...]
Translating files, stdin or inline from HTML to JS
      [<paths>...]        file paths to translate content, parsed as HTML
  -e, --ext=<extension>   output files' extension
  -h, --help              Show this help message and exit.
  -i, --inline=<inlineContents>...
                          args as HTML content, not files
      --inline-pattern=<inlinePattern>
                          Pattern for inline output filename
  -k, --keyword=<variableDeclaration>
                          variable declaration keyword
      --path-pattern=<pathPattern>
                          pattern for path-based output filenames
  -s, --selector=<targetElementSelector>
                          Target element selector
      --stdin-pattern=<stdinPattern>
                          pattern for stdin output filenames
  -t, --tty               output to stdin, not files
  -V, --version           Print version information and exit.
      --variable-name-generation-strategy=<builtinVariableNameStrategy>
                          Variable names generation strategy
```

**API**

Start it with:
```shell
java -jar jsgenerator-api/target/jsgenerator-api-0.0.1-SNAPSHOT.jar
```

Visit OpenAPI spec. at: [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml)

Visit OpenAPI UI at: [http://localhost:8080](http://localhost:8080)

> Two endpoints are exposed:
> + `POST /convert`
> + `POST /convert/files`
>
> Both accept options as follow:
> ```json
> {
>   "targetElementSelector": ":root > body",
>   "pattern": "inline-filename-pattern",
>   "variableNameStrategy": "TYPE_BASED",
>   "variableDeclaration": "LET",
>   "extension": ".extension",
>   "contents": [
>     "string"
>   ]
> }
> ```
> **NOTE:** The `content` field in options is mandatory for `POST /convert` and forbidden for `POST /convert/files`

Here follow some example with [`httpie`](https://httpie.io/)
> ```json
> { "extension":  ".js" } // ./multipart-options.json
> ```
> 
> ```html
> <!DOCTYPE html>
> <!-- ./sample.html -->
> <html>
>   <head>
>     ...
>   ...
> ...
> ```

```shell
# You can call the API with multiple **files** and at most one **options**
# Response will be of 'multipart/form-data' content type
http -vf :8080/convert/files \
  'files@./sample.html;type=multipart/form-data' \
  'options@multipart-options.json;type=application/json'

HTTP/1.1 200 
Content-Type: multipart/form-data;boundary=3N0wqEqnb7AC3WD8M1cYYG-vLfHDND_JdE90

--3N0wqEqnb7AC3WD8M1cYYG-vLfHDND_JdE90
Content-Disposition: form-data; name="0.sample.html.js"
Content-Type: application/octet-stream
Content-Length: 4156

const targetElement_000 = document.querySelector(`:root > body`);
[... truncated for brievity]
```
```shell
# You can also pass as many HTML content as you want
# Response will be of 'application/json' content type
http -vf :8080/convert \
  extension='.js' \
  contents[]='<hr/>' \
  contents[]='<button disabled>click me, please :sob:</button>'

HTTP/1.1 200 
Content-Type: application/json

{
  "status": "SUCCESS"
  "content": [
    {
      "content": "const targetElement_000 = document.querySelector(`:root > body`);\r\n\r\n\r\nconst hr_000 = document.createElement('hr');\r\ntargetElement_000.appendChild(hr_000);\r\n",
      "filename": "inline.0.js"
    },
    {
      "content": "const targetElement_001 = document.querySelector(`:root > body`);\r\n\r\n\r\nconst button_000 = document.createElement('button');\r\nbutton_000.setAttribute(`disabled`, `true`);\r\nconst text_000 = document.createTextNode(`click me, please :sob:`);\r\nbutton_000.appendChild(text_000);\r\ntargetElement_001.appendChild(button_000);\r\n",
      "filename": "inline.1.js"
    }
  ]
}
```

**WEB**

> ***TODO:** Not yet implemented.*

**DESKTOP**

> ***TODO:** Not yet implemented.*

**CODE API**

> ***TODO:** Not yet implemented.*
> See [Wiki](https://github.com/osscameroon/js-generator/wiki).

# Stack

+ JDK 17
  > NOTE: For native build (CLI, for eg.), we use GraalVM with JDK 17.
  > 
  > Recent versions of GraalVM are not bundling `native-image` CLI by default.
  > We are required to install is manually, by running:
  > ```shell
  > # Where `gu` is an executable bundled with GraalVM
  > gu install native-image
  > ```
+ Maven 3
+ Spring 5.3.22
+ Spring Boot 2.7.3

# Contribute

```shell
# 1. Clone
git clone git@github.com:osscameroon/js-generator.git

# 2. Move to root directory
cd js-generator

# 3. Tests & Build
mvn clean package

# 4. Build Native CLI (Requires GraalVM JDK 17, and a Linux-friendly shell, like Cmder)
./cli-build.sh # if provided, first argument will be the file name (useful for version tagging) 

# 5. Browse through code
# 6. Run CLI with --help and play with it
# 7. Fork the project, build, test, open a pull request
```

All your contributions are welcome!
Do not hesitate to open an issue on this repository and/or create a pull request (PR).
In order to create a PR, just fork first.

**[We started from the bottom 4 years ago](https://github.com/opensourcecameroon/jsGenerator), now we are here, we believe we will continue moving forward together 😊.** 

Thanks for your commitment, we really appreciate! 
Happy Coding! 😊🎉💯

<div align="center">
    <img src="https://forthebadge.com/images/badges/built-with-love.svg" />
    <img src="https://forthebadge.com/images/badges/built-by-developers.svg" />
</div>

[Back To The Top](#table-of-contents)
