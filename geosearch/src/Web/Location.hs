{-# LANGUAGE OverloadedStrings #-}
module Web.Location (locationHandlers) where

import Data.Location (Geometric(..))
import Data.Location.Repository (findWithinAndLimit, findByPrefectureNameAndCityName, findByPrefectureName, findById, findByPrefectureNameAndCityNameAndTownNameContaining)

import Control.Monad (when)
import Control.Monad.IO.Class (liftIO)
import Data.Map (fromList)
import qualified Data.Map as Map
import Database.HDBC (withTransaction)
import Database.HDBC.PostgreSQL (Connection)
import Network.HTTP.Types.Status (status404)
import Web.Scotty (ActionM, params, get, json, status, param, ScottyM)

locationHandlers :: Connection -> ScottyM ()
locationHandlers conn = do
  getLocationById conn
  getLocationSearch conn
  getDistance conn

getLocationById :: Connection -> ScottyM ()
getLocationById conn = get "/location/:id" $ do
  id_ <- param "id"
  mLocation <- liftIO . withTransaction conn $ flip findById id_
  case mLocation of
    Nothing -> status status404
    (Just location) -> json location

getLocationSearch :: Connection -> ScottyM ()
getLocationSearch conn = get "/location" $ do
  prefectureName <- param "prefecture"
  ps <- fromList <$> params
  locs <- liftIO $ case Map.lookup "city" ps of
    Nothing -> withTransaction conn $ flip findByPrefectureName prefectureName
    (Just cityName) -> case Map.lookup "town" ps of
      Nothing -> findByPrefectureNameAndCityName conn prefectureName cityName
      (Just townName) -> findByPrefectureNameAndCityNameAndTownNameContaining conn prefectureName cityName townName
  when (length locs == 0) (status status404)
  json locs

getDistance :: Connection -> ScottyM ()
getDistance conn = get "/distance" $ do
  lon <- param "lon" :: ActionM Double
  lat <- param "lat" :: ActionM Double
  within <- param "within"
  limit <- param "limit"
  locs <- liftIO $ findWithinAndLimit conn (Geometric lon lat) within limit
  json locs
