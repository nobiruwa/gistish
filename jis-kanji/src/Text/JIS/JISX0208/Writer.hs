{-# LANGUAGE OverloadedStrings #-}
module Text.JIS.JISX0208.Writer where

import Text.JIS.JISX0208.JISChar (JISChar(..))

import Data.Aeson ((.:), (.=), FromJSON(..), ToJSON(..), Value(..), object)
import Data.Aeson.Encode.Pretty (Config(..), Indent(..), defConfig, encodePretty', keyOrder)
import Data.ByteString.Lazy.Char8 (ByteString)
import qualified Data.ByteString.Lazy.Char8 as BLC
import Data.Ord (compare)

instance FromJSON JISChar where
  parseJSON (Object v) =
    JISChar
    <$> (v .: "shiftJis" >>= (.: "value"))
    <*> (v .: "jisX0208" >>= (.: "value"))
    <*> (v .: "unicode" >>= (.: "value"))
    <*> (v .: "shiftJis" >>= (.: "hex"))
    <*> (v .: "jisX0208" >>= (.: "hex"))
    <*> (v .: "unicode" >>= (.: "hex"))
    <*> (v .: "unicode" >>= (.: "name"))
    <*> v .: "character"

instance ToJSON JISChar where
  toJSON (JISChar sjis jisx0208 uni sjisHex jisx0208Hex uniHex uniName char) =
    object [ "shiftJis" .= object [ "value" .= sjis
                                  , "hex" .= sjisHex
                                  ]
           , "jisX0208" .= object [ "value" .= jisx0208
                                  , "hex" .= jisx0208Hex
                                  ]
           , "unicode"  .= object [ "value" .= uni
                                  , "hex" .= uniHex
                                  , "name" .= uniName
                                  ]
           , "character" .= char
           ]

encodeJISChar :: [JISChar] -> ByteString
encodeJISChar = encodePretty'
  (defConfig { confIndent = Spaces 2
             , confCompare = compare
             })

writeString :: FilePath -> [JISChar] -> IO ()
writeString path chars =
  let charText = map jisChar chars
  in writeFile path charText

writeJISChar :: FilePath -> [JISChar] -> IO ()
writeJISChar path = BLC.writeFile path . encodeJISChar
