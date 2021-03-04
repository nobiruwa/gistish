# リスト1-96
# ステータスコード変数に入れて操作しやすく変更
# ステータスコードを取得する
$url = "https://google.comi"
$statusCode = try {
    (Invoke-WebRequest -Uri $url).StatusCode
} catch {
    $Error[0].Exception.GetBaseException().Response.StatusCode.Value__
}

# ステータスコードが200かどうかを確認する
$validStatusCode = 200
If ($statusCode -ne $validStatusCode) {
    # ファイルが存在するかを確認する
    If (-Not (Test-Path 1-96\StatusCheck.log)) {
        # StatusCheck.logファイルを作る
        New-Item -Force -ItemType Directory 1-96
        New-Item -Path 1-96\StatusCheck.log
    }

    # 日付とステータスコード、URLをJSONとして保持する
    $json = @{
        Date = Get-Date
        Url = $url
        StatusCode = $statusCode
    } | ConvertTO-JSON # 1-95から補完
    # デバッグ用
    $json
}
