using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MdbConverter
{
    public class ColumnValue
    {
        public ColumnValue(object value, Type type)
        {
            Value = value;
            Type = type;
        }

        public object Value { get; }
        public Type Type { get; }
    }
}
