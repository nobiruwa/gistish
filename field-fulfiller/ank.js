/*global module:false, require:false*/

var generateANK, generateANKHelper, generateA;

/**
 * @function
 * @param {ANKConfig} config
 */
generateANK = function(config) {
  return generateANKHelper(config);
};

generateANKHelper = function(config, i = 0) {
  var newConfig;

  if (config.length <= 0) {
    return [];
  }

  newConfig = JSON.parse(JSON.stringify(config));
  newConfig.length = newConfig.length - 1;

  if (i % 3 == 0) {
    return [generateA(config.A, config.hasSmallA)].concat(generateANKHelper(newConfig, i + 1));
  } else if (i % 3 == 1) {
    return [config.N].concat(generateANKHelper(config.length - 1, i + 1));
  } else  {
    return [config.K].concat(generateANKHelper(config.length - 1, i + 1));
  }
};

generateA = function(A, hasSmallA) {
  if (hasSmallA) {
    return A.toLowerCase();
  } else {
    return A.toUpperCase();
  }
};

module.exports = {
  generate: generateANK
};

/**
 * @typedef ANKConfig
 * @prop {Boolean} hasSmallA
 * @prop {Number} length
 * @prop {String} A
 * @prop {String} N
 * @prop {String} K
 */
