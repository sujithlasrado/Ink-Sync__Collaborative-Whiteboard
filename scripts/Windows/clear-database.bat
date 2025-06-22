@echo off
rem Change directory to the script's parent directory (project root)
cd /d "%~dp0..\.."

title Ink-Sync Database Clearer

echo Clearing all data from the Ink-Sync database...
echo.

java -cp "lib\*;bin;src" server.DatabaseViewer clear

echo.
echo Database has been cleared.
echo Press any key to exit.
pause > nul 