-- PL/pgSQLの学習
-- 無名関数の実行と構文の確認
-- 参考: https://www.postgresqltutorial.com/postgresql-plpgsql/plpgsql-block-structure/

DO $anon$
<<first_block>>
DECLARE
  count INTEGER := 0;
BEGIN
  -- get the number of records
  SELECT COUNT(id) INTO count FROM json_parse_test;
  -- display a message
  RAISE NOTICE 'The number of records is %', count;
END first_block;
$anon$;