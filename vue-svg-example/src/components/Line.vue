<template>
<line :draggable="draggable"
      :x1="x1"
      :y1="y1"
      :x2="x2"
      :y2="y2"
      :stroke="stroke"
      :stroke-width="strokeWidth"
      :stroke-dasharray="strokeDasharray"
      @mousedown="onMousedown"
      @mousemove="onMousemove"
      ></line>
</template>

<script>
export default {
  props: ['mode', 'x1', 'y1', 'x2', 'y2', 'stroke', 'stroke-width', 'stroke-dasharray'],
  data() {
    return {
      mousedown: null,
      mousemove: null,
    };
  },
  computed: {
    draggable() {
      return this.mode === 'line-select-mode';
    },
  },
  methods: {
    onMousedown(e) {
      if (e.button === 0) {
        this.mousedown = {
          x1: this.x1,
          y1: this.y1,
          x2: this.x2,
          y2: this.y2,
          x: e.offsetX,
          y: e.offsetY,
        };
      }
    },
    onMousemove(e) {
      if (this.mousedown && e.button === 0) {
        this.mousemove = {
          x: e.offsetX,
          y: e.offsetY,
        };

        const diff = {
          dx: this.mousemove.x - this.mousedown.x,
          dy: this.mousemove.y - this.mousedown.y,
        };

        this.x1 = this.mousedown.x1 + diff.dx;
        this.y1 = this.mousedown.y1 + diff.dy;
        this.x2 = this.mousedown.x2 + diff.dx;
        this.y2 = this.mousedown.y2 + diff.dy;
      } else {
        this.mousedown = null;
        this.mousemove = null;
      }
    },
  },
};
</script>

<style>
.line-select-mode line {
    cursor: grab;
}
</style>
