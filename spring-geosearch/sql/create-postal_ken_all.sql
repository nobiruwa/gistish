DROP TABLE IF EXISTS public.postal_ken_all;

CREATE TABLE public.postal_ken_all
(
    id SERIAL NOT NULL,
    -- 全国地方公共団体コード（JIS X0401、X0402）
    -- <http://www.soumu.go.jp/main_content/000137948.pdf>
    japanese_local_government_code CHAR(5),
    -- （旧）郵便番号（5桁）
    ex_postal_code CHAR(5),
    -- 郵便番号（7桁）
    postal_code CHAR(7),
    -- 都道府県名
    prefecture_hankaku_katakana VARCHAR(20),
    -- 市区町村名
    city_hankaku_katakana VARCHAR(64),
    -- 町域名
    town_hankaku_katakana VARCHAR(64),
    -- 都道府県名
    prefecture VARCHAR(5),
    -- 市区町村名
    city VARCHAR(32),
    -- 町域名
    town VARCHAR(32),
    -- 一町域が二以上の郵便番号で表される場合の表示　（注3）　（「1」は該当、「0」は該当せず）
    choiki SMALLINT,
    -- 小字毎に番地が起番されている町域の表示　（注4）　（「1」は該当、「0」は該当せず）
    koaza SMALLINT,
    -- 丁目を有する町域の場合の表示　（「1」は該当、「0」は該当せず）
    chome SMALLINT,
    -- 一つの郵便番号で二以上の町域を表す場合の表示　（注5）　（「1」は該当、「0」は該当せず）
    multiple_towns SMALLINT,
    -- 更新の表示（注6）（「0」は変更なし、「1」は変更あり、「2」廃止（廃止データのみ使用））
    modified SMALLINT,
    -- 変更理由　（「0」は変更なし、「1」市政・区政・町政・分区・政令指定都市施行、「2」住居表示の実施、「3」区画整理、「4」郵便区調整等、「5」訂正、「6」廃止（廃止データのみ使用））
    reason_to_modify SMALLINT,

    PRIMARY KEY (id)
);
