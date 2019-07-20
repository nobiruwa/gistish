# vue-spring-example 

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

## ログイン画面を作る

以下のステップでログイン画面を作ってください。

1. 未ログイン状態である場合は、`/login`以外のURLへのアクセスがログイン画面にリダイレクトされるようにしてください。
1. クライアントのバリデーションを通過したらユーザーがアクセスしたかった画面URLに遷移するようにしてください。
1. クライアントのバリデーションを通過したら、ユーザー名とパスワードをサーバーのDBと照合するようにしてください。

## ログイン以外の画面を作る

1. ルートディレクトリで以下のコマンドを実行し、NPMプロジェクトを作成します。
   - `vue init webpack vue-spring-example-view`
     - `npm i -g npm @vue/cli @vue/init`を実行して、`vue`コマンドと`vue init`サブコマンドを入手してください。
     - `vue init`実行時のコンフィグは以下の[vue initのコンフィグ]の通りとします。
1. `npm i -S vuex vuex-persistedstate vee-validate vue-lazyload axios vue-multiselect vue-datepicker vue-good-table`で他の依存パッケージをインストールします。
1. `build.gradle`に以下の[build.gradleの追加タスク]を追加します。

### 依存パッケージについて

1. `vuex`はモデルの状態を管理するライブラリです。
1. `vuex-persistedstate`はVuexのモデルをブラウザの中断後も保持したい場合に呼び出すライブラリです。
1. `axios`はAjax通信を行うためのライブラリです。

画面部品として、HTML5のデフォルトの要素のかわりに以下のライブラリを用いてください。

