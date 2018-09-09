'use strict';

export default {
  props: ['item'],
  template: '<input ref="el" :type="item.type" :pattern="item.pattern" :required="item.required" v-model="item.value">',
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
