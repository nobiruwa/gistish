export default {
  props: ['columns', 'rows', 'font-size'],
  data() {
    return {
      scrollTop: 0,
    };
  },
  computed: {
    style() {
      return `top: ${-this.scrollTop}px;`;
    },
    unit() {
      return this.fontSize;
    },
    length() {
      return this.unit * this.rows;
    },
    steps() {
      return this.makeSteps();
    },
    stepTexts() {
      return this.makeStepTexts();
    },
  },
  methods: {
    makeStepTexts() {
      const texts = [];

      for (var i = 10; i < this.columns; i += 10) {
        texts.push({
          class: 'ruler-step-text',
          offset: i * this.unit + 15,
          text: i,
        });
      }

      return texts;
    },
    makeSteps() {
      const steps = [];

      for (let i = 1; i < this.columns; i++) {
        if (i % 10 === 0) {
          steps.push({
            class: 'ruler-step ruler-step-10',
            length: 20,
            index: i,
            offset: i * this.unit,
            width: 1,
          });
        } else if (i % 5 === 0) {
          steps.push({
            class: 'ruler-step ruler-step-5',
            length: 10,
            index: i,
            offset: i * this.unit,
            width: 1,
          });
        } else {
          steps.push({
            class: 'ruler-step ruler-step-1',
            length: 5,
            index: i,
            offset: i * this.unit,
            width: 1,
          });
        }
      }

      return steps;
    },
    handleScroll(e) {
      this.scrollTop = e.target.scrollTop;
    },
  },
  template: `\
<div class="main-area-ruler main-area-ruler-vruler">
  <svg
    :height="length"
    :style="style"
  >
    <line
      v-for="step in steps"
      :class="step.class"
      :y1="step.offset"
      :y2="step.offset"
      :x1="30 - step.length"
      :x2="30"
      stroke="black"
      :stroke-width="step.width"
    ></line>
    <text
      v-for="text in stepTexts"
      :class="text.class"
      :x="0"
      :y="text.offset"
    >{{ text.text }}</text>
  </svg>
</div>`,
};
