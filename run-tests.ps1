# Run Maven Tests with Java 21
# This script ensures tests run with Java 21

Write-Host "Setting up Java 21 for Maven tests..." -ForegroundColor Cyan

# Set JAVA_HOME to Java 21
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.8.9-hotspot"

# Update PATH to use Java 21
$javaPath = "$env:JAVA_HOME\bin"
$paths = $env:PATH -split ';'
$cleanPaths = $paths | Where-Object {
    $_ -notmatch 'jdk-17' -and 
    $_ -notmatch 'Oracle\\Java\\javapath'
}
$env:PATH = $javaPath + ';' + ($cleanPaths -join ';')

Write-Host "✓ Using Java:" (java -version 2>&1 | Select-Object -First 1) -ForegroundColor Green
Write-Host "`nRunning Maven tests...`n" -ForegroundColor Yellow

# Run Maven test with all parameters passed to this script
$mvnArgs = $args
if ($mvnArgs.Count -eq 0) {
    mvn clean test
} else {
    mvn $mvnArgs
}
