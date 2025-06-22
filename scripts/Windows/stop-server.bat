@echo off
rem Change directory to the project root
cd /d "%~dp0..\.."

echo Looking for running Ink-Sync server process...

REM Find the Java process running server.Server and kill it
tasklist /FI "IMAGENAME eq java.exe" /V | findstr /I "server.Server" > nul
if %ERRORLEVEL%==0 (
    echo Found running server. Attempting to stop...
    for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq java.exe" /V ^| findstr /I "server.Server"') do (
        echo Stopping process with PID %%a
        taskkill /PID %%a /F
    )
    echo ✅ Server stopped successfully!
) else (
    echo ❌ No server process found running.
) 