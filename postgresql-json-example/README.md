# postgresql-json-example

## データベースの作成

```bash
sudo -u postgres psql -c "CREATE USER testuser WITH ENCRYPTED PASSWORD 'testuser'"
sudo -u postgres psql -c "CREATE DATABASE json_test with owner testuser;"
```

## JSON関数のテスト

```bash
PGPASSWORD=testuser psql -U testuser -h localhost -d json_test
json_test=# \i create-table.sql
json_test=# \i json-parse.sql
```
