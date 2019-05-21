using System;
using System.Collections.Generic;
using System.Data.SQLite;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Record = System.Collections.Generic.Dictionary<string, MdbConverter.ColumnValue>;

namespace MdbConverter
{
    // https://www.ipentec.com/document/csharp-sqlite-insert-record

    public class SqliteWriter
    {
        public SqliteWriter(string filePath, string tableName, Dictionary<string, Type> writerSettings, Encoding encoding)
        {
            FilePath = filePath;
            TableName = tableName;
            WriterSettings = writerSettings;
            SqliteEncoding = encoding;
        }

        public string ConnectionString
        {
            get
            {
                return String.Format(
                    "Data Source={0}",
                    FilePath
                    );

            }
        }
        public string FilePath { get; }
        public string TableName { get; }
        public Dictionary<string, Type> WriterSettings { get; }
        public Encoding SqliteEncoding { get; }


        public string InsertStatement
        {
            get
            {
                return String.Format(
                    "INSERT INTO {0} ({1}) VALUES ({2})",
                    String.Format("\"{0}\"", TableName),
                    String.Join(", ", Columns),
                    String.Join(", ", PlaceHolders)
                    );
            }
        }

        private string[] Columns
        {
            get
            {
                return WriterSettings.Keys
                    .Select(k => String.Format("\"{0}\"", k))
                    .ToArray();
            }
        }

        public string[] PlaceHolders
        {
            get
            {
                return WriterSettings.Keys
                    .Select(columnName => "@" + columnName).ToArray();
            }
        }

        public void Write(List<Record> records)
        {
            SQLiteConnection conn = new SQLiteConnection(ConnectionString);
            conn.Open();

            CreateTableIfNotExists(conn);

            records.ForEach((record) => InsertRecord(conn, record));
        }

        public void CreateTableIfNotExists(SQLiteConnection openedConn)
        {
            using (SQLiteCommand cmd = openedConn.CreateCommand())
            {
                cmd.CommandText = CreateStatement;
                cmd.ExecuteNonQuery();
            }
        }

        public string CreateStatement
        {
            get
            {
                return String.Format(
                    "CREATE TABLE IF NOT EXISTS {0} ({1}, {2});",
                    String.Format("\"{0}\"", TableName),
                    "id INTEGER PRIMARY KEY AUTOINCREMENT",
                    String.Join(", ", TypedColumns)
                    );
            }
        }

        public string[] TypedColumns
        {
            get
            {
                return WriterSettings
                    .Select((KeyValuePair<string, Type> keyValuePair) => {
                        var columnName = keyValuePair.Key;
                        var columnType = keyValuePair.Value;
                        var sqliteType = GetSqliteType(columnType);
                        return String.Format(
                            "\"{0}\" {1}",
                            columnName,
                            sqliteType
                            );
                    })
                    .ToArray();
            }
        }

        public string PrimaryKey
        {
            get
            {
                return Columns[0];
            }
        }

        private string GetSqliteType(Type type)
        {
            return ConvertTypes.ToSqliteType[type];
        }

        public void InsertRecord(SQLiteConnection openedConn, Record record)
        {
            using (var transaction = openedConn.BeginTransaction())
            {
                using (SQLiteCommand cmd = openedConn.CreateCommand())
                {
                    cmd.CommandText = InsertStatement;

                    var parameters = record
                        .Select((KeyValuePair<string, ColumnValue> keyValuePair) => {
                            var columnName = keyValuePair.Key;
                            var columnValue = keyValuePair.Value;
                            return new SQLiteParameter("@" + columnName, columnValue.Value);
                        })
                        .ToArray();
                    cmd.Parameters.AddRange(parameters);

                    cmd.ExecuteNonQuery();
                }
                transaction.Commit();
            }
        }

    }
}
