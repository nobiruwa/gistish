'use strict';

import EventBus from './event-bus.js';

export default {
  props: ['fields'],
  data: function() {
    return {
      dirty: false,
      updateFields: []
    };
  },
  created: function() {
    EventBus.$on("propertyItemChange", this.saveHistory);
  },
  template: `<div @propertyItemChange="saveHistory">
<div v-if="fields.length <= 1">
<h1>{{ fields[0].fieldName }}</h1>
<property-editor-box v-if="fields[0].type === 'box'" :field="fields[0]"></property-editor-box>
<property-editor-list-box v-if="fields[0].type === 'list-box'" field="fields[0]"></property-editor-list-box>
</div>
<div v-if="fields.length > 1" >
Unimplemented
</div>
</div>`,
  methods: {
    saveHistory: function() {
      this.dirty = true;
      console.log(arguments);
    }
  }
};
