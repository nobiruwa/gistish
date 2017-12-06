/*global require:false */
"use strict";

const fs = require("fs");
const xmldom = require("xmldom");
const dom = xmldom.DOMParser;
const xpath = require("xpath");

/**
 * @description XML文字列に以下の処理からなるカノニカライズを実行した新たな文字列を生成します。
 * 1. 不要なテキストノードを取り除きます。
 *    - 開きタグと開きタグの間のホワイトスペースと改行
 *    - 閉じタグと閉じタグの間のホワイトスペースと改行
 *    - 閉じタグと開きタグの間のホワイトスペースと改行
 * 2. 空要素のフォーマットを統一します。
 *    - フォーマットはxmldomモジュールの実装に依存します。
 * 3. 開きタグ内(`<`から`>`まで)の不要なホワイトスペースと改行
 *    - xmldomモジュールが開きタグ内のホワイトスペースを無視することを利用しています。
 * 4. 閉じタグ内(`<`から`>`まで)の不要なホワイトスペースと改行
 *    - xmldomモジュールが開きタグ内のホワイトスペースを無視することを利用しています。
 */
const canonicalizeXML = function(xmlString) {
  const doc = new dom().parseFromString(readable);
  var textNodes = xpath.select("//text()", doc);
  Array.prototype.forEach.call(textNodes, function(text) {
    if (text.previousSibling) {
      text.parentNode.removeChild(text);
    } else if (text.nextSibling) {
      text.parentNode.removeChild(text);
    }
  });

  // 
  return doc.toString();
};

const readable = fs.readFileSync("readable.xml", "utf-8");
console.log(canonicalizeXML(readable));
