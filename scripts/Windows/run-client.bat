@echo off
rem Change directory to the script's parent directory (project root)
cd /d "%~dp0..\.."

title Ink-Sync Client Launcher

echo Compiling project...
javac -cp "lib\*;src" -d bin src\server\*.java src\client\*.java src\command\*.java

echo.
echo Starting Ink-Sync Client...
echo Note: Make sure the server is running first.
echo.

java -cp "lib\*;bin;src" client.Client 