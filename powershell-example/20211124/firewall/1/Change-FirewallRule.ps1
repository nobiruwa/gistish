Param
(
  $ModeNameLocalized = "1モード",
  $RuleSpecifier = "_company",
  $ModeSpecifier = "_company_1"
)

Write-Host ("---ファイアウォール切り替え: {0} (開始)---" -f $ModeNameLocalized)

# 適用前の状態確認
Write-Host "---適用前の状態確認---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier)

# ファイアウォールルールをいったん削除する(削除する対象は$RuleSpecifierの文字列を含むものに限る)
Write-Host "---ルールの削除---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier) | Remove-NetFirewallRule

# 適用したいルールをここに記述してください
Write-Host "---ルールの追加---"
New-NetFirewallRule -DisplayName ("{0}{1}" -f "テスト", $ModeSpecifier) -Direction Outbound -RemoteAddress Any -Protocol TCP -Action Allow

# 適用後の状態確認
Write-Host "---適用後の状態確認---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier)

Write-Host ("---ファイアウォール切り替え: {0} (終了)---" -f $ModeNameLocalized)