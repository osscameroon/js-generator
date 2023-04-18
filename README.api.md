# API

Using [`httpie`](https://httpie.io/):
```shell
# You can also pass as many HTML content as you want
# Response will be of 'application/json' content type
http -vf :8080/api/convert \
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

Or, give the following two files contents:
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
http -vf :8080/api/convert/files \
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

---

After starting the `jsgenerator-api` as described in the [README.md](./README.md), you can read:

+ OpenAPI spec. at: [http://localhost:8080/openapi.yaml](http://localhost:8080/openapi.yaml)
+ OpenAPI UI at: [http://localhost:8080](http://localhost:8080)

Two endpoints are exposed:
+ `POST /api/convert`
+ `POST /api/convert/files`

Both accept options as follow:
```json
{
  "targetElementSelector": ":root > body",
  "pattern": "inline-filename-pattern",
  "variableNameStrategy": "TYPE_BASED",
  "variableDeclaration": "LET",
  "extension": ".extension",
  "contents": [
    "string"
  ]
}
```
> **NOTE:** The `"content"` field is mandatory for `POST /api/convert` and forbidden for `POST /api/convert/files`
