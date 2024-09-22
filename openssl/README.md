# openssl

ルート認証局、中間認証局、サーバー証明書を作成します。

## 使用方法

### `run-template.sh`の実行

`jinja2`コマンドをインストールしてください。

```bash
$ pip install -U jinja2-cli
```

`example.org.json`を参考にcnfファイル、opensslコマンドの入力値を作成してください。

`run-template.sh`を実行してください。

```bash
$ ./run-template.sh OUTPUT_DIR DOMAIN_NAME INPUT_JSON
$ ./run-template.sh ./example.org example.org ./example.org.json
```

### `create.sh`の実行

`OUTPUT_DIR`に移動し、`create.sh`を実行してください。途中の質問にはすべて`y`と答えてください。

```bash
$ ./create.sh
[...snip...]
Sign the certificate? [y/n]:y


1 out of 1 certificate requests certified, commit? [y/n]y
Write out database with 1 new entries
Data Base Updated
[...snip...]
Sign the certificate? [y/n]:y


1 out of 1 certificate requests certified, commit? [y/n]y
Write out database with 1 new entries
Data Base Updated
```

## サーバーの設定

### nginx

`nginx.conf`に以下の設定を追加してください。

```conf
  ssl_certificate <chain.${DOMAIN_NAME}.crt.pemのフルパス>;
  ssl_password_file <password.txtのフルパス>;
  ssl_certificate_key <${DOMAIN_NAME}.key.pemのフルパス>;
```

## クライアントの設定

`rootcate/certs/rootca.${DOMAIN_NAME}.crt.pem`をクライアントの信頼されたルート認証局としてインポートしてください。

`${DOMAIN_MAME}`をDNSサーバーに登録するか、`/etc/hosts`に登録してください。

## Firefoxでの証明書の入れ替え

FQDNを変えずにルート認証局、中間認証局、サーバー証明書の組を再作成した場合は、
`<Firefoxのプロファイルディレクトリ>/cert9.db`からFQDNに対するレコードを削除してください。
それからルート認証局の証明書をインポートしてください。

```bash
$ sqlite3 `<Firefoxのプロファイルディレクトリ>/cert9.db`
sqlite> .headers ON
sqlite> SELECT * FROM nssPublic LIMIT 1;
// 認証局の名前が含まれる列を特定してください(以下はa3だった場合)
sqlite> DELETE FROM nssPublic WHERE a3 LIKE '%Private CA for%';
```

## 参考

<https://dadhacks.org/2017/12/27/building-a-root-ca-and-an-intermediate-ca-using-openssl-and-debian-stretch/>

<https://www.golinuxcloud.com/openssl-create-certificate-chain-linux/>
