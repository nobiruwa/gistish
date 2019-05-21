using MdbConverter;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Record = System.Collections.Generic.Dictionary<string, MdbConverter.ColumnValue>;

namespace MdbConverterDemo
{
    class Program
    {
        static void Main(string[] args)
        {
            var settings = new Dictionary<string, Type>() {
                    { "会社コード", typeof(string) },
                    { "会社名", typeof(string) },
                    { "部署名", typeof(string) },
                    { "郵便番号", typeof(string) },
                    { "住所", typeof(string) },
                    { "電話番号", typeof(string) },
                    { "FAX番号", typeof(string) },
                };

            SqliteWriterDemo(settings, MdbReaderDemo(settings));
        }

        static List<Record> MdbReaderDemo(Dictionary<string, Type> settings)
        {
            // http://ec.nikkeibp.co.jp/nsp/dl/09412/index.shtml
            // http://ec.nikkeibp.co.jp/nsp/dl/03634/index.shtml
            // https://www.vector.co.jp/soft/dl/winnt/business/se469691.html

            MdbReader reader = new MdbReader(
                "./03634/第13章/名刺管理13_12.mdb",
                "会社と部署",
                settings,
                Encoding.GetEncoding("Shift-JIS")
                );

            return reader.Read();
        }

        static void SqliteWriterDemo(Dictionary<string, Type> settings, List<Record> records)
        {
            // https://sqlitebrowser.org/

            var now = DateTime.Now;
            SqliteWriter writer = new SqliteWriter(
                String.Format("./{0}-{1}-{2}-{3}{4}{5}.db", now.Year, now.Month, now.Day, now.Hour, now.Minute, now.Second),
                "会社と部署",
                settings,
                UTF8Encoding.UTF8
                );

            writer.Write(records);
        }
    }
}
