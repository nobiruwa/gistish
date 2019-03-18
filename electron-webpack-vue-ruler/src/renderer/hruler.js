export default {
  props: ['columns', 'rows', 'font-size'],
  data() {
    return {
      scrollLeft: 0,
    };
  },
  mounted() {
    const mainArea = this.$el.parentElement.querySelector('.main-area');
    mainArea.addEventListener('scroll', this.handleScroll);
  },
  beforeDestroy() {
    const mainArea = this.$el.parentElement.querySelector('.main-area');
    mainArea.removeEventListener('scroll', this.handleScroll);
  },
  computed: {
    style() {
      return `left: ${-this.scrollLeft}px;`;
    },
    unit() {
      return (this.fontSize / 2);
    },
    length() {
      return this.unit * this.columns;
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
          offset: i * this.unit + 2,
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
      this.scrollLeft = e.target.scrollLeft;
    },
  },
  template: `\
<div class="main-area-ruler main-area-ruler-hruler">
  <svg
    :width="length"
    :style="style"
  >
    <line
      v-for="step in steps"
      :class="step.class"
      :x1="step.offset"
      :x2="step.offset"
      :y1="30 - step.length"
      :y2="30"
      stroke="black"
      :stroke-width="step.width"
    ></line>
    <text
      v-for="text in stepTexts"
      :class="text.class"
      :x="text.offset"
      :y="30 - 10"
    >{{ text.text }}</text>
  </svg>
</div>`,
};
