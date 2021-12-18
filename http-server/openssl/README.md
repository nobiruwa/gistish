# http-server-exe/openssl

## 実行

```console
$ cp openssl.cnf.sample openssl.cnf
$ cp san.ext.sample san.ext
```

`san.ext`をドメイン名に合わせて書き換えてください。

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
