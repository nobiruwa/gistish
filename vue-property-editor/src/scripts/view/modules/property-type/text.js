'use strict';

import checkValidity from './check-validity.js';

export default {
  mixins: [checkValidity],
  data: function() {
    return {
      value: ''
    };
  },
  created: function() {
    this.value = this.initialValue;
  },
  template: '<input ref="el" type="text" :pattern="attributes.pattern" :required="attributes.required" :value="value" @focus="check" @blur="check" @input="emitChange">'
};
