'use strict';

export default {
  items: [
    {
        label: 'フィールド名称',
        type: 'text',
        pattern: '[0-9]{0,10}',
        required: true,
        validity: null,
        value: ""
    },
    {
      label: '前景色',
      type: 'color-picker',
      validity: null,
      value: ""
    }
  ]
};
