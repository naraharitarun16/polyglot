@echo off
REM Start the Chat Application and open H2 Database Console
REM This script will start the Spring Boot application and open the H2 console in your default browser

echo.
echo ========================================
echo   Chat Application Database Launcher
echo ========================================
echo.

REM Check if Java is already running on port 8082
echo Checking if application is already running...
netstat -ano | findstr :8082 >nul
if %errorlevel% equ 0 (
    echo Application is already running on port 8082
) else (
    echo Starting Chat Application...
    cd /d "%~dp0"
    start "Chat Application" cmd /k "java -jar target\chat-0.0.1-SNAPSHOT.jar --server.port=8082"
    timeout /t 5 /nobreak
    echo Application started!
)

echo.
echo Opening H2 Database Console in your browser...
timeout /t 2 /nobreak

REM Open H2 Console in default browser
start http://localhost:8082/h2-console

echo.
echo ========================================
echo H2 Console Connection Details:
echo JDBC URL: jdbc:h2:mem:chatdb
echo Username: sa
echo Password: chat123
echo ========================================
echo.
echo Press Enter to close this window...
pause
