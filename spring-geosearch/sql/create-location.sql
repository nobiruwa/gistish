DROP TABLE IF EXISTS location CASCADE;

CREATE TABLE location
(
    id SERIAL NOT NULL,
    prefecture_name VARCHAR(5) NOT NULL,
    city_name VARCHAR(64) NOT NULL,
    town_name VARCHAR(64) NOT NULL,
    geometric GEOGRAPHY(Point,4326) NOT NULL,

    PRIMARY KEY (id)
);

-- 空間インデックスの作成
CREATE INDEX gist_location on location USING GIST (geometric);
