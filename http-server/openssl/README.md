# http-server/openssl

ルート認証局、中間認証局、サーバー証明書を作成します。

## 使用方法

サンプルファイルをコピーしてください。

```bash
$ cp -r sample/* .
```

各ファイルの`${CHANGE_HERE_DOMAIN_NAME}`をサーバーのFQDN文字列に置換してください。

### `${CHANGE_HERE_DOMAIN_NAME}`変更前

```bash
$ cat create.sh
[...snip...]
DOMAIN_NAME="${CHANGE_HERE_DOMAIN_NAME}"
[...snip...]
```

```bash
$ cat openssl_root.cnf
[...snip...]
private_key       = $dir/private/rootca.${CHANGE_HERE_DOMAIN_NAME}.key.pem
[...snip...]
certificate       = $dir/certs/rootca.${CHANGE_HERE_DOMAIN_NAME}.crt.pem
[...snip...]
crl               = $dir/crl/rootca.${CHANGE_HERE_DOMAIN_NAME}.crl.pem
[...snip...]
```

```bash
$ cat sample/openssl_csr_san.cnf
[...snip...]
sample/openssl_csr_san.cnf:DNS.1        = ${CHANGE_HERE_DOMAIN_NAME}
sample/openssl_csr_san.cnf:DNS.2        = *.${CHANGE_HERE_DOMAIN_NAME}
[...snip...]
```

```bash
$ cat openssl_intermediate.cnf
[...snip...]
private_key       = $dir/private/intermediateca.${CHANGE_HERE_DOMAIN_NAME}.key.pem
[...snip...]
certificate       = $dir/certs/intermediateca.${CHANGE_HERE_DOMAIN_NAME}.crt.pem
[...snip...]
crl               = $dir/crl/intermediateca.${CHANGE_HERE_DOMAIN_NAME}.crl.pem
[...snip...]
```

### `${CHANGE_HERE_DOMAIN_NAME}`変更後(example.comの例)

```bash
$ cat create.sh
[...snip...]
DOMAIN_NAME="example.com"
[...snip...]
```

```bash
$ cat openssl_root.cnf
[...snip...]
private_key       = $dir/private/rootca.example.com.key.pem
[...snip...]
certificate       = $dir/certs/rootca.example.com.crt.pem
[...snip...]
crl               = $dir/crl/rootca.example.com.crl.pem
[...snip...]
```

```bash
[...snip...]
sample/openssl_csr_san.cnf:DNS.1        = example.com
sample/openssl_csr_san.cnf:DNS.2        = *.example.com
[...snip...]
```

```bash
$ cat openssl_intermediate.cnf
[...snip...]
private_key       = $dir/private/intermediateca.example.com.key.pem
[...snip...]
certificate       = $dir/certs/intermediateca.example.com.crt.pem
[...snip...]
crl               = $dir/crl/intermediateca.example.com.crl.pem
[...snip...]
```

### `create.sh`の実行

`create.sh`を実行してください。途中の質問にはすべて`y`と答えてください。

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
  ssl_certificate <chain.${CHANGE_HERE_DOMAIN_NAME}.crt.pemのフルパス>;
  ssl_password_file <password.txtのフルパス>;
  ssl_certificate_key <${CHANGE_HERE_DOMAIN_NAME}.key.pemのフルパス>;
```

## クライアントの設定

`rootcate/certs/rootca.${CHANGE_HERE_DOMAIN_NAME}.crt.pem`をクライアントの信頼されたルート認証局としてインポートしてください。

`${CHANGE_HERE_DOMAIN_MAME}`をDNSサーバーに登録するか、`/etc/hosts`に登録してください。

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
