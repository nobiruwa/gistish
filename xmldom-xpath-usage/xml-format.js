/*global module:false, require:false */

const xmldom = require("@xmldom/xmldom");
const dom = xmldom.DOMParser;
const xpath = require("xpath");

/**
 * @description XML文字列に以下の処理からなるカノニカライズを実行した新たな文字列を生成します。
 *
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
 *
 * ホワイトスペースは連続する半角空白(0x20)を、改行はCR(0x0D)、LF(0x0A)、CR+LF(0x0D 0x0A)、LF+CR(0x0A 0x0D)のいずれかを意味します。
 * 以下のテキストノードは加工しません。
 *
 * - ホワイトスペースでも改行でもない文字を含むテキストノード
 *
 */
const canonicalizeXML = function(xmlString) {
  const doc = new dom().parseFromString(xmlString);
  var textNodes = xpath.select("//text()", doc);
  Array.prototype.forEach.call(textNodes, function(text) {
    if (/[^ \r\n]/.test(text)) {
      return;
    }
    if (text.previousSibling) {
      text.parentNode.removeChild(text);
    } else if (text.nextSibling) {
      text.parentNode.removeChild(text);
    }
  });

  return doc.toString();
};

module.exports = {
  canonicalizeXML: canonicalizeXML
};
