![Maven Build](https://github.com/osscameroon/js-generator/actions/workflows/maven.yml/badge.svg)

# jsgenerator : Translating from Html to Js

# About

The goal is to generate Javascript  from HTML  following the JavaScript DOM structure.
Sometimes, we forget how to use javascript to build dynamic web apps. Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. This project will be very useful for beginners learning html and javascript.

The project is based on [jsoup  library, a java html parser](https://jsoup.org/) / [Jsoup GitHub Repository](https://github.com/jhy/jsoup/) . It's all about using nodes to generate javascript. 

Actually, it's just a console program but gradually we will build a Maven/Gradle library then a web app translating from Html to Js, an app similar to Google Translate : https://translate.google.com/.

# Project Documentation

https://osscameroon.github.io/js-generator/

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
