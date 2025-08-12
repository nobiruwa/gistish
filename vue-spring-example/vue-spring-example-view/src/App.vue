<template>
  <v-app>
    <app-header></app-header>
    <router-view/>
    <app-footer></app-footer>
  </v-app>
</template>

<script>
import AppFooter from '@/components/AppFooter.vue';
import AppHeader from '@/components/AppHeader.vue';

export default {
  name: 'App',
  components: {
    'app-header': AppHeader,
    'app-footer': AppFooter,
  },
  data: () => ({
    //
  }),
};
</script>
