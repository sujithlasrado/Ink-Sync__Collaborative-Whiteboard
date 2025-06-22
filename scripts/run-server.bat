@echo off
rem Change directory to the script's parent directory (project root)
cd /d "%~dp0.."

title Ink-Sync Server Launcher

echo Compiling project...
javac -d src -cp "lib\*;src" src\server\*.java src\client\*.java src\command\*.java

echo.
echo Starting Ink-Sync Server...
echo The Server GUI will open.
echo Use the 'Stop Server' button in the GUI to shut down gracefully.
echo.

java -cp "lib\*;src" server.Server 