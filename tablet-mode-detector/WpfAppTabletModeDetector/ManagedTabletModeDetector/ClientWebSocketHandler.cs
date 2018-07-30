using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace WpfAppTabletModeDetector
{
    public class ClientWebSocketHandler
    {
        private const int ReceiveChunkSize = 1024;
        private const int SendChunkSize = 1024;

        private CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        private CancellationToken cancellationToken;

        private Uri uri;
        private ClientWebSocket clientWebSocket = null;

        public delegate void ConnectionEstablishedHandler();
        public delegate void ConnectionClosedHandler();
        public delegate void MessageReceivedHandler(string message);

        public ConnectionEstablishedHandler ConnectionEstablished;
        public ConnectionClosedHandler ConnectionClosed;
        public MessageReceivedHandler MessageReceived;

        public ClientWebSocketHandler(String uri)
        {
            this.uri = new Uri(uri);
            clientWebSocket = new ClientWebSocket();
            cancellationToken = cancellationTokenSource.Token;
        }

        public async Task OpenAsync()
        {
            await clientWebSocket.ConnectAsync(uri, cancellationToken);
            if (ConnectionClosed != null)
            {
                ConnectionEstablished();
            }
            StartListen();
        }

        public async Task CloseAsync()
        {
            await clientWebSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, string.Empty, CancellationToken.None);
        }

        public async void SendMessageAsync(string message)
        {
            if (clientWebSocket.State != WebSocketState.Open)
            {
                throw new Exception("Connection is not open.");
            }

            var messageBuffer = Encoding.UTF8.GetBytes(message);
            var messagesCount = (int)Math.Ceiling((double)messageBuffer.Length / SendChunkSize);

            for (var i = 0; i < messagesCount; i++)
            {
                var offset = (SendChunkSize * i);
                var count = SendChunkSize;
                var lastMessage = ((i + 1) == messagesCount);

                if ((count * (i + 1)) > messageBuffer.Length)
                {
                    count = messageBuffer.Length - offset;
                }

                await clientWebSocket.SendAsync(new ArraySegment<byte>(messageBuffer, offset, count), WebSocketMessageType.Text, lastMessage, cancellationToken);
            }
        }


        private async void StartListen()
        {

            var buffer = new byte[ReceiveChunkSize];

            try
            {
                while (clientWebSocket.State == WebSocketState.Open)

                {
                    var stringResult = new StringBuilder();
                    WebSocketReceiveResult result;

                    do
                    {

                        result = await clientWebSocket.ReceiveAsync(new ArraySegment<byte>(buffer), cancellationToken);



                        if (result.MessageType == WebSocketMessageType.Close)
                        {
                            await clientWebSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, string.Empty, CancellationToken.None);
                            
                            if (ConnectionClosed != null)
                            {
                                ConnectionClosed();
                            }
                        }
                        else
                        {
                            var str = Encoding.UTF8.GetString(buffer, 0, result.Count);

                            stringResult.Append(str);
                        }
                    } while (!result.EndOfMessage);

                    if (MessageReceived != null)
                    {
                        MessageReceived(stringResult.ToString());
                    }
                }

            }
            catch (Exception)
            {
                if (ConnectionClosed != null)
                {
                    ConnectionClosed();
                }
            }
            finally
            {
                clientWebSocket.Dispose();
            }
        }
    }
}
