/*global module:false, require:false*/

var ANK;

/**
 * @constructor
 * @param {ANKConfig} config
 */
ANK = function(config) {
  this.config = config;
};

ANK.prototype.generateANK = function(length, i = 0) {
  if (length <= 0) {
    return [];
  }
  if (i % 3 == 0) {
    return [this.generateA()].concat(this.generateANK(length - 1, i + 1));
  } else if (i % 3 == 1) {
    return [this.config.N].concat(this.generateANK(length - 1, i + 1));
  } else if (i % 3 == 2) {
    return [this.config.K].concat(this.generateANK(length - 1, i + 1));
  }
  return [];
};

ANK.prototype.generateA = function() {
  if (this.config.hasSmallA) {
    return this.config.A.toLowerCase();
  } else {
    return this.config.A.toUpperCase();
  }
};

module.exports = {
  ANK: ANK
};

/**
 * @typedef ANKConfig
 * @prop {Boolean} hasSmallA
 * @prop {String} A
 * @prop {String} N
 * @prop {String} K
 */
