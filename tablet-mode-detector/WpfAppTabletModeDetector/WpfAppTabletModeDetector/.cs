//using System;
//using System.Collections.Generic;
//using System.IO;
//using System.Linq;
//using System.Runtime.Serialization;
//using System.Runtime.Serialization.Json;
//using System.Text;
//using System.Threading.Tasks;

//namespace WpfAppTabletModeDetector
//{
//    [DataContract]
//    class WebSocketIncomingMessage
//    {
//        private string message;

//        [DataMember(Name = "message")]
//        public string Message { get => message; set => message = value; }

//        public string ToJson()
//        {
//            using (var ms = new MemoryStream())
//            using (var sr = new StreamReader(ms))
//            {
//                var serializer = new DataContractJsonSerializer(typeof(WebSocketOutgoingMessage));
//                serializer.WriteObject(ms, this);
//                ms.Position = 0;

//                var json = sr.ReadToEnd();

//                return json;
//            }
//        }

//        internal static WebSocketIncomingMessage FromJson(string messageUtf8)
//        {
//            using (var ms = new MemoryStream(Encoding.UTF8.GetBytes(messageUtf8)))
//            {
//                var  serializer = new DataContractJsonSerializer(typeof(WebSocketIncomingMessage));
//                var message = (WebSocketIncomingMessage)serializer.ReadObject(ms);

//                return message;
//            }
//        }
//    }
//}
