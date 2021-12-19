# http-server-exe/openssl

## 準備

```console
$ cp run.sh.sample run.sh
$ cp openssl.cnf.sample openssl.cnf
$ cp san.ext.sample san.ext
```

`run.sh`と`san.ext`をドメイン名に合わせて書き換えてください。

## 実行

```
$ ./run.sh
```

## 証明書のインポート(クライアント)

- ルート証明機関
  - `rootca.crt`をインポートしてください。
  - firefoxであれば以下の手順になります。
    - `Tools -> Settings`の`Security`タブから
      `View Certificates...`を開き、`Authorities`タブの`Import...`からインポートしてください。

## http-server-exeのHTTPS暗号化

```console
$ sudo `which http-server-exe` -c intermediateca/cacert.pem -k intermediateca/private/cakey-nopass.pem -p 443
```

## 名前解決

`hosts`ファイルかDNSサーバーにレコードを追加してください。
