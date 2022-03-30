#!/usr/bin/env bash

echo '1. remove package-lock.json'
echo '2. edit package.json: `dependencies: { /* dependencies */ }` -> `dependencies: {}`'

npm i \
    "body-parser" \
    "cors" \
    "express" \
    "mustache" \
    "puppeteer" \
    "uuid" \
    "@xmldom/xmldom" \
    "xpath"
