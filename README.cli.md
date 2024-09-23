# Command Line Interface

```shell
let targetElement_001 = document.querySelector(`:root > body`);


let div_001 = document.createElement('div');
let text_001 = document.createTextNode(`I am a `);
div_001.appendChild(text_001);

let strong_001 = document.createElement('strong');
let text_002 = document.createTextNode(`tea pot`);
strong_001.appendChild(text_002);
div_001.appendChild(strong_001);
targetElement_001.appendChild(div_001);
```

---

`jsgenerator` has several options that can be used in a console here is an example of use below

```text
Usage: jsgenerator [-chtV] [-qs] [-e=<extension>]
                   [--inline-pattern=<inlinePattern>]
                   [-k=<variableDeclaration>] [--path-pattern=<pathPattern>]
                   [-s=<targetElementSelector>]
                   [--stdin-pattern=<stdinPattern>]
                   [--variable-name-generation-strategy=<builtinVariableNameStra
                   tegy>] [-i=<inlineContents>...]... [<paths>...]
Translating files, stdin or inline from HTML to JS
      [<paths>...]        file paths to translate content, parsed as HTML
  -c, --comment           optional comments
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
      -qs, --query-selector
                          What the browser renders depends on whether "document.
                            querySelector(':root > body')" is added to the
                            output.     If added, the browser will render the
                            output successfully, it is useful for debugging
                            purpose,
                               to verify that the js output matches what the
                            html input does.
                               If not, if the user tries to run the output as
                            it is then the browser will not be able to render,
                            it will show a blank page.
                               So, it depends on what the user wants to do with
                            the output.
                               "https://jsfiddle.net/", "https://codepen.
                            io/pen/" and Browser Console  help to give a quick
                            feedback.

  -s, --selector=<targetElementSelector>
                          Target element selector
      --stdin-pattern=<stdinPattern>
                          pattern for stdin output filenames
  -t, --tty               output to stdin, not files
  -V, --version           Print version information and exit.
      --variable-name-generation-strategy=<builtinVariableNameStrategy>
                          Variable names generation strategy
```
