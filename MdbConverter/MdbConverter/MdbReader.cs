using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.OleDb;

using Record = System.Collections.Generic.Dictionary<string, MdbConverter.ColumnValue>;

namespace MdbConverter
{
    // http://kageura.hatenadiary.jp/entry/2015/03/31/140132
    // https://qiita.com/yaju/items/86314412741deb806366

    public class MdbReader
    {
        public string FilePath { get; }
        public string TableName { get; }
        public Dictionary<string, Type> ReaderSettings { get; }
        public Encoding MdbEncoding { get; }

        public string ConnectionString
        {
            get
            {
                return String.Format(
                    "Provider=Microsoft.Jet.OLEDB.4.0;Data Source={0};Persist Security Info=False",
                    FilePath
                    );
                
            }
        }

        public string SelectStatement
        {
            get
            {
                return String.Format(
                    "SELECT {0} FROM {1}",
                    String.Join(", ", Columns),
                    TableName
                    );
            }
        }

        private string[] Columns
        {
            get
            {
                return ReaderSettings.Keys.ToArray();
            }
        }

        public MdbReader(string filePath, string tableName, Dictionary<string, Type> readerSettings, Encoding encoding)
        {
            AssertX86();

            FilePath = filePath;
            TableName = tableName;
            ReaderSettings = readerSettings;
            MdbEncoding = encoding;
        }

        public void AssertX86()
        {
            if (IntPtr.Size != 4)
            {
                throw new NotSupportedException("x86のみをサポートしています。");
            }
        }

        public List<Record> Read()
        {
            OleDbConnection conn = new OleDbConnection
            {
                ConnectionString = ConnectionString
            };

            conn.Open();

            OleDbCommand comm = new OleDbCommand
            {
                CommandText = SelectStatement,
                Connection = conn
            };

            OleDbDataReader reader = comm.ExecuteReader();
            List<Record> records = new List<Record>();

            while (reader.Read())
            {
                var record = ReaderSettings
                    .ToArray()
                    .Select((KeyValuePair<string, Type> keyValue, int index) => {
                        var columnName = keyValue.Key;
                        var columnType = keyValue.Value;
                        object value = reader.GetValue(index);
                        if (columnType == typeof(string))
                        {
                            return new KeyValuePair<string, ColumnValue>(
                                columnName,
                                new ColumnValue(
                                    value, // DecodeString((byte[])value, MdbEncoding),
                                    columnType
                                    )
                                );
                        }
                        return new KeyValuePair<string, ColumnValue>(
                            columnName,
                            new ColumnValue(
                                value,
                                columnType
                                )
                            );
                    })
                    .ToDictionary(
                        keyValue => keyValue.Key,
                        keyValue => keyValue.Value
                    );
                records.Add((Record)record);
            }

            conn.Close();

            return records;
        }

        private object DecodeString(byte[] value, Encoding mdbEncoding)
        {
            return mdbEncoding.GetString(value);
        }
    }
}
