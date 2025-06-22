@echo off
title Ink-Sync Project Cleaner

rem Navigate to the project root directory
cd /d "%~dp0..\.."

echo Cleaning project by removing all compiled .class files...
if exist bin\client\*.class del /s /q bin\client\*.class
if exist bin\server\*.class del /s /q bin\server\*.class
if exist bin\command\*.class del /s /q bin\command\*.class
echo Project cleaned successfully.
echo.

echo Recompiling the entire project...
javac -cp "lib\*;src" -d bin src\server\*.java src\client\*.java src\command\*.java
echo Project recompiled successfully.
echo.

echo You can now run the server or client.
echo Press any key to exit.
pause > nul 