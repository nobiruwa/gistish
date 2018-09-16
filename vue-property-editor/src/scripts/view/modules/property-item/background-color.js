'use strict';

export default {
  props: ['value'],
  data: function() {
    return {
      label: '背景色',
      attributes: {
        required: false
      }
    };
  },
  template: `<div>
<property-label :label="label"></property-label><property-type-color-picker :attributes="attributes" :initialValue="value" :propertyName="$options.name"></property-type-color-picker>
</div>`
};
