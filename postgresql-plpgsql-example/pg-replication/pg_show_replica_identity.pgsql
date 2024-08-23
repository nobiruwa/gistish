-- 
-- https://github.com/supabase/pg_replicate/blob/main/pg_replicate/src/clients/postgres.rs

DROP TYPE IF EXISTS INDEX_RECORD CASCADE;

CREATE TYPE INDEX_RECORD AS (
  table_catalog TEXT,
  table_schema TEXT,
  table_name TEXT,
  attname NAME,
  atttypid oid,
  atttypmod INT4,
  attnotnull BOOL,
  is_identity BOOL
);

DROP FUNCTION IF EXISTS pg_show_replica_identity() CASCADE;

-- REPLICA IDENTITYかどうかを判別できるカラム一覧を取得
CREATE FUNCTION pg_show_replica_identity() RETURNS setof INDEX_RECORD AS $$
DECLARE
  tmp_record RECORD;
  result_record RECORD;
BEGIN
  FOR tmp_record IN
    -- テーブル一覧にインデックスを追加
    SELECT t.table_catalog table_catalog,
           t.table_schema table_schema,
           t.table_name table_name,
           i.indexrelid indexrelid,
           i.indrelid indrelid,
           i.indkey indkey
      FROM information_schema.tables t
      LEFT JOIN pg_catalog.pg_index i
        ON i.indrelid = to_regclass(t.table_schema || '.' || t.table_name)
     WHERE i.indrelid IS NOT NULL
       AND t.table_schema <> 'information_schema'
       AND t.table_schema <> 'pg_catalog'
  LOOP
    FOR result_record IN
      -- カラム一覧にREPLICA IDENTITYのインデックス有無を追加
      SELECT tmp_record.table_catalog::TEXT,
             tmp_record.table_schema::TEXT,
             tmp_record.table_name::TEXT,
             a.attname,
             a.atttypid,
             a.atttypmod,
             a.attnotnull,
             a.attnum = ANY(i.indkey) is_identity
        FROM pg_catalog.pg_attribute a
        LEFT JOIN pg_catalog.pg_index i
             ON (i.indexrelid = pg_get_replica_identity_index(tmp_record.indrelid))
       WHERE a.attnum > 0::pg_catalog.int2
         AND NOT a.attisdropped
         AND a.attrelid = tmp_record.indrelid
       ORDER BY a.attnum
    LOOP
      -- replication可能なテーブルのみを返却
      RETURN NEXT result_record;
    END LOOP;
  END LOOP;

END $$ LANGUAGE plpgsql;

-- -- テーブルのOIDを結合条件とする
-- SELECT t.table_catalog, t.table_schema, t.table_name, i.indexrelid, i.indrelid, i.indkey
--   FROM information_schema.tables t
--   LEFT JOIN pg_catalog.pg_index i
--   ON i.indrelid = to_regclass(t.table_schema || '.' || t.table_name);

-- -- テーブルのOIDを結合条件とする
-- SELECT t.table_catalog, t.table_schema, t.table_name, i.indexrelid, i.indrelid, i.indkey
--   FROM information_schema.tables t
--   LEFT JOIN pg_catalog.pg_index i
--     ON i.indrelid = to_regclass(t.table_schema || '.' || t.table_name)
--  WHERE i.indrelid IS NOT NULL;

-- -- インデックスのOIDを結合条件とする
-- SELECT t.table_catalog, t.table_schema, t.table_name, i.indexrelid, i.indrelid, i.indkey
--   FROM information_schema.tables t
--   LEFT JOIN pg_catalog.pg_index i
--   ON i.indexrelid = pg_get_replica_identity_index(to_regclass(t.table_schema || '.' || t.table_name));
