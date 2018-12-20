const DOMParser = require('xmldom').DOMParser;

const sample = `<?xml version="1.0" encoding="UTF-8"?>
<root xmlns="foo" xmlns:b="bar">
  <foo-child a="yes" b="yes!!" c="no"></foo-child>
  <b:bar-child>
    <bar-grand-child a="yes" b="yyy"></bar-grand-child>
    <bar-grand-child c="no" d="nnn" e="0" f="1"></bar-grand-child>
  </b:bar-child>
</root>
`;

const MULTIPLES = [
  'bar-grand-child'
];

const ATTRIBUTES = {
  integer: [
    'e',
    'f'
  ],
};

const sampleDoc = new DOMParser().parseFromString(sample);

function parseAttribute(name, textValue) {
  if (ATTRIBUTES.integer.includes(name)) {
    return parseInt(textValue, 10);
  } else {
    return textValue;
  }
};

function deserialize(doc) {
  const obj = {
    root: {}
  };
  // XML宣言の保存
  if (doc.childNodes[0].target === 'xml') {
    obj.declaration = doc.childNodes[0].data;
  }

  deserializeTailCall(doc.documentElement, obj.root);

  return obj;
}

function foldDeserialize(element, acc) {
  // 属性の転写
  Array.prototype.forEach.call(element.attributes, (attr) => {
    acc[attr.name] = parseAttribute(attr.name, attr.value);
  });

  // 子要素の転写
  const childElements = extractElement(element.childNodes);
  if (childElements.length === 0) {
    // 読み込むXML文書の特徴として、テキストノードに意味を持たない
    // よってテキストノードから値を読み込まない
    return acc;
  } else {
    childElements.forEach(child => {
      const nodeName = child.nodeName;
      // 複数持ちうるか
      if (MULTIPLES.includes(nodeName)) {
        acc[nodeName] = acc[nodeName] || [];
        acc[nodeName].push(deserializeTailCall(child, {}));
      } else {
        acc[nodeName] = deserializeTailCall(child, {});
      }
    });
    return acc;
  }
}

const NODE_TYPES = {
  ELEMENT_NODE: 1,
  ATTRIBUTE_NODE: 2,
  TEXT_NODE: 3,
  PROCESSING_INSTRUCTION_NODE: 7,
};

function extractElement(nodeList) {
  return Array.prototype.filter.call(nodeList, function(node) {
    return node.nodeType === 1;
  });
}

module.exports = {
  sample,
  sampleDoc,
  deserialize,
  extractElement,
};
