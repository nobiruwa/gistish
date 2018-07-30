using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

using System.Windows.Interop;
using System.Runtime.InteropServices;

namespace WpfAppTabletModeDetector
{
    /// <summary>
    /// MainWindow.xaml の相互作用ロジック
    /// </summary>
    public partial class MainWindow : Window
    {
        public delegate void StatusUpdateHandler(object sender, TabletModeEventArgs e);
        public event StatusUpdateHandler OnUpdateStatus;

        private ClientWebSocketHandler clientWebSocketHandler;
        private WindowInteropHelper windowInteropHelper;
        private IntPtr mainWindowHandle;

        [DllImport("user32.dll", SetLastError = true, CharSet = CharSet.Auto, EntryPoint = "GetSystemMetrics")]
        private static extern int GetSystemMetrics(int nIndex);

        [DllImport(@"C:\Users\ein\source\repos\WpfAppTabletModeDetector\x64\Debug\WrlTabletModeDetector.dll")]
        private static extern int IsTabletModeNow(IntPtr hwnd, ref int mode);

        [DllImport(@"C:\Users\ein\source\repos\WpfAppTabletModeDetector\x64\Debug\WrlTabletModeDetector.dll")]
        private static extern int IsTabletModeNowDirectly(IntPtr hwnd, ref int mode);

        public MainWindow()
        {

            InitializeComponent();

            OnUpdateStatus += OnUpdateStausHandler;
            OnUpdateStatus += OnUpdateStatus_SendMessage;

            Activated += Activated_ConnectWebSocketConnection;
            Loaded += Loaded_ConnectWebSocketConnection;
            Initialized += Initialized_ConnectWebSocketConnection;
            this.Hide();

            InitHwnd();
            ConnectWebSocketConnection();
        }

        private async void ConnectWebSocketConnection()
        {
            clientWebSocketHandler = new ClientWebSocketHandler("ws://localhost:3001/chat");
            await clientWebSocketHandler.OpenAsync();
            clientWebSocketHandler.MessageReceived += ClientWebSocket_OnMessageReceived;
        }

        private void InitHwnd()
        {
            windowInteropHelper = new WindowInteropHelper(this);
            windowInteropHelper.EnsureHandle();
        }

        private async void Initialized_ConnectWebSocketConnection(object sender, EventArgs e)
        {

        }

        private async void Loaded_ConnectWebSocketConnection(object sender, RoutedEventArgs e)
        {

            
        }

        private void OnUpdateStatus_SendMessage(object sender, TabletModeEventArgs e)
        {
            var message = new WebSocketOutgoingMessage();
            message.Message = e.IsTabletMode ? "TabletMode" : "MouseMode";
            clientWebSocketHandler.SendMessageAsync(message.ToJson());
        }

        private async void Activated_ConnectWebSocketConnection(object sender, EventArgs e)
        {
        }

        private void ClientWebSocket_OnMessageReceived(string messageUtf8)
        {
            WebSocketIncomingMessage message = WebSocketIncomingMessage.FromJson(messageUtf8);

            if (message.Message == "current-mode")
            {
                var modeName = QueryTabletModeWithPInvoke(mainWindowHandle) ? "TabletMode" : "DesktopMode";
                var response = new WebSocketOutgoingMessage();
                response.Message = modeName;
                clientWebSocketHandler.SendMessageAsync(response.ToJson());
            }
        }

        private void OnUpdateStausHandler(object sender, TabletModeEventArgs e)
        {
            tabletModeTextBox.Text = e.IsTabletMode ? "TabletMode" : "MouseMode";
        }

        protected override void OnSourceInitialized(EventArgs e)
        {
            base.OnSourceInitialized(e);

            // メインウィンドウを表示している場合は以下のコードでウィンドウメッセージを扱えます。
            //HwndSource source = PresentationSource.FromVisual(this) as HwndSource;
            //source.AddHook(WndProc);
            //mainWindowHandle = source.Handle;

            // Hide()とWindowInteropHelper.EnsureHandle()を組み合わせた結果
            // このメソッドが呼び出された場合は、以下のコードでウィンドウメッセージを扱えます。
            HwndSource source = HwndSource.FromHwnd(windowInteropHelper.Handle);
            source.AddHook(WndProc);
            mainWindowHandle = windowInteropHelper.Handle;
        }

        private IntPtr WndProc(IntPtr hwnd, int msg, IntPtr wParam, IntPtr lParam, ref bool handled)
        {
            if (0x001A == msg)
            {
                long wParamValue = wParam.ToInt64();

                string l = Marshal.PtrToStringAuto(lParam);
                if (l == "UserInteractionMode")
                {
                    if (OnUpdateStatus != null)
                    {
                        TabletModeEventArgs args = new TabletModeEventArgs();
                        // args.IsTabletMode = QueryTabletModeFromCOM(hwnd);
                        // args.IsTabletMode = QueryTabletModeFromDllSideCOM(hwnd);
                        args.IsTabletMode = QueryTabletModeWithPInvoke(hwnd);
                        OnUpdateStatus(this, args);
                    }
                }
            }

            return IntPtr.Zero;
        }

        public static Boolean QueryTabletModeFromCOM(IntPtr hwnd)
        {
            ITabletModeDetector detector = (ITabletModeDetector)new TabletModeDetector();
            int mode = -1;
            detector.IsTabletMode(hwnd, ref mode);
            return mode == 1;
        }

        public static Boolean QueryTabletModeFromDllSideCOM(IntPtr hwnd)
        {
            int mode = -1;
            IsTabletModeNow(hwnd, ref mode);
            return mode == 1;
        }

        public static Boolean QueryTabletModeWithPInvoke(IntPtr hwnd)
        {
            int mode = -1;
            IsTabletModeNowDirectly(hwnd, ref mode);
            return mode == 1;
        }
    }
}
