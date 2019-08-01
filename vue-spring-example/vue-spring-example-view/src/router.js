import Vue from 'vue';
import Router from 'vue-router';

import Dashboard from '@/pages/Dashboard';
import Login from '@/pages/Login';
import Logout from '@/pages/Logout';
import store from '@/store';

Vue.use(Router);

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: Dashboard,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
      meta: {},
    },
    {
      path: '/logout',
      name: 'Logout',
      component: Logout,
      meta: {},
    },
  ],
});

router.beforeEach((to, from, next) => {
  store.dispatch('authenticated').then(response => {
    next();
  }, _ => {
    // TODO state.login.detailのメッセージを加工する
    // 「認証が必要な画面を表示する」など
    // console.log(error);

    // 遷移先がログインを必要としなければアクセスしてよい
    // ログインを必要とする場合はログイン画面へリダイレクト
    if (to.meta.requiresAuth) {
      // TODO リダイレクト先(to)をクエリパラメータredirectに与えるには？
      console.log(to.path);
      next(`/login?redirect=${encodeURIComponent(to.path)}`);
    } else {
      next();
    }
  });
});

export default router;
