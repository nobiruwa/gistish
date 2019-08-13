module Database where

import Database.HDBC.PostgreSQL (Connection, connectPostgreSQL)
import Text.Printf (printf)

type DBUser = String
type DBPassword = String
type DBNetloc = String
type DBPort = String
type DBDBName = String

-- postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...]
connect :: DBUser -> DBPassword -> DBNetloc -> DBPort -> DBDBName -> IO Connection
connect user password netloc port dbname = connectPostgreSQL (printf "postgresql://%s:%s@%s:%s/%s" user password netloc port dbname)
