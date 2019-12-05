# tesseract-ocr

tesseractを使ったOCRを学習します。

@rrryutaro氏の記事をなぞるだけです。

- [(2017年12月) PythonとOpenCVをこれからやってみる - 3 - 文字認識(1)](https://qiita.com/rrryutaro/items/0c0a7382560e1f67123b)

## Install

- `$ sudo apt-get install tesseract-ocr tesseract-ocr-jpn`
  - `$ tesseract --list-langs`
  - `$ tesseract -l jpn resource/hiragana-katakana.png -`
- `$ pip install --user pyocr Pillow numpy opencv-python`
  - 実際はvirtualenvを使いたいので、`install.sh`を実行すること

## 実行

```
$ . source.sh
$ python3 ./app.py
```

### 実行結果

```
(2017年12月) PythonとOpenCVをこれから
やってみる - 1 - はじめの一歩
Python
OpenCV
はじめに
Python(パイソン)を中々使う機会が無く、OpenCV(オープンシーブイ)も気にはなりつつ使う機会が無く。
そんな折、ちょっとやってみようと思ったので、今からやるにあたっての導入やはじめの一歩など。
環境はWindows7 64bitでやります。
```
