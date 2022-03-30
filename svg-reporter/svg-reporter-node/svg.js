/* global module:false, require:false */
const assert = require('assert');
const uuid = require('uuid');
const path = require('path');
const fs = require('fs');
const xpath = require('xpath');
const xmldom = require('@xmldom/xmldom');

const dom = xmldom.DOMParser;
const serializer = xmldom.XMLSerializer;

const namespaces = {
  svg: 'http://www.w3.org/2000/svg',
  xhtml: 'http://www.w3.org/1999/xhtml',
};

const select = xpath.useNamespaces(namespaces);

function readTemplate(infra, templateDirectory, templateName) {
  const templatePath = path.join(
    templateDirectory,
    templateName.endsWith('.svg') ? templateName : `${templateName}.svg`,
  );

  infra.throwFileNotFound(templatePath);

  return fs.readFileSync(templatePath, { encoding: 'utf-8' });
}

function readStyle(infra, styleDirectory, styleName) {
  const stylePath = path.join(
    styleDirectory,
    styleName.endsWith('.css') ? styleName : `${styleName}.css`,
  );

  if (infra.exists(stylePath)) {
    return fs.readFileSync(stylePath, { encoding: 'utf-8' });
  } else {
    return '';
  }
}


function nodeListToObject(nodeList) {
  const obj = {};

  for (let i = 0; i < nodeList.length; i++) {
    const node = nodeList[i];
    const id = node.getAttribute('id');

    obj[id] = node;
  }

  return obj;
}

function extractFromPlaceholder(node) {
  const x = node.getAttribute('x');
  const y = node.getAttribute('y');
  const width = node.getAttribute('width');
  const height = node.getAttribute('height');

  const desc = select('./svg:desc', node);
  assert(desc.length > 0);

  const classList = desc[0].textContent;

  return {
    x,
    y,
    width,
    height,
    classList,
  };
}

function appendChildren(parent, children) {

  for (let i = 0; i < children.length; i++) {
    // 異なるドキュメントからノードをコピーするので、
    // cloneNode(true)が必要
    parent.appendChild(children[i].cloneNode(true));
  }

}

function parseHtml(value) {
  const doc = new dom().parseFromString(`<div>${value}</div>`);

  const elements = xpath.select('//*', doc);

  for (let i = 0; i < elements.length; i++) {
    elements[i].namespaceURI = namespaces.xhtml;
  }

  return {
    doc,
    children: doc.documentElement.childNodes,
  };
}

function createForeignObject(document, key, styles, value) {
  const { tempDoc, children } = parseHtml(value);

  const div = document.createElementNS(namespaces.xhtml, 'div');
  div.setAttribute('id', key);
  div.setAttribute('class', styles.classList);
  appendChildren(div, children);

  const foreignObject = document.createElementNS(namespaces.svg, 'foreignObject');
  foreignObject.setAttribute('x', styles.x);
  foreignObject.setAttribute('y', styles.y);
  foreignObject.setAttribute('width', styles.width);
  foreignObject.setAttribute('height', styles.height);
  foreignObject.appendChild(div);

  return foreignObject;
}

function appendNode(parent, child) {
  parent.appendChild(child);
}

function removeElement(node) {
  node.parentNode.removeChild(node);
}

function embedStyles(doc, styles) {
  styles.forEach((value) => {
    const style = doc.createElementNS(namespaces.svg, 'style');
    style.textContent = value;

    appendNode(doc.documentElement, style);
  });
}

function render(template, data, styles) {
  const doc = new dom().parseFromString(template);
  const nodes = nodeListToObject(select('//svg:rect[starts-with(@id, "placeholder_")]', doc));

  // nodesのプロパティ数 <= dataのプロパティ数
  assert(Object.keys(nodes).length <= Object.keys(data).length);

  const root = doc.documentElement;

  Object.entries(data).forEach(([key, value]) => {
    const node = nodes[`placeholder_${key}`];

    assert(node);

    const styles = extractFromPlaceholder(node);
    const foreignObject = createForeignObject(doc, key, styles, value);

    appendNode(root, foreignObject);
    removeElement(node);
  });

  embedStyles(doc, styles);

  return doc;
}

function saveDocument(document, path) {
  const ser = new serializer();
  const str = ser.serializeToString(document.documentElement);

  fs.writeFileSync(path, str, { encoding: 'utf-8' });
}

function create(infra, serverConfig, request) {
  assert(request.templateName);
  assert(request.data);

  const key = request.key || uuid();
  const { templateName, data } = request;
  const { outputDirectory,
          templateDirectory,
          styleDirectory,
          commonStyleName,
        } = serverConfig;
  const dirPath = path.join(outputDirectory, 'svg', key);
  const svgStyleDirectory = path.join(styleDirectory, 'svg');

  infra.throwIfDirectoryAlreadyExists(dirPath);

  // TODO SVGを作成する
  const template = readTemplate(infra, templateDirectory, templateName);
  const commonStyle = readStyle(infra, svgStyleDirectory, commonStyleName);
  const specificStyle = readStyle(infra, svgStyleDirectory, templateName);
  const renderedDocument = render(template, data, [commonStyle, specificStyle]);

  infra.createDirectoryIfNotExists(dirPath);

  const filePath = path.join(dirPath, 'report.svg');

  saveDocument(renderedDocument, filePath);

  return {
    key,
    filePath,
  };
}

module.exports = {
  create,
};
