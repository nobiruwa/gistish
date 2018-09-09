/*global Vue:false */
'use strict';

export default {
  props: ['items'],
  template: `<div>
<property-factory
  v-for="(item, index) in items"
  v-bind:key="index"
  v-bind:item="item"></property-factory>
</div>`,
  computed: {
    validity: function() {
      return this.$refs.el.validity;
    }
  },
  methods: {
    check: function() {
      console.log(this.validity);
    }
  }
};
