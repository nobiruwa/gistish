'use strict';

export default {
  props: ['item'],
  template: '<span>{{ item.value }}<input ref="el" type="color" :required="item.required" v-model="item.value"></span>',
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
