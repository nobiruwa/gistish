'use strict';

import view from '../view/view.js';
import initialModel from '../model/initial-model.js';
import alternativeModel from '../model/alternative-model.js';

let _currentModel;

export function run(model=initialModel) {
  _currentModel = model;
  view.create('#app', _currentModel);
}

export function toggle() {
  _currentModel = _currentModel === initialModel ? alternativeModel : initialModel;
  view.update(_currentModel);
}

export default {
  run: run,
  toggle: toggle
};
