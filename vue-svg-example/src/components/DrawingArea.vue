<template>
<div>
  <svg :class="{ 'line-append-mode': appendingLine, 'line-select-mode': !appendingLine }"
       ref="el"
       @mousedown="onMousedown"
       >
    <g>
      <line v-if="mousedown && mousemove"
            :x1="mousedown.x"
            :y1="mousedown.y"
            :x2="mousemove.x"
            :y2="mousemove.y"
            stroke="blue"
            ></line>
    </g>
    <g>
      <vml-line v-for="line in lines"
                :mode="mode"
                :key="line._key"
                :x1="line.x1"
                :y1="line.y1"
                :x2="line.x2"
                :y2="line.y2"
                :stroke="line.stroke"
                :stroke-width="line.strokeWidth"
          :stroke-dasharray="line.strokeDasharray"
          ></vml-line>
  </g>
  </svg>
  <span v-if="mousedown">{{ mousedown.x }} {{ mousedown.y }}</span>
  <span v-if="!mousedown">mousedown == null;</span>
  <span v-if="mousemove">{{ mousemove.x }} {{ mousemove.y }}</span>
  <span v-if="!mousemove">mousemove == null;</span>
</div>
</template>

<script>
import uuid from 'uuid';
import VmlLine from './Line';

export default {
  components: {
    VmlLine,
  },
  props: ['mode', 'height', 'width'],
  data() {
    return {
      mousedown: null,
      mousemove: null,
      defaultLine: {
        stroke: '#000000',
        strokeWidth: '1px',
        strokeDasharray: '0',
      },
      lines: [],
    };
  },
  computed: {
    appendingLine() {
      return !!(this.mode === 'line-append-mode');
    },
  },
  methods: {
    appendLine(x1, y1, x2, y2, stroke, strokeWidth, strokeDasharray) {
      this.lines.push({
        _key: uuid(),
        x1,
        y1,
        x2,
        y2,
        stroke,
        strokeWidth,
        strokeDasharray,
      });
    },
    onMousedown(e) {
      if (this.appendingLine && e.button === 0) {
        this.mousedown = {
          x: e.offsetX,
          y: e.offsetY,
        };

        // マウスは描画領域の外に出ることがあるため、
        // windowにイベントリスナーを追加する
        // イベントリスナー内部のthisを外側のthisとそろえたいために、
        // アロー関数式を使ってこのメソッド内でイベントリスナーを定義する
        const onWindowMousemove = (ev) => {
          if (this.appendingLine && ev.button === 0) {
            const sr = this.$refs.el.getBoundingClientRect();
            this.mousemove = {
              x: ev.pageX - sr.left,
              y: ev.pageY - sr.top,
            };
          } else {
            this.mousedown = null;
            this.mousemove = null;
          }
        };

        const onWindowMouseup = (ev) => {
          if (this.appendingLine && ev.button === 0) {
            if (this.mousedown && this.mousemove) {
              this.appendLine(
                this.mousedown.x,
                this.mousedown.y,
                this.mousemove.x,
                this.mousemove.y,
                this.defaultLine.stroke,
                this.defaultLine.strokeWidth,
                this.defaultLine.strokeDasharray,
              );
            }
          }

          this.mousedown = null;
          this.mousemove = null;

          /* eslint-disable-next-line */
          removeWindowEventListners();
        };

        /* eslint-disable-next-line */
        const onWindowMouseleave = removeWindowEventListners;

        const removeWindowEventListners = () => {
          window.removeEventListener('mousemove', onWindowMousemove);
          window.removeEventListener('mouseleave', onWindowMouseleave);
          window.removeEventListener('mouseup', onWindowMouseup);
        };

        window.addEventListener('mousemove', onWindowMousemove);
        window.addEventListener('mouseleave', onWindowMouseleave);
        window.addEventListener('mouseup', onWindowMouseup);
      } else {
        this.mousedown = null;
      }
    },
  },
};
</script>

<style>
svg {
    background-color: transparent;
    border: solid #898887 1px;
}
</style>
