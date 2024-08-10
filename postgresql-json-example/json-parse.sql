-- データを2件INSERTします
-- $.common.contentsにJSON Objectに変換可能な文字列を持っています。
BEGIN;

TRUNCATE TABLE json_parse_test RESTART IDENTITY;

INSERT INTO json_parse_test (json_text) VALUES ('{"common": { "contents": "{\"city\":\"Tokyo\"}" } }');
INSERT INTO json_parse_test (json_text) VALUES ('{"common": { "contents": "{\"age\":24}" } }');

COMMIT;

-- 変換前のデータを表示します
SELECT * FROM json_parse_test;

-- 一時的なテーブルcontentsにJSON.parse()したデータを置き、$.common.contentsをstring型からobject型に変換します
WITH contents AS (
     SELECT
        id,
        (json_text::jsonb #>> '{common,contents}')::jsonb AS as_jsonb
        FROM json_parse_test
)
UPDATE json_parse_test
  SET json_text = jsonb_set(
        json_text::jsonb,
        '{common,contents}'::text[],
        contents.as_jsonb
      )
  FROM contents
  WHERE json_parse_test.id=contents.id;

-- 変換後のデータを表示します
SELECT * FROM json_parse_test;
