import { createRouter, createWebHistory } from 'vue-router';

import AppDashboard from '@/pages/AppDashboard';
import AppLogin from '@/pages/AppLogin';
import AppLogout from '@/pages/AppLogout';
import store from '@/store';

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'dashboard',
      component: AppDashboard,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/login',
      name: 'Login',
      component: AppLogin,
      meta: {},
    },
    {
      path: '/logout',
      name: 'Logout',
      component: AppLogout,
      meta: {},
    },
  ],
});

router.beforeEach((to, from, next) => {
  store.dispatch('authenticated').then(
    // eslint-disable-next-line no-unused-vars
    response => {
      next();
    },
    // eslint-disable-next-line no-unused-vars
    _ => {
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
