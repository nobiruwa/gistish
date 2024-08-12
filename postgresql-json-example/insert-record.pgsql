CREATE OR REPLACE FUNCTION insert_record()
RETURNS VOID
AS $$
DECLARE
  count INTEGER;
BEGIN
TRUNCATE TABLE json_parse_test RESTART IDENTITY;
FOR count IN 1..100000
LOOP
  INSERT INTO json_parse_test (json_text) VALUES ('{"common": { "contents": "{\"age\":' || count || '}" } }');
END LOOP;
END $$ LANGUAGE plpgsql;
