/* global module:false, require:false */
const assert = require('assert');
const uuid = require('uuid');
const path = require('path');
const fs = require('fs');
const xmldom = require('xmldom');
const Mustache = require('mustache');

const dom = xmldom.DOMParser;
const serializer = xmldom.XMLSerializer;

function readTemplate(infra, templateDirectory, templateName) {
  const templatePath = path.join(
    templateDirectory,
    templateName.endsWith('.html') ? templateName : `${templateName}.html`,
  );

  infra.throwFileNotFound(templatePath);

  return fs.readFileSync(templatePath, { encoding: 'utf-8' });
}

function render(template, data) {
  return Mustache.render(template, data);
}

function saveDocument(documentString, path) {
  fs.writeFileSync(path, documentString, { encoding: 'utf-8' });
}

function create(infra, serverConfig, request) {
  assert(request.templateName);
  assert(request.data);

  const key = request.key || uuid();
  const { templateName, data } = request;
  const { outputDirectory,
          templateDirectory,
          commonStyleName,
        } = serverConfig;
  const dirPath = path.join(outputDirectory, 'html', key);

  infra.throwIfDirectoryAlreadyExists(dirPath);

  // TODO SVGを作成する
  const template = readTemplate(infra, templateDirectory, templateName);
  const renderedDocument = render(template, data);

  infra.createDirectoryIfNotExists(dirPath);

  const filePath = path.join(dirPath, 'report.html');

  saveDocument(renderedDocument, filePath);

  return {
    key,
    filePath,
  };
}

module.exports = {
  create,
};
