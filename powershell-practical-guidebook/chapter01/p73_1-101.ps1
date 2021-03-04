# リスト1-101
# JSONとして結果を受け取る
# ステータスコードを取得する
$url = "https://google.com"
$statusCode = try {
    (Invoke-WebRequest -Uri $url).StatusCode
} catch {
    $Error[0].Exception.GetBaseException().Response.StatusCode.Value__
}

# ステータスコードが200かどうかを確認する
#$validStatusCode = 200
$validStatusCode = 404
If ($statusCode -ne $validStatusCode) {
    # ファイルが存在するかを確認する
    If (-Not (Test-Path 1-101\StatusCheck.log)) {
        # StatusCheck.logファイルを作る
        New-Item -Force -ItemType Directory 1-101
        New-Item -Path 1-101\StatusCheck.log
    }

    # 日付とステータスコード、URLをJSONとして保持する
    $json = @{
        Date = (Get-Date).ToString("F")
        Url = $url
        StatusCode = $statusCode
    } | ConvertTO-JSON -Compress
}
