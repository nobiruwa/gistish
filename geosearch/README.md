# geosearch

データベースで地図情報を扱うジオメトリ型を使って、近傍探索を行うAPIを作成します。

やることは、以下のブログ記事をなぞるだけです。

[Redis、PostgreSQL、MySQLで近傍検索](https://shogo82148.github.io/blog/2017/03/28/database-gis/)

## PostgreSQLの準備

PostgreSQL 11がport 5434で動作しているとします。

```
$ sudo -u postgres createuser -p 5434 -W testuser
$ sudo -u postgres psql -p 5434
postgres=# alter user testuser with encrypted password 'testuser';
```

## PostgreSQL + PostGIS

- PostGIS
  - GPLv2
  - <https://postgis.net>
  - `# apt-get install postgis`
    - work with PostgreSQL 11
    - 2.5+
      - <https://postgis.net/stuff/postgis-2.5.pdf>

## table

### create database

```
postgres=# CREATE DATABASE geosearch;
postgres=# grant all privileges on database geosearch to testuser;
geosearch=# grant all privileges on all sequences in schema public to testuser;
geosearch=# grant all privileges on all tables in schema public to testuser;
```

### create extension

```
geosearch=# CREATE EXTENSION postgis;
geosearch=# CREATE EXTENSION postgis_sfcgal;
geosearch=# CREATE EXTENSION fuzzystrmatch; --needed for postgis_tiger_geocoder
geosearch=# CREATE EXTENSION address_standardizer;
geosearch=# CREATE EXTENSION address_standardizer_data_us;
geosearch=# CREATE EXTENSION postgis_tiger_geocoder;
geosearch=# CREATE EXTENSION postgis_topology;
```

### create table

```
geosearch=> \i ./sql/create-location.sql;
```

## 元データの収集

###  位置参照情報ダウンロードサービス

#### テーブルの作成

```
geosearch=> \i ./sql/create-mlit_isj.sql
```

#### 大字・町丁目レベル位置参照情報

- 都道府県コード
  - JIS都道府県コード
- 都道府県名
  - 当該範囲の都道府県名
- 市区町村コード
  - JIS市区町村コード
- 市区町村名
  - 当該範囲の市区町村名
  - （郡部は郡名、政令指定都市の区名も含む）
- 大字町丁目コード　
  - 大字町丁目コード
  - （JIS市区町村コード＋独自7桁）
- 大字町丁目名
  - 当該範囲の大字・町丁目名
  - （町丁目の数字は漢数字）
- 緯度
  - 十進経緯度
  - （少数第6位まで、半角）
- 経度
  - 十進経緯度
  - （少数第6位まで、半角）
- 原典資料コード
  - 大字・町丁目位置参照情報作成における原典資料を表すコード
  - 1：自治体資料　2：街区レベル位置参照　3：1/25000地形図　0：その他資料
- 大字・字・丁目区分コード
  - 大字/字/丁目の区別を表すコード
  - 1：大字　2：字　3：丁目　0：不明

### 郵便番号

#### テーブルの作成

```
geosearch=> \i ./sql/create-postal_ken_all.sql
```

#### 郵便番号データファイルの形式等

- 全国地方公共団体コード（JIS X0401、X0402）
  - 半角数字
- （旧）郵便番号（5桁）
  - 半角数字
- 郵便番号（7桁）
  - 半角数字
- 都道府県名　
  - 半角カタカナ（コード順に掲載）　（注1）
- 市区町村名　
  - 半角カタカナ（コード順に掲載）　（注1）
- 町域名　
  - 半角カタカナ（五十音順に掲載）　（注1）
- 都道府県名　
  - 漢字（コード順に掲載）　（注1,2）
- 市区町村名　
  - 漢字（コード順に掲載）　（注1,2）
- 町域名　
  - 漢字（五十音順に掲載）　（注1,2）
- 一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず）
- 小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず）
- 丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず）
- 一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず）
- 更新の表示（注6）（「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用））
- 変更理由　（「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用））

## データの登録

```
$ stack ghci
*> conn <- connect "testuser" "testuser" "127.0.0.1" "5434" "geosearch"
*> migrate conn "data/mlit_isj/h30/13000-12.0b/13_2018.csv"
```

## misc

Google Mapのリンクの表示で済む場合もあります。

- [qiita.com - GoogleMapへのリンクURL作成方法を調べた](https://qiita.com/hiron2225/items/8d5cd1b6728b4d63434b)
- [developers.google.com - Display a map](https://developers.google.com/maps/documentation/urls/guide#map-action)
