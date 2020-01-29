# Guide

## 外部設計

### 命名付与基準

外部設計を行う上で遵守すべき命名付与基準を示す。

項番 項目名                       形式                 例
---- ---------------------------- -------------------- ----------
1    画面ID                       SC<業務ID><999999>   SCKT010301
2    イベントID                   (本PJでは存在しない)
3    画面メッセージID             (本PJでは存在しない)
4    ログメッセージID             (本PJでは存在しない)
5    入力チェック基本メッセージID (本PJでは存在しない)

※ <999999>: 000001から始まる6桁の連番

### 画面メッセージ

画面メッセージとは、ユーザの画面操作の結果、画面に表示する文言を指す。本システムではメッセージ内容の重要度に応じ、以下の表に示す3レベルに分けてメッセージを管理する。メッセージ定義の際は出力するメッセージが以下のどのレベルに属するか意識して定義すること。

メッセージレベル カテゴリ                 概要
---------------- ------------------------ --------------------------------------------------------------------------------------------------------------
INFO             情報メッセージ           ユーザの操作による処理が正常に実行された後、画面に表示するメッセージ。
WARN             警告メッセージ           何らかの問題があるが、処理を続行できる場合のメッセージ。
ERROR            入力エラーメッセージ     ユーザの入力値が不正な場合に、入力画面に表示するメッセージ。
ERROR            業務エラーメッセージ     業務ロジックでのエラーと判定された場合のメッセージ。
ERROR            システムエラーメッセージ システム的なエラー(データベースとの接続不可など)で、ユーザの操作により正常復帰できない場合のエラーメッセージ。

### 入力値チェック

#### 入力値チェックの種類

#### チェック対象

#### エラーメッセージの出力場所

### ファイルアップロード、ダウンロード

### 帳票

### UIコンポーネント

#### ページネーション

#### テーブルUI

## AP基盤設計書

### エラー処理

### 発生するエラーの定義とパターン、ハンドリング方法

## ログ出力のパフォーマンスログの取得方法

### 実装の基本方針

パフォーマンスログの挿入は、CDIのInterceptorによるAOPプログラミングによりアノテーションによる対象の指定を行う。

メソッドの前後に任意の処理を織り込むことをAOP、織り込む処理のことをインターセプターと言う。

インターセプターを作るには次の2つのモジュールを作成する。

- アノテーション：メソッドにどのインターセプターを適用するか、というマーカーとなるアノテーションを作る。
- インターセプタークラス：メソッドの前後に織り込む処理を実装する。

#### 実装例

ここでは、`LoggingInterceptor`アノテーションを定義し、パフォーマンスの計測対象である`SampleBackingBean`を例に取り、実装の手順を説明する。

まずは、パフォーマンスログ専用のアノテーションを定義する。

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
import javax.interceptor.InterceptorBinding;
 
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface PerformanceLogging {
    
}
```

定義したアノテーションは「パフォーマンスログを計測するクラス」と、「計測されるクラス」の両方に付与する必要がある。

以下にパフォーマンスログを計測するクラスのコード例を示す。

```java
// パフォーマンスログの内容を定義する共通部品
import java.util.concurrent.TimeUnit;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PerformanceLogging
@Interceptor
public class MeasureTimeInterceptor {
	
    // このロギング処理が例外を起こしては本末転倒であるため、
    // 実装は安全かつ最低限に抑える
    @AroundInvoke
    public Object logInvocation(InvocationContext ctx) throws Exception {
        Logger log = LoggerFactory.getLogger(context.getMethod().getDeclaringClass());

        try {
            // ステップ1: メソッド呼び出し前の処理
            long start = System.nanoTime();
            // ステップ2: 本来のメソッドを実行する
            Object result = ctx.proceed();

            return result;
        } finally {
            // ステップ3: メソッド呼び出し後の処理
            long end = System.nanoTime();
            long durationInMs = TimeUnit.NANOSECONDS.toMillis(end - start);

            String clazz = ctx.getMethod().getDeclaringClass().getName();
            String method = ctx.getMethod().getName();
            // TRACEの出力
            // ここでは計測対象のメソッド名を特定するとともに、
            // どのスレッドがいつ以下のログを書いたかはSLF4Jが出力すると仮定
            log.trace(String.format("{0}.{1}: {2} ms", clazz, method, durationInMs)); 
        }
    }

}
```

最後に、計測対象のメソッドにアノテーションを付与する。計測されるクラスはCDI管理ビーンである必要がある。

```java
public class SampleBB {
  @PerformanceLogging
  public void measuredMethod() {
    /* 省略 */
  }
}
```

## 参考資料

タイトル                                                               URL
---------------------------------------------------------------------- --------------------------------------------------------
再利用と部品化                                                         https://www.ipa.go.jp/security/awareness/vendor/programmingv1/b10_02.html
参考資料６：アーキテクチャー設計の考え方と成果物のイメージ             https://www.mhlw.go.jp/sinsei/chotatu/chotatu/shiyousho-an/dl/090327-1m.pdf
4. ソフトウェア開発技法実践的演習教育コンテンツ                        https://www.ipa.go.jp/jinzai/renkei/ipedia/hanyou_sample
25.6 Using Interceptors in CDI Applications                            https://docs.oracle.com/javaee/7/tutorial/cdi-adv006.htm
Interface InvocationContext                                            https://docs.oracle.com/javaee/7/api/javax/interceptor/InvocationContext.html
CDI AOP Tutorial: Java Standard Method Interception Tutorial - Java EE https://dzone.com/articles/cdi-aop
Analyze application performance with CDI Interceptors                  http://claudioaltamura.de/javaee/analyze-application-performance-with-cdi-interceptors
