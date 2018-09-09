/*global Vue:false */
'use strict';

import box from './modules/box-editor.js';
import {} from './component.js';

let _viewModel;

function jsonSafeCopy(obj) {
  return JSON.parse(JSON.stringify(obj));
}

function toViewModel(model) {
  if (model.type === 'box') {
    let props = jsonSafeCopy(box);

    // フィールド名称
    props.items[0].value = model.value;
    // 前景色
    props.items[1].value = model.foregroundColor;

    return props;
  } else {
    throw `Unknown Model Type Error. Expected a string value such as "box", but ${type}`;
  }
}

export function create(selector, model) {
  _viewModel = toViewModel(model);

  new Vue({
    el: selector,
    data: _viewModel
  });
}

export function update(model) {
  _viewModel.items = toViewModel(model).items;
}

export default  {
  create: create,
  update: update
};
