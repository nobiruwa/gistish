'use strict';

import checkValidity from './check-validity.js';

export default {
  mixins: [checkValidity],
  template: '<span><select ref="el" v-model="value"><option v-for="option in attributes.options">{{ option.value }}:{{ option.description }}</option></select></span>'
};
