<template>
<div class="logout">
  <h1>ログインしてください</h1>
  <div class="alert" role="alert">(エラーメッセージがあれば表示)</div>
  <form :action="actionUrl" method="POST" @submit.prevent="onSubmit">
    <p>本当にログインしますか？</p>
    <input type="submit" value="ログアウト"/>
  </form>
</div>
</template>

<script>
export default {
  name: 'AppLogout',
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
