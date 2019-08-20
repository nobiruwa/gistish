module Main where

import Database (connect)
import Data.Location (Geometric(..), Location(..))
import Data.Location.Repository (findWithinAndLimit)
import Data.MlitIsj (migrateAll)
import Web.Location (locationHandlers)

import Text.Printf (printf)

import Data.Monoid ()
import Data.Char ()
import Text.Read ()
import Options.Applicative (progDesc, footer, header, fullDesc, info, ParserInfo, execParser, helper, help, long, switch, Parser)
import Web.Scotty (scotty)

data Options = Options
  { initA :: Bool
  , initB :: Bool
  , example :: Bool
  } deriving Show

initAParser :: Parser Bool
initAParser = switch $ long "init-a" <> help "initialize database with mlit's type-a isj data."

initBParser :: Parser Bool
initBParser = switch $ long "init-b" <> help "initialize database with mlit's type-b isj data."

exampleParser :: Parser Bool
exampleParser = switch $ long "example" <> help "perform an example of search on CLI."

optionsParser :: Parser Options
optionsParser = (<*>) helper $ Options <$> initAParser <*> initBParser <*> exampleParser

parserInfo :: ParserInfo Options
parserInfo = info optionsParser $ mconcat
  [ fullDesc
  , progDesc "geosearch"
  -- , header "header"
  -- , footer "footer"
  , progDesc "neighbor search."
  ]

runInitA :: IO ()
runInitA = do
  conn <- connect "testuser" "testuser" "127.0.0.1" "5434" "geosearch"
  migrateAll conn "./data/mlit_isj/h30/*a/*.csv" True

runInitB :: IO ()
runInitB = do
  conn <- connect "testuser" "testuser" "127.0.0.1" "5434" "geosearch"
  migrateAll conn "./data/mlit_isj/h30/*b/*.csv" True

runSearchExample :: IO ()
runSearchExample = do
  conn <- connect "testuser" "testuser" "127.0.0.1" "5434" "geosearch"
  locs <- findWithinAndLimit conn (Geometric 139.754182 35.670812) 600.0 100
  putStrLn ("- 東京都千代田区内幸町二丁目(経度=139.754182、緯度=35.670812)は...")
  mapM_ showLocation locs
  where
    showLocation ((Location p c n _), d) = printf "  - %s%s%sまで%f[m]\n" p c n d

runServer :: IO ()
runServer = do
  conn <- connect "testuser" "testuser" "127.0.0.1" "5434" "geosearch"
  scotty 8080 (locationHandlers conn)

runWithOptions :: Options -> IO ()
runWithOptions options
  | initA options = runInitA
  | initB options = runInitB
  | example options = runSearchExample
  | otherwise = runServer

main :: IO ()
main = do
  options <- execParser parserInfo
  runWithOptions options
