'use strict';

import EventBus from '../event-bus.js';

export default {
  props: ['attributes', 'initialValue', 'propertyName'],
  data: function() {
    return {
      isValid: false,
      validity: null
    };
  },
  methods: {
    check: function() {
      this.isValid = this.$refs.el.checkValidity();
      this.validity = this.$refs.el.validity;
    },
    emitChange: function(event) {
      EventBus.$emit('propertyItemChange', {
        name: this.$options.name,
        isValid: this.isValid,
        value: this.value
      });
    }
  }
};
