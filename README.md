# jsGenerator : Translating from Html to Js

# About

The goal is to generate Javascript  from HTML  following the JavaScript DOM structure.
Sometimes, we forget how to use javascript to build dynamic web apps. Thus, the goal of this project is helping developers gaining time by producing javascript code as output based on html as input. This project will be very useful for beginners learning html and javascript.

The project is based on [jsoup  library, a java html parser](https://jsoup.org/) / [Jsoup GitHub Repository](https://github.com/jhy/jsoup/) . It's all about using nodes to generate javascript. 

Actually, it's just a console program but gradually we will build an API.

# Code Info

Language: JAVA

Version: 11

Build Tool: Maven

# Build

1. Clone

2. Build -> `mvn clean package`

3. Browse through source codes and find Main file

4. Paste HTML code and test... should output some JS for you...

# todo
* Fix to recognise open-ended tags e.g ``<img src="" alt="">`` should be treated as ``<img src="" alt=""></img>`` - behaviour now is functions like sibling attribute is child
* ~~Fix to refactor tag names so multiple tags don't end up with the same name when appending~~
* Add Tests and Documentation to this legacy code. We apologize for not respecting TDD (Test Driven Development) from the beginning.
