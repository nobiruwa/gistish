{-# LANGUAGE OverloadedStrings #-}
module Embedded
  ( embeddedFavicon
  ) where

import           Crypto.Hash.MD5                ( hashlazy )
import qualified Data.ByteString.Base64        as B64
import qualified Data.ByteString.Lazy          as BL
import qualified Data.Text                     as T
import qualified Data.Text.Encoding            as TE
import           System.Directory               ( doesFileExist
                                                , getCurrentDirectory
                                                )
import           System.FilePath                ( (</>) )
import           WaiAppStatic.Storage.Embedded  ( EmbeddableEntry(..) )

loadFavicon :: IO (T.Text, BL.ByteString)
loadFavicon = BL.readFile "favicon.ico" >>= \c -> return (hash c, c)
  where hash = T.take 8 . TE.decodeUtf8 . B64.encode . hashlazy

{-|
コンパイル時にfavicon.icoを実行ファイルに埋め込むために、Template Haskellから使用してください。

@
{-# LANGUAGE TemplateHaskell #-}
import WaiAppStatic.Storage.Embedded (mkSettings)

let embeddedStaticSettings = $(mkSettings embeddedFavicon)
@

参考: <https://hackage.haskell.org/package/wai-app-static-3.1.7.3/docs/WaiAppStatic-Storage-Embedded.html>
-}
embeddedFavicon :: IO [EmbeddableEntry]
embeddedFavicon = do
  favicon <- loadFavicon
  return
    [ EmbeddableEntry { eLocation = "favicon.ico"
                      , eMimeType = "image/x-icon"
                      -- Rightはリクエストの度にリロードする(デバッグモードに有効)
                      , eContent  = Left favicon
                      }
    ]