1. `vuetifyjs`はvueのコンポーネント(画面部品)ライブラリです。
1. `vee-validate`はクライアントサイドのフォームバリデーションを設定するためのライブラリです。
   - [vuetifyjsとの統合例](https://codepen.io/pen/?&editable=true&editors=101)を確認してください。
   - [vee-validateのサイト](https://baianat.github.io/vee-validate/)を確認してください。
1. `vue-multiselect`は複数選択に必要なVueのコンポーネントです。
1. `vue-datepicker`は日付選択に必要なVueのコンポーネントです。
1. `vue-good-table`はユーザー操作に対応したテーブル作成に必要なVueのコンポーネントです。

### 画面の状態を作る

Vuexでは、画面が扱うモデルを、1つの巨大なツリー(`state`)で表現します。
画面が遷移しても同じツリーを共有します(ワークフロープロセスのプロセス変数に似ています)。

たとえば、`state`は以下のような構造になります。

```
{
  // 画面遷移で共通
  user: {
    // ユーザー情報
  },
  login: {
    // 未ログイン
    // ログイン情報
  },
  // フォーム画面のみ値が入っている
  // ログイン画面では空オブジェクトになっている
  process: {
    // プロセス情報
  },
}
```

各画面は、ツリーの**一部分**を参照し、通信や画面入力で得たデータを加工して
ツリーの更新する、というサイクルを繰り返します。

1. `vue-spring-example-view/src/store`ディレクトリを作成する。
1. `vue-spring-example-view/src/store/index.js`を作成する。
1. `vue-spring-example-view/src/store/index.js`に`const state`, `const getters`, `const mutations`, `const actions`という4つの変数を宣言する。
1. `const state`にはモデルの初期状態を定義します。
1. `const getters`にはモデルのpublic getterを定義します。
1. `const mutations`にはモデルの**private** setterを定義します。同期関数のみが認められます。
1. `const actions`にはコンポーネントが使ってよいpublicなメソッドを定義します。mutationsのsetterを呼び出します。非同期関数を記述することができます。
   - Ajax通信を伴う処理はactionsで実装します。

### コンポーネントを作る手順

1. `vue-spring-example-view/src/components`配下にvueファイルを作ります。ファイル名はCamel Caseとします。
1. vueファイルはより深い場所にあるVueファイルに依存してよいです。つまり、小さく基本的な部品ほど深い場所に置いてください。
   - 依存関係は`<script>`タグの内側で`import OtherComponent from './other/directory/OtherComponent'`でコンストラクタを読み込み、
     `components`プロパティで`'other-component': OtherComponent`のようにバインドしてください。
1. 関心の分離の原則に従い、振舞は`<script>`に記述し、スタイルは`<style>`に記述し、
   マークアップは`<template>`に記述します。
1. コンポーネントは、画面に必要なモデルデータを必要とする場合、vuexにgettersを介して依頼します。
1. コンポーネントは、モデルデータの更新をする場合、vuexにactionsを介して依頼します。
1. コンポーネントは、Ajax通信を必要とする場合、vuexにactionsを介して依頼します。
   - Ajax通信の結果は、vuexのgettersを介して間接的に得ることができます。

### ページコンポーネントを作る手順

ページコンポーネントは、ページと認識されるメインのコンポーネントを指します。
実装の要領は[通常のコンポーネント](#コンポーネントを作る手順)と変わりありません。

1. ページコンポーネントのvueファイルは、`vue-spring-example-view/src/pages`配下に置いてください。
   - つまり、開発初期は`vue-spring-example-view/src/pages`で画面を開発することになります。
1. フォーム画面を作る場合は[Vuexの公式ページ](https://vuex.vuejs.org/guide/forms.html)に従ってください。

### 画面遷移フローを作る手順

1. `vue-spring-example-view/src/router/index.js`にページコンポーネントをimportします。
1. `vue-spring-example-view/src/router/index.js`の`Router`インスタンスの`routes`プロパティで、パスとコンポーネントをバインドします。
1. クライアントサイドのバリデーションは[vee-validate](https://baianat.github.io/vee-validate/)を用いて実装します。

### vue initのコンフィグ

```
? Project name vue-spring-example-view
? Project description (A Vue.js project) 
? Project description A Vue.js project
? Author (ein <ein@hpnull2>) 
? Author ein <ein@hpnull2>
? Vue build (Use arrow keys)
❯ Runtime + Compiler: recommended for most users 
  Runtime-only: about 6KB lighter min+gzip, but templates (or any Vue-specific HTML) are ONLY allowed in .vue files - render functions 
are required elsewhere 
? Vue build standalone
? Install vue-router? (Y/n) 
? Install vue-router? Yes
? Use ESLint to lint your code? (Y/n) 
? Use ESLint to lint your code? Yes
? Pick an ESLint preset (Use arrow keys)
❯ Standard (https://github.com/standard/standard) 
  Airbnb (https://github.com/airbnb/javascript) 
  none (configure it yourself) 
? Pick an ESLint preset Standard
? Set up unit tests (Y/n) y
? Set up unit tests (Y/n) y? Set up unit tests Yes
? Pick a test runner (Use arrow keys)
❯ Jest 
  Karma and Mocha 
  none (configure it yourself) 
? Pick a test runner jest
? Setup e2e tests with Nightwatch? (Y/n) 
? Setup e2e tests with Nightwatch? Yes
? Should we run `npm install` for you after the project has been created? (recommended) (Use arrow keys)
❯ Yes, use NPM 
  Yes, use Yarn 
  No, I will handle that myself 
? Should we run `npm install` for you after the project has been created? (recommended) npm
```

### build.gradleの追加タスク

```
task buildView(type:Exec) {
    workingDir './vue-spring-example-view'
    commandLine 'npm', 'run', 'build'
    standardOutput = new ByteArrayOutputStream()
    ext.output = {
        return standardOutput.toString()
    }
}

processResources {
    dependsOn 'buildView'
    from ('vue-spring-example-view/dist') {
        into ('static')
    }
}
```

### 画面

ただし、イテレーションが進みオブジェクトが巨大になってきたら、
[モジュール化](https://vuex.vuejs.org/guide/modules.html)を図ることとします。

モジュールごとに`state`、`mutations`、`actions`、`getters`を定義します。

モジュールは`vue-spring-example-view/src/store/index.js`で1つの`Vuex.Store`にまとめます。

```
const store = new Vuex.Store({
  modules: {
    a: moduleA,
    b: moduleB
  }
});
```

## Build & Boot

```
$ ./gradlew wrapper --gradle-version=5.5
$ ./gradlew buildView build bootRun
```

## References

最低限抑えるべき情報を上げます。

- <https://start.spring.io/>
- <https://www.baeldung.com/spring-boot-vue-js>
- <https://jp.vuejs.org/v2/style-guide/>
- <https://vuex.vuejs.org/guide/>
- <https://baianat.github.io/vee-validate/>
- <https://kntmr.hatenablog.com/entry/2018/02/28/200112>
