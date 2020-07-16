package javaee7.jsf.logging.listener;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>このクラスは、JSFのページではっせいするリクエストデータを自動的に記録するロギングクラスです。
 * ユーザによる画面操作を解析することを目的としており、全てのデータをダンプするわけではありません。
 * JSFのライフサイクルにAOPされます。</p>
 * <p>このクラスは、以下のデータをログに記録します。</p>
 * <ul>
 *   <li>セッションデータ</li>
 *   <li>リクエストパラメータ</li>
 *   <li>Servlet属性</li>
 * </ul>
 * <p>ログ内容は以下の書式で書き込まれます。</p>
 * <pre><code>Request Parameters : [(キー名1=値1) (キー名2=値2) ...]</code></pre>
 * <p>値の表記は出力されるクラスのtoString()メソッドの実装に依存します。記録する値が文字列や数値といった単純でない、それ以外のクラスはクラス名とメモリ内での識別IDが表示される場合があります。</p>
 * <ul>
 *  <li>使用するためには、faces-config.xmlにphase-listenerとして登録する必要があります。</li>
 *  <li>request-logger.propertiesの設定に従います。request-logger.propertiesは必ず作成してください。</li>
 *  <li>ロガー名はLoggingPhaseListenerLoggerです。</li>
 *  <li>ログの記録先は、アプリケーションログ(コンテキスト名.log)です。</li>
 * </ul>
 * <p>request-logger.propertiesでは以下のカスタイズが可能です。</p>
 * <ul>
 *  <li>ログを書き込む際のログレベル。デフォルトはINFOです。</li>
 *  <li>ログを書き込むページのリスト。'request.logger.allow.'で始まる任意のキーを
 *  ビューのID('http://hostname:8080/cms/aaa/bbb/ccc.xhtml'というページの場合、
 *  '/aaa/bbb/ccc.xhtml')をプロパティの値に指定します。0個以上のビューのIDを指定できます。</li>
 * </ul>
 */
