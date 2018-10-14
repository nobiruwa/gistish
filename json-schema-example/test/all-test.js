/*global __dirname:false, exports:false, module:false, require:false */

const Ajv = require("ajv");
const ajv = new Ajv();
const { expect } = require("chai");
const { after, afterEach, before, beforeEach, describe, it } = require("mocha");

const fs = require("fs");
const path = require("path");

describe("all.json", function() {
  let schema = JSON.parse(fs.readFileSync(path.join("resources", "all.json"), "utf-8"));
  let originalExample = fs.readFileSync(path.join(__dirname, "all-example.json"), "utf-8");
  let target;
  let validate;

  beforeEach(function() {
    target = JSON.parse(originalExample);
  });

  describe("schema", function() {
    it("can load", function() {
      const validate = ajv.compile(schema);
      expect(typeof validate).to.be.equal("function");
    });
    it("can validate", function() {
      const validate = ajv.compile(schema);
      const valid = validate(target);
      expect(valid).to.be.true;
    });
  });

  describe("field", function() {
    beforeEach(function() {
      validate = ajv.compile(schema);
    });

    it("is array", function() {
      const valid = validate(target);
      expect(valid).to.be.true;
    });

    describe("type", function() {
      it("allows 0-16 but 9", function() {
        const valid = validate(target);
        expect(valid).to.be.true;
      });
      it("9 is not allowed", function() {
        target.xxx.fields.field.push({
          type: 9
        });
        const valid = validate(target);
        expect(valid).to.be.false;
      });
    });
  });
});
