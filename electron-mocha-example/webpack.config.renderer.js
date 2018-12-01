const HtmlWebpackPlugin = require('html-webpack-plugin');

const path = require('path');
const process = require('process');
const fs = require('fs');

module.exports = {
  entry: {
    'development/renderer-process/index': './src/renderer-process/index.js'
  },
  node: {
    __dirname: false
  },
  mode: 'development',
  output: {
    path: path.resolve(__dirname, 'dist')
  },
  plugins: [
    new HtmlWebpackPlugin({
      inject: 'head',
      template: './src/renderer-process/index.html',
      filename: './development/renderer-process/index.html',
    })
  ],
  target: 'electron-renderer'
};
