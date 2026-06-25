#!/usr/bin/env pwsh

# Chat Application Database Launcher (PowerShell)
# This script will start the Spring Boot application and open the H2 console in your default browser

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Chat Application Database Launcher" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# Get the script directory
$scriptPath = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptPath

# Check if Java is already running on port 8082
Write-Host "Checking if application is already running..." -ForegroundColor Yellow
$portCheck = Get-NetTCPConnection -LocalPort 8082 -ErrorAction SilentlyContinue

if ($portCheck) {
    Write-Host "✓ Application is already running on port 8082" -ForegroundColor Green
} else {
    Write-Host "Starting Chat Application..." -ForegroundColor Yellow
    
    # Check if JAR file exists
    if (-not (Test-Path ".\target\chat-0.0.1-SNAPSHOT.jar")) {
        Write-Host "Building application..." -ForegroundColor Yellow
        .\mvnw clean package -DskipTests -q
    }
    
    # Start the application in a new PowerShell window
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$scriptPath'; java -jar `".\target\chat-0.0.1-SNAPSHOT.jar`" --server.port=8082" -WindowStyle Normal
    
    Write-Host "Application started! Waiting for it to initialize..." -ForegroundColor Green
    Start-Sleep -Seconds 5
}

Write-Host "`nOpening H2 Database Console in your browser..." -ForegroundColor Yellow
Start-Sleep -Seconds 2

# Open H2 Console in default browser
$h2Url = "http://localhost:8082/h2-console"
Start-Process $h2Url

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "H2 Console Connection Details:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "URL:      $h2Url" -ForegroundColor White
Write-Host "JDBC URL: jdbc:h2:mem:chatdb" -ForegroundColor White
Write-Host "Username: sa" -ForegroundColor White
Write-Host "Password: chat123" -ForegroundColor White
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "✓ Database console opened successfully!" -ForegroundColor Green
