let title = document.createElement("title");
title.appendChild(document.createTextNode("Sample "));

let link = document.createElement("link");
link.setAttribute("rel", "stylesheet");
link.setAttribute("href", "");

let meta = document.createElement("meta");
meta.setAttribute("charset", "utf-8");

let head = document.createElement("head");
head.appendChild(meta);

let h1 = document.createElement("h1");
h1.appendChild(document.createTextNode("Sample "));

let img = document.createElement("img");
img.setAttribute("src", "kanye.jpg");
img.setAttribute("alt", "kanye");

let div = document.createElement("div");
div.setAttribute("id", "header");
div.appendChild(h1);
div.appendChild(img);

let h2 = document.createElement("h2");
h2.appendChild(document.createTextNode("Main "));

let p = document.createElement("p");
p.appendChild(document.createTextNode("This is the main content. "));

let img_ = document.createElement("img");
img_.setAttribute("src", "");
img_.setAttribute("alt", "");

let div_ = document.createElement("div");
div_.setAttribute("id", "main");
div_.appendChild(h2);
div_.appendChild(p);
div_.appendChild(img_);

let p_ = document.createElement("p");
p_.appendChild(document.createTextNode("Copyright © 2019 "));

let div__ = document.createElement("div");
div__.setAttribute("id", "footer");
div__.appendChild(p_);

let div___ = document.createElement("div");
div___.setAttribute("id", "container");
div___.appendChild(div);
div___.appendChild(div_);
div___.appendChild(div__);

let body = document.createElement("body");
body.appendChild(div___);

let html = document.createElement("html");
html.appendChild(head);
html.appendChild(body);