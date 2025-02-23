# karate-example

## テスト対象

- http://localhost:3000/
  - #username フィールドに任意の文字列を入力
  - #submit ボタンを押下
  - #response-status 要素に `200` と表示される
  - セッションを取得
- http://localhost:3000/protect/hello
  - body 要素に `Hello!!!` と表示される


## 参考

https://github.com/karatelabs/karate
https://qiita.com/takanorig/items/05f424ebf0aa8eed9344
https://github.com/mozilla/geckodriver/releases
https://karatelabs.github.io/karate/karate-core/#driver

## 生成されるレポート

build/reports/tests/test/classes/karate.example.AppTest.html
build/karate-reports/karate.example.e2e.html


