<template>
<div id="app">
  <select v-model="selectedMode">
    <option v-for="mode in modes"
            :key="mode.name"
            :value="mode.value">{{ mode.description }}</option>
  </select>
  <drawing-area :mode="selectedMode" :width="width" :height="height"></drawing-area>
</div>
</template>

<script>
import DrawingArea from './components/DrawingArea';

export default {
  name: 'App',
  components: {
    DrawingArea,
  },
  data() {
    return {
      width: 800,
      height: 600,
      selectedMode: 'line-select-mode',
      modes: [
        {
          name: 'line-append-mode',
          description: 'Line Append Mode',
          value: 'line-append-mode',
        },
        {
          name: 'line-select-mode',
          description: 'Line Select Mode',
          value: 'line-select-mode',
        },
      ],
    };
  },
};
</script>

<style>
#app {
}
</style>
