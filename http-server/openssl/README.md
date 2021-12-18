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
- 中間証明機関
  - `intermediateca.crt`をインポートしてください。

## http-server-exeのHTTPS暗号化

```console
$ sudo `which http-server-exe` -c intermediateca/cacert.pem -k intermediateca/private/cakey-nopass.pem -p 443
```
