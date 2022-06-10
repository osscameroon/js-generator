![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)
[![codecov](https://codecov.io/gh/osscameroon/js-generator/branch/main/graph/badge.svg?token=QJEBIRY8JJ)](https://codecov.io/gh/osscameroon/js-generator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

# jsgenerator : Translating from Html to Js

# About

The goal is to generate Javascript  from HTML  following the JavaScript DOM structure.
Sometimes, we forget how to use javascript to build dynamic web apps. Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. This project will be very useful for beginners learning html and javascript.

The project is based on [jsoup  library, a java html parser](https://jsoup.org/) / [Jsoup GitHub Repository](https://github.com/jhy/jsoup/) . It's all about using nodes to generate javascript. 

Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to Google Translate : https://translate.google.com/.

# Project Documentation

https://osscameroon.github.io/js-generator/

# Examples

This is one example to show you how things work, there are more examples in our [Wiki](https://github.com/osscameroon/js-generator/wiki).

Let's suppose we are building an Html page and this is the initial code:

```html
<!DOCTYPE html>
<html>
  <body>

    <div id="divtest">

    </div>

  </body>
</html>
```
Then, we want to add more data inside the **`div tag with id "divtest"`**. It's possible to do it manually but we don't want. Our goal is to make it dynamic with Javascript. We want to create Javascript variables that we'll use for whatever we want.

This is the html code we'll add into divtest:
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
Let's go to the [Main Class JSGenerator](https://github.com/osscameroon/js-generator/blob/main/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java#L87), just copy the html code into the variable named **html** then run the program.

```java
    /**
     * Converts built-in code from Html to Js and prints the result.
     */
     
    static void convertAndPrintBuiltInCodeFromHtmlToJs() {

	// Use log instead of system.out.println to show steps
	logger.log(Level.INFO, " **** Converting built-in code from html to js **** ");
	logger.log(Level.INFO, " **** Html to convert:  **** ");
  
  
  // Copy the html code into the variable named html then run the program.
  
  String html="";
  
	logger.log(Level.INFO, "\n\n" + html + "\n");

	logger.log(Level.INFO, " **** generated js:  **** "+ "\n");

	System.out.println(convertService.convert(html));
    }
```

This is Js ouput we get after translating from the console :

```javascript
let h1 = document.createElement("h1");
h1.appendChild(document.createTextNode("Open Source Society Cameroon"));

let p = document.createElement("p");
p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js"));

let h2 = document.createElement("h2");
h2.appendChild(document.createTextNode("About"));

let p_ = document.createElement("p");
p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure. 
          Sometimes, we forget how to use javascript to build dynamic web apps. 
          Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. 
          This project will be very useful for beginners learning html and javascript."));

let a = document.createElement("a");
a.setAttribute("href", "https://jsoup.org/");
a.appendChild(document.createTextNode("jsoup library, a java html parser"));

let a_ = document.createElement("a");
a_.setAttribute("href", "https://github.com/jhy/jsoup/");
a_.appendChild(document.createTextNode("Jsoup GitHub Repository"));

let p__ = document.createElement("p");
p__.appendChild(document.createTextNode("The project is based on"));
p__.appendChild(a);
p__.appendChild(document.createTextNode("/"));
p__.appendChild(a_);
p__.appendChild(document.createTextNode(".It's all about using nodes to generate javascript."));

let a__ = document.createElement("a");
a__.setAttribute("href", "https://translate.google.com/");
a__.appendChild(document.createTextNode("Google Translate"));

let p___ = document.createElement("p");
p___.appendChild(document.createTextNode("Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to"));
p___.appendChild(a__);
p___.appendChild(document.createTextNode("."));

let div = document.createElement("div");
div.appendChild(h1);
div.appendChild(p);
div.appendChild(h2);
div.appendChild(p_);
div.appendChild(p__);
div.appendChild(p___);


```

It's time to link our output to our initial html code

```
let divtest = document.getElementById("divtest");

divtest.appendChild(div);
```
In order to test that it works, just compare the results on https://jsfiddle.net/

## Result 1

```
<!DOCTYPE html>
<html>
<body>

<div id="divtest">

  <div>
  
    <h1>Open Source Society Cameroon</h1>
    
    <p>jsgenerator : Translating from Html to Js</p>
    
    <h2>About</h2>
    
    <p>
    The goal is to generate Javascript from HTML following the JavaScript DOM structure. Sometimes, we forget how to use javascript to build dynamic web apps. Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. This project will be very useful for beginners learning html and javascript.

The project is based on <a href="https://jsoup.org/">jsoup library, a java html parser</a> / <a href="https://github.com/jhy/jsoup/">Jsoup GitHub Repository</a> .It's all about using nodes to generate javascript.

Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to  <a href=" https://translate.google.com/">Google Translate</a> .
    </p>
    
  </div>
  
</div>

</body>
</html>
```

## Result 2

Html code

```
<!DOCTYPE html>
<html>
<body>

<div id="divtest">

</div>

</body>
</html>

```
Js code

```
let h1 = document.createElement("h1");
h1.appendChild(document.createTextNode("Open Source Society Cameroon"));

let p = document.createElement("p");
p.appendChild(document.createTextNode("jsgenerator : Translating from Html to Js"));

let h2 = document.createElement("h2");
h2.appendChild(document.createTextNode("About"));

let a = document.createElement("a");
a.setAttribute("href", "https://jsoup.org/");
a.appendChild(document.createTextNode("jsoup library, a java html parser"));

let a_ = document.createElement("a");
a_.setAttribute("href", "https://github.com/jhy/jsoup/");
a_.appendChild(document.createTextNode("Jsoup GitHub Repository"));

let a__ = document.createElement("a");
a__.setAttribute("href", " https://translate.google.com/");
a__.appendChild(document.createTextNode("Google Translate"));

let p_ = document.createElement("p");
p_.appendChild(document.createTextNode("The goal is to generate Javascript from HTML following the JavaScript DOM structure. Sometimes, we forget how to use javascript to build dynamic web apps. Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. This project will be very useful for beginners learning html and javascript.

The project is based on"));
p_.appendChild(document.createTextNode("/"));
p_.appendChild(document.createTextNode(".It's all about using nodes to generate javascript.

Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to"));
p_.appendChild(document.createTextNode("."));
p_.appendChild(a);
p_.appendChild(a_);
p_.appendChild(a__);

let div = document.createElement("div");
div.appendChild(h1);
div.appendChild(p);
div.appendChild(h2);
div.appendChild(p_);

// It's time to link our output to our initial html code

let divtest = document.getElementById("divtest");

divtest.appendChild(div);
```

# Code Info

Language: JAVA

Version: 11

Build Tool: Maven

# Build

1. Clone

2. Build -> `mvn clean package`

3. Browse through source codes and find [Main](https://github.com/osscameroon/js-generator/blob/main/src/main/java/com/osscameroon/jsgenerator/JSGenerator.java) file

4. Paste HTML code and test... should output some JS for you...


# How to Contribute

Hello World, all your contributions are welcome! 
Don't hesitate to open an issue on this repository and/or create a pull request (PR).
In order to create a PR, just fork first.

Thanks for your commitment, we really appreciate! 
Happy Coding! 😊🎉💯
