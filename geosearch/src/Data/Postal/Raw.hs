module Data.Postal.Raw where

import Data.ByteString.Lazy (ByteString, fromStrict, toStrict)
import Data.Csv.Streaming (HasHeader(..), decode)
import Data.Either (fromRight)
import Data.Foldable (toList)
import Data.Text.Encoding (encodeUtf8)
import Data.Text.ICU.Convert (toUnicode, open)
import Data.Text.Lazy (Text)
import Data.Traversable (forM)

data Postal = Postal
  { postalJapaneseLocalGovernmentCode :: Text
  , postalExPostalCode :: Text
  , postalPostalCode :: Text
  , postalPrefectureHankakuKatakana :: Text
  , postalCityHankakuKatakana :: Text
  , postalTownHankakuKatakana :: Text
  , postalPrefecture :: Text
  , postalCity :: Text
  , postalTown :: Text
  , postalChoiki :: Int
  , postalKoaza :: Int
  , postalChome :: Int
  , postalMultipleTowns :: Int
  , postalModified :: Int
  , postalReasonForModified :: Int
  } deriving (Eq, Show)

postal :: (Text, Text, Text, Text, Text, Text, Text, Text, Text, Int, Int, Int, Int, Int, Int) -> Either String Postal
postal (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o) = Right (Postal a b c d e f g h i j k l m n o)

decodeKenAll :: ByteString -> IO [Postal]
decodeKenAll csv = do
  converter <- open "cp932" Nothing
  return $ fromRight [] $ toList <$> forM (decode' converter csv) postal
  where
    decode' converter = decode NoHeader . fromStrict . encodeUtf8 . toUnicode converter . toStrict
