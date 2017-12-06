/*global require:false */

const fs = require("fs");

const format = require("./xml-format");

const readable = fs.readFileSync("readable.xml", "utf-8");

/* eslint-disable */
console.log(format.canonicalizeXML(readable));
/* eslint-enable */
