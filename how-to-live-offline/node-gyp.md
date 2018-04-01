# node-gyp

Node.JSのあるパッケージはネイティブのアドオンを必要とします。
そのようなパッケージをnpmでインストールするとき、
パッケージのインストール時にバイナリをダウンロードして
展開するか、何らかの理由でダウンロードできない場合は
ソースコードからビルドを試みます。

node-gypはNode.JSパッケージのネイティブのアドオンをビルドするためのツールです。
ここでは、オフラインのWindowsにてnode-gypをセットアップする方法について説明します。

## インターネットから資材をダウンロードする

必要な資材をインターネットから取得しなければなりません。

### node-gypの情報について

node-gypの開発元のドキュメントを参照してください[1]。
また、Microsoftのドキュメントを参照してください[2]。

### Node.JS

1. Node.JSのインストーラをダウンロードしてください[3]。
2. Node.JSのヘッダーファイルをダウンロードしてください[3]。
   `http://nodejs.org/dist/v<version-number>/node-v<version-number>-heaers.tar.gz`という名称で置かれています。
3. Node.JSの静的ライブラリをダウンロードしてください[3]。
   `http://nodejs.org/dist/v<version-number>/win-x64/node.lib`と
   `http://nodejs.org/dist/v<version-number>/win-x86/node.lib`の
   両方が必要です。

### Python

1. Pythonのインストーラをダウンロードしてください[4]。
   node-gypの要件に従ってバージョンを選択してください[1]。
   すなわち、Python2.7系を用いる必要があります。

### Visual C++ Build Environment

1. Visual C++ Build Toolsのダウンローダを
   ダウンロードしてください。
   `windows-build-tools`の`constant.js`で、Visual Studio 2015
   のURLを確認してください[5]。
2. 以下のコマンドを実行してください。
   - `visualcppbuildtools_full.exe /layout`
3. `visualcppbuildtools_full.exe`が表示するダイアログで、
   任意のフォルダを選択し、`.exe`を含むインストーラ等の資材を
   ダウンロードしてください。

## セットアップ

任意の媒体を用いて、インストーラ等の資材を、オフラインのWindowsにコピーしてください。

以下の手順に従ってセットアップしてください。

1. Node.JSをインストールしてください。
   オプションの選択を求められた場合は、全てデフォルトを選択してください。
   環境変数PATHにNode.JSのフォルダが追加されたことを確認してください。
2. Pythonをインストールしてください。
   オプションの選択を求められた場合は、全てデフォルトを選択してください。
   環境変数PATHにPythonのフォルダが追加されたことを確認してください。
3. `.exe`を実行し、Visual C++ Build Toolsをインストールしてください。
4. Windowsを再起動してください。
5. 以下のコマンドを実行してください。
   - `npm config set python python2.7`
   - `npm config set msvs_version 2015`
6. ヘッダーファイルと静的ライブラリを下記のように置いてください。

```
%USERPROFILE%/
`-- .node-gyp
    `-- v<version-number>
        |-- ia32
        |   `-- node.lib (1)
        |-- include
        |   `-- ...      (2)
        `-- x64
            `-- node.lib (3)
```

(1)は`http://nodejs.org/dist/v<version-number>/win-x86/node.lib`、
(2)は`http://nodejs.org/dist/v<version-number>/node-v<version-number>-heaers.tar.gz`を解答して得られるincludeフォルダ、
(3)は`http://nodejs.org/dist/v<version-number>/win-x64/node.lib`です。

## Links

- [1] https://github.com/nodejs/node-gyp
- [2] https://github.com/Microsoft/nodejs-guidelines/blob/master/windows-environment.md#compiling-native-addon-modules
- [3] https://nodejs.org/en/download/
- [4] https://www.python.org/downloads/
- [5] https://github.com/felixrieseberg/windows-build-tools/blob/master/src/constants.js
- [6] http://landinghub.visualstudio.com/visual-cpp-build-tools
- [7] https://docs.microsoft.com/en-us/visualstudio/install/create-an-offline-installation-of-visual-studio#how-to-customize-your-offline-%20installer
