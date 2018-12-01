const path = require('path');
const process = require('process');
const fs = require('fs');

module.exports = {
  entry: {
    'development/main-process/main': './src/main-process/main.js'
  },
  node: {
    __dirname: false
  },
  mode: 'development',
  output: {
    path: path.resolve(__dirname, 'dist')
  },
  target: 'electron-main'
};
