Param(
    [string]
    $AppLockerFilePath = 'applocker_default.xml'
)

Function ExtractXMLNodeFrom-XMLFile(
    [string]
    $FilePath,
    [string]
    $XPath
)
{
    $Selected = Select-Xml -Xpath $XPath -Path $FilePath

    If ($Selected) {
        Return $Selected.Node.OuterXml
    } Else {
        Return $null
    }
}

Function Deploy-AppLockerRuleCollection(
    [string]
    $NamespaceName = "root\cimv2\mdm\dmmap", # Do not change this
    [string]
    $InstanceID,
    [string]
    $ClassName,
    [string]
    $ParentID,
    [string]
    $RuleString
)
{
    Write-Host $InstanceID

    If ($RuleString) {
        Add-Type -AssemblyName System.Web

        # ルールセットの名前を使い回すため、一度既存のルールインスタンスを削除する
        Get-CimInstance -Namespace $NamespaceName -ClassName $ClassName | Remove-CimInstance

        $Policy = [System.Net.WebUtility]::HtmlEncode($RuleString);

        # ルールセットインスタンスの作成
        New-CimInstance -Namespace $NamespaceName -ClassName $ClassName -Property @{ ParentID=$ParentID; InstanceID=$InstanceID; Policy=$Policy; EnforcementMode="Enabled"; }
    }
}

$RuleAppxString = ExtractXMLNodeFrom-XMLFile -FilePath $AppLockerFilePath -XPath "/AppLockerPolicy/RuleCollection[@Type='Appx']"
$RuleDllString = ExtractXMLNodeFrom-XMLFile -FilePath $AppLockerFilePath -XPath "/AppLockerPolicy/RuleCollection[@Type='Dll']"
$RuleExeString = ExtractXMLNodeFrom-XMLFile -FilePath $AppLockerFilePath -XPath "/AppLockerPolicy/RuleCollection[@Type='Exe']"
$RuleMsiString = ExtractXMLNodeFrom-XMLFile -FilePath $AppLockerFilePath -XPath "/AppLockerPolicy/RuleCollection[@Type='Msi']"
$RuleScriptString = ExtractXMLNodeFrom-XMLFile -FilePath $AppLockerFilePath  -XPath "/AppLockerPolicy/RuleCollection[@Type='Script']"

# -InstanceIDに渡してよい値は
# <https://docs.microsoft.com/en-us/windows/client-management/mdm/applocker-csp>
# にある
# AppLocker/ApplicationLaunchRestrictions/<Grouping>/xxx
# xxxの部分 StoreApps, EXE, DLL, Script, MSI, CodeIntegrity

# -ClassName と　-ParentIDに渡してよい値は各参考ページ参照
# -ParentIDの最後の'/'の後ろに付ける名前はこのスクリプトで自由に決めてよい

# Appx (StoreApps)
# 参考: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-applicationlaunchrestrictions01-storeapps03
Deploy-AppLockerRuleCollection -InstanceID "StoreApps" -ClassName "MDM_AppLocker_ApplicationLaunchRestrictions01_StoreApps03" -ParentID "./Vendor/MSFT/AppLocker/ApplicationLaunchRestrictions/AppLockerStoreApps001" -RuleString $RuleAppxString

# Exe (EXE)
# 参考: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-applicationlaunchrestrictions01-exe03
Deploy-AppLockerRuleCollection -InstanceID "EXE" -ClassName "MDM_AppLocker_ApplicationLaunchRestrictions01_EXE03" -ParentID "./Vendor/MSFT/AppLocker/ApplicationLaunchRestrictions/AppLockerExe001" -RuleString $RuleExeString

# Dll (DLL)
# 参考: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-dll03
Deploy-AppLockerRuleCollection -InstanceID "DLL" -ClassName "MDM_AppLocker_DLL03" -ParentID "./Vendor/MSFT/AppLocker/ApplicationLaunchRestrictions/AppLockerDll001" -RuleString $RuleDllString

# Script (Script)
# 参考: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-script03
Deploy-AppLockerRuleCollection -InstanceID "Script" -ClassName "MDM_AppLocker_Script03" -ParentID "./Vendor/MSFT/AppLocker/ApplicationLaunchRestrictions/AppLockerScript001" -RuleString $RuleScriptString

# Msi (MSI)
# 参考: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-msi03
Deploy-AppLockerRuleCollection -InstanceID "MSI" -ClassName "MDM_AppLocker_MSI03" -ParentID "./Vendor/MSFT/AppLocker/ApplicationLaunchRestrictions/AppLockerMsi001" -RuleString $RuleMsiString

# Local Variables:
# coding: utf-16-dos
# End:
