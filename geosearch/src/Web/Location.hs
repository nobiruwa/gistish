{-# LANGUAGE OverloadedStrings #-}

module Web.Location (locationHandlers) where

import Control.Monad (when)
import Control.Monad.IO.Class (liftIO)
import Data.Location (Geometric (..))
import Data.Location.Repository (findById, findByPrefectureName, findByPrefectureNameAndCityName, findByPrefectureNameAndCityNameAndTownNameContaining, findWithinAndLimit)
import Data.Map (fromList)
import qualified Data.Map as Map
import Database.HDBC (withTransaction)
import Database.HDBC.PostgreSQL (Connection)
import Network.HTTP.Types.Status (status404)
import Web.Scotty (ActionM, ScottyM, captureParam, get, json, queryParam, queryParams, status)

locationHandlers :: Connection -> ScottyM ()
locationHandlers conn = do
  getLocationById conn
  getLocationSearch conn
  getDistance conn

getLocationById :: Connection -> ScottyM ()
getLocationById conn = get "/location/:id" $ do
  id_ <- captureParam "id"
  mLocation <- liftIO . withTransaction conn $ flip findById id_
  case mLocation of
    Nothing -> status status404
    (Just location) -> json location

getLocationSearch :: Connection -> ScottyM ()
getLocationSearch conn = get "/location" $ do
  prefectureName <- queryParam "prefecture"
  ps <- fromList <$> queryParams
  locs <- liftIO $ case Map.lookup "city" ps of
    Nothing -> withTransaction conn $ flip findByPrefectureName prefectureName
    (Just cityName) -> case Map.lookup "town" ps of
      Nothing -> findByPrefectureNameAndCityName conn prefectureName cityName
      (Just townName) -> findByPrefectureNameAndCityNameAndTownNameContaining conn prefectureName cityName townName
  when (null locs) (status status404)
  json locs

getDistance :: Connection -> ScottyM ()
getDistance conn = get "/distance" $ do
  lon <- queryParam "lon" :: ActionM Double
  lat <- queryParam "lat" :: ActionM Double
  within <- queryParam "within"
  limit <- queryParam "limit"
  locs <- liftIO $ findWithinAndLimit conn (Geometric lon lat) within limit
  json locs
