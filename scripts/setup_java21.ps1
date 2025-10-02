# PowerShell Script to Set Java 21 as Default
# This ensures JAVA_HOME and PATH are correctly set for every new PowerShell session

Write-Host "Setting up Java 21 environment..." -ForegroundColor Cyan

# Set JAVA_HOME to Java 21
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"

# Remove old Java paths and add Java 21 bin to PATH
$javaPath = "$env:JAVA_HOME\bin"

# Clean up PATH - remove old Java paths
$paths = $env:PATH -split ';'
$cleanPaths = $paths | Where-Object {
    $_ -notmatch 'jdk-17' -and 
    $_ -notmatch 'Oracle\\Java\\javapath'
}

# Add Java 21 to the beginning if not already there
if ($cleanPaths -notcontains $javaPath) {
    $env:PATH = $javaPath + ';' + ($cleanPaths -join ';')
}

Write-Host "✓ JAVA_HOME set to: $env:JAVA_HOME" -ForegroundColor Green
Write-Host "✓ Java version:" -ForegroundColor Green
java -version 2>&1 | Select-Object -First 1

# Verify Maven sees Java 21
Write-Host "`n✓ Maven Java version:" -ForegroundColor Green
mvn -version 2>&1 | Select-String "Java version"

Write-Host "`nJava 21 environment ready!`n" -ForegroundColor Green
