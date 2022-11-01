# Command Line Interface

```shell
let targetElement_000 = document.querySelector(`:root > body`);


let div_000 = document.createElement('div');
let text_000 = document.createTextNode(`I am a `);
div_000.appendChild(text_000);

let strong_000 = document.createElement('strong');
let text_001 = document.createTextNode(`tea pot`);
strong_000.appendChild(text_001);
div_000.appendChild(strong_000);
targetElement_000.appendChild(div_000);
```

---

`jsgenerator` has several options that can be used in a console here is an example of use below

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