# postgresql-json-example

## データベースの作成

```bash
sudo -u postgres psql -c "CREATE USER testuser WITH ENCRYPTED PASSWORD 'testuser'"
sudo -u postgres psql -c "CREATE DATABASE json_test with owner testuser;"
```

## テーブルの作成

```bash
PGPASSWORD=testuser psql -U testuser -h localhost -d json_test
json_test=# \i create-table.sql
```

## PL/pgSQLのチュートリアル

```bash
json_test=# \i plpgsql-tutorial/count.pgsql
json_test=# \i plpgsql-tutorial/subblock.pgsql
```

## PL/pgSQLによる処理の関数化

```bash
json_test=# \i insert-record.pgsql
json_test=# \i convert-imperative.pgsql
json_test=# \i convert-declarative.pgsql
```

## 関数の実行

```bash
json_test=# EXPLAIN ANALYZE SELECT insert_record();
json_test=# EXPLAIN ANALYZE SELECT convert_imperative();
json_test=# EXPLAIN ANALYZE SELECT insert_record();
json_test=# EXPLAIN ANALYZE SELECT convert_declarative();
```
