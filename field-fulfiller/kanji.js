/*global module:false, require:false*/

var generateKanji, generateKanjiHelper, generateA;

/**
 * @function
 * @param {KanjiConfig} config
 * @return {string}
 */
generateKanji = function(config) {
  var newConfig,
      doubleWidthLength = Math.floor(config.length / 2);

  newConfig = JSON.parse(JSON.stringify(config));
  newConfig.length = doubleWidthLength;

  return generateKanjiHelper(newConfig).join("");
};

generateKanjiHelper = function(config, i = 0) {
  var newConfig;

  if (config.length <= 0) {
    return [];
  }

  newConfig = JSON.parse(JSON.stringify(config));
  newConfig.length = config.length - 1;

  if (i % 3 == 0) {
    return [config.Hiragana].concat(generateKanjiHelper(newConfig, i + 1));
  } else if (i % 3 == 1) {
    return [config.Katakana].concat(generateKanjiHelper(newConfig, i + 1));
  } else  {
    return [config.Kanji].concat(generateKanjiHelper(newConfig, i + 1));
  }
};

module.exports = {
  generate: generateKanji
};

/**
 * @typedef KanjiConfig
 * @prop {Number} length the length of half-width characters
 * @prop {String} Hiragana
 * @prop {String} Katakana
 * @prop {String} Kanji
 */
