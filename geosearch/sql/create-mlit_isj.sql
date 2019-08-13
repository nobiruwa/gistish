DROP TABLE IF EXISTS public.mlit_isj;

CREATE TABLE public.mlit_isj
(
    id SERIAL NOT NULL,
    -- 都道府県コード(JIS都道府県コード)
    prefecture_code CHAR(2),
    -- 都道府県名(当該範囲の都道府県名)
    prefecture_name VARCHAR(5),
    -- 市区町村コード(JIS市区町村コード)
    city_town_code VARCHAR(5),
    -- 市区町村名(当該範囲の市区町村名(郡部は郡名，政令指定都市の区名も含む))
    city_town_name VARCHAR(32),
    -- 大字町丁目コード(大字町丁目コード(JIS市区町村コード＋独自7桁))
    oaza_machi_chome_code VARCHAR(12),
    -- 大字町丁目名(当該範囲の大字・町丁目名(町丁目の数字は漢数字))
    oaza_machi_chome_name VARCHAR(64),
    -- 緯度(十進経緯度(単位：度、小数点以下第6位まで、半角))
    latitude DECIMAL,
    -- 経度(十進経緯度(単位：度、小数点以下第6位まで、半角))
    longitude DECIMAL,
    -- 原典資料コード(大字・町丁目位置参照情報作成における原典資料を表すコード(1：自治体資料、2：街区レベル位置参照情報、3：1/25000地形図、0：その他資料))
    source CHAR(1),
    -- 大字・字・丁目区分コード(大字／字／丁目の区別を表すコード(1：大字、2：字、3：丁目、0：不明(通称)))
    oaza_aza_chome_division_code CHAR(1),

    PRIMARY KEY (id)
);
