CREATE OR REPLACE FUNCTION convert_imperative()
RETURNS VOID
AS $$
DECLARE
  f record;
BEGIN
  FOR f in SELECT
             id,
             (json_text::jsonb #>> '{common,contents}')::jsonb AS as_jsonb
           FROM json_parse_test
           WHERE json_text LIKE '{%"contents":%}'
  LOOP
    UPDATE json_parse_test
      SET json_text = jsonb_set(
            json_text::jsonb,
            '{common,contents}'::text[],
            f.as_jsonb
          )
      WHERE json_parse_test.id=f.id;
  END LOOP;
END $$ LANGUAGE plpgsql;