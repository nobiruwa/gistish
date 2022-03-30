<template>
<div class="login">
  <h1>ログインしてください</h1>
  <div class="alert" role="alert">{{ loginErrorMessage }}</div>
  <form :action="actionUrl" method="POST" @submit.prevent="onSubmit">
    <p>
      <label for="username">ユーザー名</label>
      <input autofocus id="username" name="username" type="text" v-model="username" placeholder="ユーザー名"/>
    </p>
    <p>
      <label for="username">パスワード</label>
      <input id="password" name="password" type="password" v-model="password" placeholder="パスワード"/>
    </p>
    <input type="submit" value="ログイン"/>
  </form>
</div>
</template>

<script>
import router from '@/router';
import { mapActions, mapGetters } from 'vuex';

export default {
  name: 'AppLogin',
  data() {
    return {
      username: '',
      password: '',
    };
  },
  computed: {
    ...mapGetters([
      'loginState',
      'loggedIn',
      'loginErrorMessage',
    ]),
    actionUrl() {
      return window.location.port === '8080' ? '/login' : 'http://localhost:8080/login';
    },
  },
  watch: {
    loggedIn() {
      router.push(this.$route.query.redirect);
    },
  },
  methods: {
    ...mapActions([
      'login',
    ]),
    onSubmit() {
      this.login({
        url: this.actionUrl,
        username: this.username,
        password: this.password,
      });
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
