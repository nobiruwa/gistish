Function Delete-AppLockerRuleCollection(
    [string]
    $NamespaceName = "root\cimv2\mdm\dmmap", # Do not change this
    [string]
    $ClassName
)
{
    Write-Host $InstanceID
    # ���[���Z�b�g�̖��O���g���񂷂��߁A��x�����̃��[���C���X�^���X���폜����
    Get-CimInstance -Namespace $NamespaceName -ClassName $ClassName | Remove-CimInstance
}

# Appx (StoreApps)
# �Q�l: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-applicationlaunchrestrictions01-storeapps03
Delete-AppLockerRuleCollection -ClassName "MDM_AppLocker_ApplicationLaunchRestrictions01_StoreApps03"

# Exe (EXE)
# �Q�l: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-applicationlaunchrestrictions01-exe03
Delete-AppLockerRuleCollection -InstanceID "EXE" -ClassName "MDM_AppLocker_ApplicationLaunchRestrictions01_EXE03"

# Dll (DLL)
# �Q�l: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-dll03
Delete-AppLockerRuleCollection -InstanceID "DLL" -ClassName "MDM_AppLocker_DLL03"
# Script (Script)
# �Q�l: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-script03
Delete-AppLockerRuleCollection -InstanceID "Script" -ClassName "MDM_AppLocker_Script03"
# Msi (MSI)
# �Q�l: https://docs.microsoft.com/en-us/windows/win32/dmwmibridgeprov/mdm-applocker-msi03
Delete-AppLockerRuleCollection -InstanceID "MSI" -ClassName "MDM_AppLocker_MSI03"

# Local Variables:
# coding: utf-16-dos
# End:
