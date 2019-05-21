using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MdbConverter
{
    public static class ConvertTypes
    {
        public static Dictionary<Type, string> ToSqliteType
        {
            get
            {

                return new Dictionary<Type, string>()
                {
                    { typeof(uint), "INTEGER PRIMARY KEY AUTOINCREMENT" }, 
                    { typeof(string), "TEXT" },
                    { typeof(int), "INTEGER" },
                    { typeof(float), "REAL" },
                    { typeof(byte[]), "BLOB" }
                };
            }
        }
    }
}
