# リスト1-92
# 確認するURLを変数に変えた全体像
# ステータスコードを取得する
$url = "https://google.com"
$statusCode = try {
    (Invoke-WebRequest -Uri $url).StatusCode
} catch {
    $Error[0].Exception.GetBaseException().Response.StatusCode.Value__
}

If ($statusCode -ne 200) {
    # ファイルが存在するかを確認する
    If (-Not (Test-Path 1-92\StatusCheck.log)) {
        # StatusCheck.logファイルを作る
        New-Item -Force -ItemType Directory 1-92
        New-Item -Path 1-92\StatusCheck.log
    }
}
