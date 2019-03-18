export default {
  props: ['columns', 'rows', 'font-size'],
  computed: {
    width() {
      return (this.fontSize / 2) * this.columns;
    },
    height() {
      return this.fontSize * this.rows;
    },
    style() {
      return `width: ${this.width}px;height: ${this.height}px;`;
    },
  },
  template: `\
<div class="main-area">
  <div
    class="main-area-children"
    :style="style"
  >
  </div>
</div>`,
};
