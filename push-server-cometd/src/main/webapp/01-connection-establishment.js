/*global jQuery:false */
(function($, window, document, undefined) {
  window.addEventListener("load", function() {
    /* クエリパラメータの処理 */
    let names = ["path", "transport", "fallback-transport", "user", "room"];

    // クエリパラメータを取得します
    function getQueryParameter() {
      return new window.URLSearchParams(window.location.search);
    }
    // クエリパラメータが必要な情報を持っているかをチェックします
    function isQueryParameterSufficient(params, names=names) {

      return names.every(function(name) {
        // 何か値があればOK
        return params.has(name) && !!params.get(name);
      });
    }
    /* チャット */
    let Chat = function(params) {
      this._url = window.location.protocol + "//" + window.location.host + params.get("path");
      this._transport = params.get("transport");
      this._fallbackTransport = params.get("fallback-transport");
      this._user = params.get("user");
      this._room = params.get("room");

      // 他の変数を初期化
      this._cometd = new org.cometd.CometD();
      this._cometd.registerExtension('reload', new org.cometd.ReloadExtension());

      this._connected = false;

      this._roomSubscription = null;
    };

    Chat.prototype._connect = function() {
      // metaチャネルは元々cometdが備えている
      // 読取専用のためlistenする
      this._cometd.addListener('/meta/handshake', this._metaHandshake.bind(this));
    };

    Chat.prototype._metaHandshake = function(message) {
      if (message.successful) {
        this._connectionInitialized();
      }
    };

    Chat.prototype._connectionInitialized = function() {
      var self = this;
        this._cometd.batch(function() {
          self._subscribe();
        });
    };

    Chat.prototype._subscribe = function() {
      // サーバーロジックで定義したサービスチャネルをsubscribeする
      // クライアントからpublishすることもできるが、サーバー通知のみの用途のため今回はpublishしない
      this._roomSubscription = this._cometd.subscribe(this._room, this.receive.bind(this));
    };

    Chat.prototype._metaConnect = function(message) {
        if (this._disconnecting) {
          this._connected = false;
          this._connectionClosed();
        } else {
          var wasConnected = this._connected;
          this._connected = message.successful === true;
          if (!wasConnected && this._connected) {
            this._connectionEstablished();
          } else if (wasConnected && !this._connected) {
            this._connectionBroken();
          }
        }
    };

    Chat.prototype._connectionEstablished = function() {
      // 画面に情報を表示
      this.receive({
        data: {
          user: 'system',
          chat: 'Connection to Server Opened'
        }
      });
    };

    Chat.prototype._connectionBroken = function() {
      // 画面に情報を表示
      this.receive({
        data: {
          user: 'system',
          chat: 'Connection to Server Broken'
        }
      });
    };

    Chat.prototype._connectionClosed = function() {
      // 画面に情報を表示
      this.receive({
        data: {
          user: 'system',
          chat: 'Connection to Server Closed'
        }
      });
    };

    Chat.prototype._join = function() {
      this._cometd.configure({
        url: this._url,
        logLevel: "debug"
      });

      this._cometd.websocketEnabled = this._transport === "websocket" || this._fallbackTransport === "websocket";

      if (this._transport === "callback-polling" || this._fallbackTransport === "callback-polling") {
        this._cometd.unregisterTransport('callback-polling');
      }

      if (this._transport === "long-polling" || this._fallbackTransport === "long-polling") {
        this._cometd.unregisterTransport('long-polling');
      }

      this._cometd.handshake();
    };

    Chat.prototype.disconnect = function() {
      
    };
  });
})(jQuery, window, document);
