{-# LANGUAGE OverloadedStrings #-}
module Main where

import Data.Maybe (isJust)
import Network.Wai.Application.Static (StaticSettings(..), defaultFileServerSettings, staticApp)
import Network.Wai.Handler.Warp (Port, Settings, defaultSettings, getHost, getPort, runSettings, setPort)
import Network.Wai.Handler.WarpTLS (runTLS, tlsSettings)
import Network.Wai.Middleware.Cors (simpleCors)
import System.Console.GetOpt (ArgDescr(..), ArgOrder(..), OptDescr(..), getOpt)
import System.Environment (getArgs)

-- 引数のデータ構造
data Options = Options { optPort :: Port
                       , optDirectory :: FilePath
                       , optCors :: Bool
                       , optCert  :: Maybe FilePath
                       , optKey :: Maybe FilePath
                       }

defaultOptions = Options { optPort = 3000
                         , optDirectory = "."
                         , optCors = True
                         , optCert = Nothing
                         , optKey = Nothing
                         }

options :: [OptDescr (Options -> Options)]
options = [ Option ['p'] ["port"]
            (ReqArg (\f opts -> opts { optPort = read f }) "PORT")
            "PORT number"
          , Option ['d'] ["directory"]
            (ReqArg (\f opts -> opts { optDirectory = f }) "DIR")
            "root DIR"
          , Option [] ["disable-cors"]
            (NoArg (\opts -> opts { optCors = False }))
            "disable cors"
          , Option ['c'] ["certificate"]
            (ReqArg (\f opts -> opts { optCert = Just f }) "CERT")
            "certificate file"
          , Option ['k'] ["key"]
            (ReqArg (\f opts -> opts { optKey = Just f }) "KEY")
            "key file"
          ]

compilerOpts :: [String] -> IO (Options, [String])
compilerOpts args =
  case getOpt RequireOrder options args of
       (o, n, []) -> return (foldl (flip id) defaultOptions o, n)
       (_, _, errs) -> ioError (userError (concat errs))

printOptions :: Options -> IO ()
printOptions opts = do
  putStrLn $ "directory: " ++ optDirectory opts
  putStrLn $ "port: " ++ show (optPort opts)
  putStrLn $ "cors: " ++ show (optCors opts)
  case tlsOptions opts of
    Nothing -> putStrLn "tls: False"
    Just _  -> putStrLn "tls: True"

settings :: Port -> Settings
settings port = setPort port defaultSettings

staticSettings :: FilePath -> StaticSettings
staticSettings filePath =
  let sSettings = defaultFileServerSettings filePath
  in sSettings { ssIndices = []
               , ssRedirectToIndex = True
               }

instance Show Settings where
  show settings =
    "Settings {" ++
    " settingsPort=" ++ show (getPort settings) ++
    ", settingsHost=" ++ show (getHost settings) ++
    " }"

instance Show StaticSettings where
  show settings =
    "StaticSettings {" ++
    " ssIndices=" ++ show (ssIndices settings) ++
    ", ssRedirectToIndex=" ++ show (ssRedirectToIndex settings) ++
    " }"

tlsOptions :: Options -> Maybe (FilePath, FilePath)
tlsOptions (Options _ _ _ (Just cert) (Just key)) = Just (cert, key)
tlsOptions _ = Nothing

main :: IO ()
main = do
  (opts, n) <- getArgs >>= compilerOpts
  let s = settings $ optPort opts
  let ss = staticSettings $ optDirectory opts
  let application = if optCors opts
        then simpleCors $ staticApp ss
        else staticApp ss
  printOptions opts
  case tlsOptions opts of
    Nothing -> runSettings s application
    Just (cert, key) -> do
      let tls = tlsSettings cert key
      runTLS tls s application