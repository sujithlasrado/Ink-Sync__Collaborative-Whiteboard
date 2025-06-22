@echo off
rem Change directory to the script's parent directory (project root)
cd /d "%~dp0..\.."

title Ink-Sync Server Launcher

echo Compiling project...
javac -cp "lib\*;src" -d bin src\server\*.java src\client\*.java src\command\*.java

echo.
echo Starting Ink-Sync Server in a new window...
start "Ink-Sync Server" cmd /c "java -cp "lib\*;bin;src" server.Server & pause"

REM Wait a moment to check if the server started (not perfect, but gives user feedback)
ping 127.0.0.1 -n 3 > nul

echo Server started (in new window)! Use Task Manager or a stop-server.bat script to stop it. 