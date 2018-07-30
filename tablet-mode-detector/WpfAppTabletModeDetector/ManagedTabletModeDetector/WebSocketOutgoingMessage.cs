using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace WpfAppTabletModeDetector
{
    [DataContract]
    public class WebSocketOutgoingMessage
    {

        [DataMember(Name = "message")]
        public string Message { get; set; }

        public string ToJson()
        {
            using (var ms = new MemoryStream())
            using (var sr = new StreamReader(ms))
            {
                var serializer = new DataContractJsonSerializer(typeof(WebSocketOutgoingMessage));
                serializer.WriteObject(ms, this);
                ms.Position = 0;

                var json = sr.ReadToEnd();

                return json;
            }
        }
    }
}
