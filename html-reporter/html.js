/* global module:false, require:false */
const assert = require('assert');
const uuid = require('uuid');
const path = require('path');
const fs = require('fs');
const { JSDOM } = require('jsdom');
const Mustache = require('mustache');

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

function replaceLinkToStyle(infra, templateDirectory, documentString) {
  const dom = new JSDOM(documentString);
  const document = dom.window.document;
  const links = document.querySelectorAll('link[rel=stylesheet]');

  for (var i = 0; i < links.length; i++) {
    const link = links[i];

    const cssPath = path.resolve(templateDirectory, link.getAttribute('href'));

    if (infra.exists(cssPath)) {
      const cssContent = fs.readFileSync(cssPath, { encoding: 'utf-8' });

      const style = document.createElement('style');
      style.setAttribute('type', 'text/css');
      style.innerHTML = cssContent;

      link.parentNode.replaceChild(style, link);
    } else {
      const comment = document.createComment(`[${cssPath}] not found.`);

      link.parentNode.replaceChild(comment, link);
    }
  }

  return dom.serialize();
}

function embedCSS(infra, templateDirectory, document) {
  return replaceLinkToStyle(infra, templateDirectory, document);
}

function replaceToEmbeddedImage(infra, templateDirectory, extension, mimeType, documentString) {
  const dom = new JSDOM(documentString);
  const document = dom.window.document;
  const imgs = document.querySelectorAll(`img[src$=${extension}]`);

  for (var i = 0; i < imgs.length; i++) {
    const img = imgs[i];

    const imagePath = path.resolve(templateDirectory, img.getAttribute('src'));

    if (infra.exists(imagePath)) {
      const imageContent = fs.readFileSync(imagePath, null);
      const imageBase64 = imageContent.toString('base64');

      const newImg = document.createElement('img');
      newImg.setAttribute('id', img.getAttribute('id'));
      newImg.setAttribute('class', img.getAttribute('class'));
      newImg.setAttribute('src', `data:${mimeType};base64,${imageBase64}`);

      img.parentNode.replaceChild(newImg, img);
    } else {
      const comment = document.createComment(`[${imagePath}] not found.`);

      img.parentNode.replaceChild(comment, img);
    }
  }

  return dom.serialize();
}

function embedImage(infra, templateDirectory, extension, mimeType, document) {
  return replaceToEmbeddedImage(infra, templateDirectory, extension, mimeType, document);
}

function create(infra, serverConfig, request) {
  assert(request.templateName);
  assert(request.data);

  const key = request.key || uuid.v4();
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
  const cssEmbeddedDocumentString = embedCSS(infra, templateDirectory, renderedDocument);
  const cssImageEmbeddedDocumentString = embedImage(infra, templateDirectory, 'png', 'mage/png', cssEmbeddedDocumentString);

  infra.createDirectoryIfNotExists(dirPath);

  const filePath = path.join(dirPath, 'report.html');

  saveDocument(cssImageEmbeddedDocumentString, filePath);

  return {
    key,
    filePath,
  };
}

module.exports = {
  create,
};
