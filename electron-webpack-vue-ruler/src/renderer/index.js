/* global Vue */
'use strict';

import './index.css';
import HRuler from './hruler.js';
import VRuler from './vruler.js';
import MainArea from './main-area.js';

const vueScript = document.createElement('script');

function init() {
  Vue.config.devtools = false;
  Vue.config.productionTip=false;

  new Vue({
    components: {
      HRuler,
      VRuler,
      MainArea,
    },
    data: {
      columns: 60,
      rows: 30,
      fontSize: 24,
    },
    methods:{
    },
    template:`\
<div class="content">
  <div class="main-area-ruler-cube"></div>
  <h-ruler :columns="columns" :rows="rows" :font-size="fontSize"></h-ruler>
  <v-ruler :columns="columns" :rows="rows" :font-size="fontSize"></v-ruler>
  <main-area :columns="columns" :rows="rows" :font-size="fontSize"></main-area>
</div>`
  }).$mount('#app');
}

vueScript.setAttribute('type','text/javascript');
vueScript.setAttribute('src','https://unpkg.com/vue');
vueScript.onload = init;
document.head.appendChild(vueScript);
