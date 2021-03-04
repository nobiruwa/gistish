# リスト1-115
# 整理・調整を行って完成したスクリプト
# ステータスコードを取得する
$url = "https://google.com"
#$validStatusCode = 200
$validStatusCode = 404
$logDirectory = "1-115"
$logFile = "1-115\StatusCheck.log"
while ($true) {
    # ステータスコードを取得する
    $statusCode = try {
        (Invoke-WebRequest -Uri $url).StatusCode
    } catch {
        $Error[0].Exception.GetBaseException().Response.StatusCode.Value__
    }

    # ステータスコードが200かどうかを確認する
    If ($statusCode -ne $validStatusCode) {
        # ファイルが存在するかを確認する
        If (-Not (Test-Path $logFile)) {
            # StatusCheck.logファイルを作る
            New-Item -Force -ItemType Directory $logDirectory
            New-Item -Path $logFile
        }

        # ファイルは後からコマンドから読みやすいようにJSONとする
        $json = [ordered]@{
            Date = (Get-Date).ToString("F")
            Url = $url
            StatusCode = $statusCode
        } | ConvertTO-JSON -Compress

        # 1分ごとに繰り返す
        $json | Out-File -LiteralPath $logFile -Append
    }
    # 60秒停止する
    Start-Sleep -Seconds 60
}
