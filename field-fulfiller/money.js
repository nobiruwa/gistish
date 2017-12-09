/*global module:false, require:false*/

const NEGATIVE = "-",
      ONE = "1",
      ZERO = "0",
      PERIOD = ".";

var generateMoney, generateInteger, replicate;

generateMoney = function(config) {
  if (config.length <= 2) {
    // 桁が少ないので、整数を作成する
    return generateInteger(config.isNegative, config.length).join("");
  } else if (config.decimalPartLength <= 0) {
    // 整数を作成する
    return generateInteger(config.isNegative, config.length).join("");
  } else if (config.length - config.decimalPartLength <= 1) {
    // 整数部も小数部も最低1桁は消費する
    // 小数点部から整数部へ1桁移す
    return generateInteger(
      config.isNegative,
      config.length - config.decimalPartLength
    ).concat(
      [PERIOD]
    ).concat(
      replicate(config.decimalPartLength - 1, ONE)
    ).join("");
  } else {
    // 小数点に1桁消費し、残りを整数部と小数部で消費する
    return generateInteger(
      config.isNegative,
      config.length - config.decimalPartLength - 1
    ).concat(
      [PERIOD]
    ).concat(
      replicate(config.decimalPartLength, ONE)
    ).join("");
  }
};

generateInteger = function(isNegative, length) {
  if (length <= 0) {
    return [];
  } else if (length == 1) {
    // 桁が少ないので、負数入力可であっても正数を作る
    return [ONE];
  } else if (isNegative) {
    return [NEGATIVE, ONE].concat(replicate(length - 2, ZERO));
  } else {
    return [ONE].concat(replicate(length - 1, ZERO));
  }
};

replicate = function(length, element) {
  var arr = [];

  for(var i = 0; i < length; i++) {
    arr.push(element);
  }
  return arr;
};

module.exports = {
  generate: generateMoney
};

/**
 * @typedef MoneyConfig
 * @prop {boolean} isNegative
 * @prop {number} decimalPartLength
 * @prop {number} length
 */
