/*global require:false */
var money = require("../money"),
    t = require("mocha"),
    assert = require("assert");

var testcase = [
  [0, true, 0, ""],
  [0, true, 1, ""],
  [0, true, 2, ""],
  [0, false, 0, ""],
  [0, false, 1, ""],
  [0, false, 2, ""],
  [1, true, 0, "1"],
  [1, true, 1, "1"],
  [1, true, 2, "1"],
  [1, false, 0, "1"],
  [1, false, 1, "1"],
  [1, false, 2, "1"],
  [2, true, 0, "-1"],
  [2, true, 1, "-1"],
  [2, true, 2, "-1"],
  [2, false, 0, "10"],
  [2, false, 1, "10"],
  [2, false, 2, "10"],
  [3, true, 0, "-10"],
  [3, true, 1, "1.1"],
  [3, true, 2, "1.1"],
  [3, false, 0, "100"],
  [3, false, 1, "1.1"],
  [3, false, 2, "1.1"],
  [10, true, 0, "-100000000"],
  [10, true, 1, "-1000000.1"],
  [10, true, 2, "-100000.11"],
  [10, false, 0, "1000000000"],
  [10, false, 1, "10000000.1"],
  [10, false, 2, "1000000.11"]
];

t.describe("money", function() {
  testcase.forEach(function(testcase) {
    var config = {
      length: testcase[0],
      isNegative: testcase[1],
      decimalPartLength: testcase[2]
    };
    t.it(JSON.stringify(config), function() {
      assert.equal(money.generate(config), testcase[3]);
    });
  });
});
