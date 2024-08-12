CREATE OR REPLACE FUNCTION convert_declarative()
RETURNS VOID
AS $$
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
$$ LANGUAGE sql;