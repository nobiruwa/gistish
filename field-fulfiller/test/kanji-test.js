/*global require:false */
var kanji = require("../kanji"),
    t = require("mocha"),
    assert = require("assert");

t.describe("kanji", function() {
  t.it("length=-1", function() {
    var config = {
      length: -1,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "");
  });
  t.it("length=0", function() {
    var config = {
      length: 0,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "");
  });
  t.it("length=1", function() {
    var config = {
      length: 1,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "");
  });
  t.it("length=2", function() {
    var config = {
      length: 2,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あ");
  });
  t.it("length=3", function() {
    var config = {
      length: 3,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あ");
  });
  t.it("length=4", function() {
    var config = {
      length: 4,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あア");
  });
  t.it("length=5", function() {
    var config = {
      length: 5,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あア");
  });
  t.it("length=6", function() {
    var config = {
      length: 6,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あア亜");
  });
  t.it("length=60", function() {
    var config = {
      length: 60,
      Hiragana: "あ",
      Katakana: "ア",
      Kanji: "亜"
    };
    assert.equal(kanji.generate(config), "あア亜あア亜あア亜あア亜あア亜あア亜あア亜あア亜あア亜あア亜");
  });
});
