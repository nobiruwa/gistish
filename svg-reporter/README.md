# svg-reporter

お遊びで帳票に見える何かを出力したいと思い、SVGで構築することにしました。

## 技術的アイディア

1. Inkscapeによるテンプレートファイルの作成
   1. 編集時のファイルフォーマットは`Inkscape SVG`とします。
   1. ページサイズはA4などとします。
   1. 罫線用のレイヤーとプレースホルダー(プログラムによって、テキストが埋め込まれる領域)用のレイヤーを分けます。
   1. 帳票`<rect>`と`<path>`で罫線を作成します。
   1. プレースホルダーレイヤーにプレースホルダー要素`<rect>`を置きます。オブジェクトのプロパティを開き、(`Shift+Ctrl+O`で開く)以下の規約で値を設定します。
      1. 線種を点線にするとよいでしょう。
      1. IDにはプレースホルダーと分かるようプレフィックス`placeholder_`を付けます。用途を特定できるようなIDを名付けてください。
      1. ラベルにはIDの先頭に`#`を付けた文字列とすればよいでしょう。
      1. タイトルは空のままでよいです。
      1. 詳細には、プログラムが埋め込むHTML要素のクラス属性を記述します。
         - 例: `align-right small`
   1. プログラムが使用するファイルフォーマット`Plain SVG`を別ファイルとして保存します。
1. プログラムによるレンダー(データにテンプレートを適用し、帳票を作成すること)は以下のフローに従います。
   1. テンプレートを適用したい文字列とそのIDを、オブジェクトで管理します。
      - [リクエストデータの例]の`data`プロパティを参照してください。
   1. 文字列のIDにプレフィックス`placeholder_`を付けてプレースホルダーを検索します。
   1. `<foreignObject>`要素を作り、プレースホルダーが持つ`x` `y` `width` `height`属性をコピーします。
      1. `<foreignObject>`要素の子要素として`<div>`要素を作り、ID属性を付与し、文字列を代入します。
         - セキュリティを無視して、文字列をDOMにパースしてから子ノードとして`<div>`要素に追加します。
      1. プレースホルダーの子要素`<desc>`からテキストコンテンツを取り出し、`<div>`要素の`class`属性にコピーします。
      1. 全てのHTML要素に`xmlns="http://www.w3.org/1999/xhtml"`属性を付けます。
      1. `<svg>`要素の子要素として、`<foreignObject x="..." ...><div id="..." xmlns="...">...</div></foreignObject>`を追加します。
      1. プレースホルダー要素を削除するか、プレースホルダーレイヤーを`style="display:none;"`にします。
  1. `common.css`と`<テンプレート名>.css`を探し、存在すれば、それぞれ`<style>`属性を作り、`<svg>`要素に追加します。

## リクエストデータの例

```
{
  "templateName": "meeting-minutes",
  "data": {
    "title": "株主総会議事録",
    "datetime": "2019年8月32日",
    "location": "東京都米花市米花町米花ホテル",
    "participants": "江戸川<br/>怪盗キッド<br/>鈴木(記)",
    "contents": "April　fool<br/><br/>月が　二人を　分かつ時<br/><br/>漆黒の星の名の下に<br/><br/>波にいざなわれて<br/><br/>我は参上する<br/><br/>　　　　怪盗キッド<br/><br/>了解した。(江戸川)"
  }
}
```

## svg-reporter-node

上記のアイディアをNode.JS上で作りました。

### 起動方法

```
$ cd svg-reporter-node
$ npm install
$ npm run serve
```

#### curlによるPOST

```
$ curl -X POST http://localhost:3000/report -H 'Content-Type: application/json' -d '@./sample/meeting-minutes.json'
{"svg":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/svg/5a3284e6-d18a-4655-a555-54e4ac41c155/report.svg"},"pdf":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/pdf/5a3284e6-d18a-4655-a555-54e4ac41c155/report.pdf"}}
```

#### SVGもしくはPDFのダウンロード

```
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.svg
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.pdf
```

## SVG to PDF

SVGの印刷機能を用いればPDFに変換できます。ChromeでもChromiumでも、フォントやプリンターの設定(たとえば、ヘッダーとフッターを印刷しない)を施した設定ファイルをデフォルトのuser data directoryとは別に用意しておけば、SVGビューワー兼PDF作成ツールになります。

起動は`chromium --user-data-dir=/path/to/foo`を使えばよいです。

が、ここではサーバーサイドでの変換を検討します。
サーバーにX11などGUIの依存関係が含まれてもよいものとします。

### Linux

1. ~~librsvg2-\*を使います。~~
   - `<foreignObject>`に対応していないので、今回のアイディアにはマッチしませんでした。
1. ~~CairoSVGを使います。~~
   - `<foreignObject>`に対応していないので、今回のアイディアにはマッチしませんでした。
1. ~~Inkscapeのコマンドラインインターフェースを使います。~~
   - `<foreignObject>`に対応していないので、今回のアイディアにはマッチしませんでした。
1. Chromiumをpuppeteer越しに使う

### Windows

1. ~~CairoSVGを使います。~~
   - `<foreignObject>`に対応していないので、今回のアイディアにはマッチしませんでした。
1. ~~Inkscapeのコマンドラインインターフェースを使います。~~
   - `<foreignObject>`に対応していないので、今回のアイディアにはマッチしませんでした。
1. Chromiumをpuppeteer越しに使う

## Misc

SVGをTeX+PostScriptに、InkscapeをTeXstudioに変更すると真っ当なシステムになるでしょう。
事実、[（株）オンネット・システムズ](https://www.onnet.ne.jp/)が素晴しい記事[TeX→PDF変換ツールを利用したバッチ処理による帳票出力(2005年)](https://codezine.jp/article/detail/176)で丁寧に紹介なさっています。