# Disable Chrome Password Manager & Leak Detection (User-level Preferences)
# This script modifies Chrome preferences at the user level (no admin required)

Write-Host "Disabling Chrome Password Manager and Leak Detection at user level..." -ForegroundColor Cyan

# Chrome User Data Path
$chromeUserData = "$env:LOCALAPPDATA\Google\Chrome\User Data"

# Check if Chrome is installed
if (-not (Test-Path $chromeUserData)) {
    Write-Host "Chrome user data folder not found. Chrome may not be installed." -ForegroundColor Yellow
    exit
}

# Function to update Chrome preferences
function Update-ChromePreferences {
    param (
        [string]$PrefsPath
    )
    
    if (Test-Path $PrefsPath) {
        Write-Host "Updating preferences at: $PrefsPath" -ForegroundColor Green
        
        # Read the current preferences
        $prefs = Get-Content $PrefsPath -Raw | ConvertFrom-Json
        
        # Update password manager settings
        if (-not $prefs.PSObject.Properties['profile']) {
            $prefs | Add-Member -MemberType NoteProperty -Name 'profile' -Value @{}
        }
        
        $prefs.profile.password_manager_enabled = $false
        $prefs.profile.password_manager_leak_detection = $false
        
        if (-not $prefs.PSObject.Properties['credentials_enable_service']) {
            $prefs | Add-Member -MemberType NoteProperty -Name 'credentials_enable_service' -Value $false
        } else {
            $prefs.credentials_enable_service = $false
        }
        
        # Save the updated preferences
        $prefs | ConvertTo-Json -Depth 100 | Set-Content $PrefsPath
        
        Write-Host "Preferences updated successfully!" -ForegroundColor Green
    }
}

# Update Default profile
$defaultPrefs = "$chromeUserData\Default\Preferences"
if (Test-Path $defaultPrefs) {
    Update-ChromePreferences -PrefsPath $defaultPrefs
}

# Update Profile 1 if exists
$profile1Prefs = "$chromeUserData\Profile 1\Preferences"
if (Test-Path $profile1Prefs) {
    Update-ChromePreferences -PrefsPath $profile1Prefs
}

Write-Host "`nDone! Please close all Chrome windows and restart Chrome for changes to take effect." -ForegroundColor Green
Write-Host "Note: For automated tests, the Boyka config file now includes Chrome options that will override these settings." -ForegroundColor Yellow
