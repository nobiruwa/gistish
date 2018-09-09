/*global Vue:false */
'use strict';

import propertyColorPicker from './modules/property/color-picker.js';
import propertyText from './modules/property/text.js';
import propertyFactory from './modules/property/factory.js';
import propertyEditor from './modules/property-editor.js';

import boxEditor from './modules/box-editor.js';

Vue.component('property-color-picker', propertyColorPicker);
Vue.component('property-text', propertyText);
Vue.component('property-factory', propertyFactory);
Vue.component('property-editor', propertyEditor);

Vue.component('box-editor', boxEditor);
