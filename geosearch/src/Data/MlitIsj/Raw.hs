module Data.MlitIsj.Raw where

import Data.ByteString.Lazy (ByteString, fromStrict, toStrict)
import Data.Csv.Streaming (HasHeader(..), decode)
import Data.Either (fromRight)
import Data.Foldable (toList)
import Data.Text.Encoding (encodeUtf8)
import Data.Text.ICU.Convert (toUnicode, open)
import Data.Text.Lazy (Text)
import Data.Traversable (forM)

{-| ISJ(位置参照情報のローマ字Acronym)
-}
data MlitIsj = MlitIsj
  { mlitIsjPrefectureCode :: Text
  , mlitIsjPrefectureName :: Text
  , mlitIsjCityTownCode :: Text
  , mlitIsjCityTownName :: Text
  , mlitIsjOazaMachiChomeCode :: Text
  , mlitIsjOazaChomeName :: Text
  , mlitIsjLatitude :: Double -- 緯度
  , mlitIsjLongitude :: Double -- 経度
  , mlitIsjSource :: Text
  , mlitIsjOazaAzaChomeDivisionCode :: Text
  } deriving (Eq, Show)

mlitIsj :: (Text, Text, Text, Text, Text, Text, String, String, Text, Text) -> Either String MlitIsj
mlitIsj (a, b, c, d, e, f, g, h, i, j) = Right (MlitIsj a b c d e f (read g) (read h) i j)

decodeMlitIsj :: ByteString -> IO [MlitIsj]
decodeMlitIsj csv = do
  converter <- open "cp932" Nothing
  return $ fromRight [] $ toList <$> forM (decode' converter csv) mlitIsj
  where
    decode' converter = decode HasHeader . fromStrict . encodeUtf8 . toUnicode converter . toStrict

decodeMlitIsj' :: ByteString -> IO (Either String [MlitIsj])
decodeMlitIsj' csv = do
  converter <- open "cp932" Nothing
  return $ toList <$> forM (decode' converter csv) mlitIsj
  where
    decode' converter = decode HasHeader . fromStrict . encodeUtf8 . toUnicode converter . toStrict
