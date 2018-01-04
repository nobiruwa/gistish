/*global require:false */

const assert = require('assert');
const webdriver = require('selenium-webdriver');
const By = webdriver.By;
const driver = new webdriver.Builder().forBrowser('firefox').build();

const expected = require('fs').readFileSync('public/answer.txt', 'utf-8').replace('\n', '');

const atob = function(base64) {
  return decodeURIComponent(Buffer(base64, 'base64').toString('ascii'));
};

driver.get('http://localhost:5000/public/index.html').then(function() {
  const clientFunc = function() {
    var element = document.getElementById('paragraph');
    var text = element.innerHTML;
    return btoa(encodeURIComponent(text));
  };

  return Promise.all([
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc),
    driver.executeScript(clientFunc)
  ]);
}).then(function(actuals) {
  actuals.forEach(function(base64) {
    const actual = atob(base64);
    if (expected.length < actual.length) {
      var invalids = actual.replace(/あ|い|う|え|お|/g, '');
      for(var i = 0; i < invalids.length; i++) {
        console.log(invalids.charCodeAt(i));
      }
    }
    assert.equal(actual, expected);
  });
}).then(function(actuals) {
  console.log('success');
  driver.quit();
}).catch(function(error) {
  console.error(error);
  driver.quit();
});
