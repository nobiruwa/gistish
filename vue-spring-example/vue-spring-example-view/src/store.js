import axios from 'axios';
import { createStore } from 'vuex';

const state = {
  login: {
    status: 'logout',
    username: '',
    password: '',
    authorization: '',
    detail: {
      name: '',
      message: '',
    },
  },
  greeting: '',
};

const getters = {
  loginState(state) {
    return state.login;
  },
  greetingState(state) {
    return state.greeting;
  },
  loggedIn(state) {
    if (state.login.status === 'success' && state.login.authorization.startsWith('Bearer')) {
      return true;
    }
    return false;
  },
  loginErrorMessage() {
    if (state.login.detail.message) {
      return state.login.detail.message;
    }
    return '';
  },
};

const mutations = {
  updateLogin(state, login) {
    state.login = login;
  },
  updateGreeting(state, greeting) {
    state.greeting = greeting;
  },
};

function secureAjax({ state }, url, config) {
  // https://github.com/axios/axios#axiosconfig
  const secureConfig = {
    headers: {},
    ...config,
  };
  secureConfig.headers['Authorization'] = state.login.authorization;

  return axios(url, secureConfig);
}

function isAuthenticated(state) {
  // state.loginがsuccessでなければ失敗
  if (state.login.status !== 'success') {
    return Promise.reject(new Error('Please log in.'));
  }
  // state.login.detail.XXXがYYYでなければ失敗
  return Promise.resolve();
}

const actions = {
  async fetchGreeting({ commit }) {
    return axios.post('http://localhost:8080/api/greeting')
      .then(response => {
        commit('updateGreeting', response.data);
      }, error => {
        commit('updateGreeting', {
          name: error.name,
          message: error.message,
        });
      });
  },
  async fetchSecureGreeting({ commit, state }) {
    return isAuthenticated(state)
      .then(
        // eslint-disable-next-line no-unused-vars
        _ => secureAjax({ state }, 'http://localhost:8080/api/secure-greeting', {
          method: 'POST',
        })
      )
      .then(response => {
        commit('updateGreeting', response.data);
      }, error => {
        commit('updateGreeting', error);
      });
  },
  async login({ commit }, { url, username, password }) {
    return axios.post(url, {
      username,
      password,
    }, {
      crossdomain: true,
    }).then(
      (response) => {
        commit('updateLogin', {
          status: 'success',
          authorization: response.data.authorization,
          detail: {},
        });
      },
      (error) => {
        commit('updateLogin', {
          status: 'logout',
          authorization: '',
          detail: error,
        });
      },
    );
  }, // end of login
  // eslint-disable-next-line no-unused-vars
  async authenticated({ commit, state }) {
    return isAuthenticated(state);
  },
};

export default createStore({
  state,
  getters,
  mutations,
  actions,
});