public class LoggingPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 7896550322450821498L;

    private static final Logger logger = Logger.getLogger("LoggingPhaseListenerLogger");

    // 定数値をpropertiesファイルから取得する

    // <p>コンテキストパス/WEB-INF/classesを起点とした、
    // このクラスのための設定プロパティファイルの相対パス</p>
    private static final String PROPERTIES_PATH = "request-logger.properties";

    // プロパティキー: ログレベル
    private static final String PROP_KEY_LOG_LEVEL = "request.logger.level";
    // プロパティキー: パスのURLのプリフィックス
    // このプリフィクスで始まるキーはallowListに追加される
    private static final String PROP_KEY_PREFIX_URL = "request.logger.allow.";

    // クラス内部のマジックナンバーの除去
    private static final String NO_VIEW = "(no view)";

    // メンバー
    // ログ書き込みを許可するURLのリスト
    // プロパティキーとURLの組はallowMapに
    // URLのリストはallowSetに格納する
    private Map<String, String> allowMap;
    private Set<String> allowSet;
    // ログを書き込む際のログレベル
    private Level logLevel;

    public LoggingPhaseListener() {
        // プロパティから設定値を読み込む
        loadProperties();
    }

    private void loadProperties() {
        // プロパティを読み込むことをログに記録する
        // このログが頻繁に発生する場合は
        // 設計を見直す
        ResourceBundle bundle = openProperties();

        // ログレベルの決定
        logLevel = readLogLevel(bundle);
        // 許可URLリストの作成
        allowMap = readAllowMap(bundle);
        allowSet = new HashSet<String>(allowMap.values());
    }

    private ResourceBundle openProperties() {
        ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_PATH);
        return bundle;
    }

    private Level readLogLevel(ResourceBundle bundle) {
        String logLevelValue = bundle.getString(PROP_KEY_LOG_LEVEL);
        // 文字列からログレベルを表す定数オブジェクトに変換する

        // 不正な値の場合はログレベルINFOで書き込むようフォールバックする
        final Level fallBackLevel = Level.INFO;
        try {
            // 大文字小文字の違いは無視する
            return Level.parse(logLevelValue.toUpperCase());
        } catch (NullPointerException|IllegalArgumentException ex) {
            return fallBackLevel;
        }
    }

    private Map<String, String> readAllowMap(ResourceBundle bundle) {
        Enumeration<String> keys = bundle.getKeys();

        Map<String, String> map = new ConcurrentHashMap<>();

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            // 特定のプリフィックスで始まるキーのみ格納する
            if (key.startsWith(PROP_KEY_PREFIX_URL)) {
                String value = bundle.getString(key);
                map.put(key, value);
            }
        }

        return map;
    }

    public void afterPhase(final PhaseEvent event) {
        // 何もしない
        // テストで不具合があった場合
    }

    public void beforePhase(final PhaseEvent event) {
        try {
            // 書き込みに適したJSFライフサイクルフェーズかを判断する
            if (event.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
                // リクエストの発生元のビューIDを取得する
                String viewID = getCurrentViewID(event);

                // ビューIDからログを書き込むべきかを判別する
                if (shouldWriteLog(viewID)) {
                    record(viewID, event, "before phase:");
                }
            }
        } catch (Exception e) {
            // このクラスが引き起こす例外で本来の動作を止めないよう
            // エラーメッセージの記録に留める
            System.err.println(e.getMessage());
        }
    }

    private Boolean shouldWriteLog(String viewID) {
        // 許可URLリスト(allow list)の定義
        // リクエストロガーはURLが完全一致する場合に書き込みを行う
        // 以下のルールに従って記述すること
        // - コンテキストパス名は除く(URLが'http://hostname:8080/aaa/bbb/ccc/ddd.xhtml'ならば'/bbb/ccc/ddd.xhtml'と書く)
        // - リクエストパラメータ('?a=foo&b=bar'など)は除去すること
        // - キー名を名付ける際、request.logger.allow.に続く文字列は衝突しなければ何でもよい。'.'を何個続けてもよい。
        return allowSet.contains(viewID);
    }

    protected void record(final String viewID, final PhaseEvent event, final String msg) {
        try {
            // ビューIDの記録
            writeLog(msg + event.getPhaseId() + " " + viewID);

            // ユーザの操作の記録
            // 1. セッション情報の記録
            writeSessionAttributes(event);
            // 2. リクエストのパラメータの記録
            writeRequestParameters(event);
            // 3. リクエストの属性の記録
            writeRequestAttributes(event);
        } catch (Exception e) {
            // このクラスが引き起こす例外で本来の動作を止めないよう
            // エラーメッセージの記録に留める
            System.err.println(e.getMessage());
        }
    }

    private String getCurrentViewID(PhaseEvent event) {
        final UIViewRoot view = event.getFacesContext().getViewRoot();

        String viewID = NO_VIEW;
        if (view != null) {
            viewID = view.getViewId();
        }

        return viewID;
    }

    private void writeSessionAttributes(final PhaseEvent event) {
        final Map<String, Object> sessAttrs = event.getFacesContext().getExternalContext().getSessionMap();
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (final String key : sessAttrs.keySet()) {
            sb.append("(" + key + "=" + sessAttrs.get(key) + ") ");
        }
        sb.append("]");
        writeLog("Session Attributes : " + sb.toString());
    }

    private void writeRequestAttributes(final PhaseEvent event) {
        final Map<String, Object> reqAttrs = event.getFacesContext().getExternalContext().getRequestMap();
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (final String key : reqAttrs.keySet()) {
            sb.append("(" + key + "=" + reqAttrs.get(key) + ") ");
        }
        sb.append("]");
        writeLog("Request Attributes : " + sb.toString());
    }

    private void writeRequestParameters(final PhaseEvent event) {
        final Map<String, String> reqParams = event.getFacesContext().getExternalContext().getRequestParameterMap();
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (final String key : reqParams.keySet()) {
            sb.append("(" + key + "=" + reqParams.get(key) + ") ");
        }
        sb.append("]");
        writeLog("Request Parameters : " + sb.toString());
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private void writeLog(final String message) {
        logger.log(logLevel, message);
    }
}
