# vue-spring-example 

以下は`M-x lsp-java-spring-initializr`ウイザードでの入力値です。

- Project `Gradle Project`
- Spring Boot `2.1.6` (default)
- Project Metadata
  - Packaging `War`
    - Tomcatへデプロイすることはなく、`java -jar xxx.war`でサーバーを起動します。
  - Java `8`
- add a dependency `Spring Security`
- add a dependency `Thymeleaf`

## CORS

開発を容易にするために、CORSを有効にしてください。

1. [Global CORS configuration](https://spring.io/guides/gs/rest-service-cors/#_global_cors_configuration)の要領でAPIにCORSを設定してください。

## ログイン機構を作る

SPAでは、ログイン画面とログアウト画面を用いる従来のWebアプリケーションとは異なり、RESTful APIをJWT認証などのトークンで保護します。

spring-securityでは、JWT認証を司る`org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter`を実装し、RESTful APIのURLに対してフィルターとして追加します。

JWT認証のフィルターを通過するためのトークンは、画面を持たないログインAPIから取得できるように実装します。
この手順は<https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/>に従います。

### ログインユーザー

`com.example.vuespringexample.SecurityConfig#configure(AuthenticationManagerBuilder auth)`メソッドで以下の1ユーザーを登録しています。

- `user`/`password`

## vue-spring-example-view

### 新規作成

Vue CLIによって`vue-spring-example-view`プロジェクトを再作成する必要があります。

```console
$ npm i @vue/cli
$ npx vue create vue-spring-example-view
$ cd vue-spring-example-view
$ npm i axios vue-router vuex
// 更新する場合
$ mv vue-spring-example-view vue-spring-example-view.old
$ npm i @vue/cli
$ npx vue create vue-spring-example-view
$ cd vue-spring-example-view
$ npm i axios vue-router vuex
$ cp -r vue-spring-example-view.old/src/ .
$ rm src/components/HelloWorld.vue
```

## Build & Boot

バックエンドを`8080/TCP`にバインドしてください。
バックエンドを先に起動すればフロントエンドは`8081/TCP`など空いているポートを使用します。

### Gradleの最新化

```
$ ./gradlew wrapper --gradle-version=7.4
```

### フロントエンドとバックエンドを両方ビルドする場合

```
$ ./gradlew buildView build bootRun
```

### フロントエンドのビルド時間を短縮したい場合(推奨)

```
$ cd vue-spring-example-view
$ npm run watch
```

```
$ ./gradlew build bootRun
```

## References

最低限抑えるべき情報を上げます。

- <https://start.spring.io/>
- <https://www.baeldung.com/spring-boot-vue-js>
- <https://jp.vuejs.org/v2/style-guide/>
- <https://vuex.vuejs.org/guide/>
- <https://vuejs.org/v2/guide/computed.html>
- <https://baianat.github.io/vee-validate/>
- <https://kntmr.hatenablog.com/entry/2018/02/28/200112>
  - axiosをVuexで扱う場合の考慮
- <https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/>
  - JWT認証の実装例
- <https://qiita.com/nyasba/items/f9b1b6be5540743f8bac>
  - JWT認証の実装例
- <https://www.mkyong.com/spring-boot/spring-security-there-is-no-passwordencoder-mapped-for-the-id-null/>
  - 平文のパスワードを扱う
- <https://qiita.com/takatama/items/05e9fbc7199cde4caf60>
  - Vue Routerの画面遷移に認証を差し挟む
- <https://router.vuejs.org/guide/advanced/navigation-guards.html#global-before-guards>
  - Vue Routerの画面遷移に処理を差し挟む
