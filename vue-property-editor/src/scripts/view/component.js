/*global Vue:false */
'use strict';

import propertyTypeColorPicker from './modules/property-type/color-picker.js';
import propertyTypeSingleList from './modules/property-type/single-list.js';
import propertyTypeText from './modules/property-type/text.js';

import propertyLabel from './modules/property-label.js';

import propertyItemFieldName from './modules/property-item/field-name.js';
import propertyItemBackgroundColor from './modules/property-item/background-color.js';

import propertyEditorBox from './modules/property-editor/box.js';
import propertyEditorListBox from './modules/property-editor/list-box.js';

import propertyEditorFactory from './modules/property-editor-factory.js';

import fieldDelimiter from './modules/dummy-field/delimiter.js';

Vue.component('property-type-color-picker', propertyTypeColorPicker);
Vue.component('property-type-single-list', propertyTypeSingleList);
Vue.component('property-type-text', propertyTypeText);

Vue.component('property-label', propertyLabel);

Vue.component('property-item-field-name', propertyItemFieldName);
Vue.component('property-item-background-color', propertyItemBackgroundColor);

Vue.component('property-editor-box', propertyEditorBox);
Vue.component('property-editor-list-box', propertyEditorListBox);

Vue.component('property-editor-factory', propertyEditorFactory);

Vue.component('field-delimiter', fieldDelimiter);
