# リスト1-70
# 存在しないURLステータスを確認する
(Invoke-WebRequest -Uri https://httpbin.org/status/404).StatusCode
