![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/osscameroon/js-generator/branch/main/graph/badge.svg?token=QJEBIRY8JJ)](https://codecov.io/gh/osscameroon/js-generator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# jsgenerator : Translating from HTML to JS

# Disclaimer

**This project is different from the [JavaScript Generator Object](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Generator).**

# About

The goal is to generate JS  from HTML  following the JavaScript DOM structure.
Sometimes, we forget how to write JavaScript to build dynamic web apps. Even if we know JS, it happens that we don't always have enough time to generate JS from a big HTML code. Thus, the goal of this project is helping developers gaining time by producing JS code as Output based on HTML as Input. This project will be very useful for beginners learning HTML and JavaScript. Also, it will help more experienced developers whenever they want to use JS.

The project is based on [jsoup  library, a Java HTML parser](https://jsoup.org/) / [jsoup GitHub Repository](https://github.com/jhy/jsoup/). It's all about using Nodes to generate JavaScript. 

**Actually, there is no release yet, it's just a console program but gradually we will build a Maven/Gradle Library, a Web & Desktop App and a CLI (Command Line Interface) tool translating from HTML to JS.**

# Wiki

https://github.com/osscameroon/js-generator/wiki

# Project Documentation

https://osscameroon.github.io/js-generator/

# Example

We invite you to run the [example branch code](https://github.com/osscameroon/js-generator/tree/example) to follow this tutorial.

You are free to copy and paste into [JSFiddle](https://jsfiddle.net/) or just click on the link provided to see the result.

This example gives you a big picture, our [Wiki](https://github.com/osscameroon/js-generator/wiki) provides more explanation on how things work under the hood.

Let's suppose we are building an HTML page starting with this initial code [JSFiddle](https://jsfiddle.net/a23j0nxf/):

```html
<!DOCTYPE html>
<html>
  <body>

    <div id="divtest">

    </div>

  </body>
</html>
```
Then, we want to add more data inside the **`div tag with id "divtest"`**. It's possible to do it manually but we don't want that. **Our goal is to make it dynamic with JavaScript. We want to create JavaScript variables that we'll use for whatever we want.**

This is the HTML code we'll add to divtest [JSFiddle](https://jsfiddle.net/xyrqa05c/):
```html

 <div>
  
    <h1>Open Source Society Cameroon</h1>
    
    <p>jsgenerator : Translating from Html to Js</p>
    
    <h2>About</h2>
    
    <p>
      
          The goal is to generate Javascript from HTML following the JavaScript DOM structure. 
          Sometimes, we forget how to use javascript to build dynamic web apps. 
          Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. 
          This project will be very useful for beginners learning html and javascript.

    </p>
      
    <p>
      
          The project is based on <a href="https://jsoup.org/">jsoup library, a java html parser</a> / <a href="https://github.com/jhy/jsoup/">Jsoup GitHub Repository</a> .It's all about using nodes to generate javascript.
      
    </p>
      
    <p>
      
          Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to  <a href="https://translate.google.com/">Google Translate</a>.

    </p>
      
</div>

```
Let's go to the [Main Class JSGenerator](https://github.com/osscameroon/js-generator/blob/example/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java), just copy and paste the HTML code into the variable named **html** then run the program.

It's possible to choose between **let** or **var** to declare your JavaScript variables as you can see here. We'll use **let** for this example:


```java
    /*
     * Just choose between VAR or LET for your variable declarations
     *
     * static ConvertService convertService = new
     * ConvertServiceImpl(JsVariableDeclaration.VAR);
     */

    static ConvertService convertService = new ConvertServiceImpl(JSVariableDeclaration.LET);

```


```java
    /**
     * Converts built-in code from Html to Js and prints the result.
     */
    static void convertAndPrintBuiltInCodeFromHtmlToJs() {

	// Use log instead of system.out.println to show steps
	logger.log(Level.INFO, " **** Converting built-in code from html to js **** ");
	logger.log(Level.INFO, " **** Html to convert:  **** ");

	// Copy the html code into the variable named html then run the program.

	String html = "";

	logger.log(Level.INFO, "\n\n" + html + "\n");

	logger.log(Level.INFO, " **** generated js:  **** " + "\n");

	System.out.println(convertService.convert(html));

    }

```

JS ouput we get after translating from the console:

```javascript

let h1 = document.createElement("h1");
h1.appendChild(document.createTextNode("Open Source Society Cameroon "));

let p = document.createElement("p");
p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js "));

let h2 = document.createElement("h2");
h2.appendChild(document.createTextNode("About "));

let p_ = document.createElement("p");
p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure. 
          Sometimes, we forget how to use javascript to build dynamic web apps. 
          Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. 
          This project will be very useful for beginners learning html and javascript. "));

let a = document.createElement("a");
a.setAttribute("href", "https://jsoup.org/");
a.appendChild(document.createTextNode("jsoup library, a java html parser "));

let a_ = document.createElement("a");
a_.setAttribute("href", "https://github.com/jhy/jsoup/");
a_.appendChild(document.createTextNode("Jsoup GitHub Repository "));

let p__ = document.createElement("p");
p__.appendChild(document.createTextNode("The project is based on "));
p__.appendChild(a);
p__.appendChild(document.createTextNode("/ "));
p__.appendChild(a_);
p__.appendChild(document.createTextNode(".It's all about using nodes to generate javascript. "));

let a__ = document.createElement("a");
a__.setAttribute("href", "https://translate.google.com/");
a__.appendChild(document.createTextNode("Google Translate "));

let p___ = document.createElement("p");
p___.appendChild(document.createTextNode("Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to "));
p___.appendChild(a__);
p___.appendChild(document.createTextNode(". "));

let div = document.createElement("div");
div.appendChild(h1);
div.appendChild(p);
div.appendChild(h2);
div.appendChild(p_);
div.appendChild(p__);
div.appendChild(p___);


```

It's time to link our output to our initial HTML code:

```javascript
let divtest = document.getElementById("divtest");

divtest.appendChild(div);
```
In order to test that it works, just compare the results on https://jsfiddle.net/

## Expected Result

[JSFiddle](https://jsfiddle.net/pyahn1fc/)

```html
<!DOCTYPE html>
<html>
  <body>

    <div id="divtest">
    
       <div>

          <h1>Open Source Society Cameroon</h1>

          <p>jsgenerator : Translating from Html to Js</p>

          <h2>About</h2>

          <p>

                The goal is to generate Javascript from HTML following the JavaScript DOM structure. 
                Sometimes, we forget how to use javascript to build dynamic web apps. 
                Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. 
                This project will be very useful for beginners learning html and javascript.

          </p>

          <p>

                The project is based on <a href="https://jsoup.org/">jsoup library, a java html parser</a> / <a href="https://github.com/jhy/jsoup/">Jsoup GitHub Repository</a> .It's all about using nodes to generate javascript.

          </p>

          <p>

                Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to  <a href="https://translate.google.com/">Google Translate</a>.

          </p>

      </div>

    </div>

  </body>
</html>
```

## Equivalent Result with the Initial HTML and JS generated code

[JSFiddle](https://jsfiddle.net/yaoqt24x/)

### Initial HTML Code

```html
<!DOCTYPE html>
<html>
  <body>

    <div id="divtest">

    </div>

  </body>
</html>
```
### JavaScript Generated Code

```javascript

let h1 = document.createElement("h1");
h1.appendChild(document.createTextNode("Open Source Society Cameroon "));

let p = document.createElement("p");
p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js "));

let h2 = document.createElement("h2");
h2.appendChild(document.createTextNode("About "));

let p_ = document.createElement("p");
p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure. 
          Sometimes, we forget how to use javascript to build dynamic web apps. 
          Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. 
          This project will be very useful for beginners learning html and javascript. "));

let a = document.createElement("a");
a.setAttribute("href", "https://jsoup.org/");
a.appendChild(document.createTextNode("jsoup library, a java html parser "));

let a_ = document.createElement("a");
a_.setAttribute("href", "https://github.com/jhy/jsoup/");
a_.appendChild(document.createTextNode("Jsoup GitHub Repository "));

let p__ = document.createElement("p");
p__.appendChild(document.createTextNode("The project is based on "));
p__.appendChild(a);
p__.appendChild(document.createTextNode("/ "));
p__.appendChild(a_);
p__.appendChild(document.createTextNode(".It's all about using nodes to generate javascript. "));

let a__ = document.createElement("a");
a__.setAttribute("href", "https://translate.google.com/");
a__.appendChild(document.createTextNode("Google Translate "));

let p___ = document.createElement("p");
p___.appendChild(document.createTextNode("Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to "));
p___.appendChild(a__);
p___.appendChild(document.createTextNode(". "));

let div = document.createElement("div");
div.appendChild(h1);
div.appendChild(p);
div.appendChild(h2);
div.appendChild(p_);
div.appendChild(p__);
div.appendChild(p___);



// It's time to link our output to our initial html code

let divtest = document.getElementById("divtest");

divtest.appendChild(div);
```
We hope this example makes you better understand this project. In a real project, you might want to use the generated JS code for whatever you want, it's up to you. If not enough, the [Wiki](https://github.com/osscameroon/js-generator/wiki) provides more explanation on how things work under the hood.

# Code Info

Language: JAVA

Version: 11

Build Tool: Maven

# Build

1. Clone

2. Build -> `mvn clean package`

3. Browse through code and find [Main Class JSGenerator](https://github.com/osscameroon/js-generator/blob/example/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java)

4. Paste HTML code and test... should output some JS for you...


# How to Contribute

Hello World, all your contributions are welcome! 
Don't hesitate to open an issue on this repository and/or create a pull request (PR).
In order to create a PR, just fork first.

Thanks for your commitment, we really appreciate! 
Happy Coding! 😊🎉💯
