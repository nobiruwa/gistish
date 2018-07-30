# WpfAppTabletModeDetector

このソリューションは、Windows10においてタブレットモードの有効・無効を検知します。

Windows8以降Windows Runtime APIとして搭載された、ContinuumのTablet Mode APIを用います。

Windows Runtime APIは以下の方式で呼び出すことができます。

1. UWPアプリケーションから直接Tablet Mode APIを呼び出すことができます。
   - ただし、UWPアプリケーションサービスはバックグラウンドタスクを実行するプロセスであり、ウィンドウハンドルを持たないのでTablet Mode APIを呼び出すことができません。
2. Visual C++のライブラリからC++/WRLを用いてTablet Mode APIを呼び出すことができます。
   - アンマネージドコードであるため、C#アプリケーションからの呼び出し方法は以下の2通りです。
     1. Visual C++のライブラリをCOMインターフェースとして公開しておき、C#アプリケーションはCOMインスタンスを呼び出します。
     2. Visual C++のライブラリをP/Invokeを使って呼び出します。

- WebSocket
  - 
  - ループバックでの通信を越えるために、FQDN名をホストに登録する必要があります。
- HTTP
  - ループバックでの通信を越えるために、FQDN名をホストに登録する必要があります。


## プロジェクト依存関係

- Wrl
- WpfAppTabletModeDetector

## 参考文献

### Wrl

1. プロジェクトの作成手順は[How to: Create a Classic COM Component Using WRL](https://docs.microsoft.com/en-us/cpp/windows/how-to-create-a-classic-com-component-using-wrl)を参考にしました。
2. COMコンポーネントへのGUID生成手順は[[Windows]Windowsデスクトップ環境でUUID(GUID)を生成する方法](http://d.hatena.ne.jp/torutk/20160519/p1)を参考にしました。
3. COMコンポーネントにTablet Mode APIを組み込むために必要なヘッダーファイルと実装は[Chromium: src/base/win/win_util.cc](https://cs.chromium.org/chromium/src/base/win/win_util.cc?type=cs&sq=package:chromium&g=0)と[GitHub: mozilla/newtab-dev/widget/windows/WindowsUIUtils.cpp](https://github.com/mozilla/newtab-dev/blob/master/widget/windows/WindowsUIUtils.cpp)を参考にしました。
4. WRLコンポーネントを直接インスタンス化する手順は[方法: WRL コンポーネントを直接インスタンス化する](https://msdn.microsoft.com/ja-jp/library/jj873953.aspx)を参考にしました。

1. [UWPアプリケーションはWebSocketをホストできない](https://github.com/Microsoft/Windows-universal-samples/issues/688)、という点から、WRLコンポーネントを用いることにしました。