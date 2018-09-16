'use strict';

import checkValidity from './check-validity.js';

export default {
  mixins: [checkValidity],
  data: function() {
    return {
      colorPickerValue: '',
      colorPickerDefaultValue: '#000000',
      value: ''
    };
  },
  created: function() {
    this.colorPickerValue = this.initialValue ? this.initialValue : this.colorPickerDefaultValue;
    this.value = this.initialValue;
  },
  template: `<span>
<span>{{ preview }}</span><input ref="el" type="color" :required="attributes.required" @focus="check" @blur="check" :value="colorPickerValue" @change="colorPickerChange"><button @click.prevent.stop="setDefault">リセット</button></span>`,
  computed: {
    preview: function() {
      return this.value ? this.value : "(既定色)";
    }
  },
  methods: {
    colorPickerChange: function(event) {
      if (this.colorPickerValue !== event.target.value) {
        console.log('change!' + this.colorPickerValue + ' -> ' + event.target.value);
        this.colorPickerValue = event.target.value;
        if (this.value !== this.colorPickerValue) {
          console.log('change!' + this.value + ' -> ' + this.colorPickerValue);
          this.value = this.colorPickerValue;
          console.log('change!' + this.value);
          this.emitChange(event);
        }
      }
    },
    setDefault: function(event) {
      this.colorPickerValue = this.colorPickerDefaultValue;
      if (this.value !== "") {
        this.value = "";
        this.emitChange(event);
      }
    }
  }
};
