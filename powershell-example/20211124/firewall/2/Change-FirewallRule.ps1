Param
(
  $ModeNameLocalized = "2���[�h",
  $RuleSpecifier = "_company",
  $ModeSpecifier = "_company_2"
)

Write-Host ("---�t�@�C�A�E�H�[���؂�ւ�: {0} (�J�n)---" -f $ModeNameLocalized)

# �K�p�O�̏�Ԋm�F
Write-Host "---�K�p�O�̏�Ԋm�F---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier)

# �t�@�C�A�E�H�[�����[������������폜����(�폜����Ώۂ�$RuleSpecifier�̕�������܂ނ��̂Ɍ���)
Write-Host "---���[���̍폜---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier) | Remove-NetFirewallRule

# �K�p���������[���������ɋL�q���Ă�������
Write-Host "---���[���̒ǉ�---"

# �K�p��̏�Ԋm�F
Write-Host "---�K�p��̏�Ԋm�F---"
Get-NetFirewallRule -DisplayName ("*{0}*" -f $RuleSpecifier)

Write-Host ("---�t�@�C�A�E�H�[���؂�ւ�: {0} (�I��)---" -f $ModeNameLocalized)