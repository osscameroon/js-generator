# API

After starting the `jsgenerator-api` as described in the [README.md](./README.md), you can read:

+ OpenAPI spec. at: [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml)
+ OpenAPI UI at: [http://localhost:8080](http://localhost:8080)

Two endpoints are exposed:
+ `POST /convert`
+ `POST /convert/files`

Both accept options as follow:
```json
{
  "targetElementSelector": ":root > body",
  "pattern": "inline-filename-pattern",
  "variableNameStrategy": "TYPE_BASED",
  "variableDeclaration": "LET",
  "extension": ".jsgenerator.js",
  "commentConversionModeActivated": true,
  "querySelectorAdded": true,
  "contents": [
    "string"
  ]
}
```
> **NOTE:** The `"contents"` field is mandatory for `POST /convert` and forbidden for `POST /convert/files`


---

Using [`curl`](https://curl.se/):

+ `POST /convert`
```shell
# You can also pass as many HTML content as you want
# Response will be of 'application/json' content type
curl -H 'content-type: application/json' -X POST --data '{"contents": ["<div>js-jsgenerator</div>"]}' http://localhost:8080/convert

#Response
{"content":[{"filename":"inline.0.jsgenerator.js","content":"let targetElement_001 = document.querySelector(`:root > body`);\r\n\r\n\r\nlet div_001 = document.createElement('div');\r\nlet text_001 = document.createTextNode(`js-generator`);\r\ndiv_001.appendChild(text_001);\r\ntargetElement_001.appendChild(div_001);\r\n"}],"status":"SUCCESS"}
```

+ `POST /convert/files`
```shell
# You can call the API with multiple **files** and at most one **options**
# Response will be of 'multipart/form-data' content type
curl -s -X POST -H 'content-type: multipart/form-data' -F files=@illustrations/sample.html -F "options={ \"querySelectorAdded\": true, \"variableDeclaration\": \"VAR\" }; type=application/json" http://localhost:8080/convert/files

# -s flag is added in order to prevent curl to mix response and progress meter
#if not added, this will happen: 100  5280  100  4275  100  1005   117k  28194 --:--:-- --:--:-- --:--:--  147kment.createTextNode(`    `);

#Response

--d2a-7NlH3rlmcFC3loiJxDxom6iojCunhkzzH
Content-Disposition: form-data; name="inline.0.jsgenerator.js"
Content-Type: application/octet-stream
Content-Length: 4069

var targetElement_001 = document.querySelector(`:root > body`);


var html_001 = document.createElement('html');
var text_001 = document.createTextNode(`    `);
html_001.appendChild(text_001);

var head_001 = document.createElement('head');
var text_002 = document.createTextNode(`        `);
head_001.appendChild(text_002);

var meta_001 = document.createElement('meta');
meta_001.setAttribute(`charset`, `utf-8`);
head_001.appendChild(meta_001);
var text_003 = document.createTextNode(`        `);
head_001.appendChild(text_003);

var title_001 = document.createElement('title');
var text_004 = document.createTextNode(`Sample`);
title_001.appendChild(text_004);
head_001.appendChild(title_001);
var text_005 = document.createTextNode(`        `);
head_001.appendChild(text_005);

var link_001 = document.createElement('link');
link_001.setAttribute(`rel`, `stylesheet`);
link_001.setAttribute(`href`, ``);
head_001.appendChild(link_001);
var text_006 = document.createTextNode(`    `);
head_001.appendChild(text_006);
html_001.appendChild(head_001);
var text_007 = document.createTextNode(`    `);
html_001.appendChild(text_007);

var body_001 = document.createElement('body');
var text_008 = document.createTextNode(`        `);
body_001.appendChild(text_008);

var div_001 = document.createElement('div');
div_001.setAttribute(`id`, `container`);
var text_009 = document.createTextNode(`            `);
div_001.appendChild(text_009);

var div_002 = document.createElement('div');
div_002.setAttribute(`id`, `header`);
var text_010 = document.createTextNode(`                `);
div_002.appendChild(text_010);

var h1_001 = document.createElement('h1');
var text_011 = document.createTextNode(`Sample`);
h1_001.appendChild(text_011);
div_002.appendChild(h1_001);
var text_012 = document.createTextNode(`                `);
div_002.appendChild(text_012);

var img_001 = document.createElement('img');
img_001.setAttribute(`src`, `kanye.jpg`);
img_001.setAttribute(`alt`, `kanye`);
div_002.appendChild(img_001);
var text_013 = document.createTextNode(`            `);
div_002.appendChild(text_013);
div_001.appendChild(div_002);
var text_014 = document.createTextNode(`            `);
div_001.appendChild(text_014);

var div_003 = document.createElement('div');
div_003.setAttribute(`id`, `main`);
var text_015 = document.createTextNode(`                `);
div_003.appendChild(text_015);

var h2_001 = document.createElement('h2');
var text_016 = document.createTextNode(`Main`);
h2_001.appendChild(text_016);
div_003.appendChild(h2_001);
var text_017 = document.createTextNode(`                `);
div_003.appendChild(text_017);

var p_001 = document.createElement('p');
var text_018 = document.createTextNode(`This is the main content.`);
p_001.appendChild(text_018);
div_003.appendChild(p_001);
var text_019 = document.createTextNode(`                `);
div_003.appendChild(text_019);

var img_002 = document.createElement('img');
img_002.setAttribute(`src`, ``);
img_002.setAttribute(`alt`, ``);
div_003.appendChild(img_002);
var text_020 = document.createTextNode(`            `);
div_003.appendChild(text_020);
div_001.appendChild(div_003);
var text_021 = document.createTextNode(`            `);
div_001.appendChild(text_021);

var div_004 = document.createElement('div');
div_004.setAttribute(`id`, `footer`);
var text_022 = document.createTextNode(`                `);
div_004.appendChild(text_022);

var p_002 = document.createElement('p');
var text_023 = document.createTextNode(`Copyright - 2019`);
p_002.appendChild(text_023);
div_004.appendChild(p_002);
var text_024 = document.createTextNode(`            `);
div_004.appendChild(text_024);
div_001.appendChild(div_004);
var text_025 = document.createTextNode(`        `);
div_001.appendChild(text_025);
body_001.appendChild(div_001);
var text_026 = document.createTextNode(`    `);
body_001.appendChild(text_026);
html_001.appendChild(body_001);
targetElement_001.appendChild(html_001);

--d2a-7NlH3rlmcFC3loiJxDxom6iojCunhkzzH--
```
