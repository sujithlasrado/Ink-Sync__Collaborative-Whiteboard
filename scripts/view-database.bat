@echo off
rem Change directory to the script's parent directory (project root)
cd /d "%~dp0.."

title Ink-Sync Database Viewer

echo === Ink-Sync Database Viewer ===
echo.

java -cp "lib\*;src" server.DatabaseViewer

echo.
echo Press any key to exit.
pause > nul 