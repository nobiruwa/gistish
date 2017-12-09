/*global require:false */
var ank = require("../ank"),
    t = require("mocha"),
    assert = require("power-assert");

t.describe("ank", function() {
  t.it("length=-1", function() {
    var config = {
      length: -1,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "");
  });
  t.it("length=0", function() {
    var config = {
      length: 0,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "");
  });
  t.it("length=1", function() {
    var config = {
      length: 1,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "A");
  });
  t.it("length=2", function() {
    var config = {
      length: 2,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "A0");
  });
  t.it("length=3", function() {
    var config = {
      length: 3,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "A0ｱ");
  });
  t.it("length=30", function() {
    var config = {
      length: 30,
      A: "A",
      N: "0",
      K: "ｱ"
    };
    assert.equal(ank.generate(config), "A0ｱA0ｱA0ｱA0ｱA0ｱA0ｱA0ｱA0ｱA0ｱA0ｱ");
  });
});
