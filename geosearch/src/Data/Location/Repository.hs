{-# LANGUAGE OverloadedStrings #-}
module Data.Location.Repository where

import Data.Location (Geometric(..), Location(..))

import Data.Attoparsec.ByteString.Char8 (Parser, double, inClass, many', parseOnly, satisfy, string)
import Data.ByteString (ByteString, pack, unpack)
import Data.Either (fromRight)
import Data.Maybe (listToMaybe)
import Data.Text (Text)
import Database.HDBC (run, SqlValue, execute, fetchAllRows', fromSql, prepare, toSql)
import Database.HDBC.PostgreSQL (Connection)
import Text.Printf (printf)

toPoint :: Double -> Double -> String
toPoint = printf "SRID=4326;POINT(%f %f)"

persist :: Connection -> Location -> IO Integer
persist conn (Location p c t (Geometric lon lat)) = do
  statement <- prepare conn "INSERT INTO location (prefecture_name, city_name, town_name, geometric) VALUES (?, ?, ?, ST_GeographyFromText(?))"
  execute statement [toSql p, toSql c, toSql t, toSql (toPoint lon lat)]

findById :: Connection -> Text -> IO (Maybe Location)
findById conn id_ = do
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric) FROM location WHERE id = ?"
  execute statement [ toSql id_ ]
  listToMaybe . map toLocationFromList <$> fetchAllRows' statement

findByPrefectureName :: Connection -> Text -> IO [Location]
findByPrefectureName conn prefectureName = do
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric) FROM location WHERE prefecture_name = ?"
  execute statement [ toSql prefectureName ]
  map toLocationFromList <$> fetchAllRows' statement

findByPrefectureNameAndCityName :: Connection -> Text -> Text -> IO [Location]
findByPrefectureNameAndCityName conn prefectureName cityName = do
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric) FROM location WHERE prefecture_name = ? AND city_name = ?"
  execute statement [ toSql prefectureName, toSql cityName ]
  map toLocationFromList <$> fetchAllRows' statement

findByPrefectureNameAndCityNameAndTownNameContaining :: Connection -> Text -> Text -> Text -> IO [Location]
findByPrefectureNameAndCityNameAndTownNameContaining conn prefectureName cityName townName = do
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric) FROM location WHERE prefecture_name = ? AND city_name = ? AND town_name LIKE ?"
  execute statement [ toSql prefectureName, toSql cityName, toSql (mconcat ["%", townName, "%"]) ]
  map toLocationFromList <$> fetchAllRows' statement

findWithinAndLimit :: Connection -> Geometric -> Double -> Integer -> IO [(Location, Double)]
findWithinAndLimit conn (Geometric lon lat) within limit = do
  let point = toPoint lon lat
  statement <- prepare conn "SELECT prefecture_name, city_name, town_name, ST_AsText(geometric), ST_Distance(?, geometric) as distance FROM location WHERE ST_DWithin(geometric, ST_GeographyFromText(?), ?) ORDER BY distance;"
  execute statement [ toSql point
                    , toSql point
                    , toSql within
                    ]
  map toLocationWithDistanceFromList <$> fetchAllRows' statement

toLocationWithDistanceFromList :: [SqlValue] -> (Location, Double)
toLocationWithDistanceFromList [p, c, t, g, d] = (toLocation p c t g, fromSql d :: Double)
toLocationWithDistanceFromList _ = error "実装バグ: toLocationWithDistanceFromList の引数の数が不正です。"

toLocationFromList :: [SqlValue] -> Location
toLocationFromList [p, c, t, g] = toLocation p c t g
toLocationFromList _ = error "実装バグ: toLocationFromList の引数の数が不正です。"

toLocation :: SqlValue -> SqlValue -> SqlValue -> SqlValue -> Location
toLocation p c t g = Location (fromSql p) (fromSql c) (fromSql t) (parsePoint (fromSql g))

parsePoint :: ByteString -> Geometric
parsePoint = fromRight (Geometric 0 0) . parseOnly pointParser

pointParser :: Parser Geometric
pointParser = do
  string "POINT("
  lon <- double
  string " "
  lat <- double
  string ")"
  return (Geometric lon lat)

clear :: Connection -> IO Integer
clear conn = do
  statement <- prepare conn "DELETE FROM location"
  execute statement []
