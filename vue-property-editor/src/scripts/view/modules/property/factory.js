'use strict';

export default {
  props: ['item'],
  template: `<div>
  <label>{{ item.label }}
    <property-color-picker v-if="item.type==='color-picker'" v-bind:item="item"></property-color-picker>
    <property-text v-if="item.type==='text'" v-bind:item="item"></property-text>
  </label>
</div>`
};
