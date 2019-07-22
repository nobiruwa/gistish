<template>
<div>
  <h1>Footer</h1>
  <div><button @click="onGreetingButtonClick">Greeting</button></div>
  <div><button @click="onSecureGreetingButtonClick">Secure Greeting</button></div>
  <div>{{ greetingState }}</div>
  <div>{{ loginState }}</div>
</div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex';

export default {
  computed: {
    ...mapGetters([
      'loginState',
      'greetingState',
    ]),
  },
  methods: {
    ...mapActions([
      'fetchGreeting',
      'fetchSecureGreeting',
    ]),
    onGreetingButtonClick() {
      this.fetchGreeting();
    },
    onSecureGreetingButtonClick() {
      this.fetchSecureGreeting();
    },
  },
};
</script>

<style scoped>

</style>
