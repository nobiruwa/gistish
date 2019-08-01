<template>
  <v-app>
    <app-header></app-header>
    <router-view/>
    <app-footer></app-footer>
  </v-app>
</template>

<script>
import Footer from '@/components/Footer';
import Header from '@/components/Header';

export default {
  name: 'App',
  components: {
    'app-header': Header,
    'app-footer': Footer,
  },
  data: () => ({
    //
  }),
};
</script>
