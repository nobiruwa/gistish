# html-reporter

## 起動方法

```
// 以下1行はLinuxのみ
$ sudo sysctl -w kernel.unprivileged_userns_clone=1

// 以下2行はLinuxのみ
$ cd output/html
$ ln -s ../../style style

// 以下2行はWindowsのみ、管理者権限のcmd.exe(powershell.exeではない)で実行する
> cd output\html
> mklink /D style ..\..\style

$ npm install
$ npm run serve
```

### curlによるPOST(HTMLテンプレート)

```
$ curl -X POST http://localhost:3000/report -H 'Content-Type: application/json' -d '@./sample/meeting-minutes.json'
{"html":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/html/5a3284e6-d18a-4655-a555-54e4ac41c155/report.html"},"pdf":{"key":"5a3284e6-d18a-4655-a555-54e4ac41c155","filePath":"output/pdf/5a3284e6-d18a-4655-a555-54e4ac41c155/report.pdf"}}
```


### HTMLもしくはPDFのダウンロード

```
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.html
$ curl -X GET http://localhost:3000/report/5a3284e6-d18a-4655-a555-54e4ac41c155.pdf
```

