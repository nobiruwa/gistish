module Data.MlitIsj where

import Data.MlitIsj.Raw (MlitIsj(..), decodeMlitIsj)
import Data.Location (Geometric(..), Location(..))
import Data.Location.Repository (clear, persist)

import Control.Monad (void, when)
import qualified Data.ByteString.Lazy as BL
import Database.HDBC (withTransaction)
import Database.HDBC.PostgreSQL (Connection)
import System.FilePath.Glob (globDir1, glob, compile, Pattern)

toLocation :: MlitIsj -> Location
toLocation (MlitIsj _ p _ c _ t lat lon _ _) = Location p c t (Geometric lon lat)

migrate :: Connection -> FilePath -> IO ()
migrate conn path = do
  csv <- BL.readFile path
  locs <- map toLocation <$> decodeMlitIsj csv
  inserted <- withTransaction conn $ \conn' -> sum <$> mapM (persist conn') locs
  print inserted

migrateAll :: Connection -> FilePath -> Bool -> IO ()
migrateAll conn pat deleteIfExists = do
  when deleteIfExists (void (clear conn))
  csvs <- globDir1 (compile pat) "./"
  mapM_ (migrate conn) csvs
