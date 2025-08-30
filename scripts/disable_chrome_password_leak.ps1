# Disable Chrome Password Manager & Leak Detection (Enterprise Policies)
# Run PowerShell as Administrator before executing this script.

$policyPath = "HKLM:\SOFTWARE\Policies\Google\Chrome"
if (-not (Test-Path $policyPath)) {
    New-Item -Path $policyPath -Force | Out-Null
}

# 0 = disabled, 1 = enabled
New-ItemProperty -Path $policyPath -Name PasswordManagerEnabled -PropertyType DWord -Value 0 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name PasswordLeakDetectionEnabled -PropertyType DWord -Value 0 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name PasswordProtectionEnabled -PropertyType DWord -Value 0 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name SyncDisabled -PropertyType DWord -Value 1 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name AutofillAddressEnabled -PropertyType DWord -Value 0 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name AutofillCreditCardEnabled -PropertyType DWord -Value 0 -Force | Out-Null
New-ItemProperty -Path $policyPath -Name SafeBrowsingProtectionLevel -PropertyType DWord -Value 0 -Force | Out-Null

Write-Host "Chrome password manager & leak detection disabled via policies. Restart Chrome for changes to apply."
