'use strict';

export default {
  props: ['value'],
  data: function() {
    return {
      label: 'フィールド名称',
      attributes: {
        pattern: '[0-9a-zA-Z]{8}',
        required: true
      }
    };
  },
  template: `<div>
<property-label :label="label"></property-label><property-type-text :attributes="attributes" :initialValue="value" :propertyName="$options.name"></property-type-text>
</div>`
};
