-- PL/pgSQLの学習
-- サブブロック
-- 参考: https://www.postgresqltutorial.com/postgresql-plpgsql/plpgsql-block-structure/

DO $$
<<outer>>
DECLARE
  x INTEGER := 0;
BEGIN
  x = x + 1;
  <<inner>>
  DECLARE
  BEGIN
  END inner;
END outer;
$$;