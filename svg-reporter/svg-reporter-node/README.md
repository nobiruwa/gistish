# svg-reporter-node

<../README.md>のアイディアをNode.JS上で作りました。

## 結論

SVGのforeignObject内に文字を描画するときのフォントサイズの設定が上手くいかず、使用に耐えないという結論になりました。

HTMLをテンプレートとして作成するようにしました。

## 起動方法

```
// 以下1行はLinuxのみ
$ sudo sysctl -w kernel.unprivileged_userns_clone=1
$ cd svg-reporter-node
$ npm install
$ npm run serve
```

### curlによるPOST(HTMLテンプレート)

```
$ curl -X POST http://localhost:3000/report -H 'Content-Type: application/json' -d '@./sample/meeting-minutes.json'
{"svg":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/svg/5a3284e6-d18a-4655-a555-54e4ac41c155/report.svg"},"pdf":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/pdf/5a3284e6-d18a-4655-a555-54e4ac41c155/report.pdf"}}
```

### curlによるPOST(SVGテンプレート)

```
$ curl -X POST http://localhost:3000/svg/report -H 'Content-Type: application/json' -d '@./sample/meeting-minutes.json'
{"svg":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/html/5a3284e6-d18a-4655-a555-54e4ac41c155/report.html"},"pdf":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/pdf/5a3284e6-d18a-4655-a555-54e4ac41c155/report.pdf"}}
```

### HTML, SVGもしくはPDFのダウンロード

```
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.html
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.svg
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.pdf
```

