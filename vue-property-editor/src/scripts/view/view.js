/*global Vue:false */
'use strict';

import {} from './component.js';

let _viewModel;

function jsonSafeCopy(obj) {
  return JSON.parse(JSON.stringify(obj));
}

function toViewModel(model) {
  return jsonSafeCopy(model);
}

export function create(selector, model) {
  _viewModel = toViewModel(model);
  new Vue({
    el: selector,
    data: _viewModel
  });
}

export function update(model) {
  _viewModel.fields = toViewModel(model).fields;
}

export default  {
  create: create,
  update: update,
  currentViewModel: () => _viewModel
};
