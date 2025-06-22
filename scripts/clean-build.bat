@echo off
title Ink-Sync Project Cleaner

rem Navigate to the project root directory
cd /d "%~dp0.."

echo Cleaning project by removing all compiled .class files...
if exist src\client\*.class del /s /q src\client\*.class
if exist src\server\*.class del /s /q src\server\*.class
if exist src\command\*.class del /s /q src\command\*.class
echo Project cleaned successfully.
echo.

echo Recompiling the entire project...
javac -d src -cp "lib\*;src" src\server\*.java src/client\*.java src/command\*.java
echo Project recompiled successfully.
echo.

echo You can now run the server or client.
echo Press any key to exit.
pause > nul 