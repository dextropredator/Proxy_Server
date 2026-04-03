@echo off
REM Navigate up one level to the project root
cd /d "%~dp0.."

echo Compiling project...

if not exist bin mkdir bin

REM Find all java files and compile them into the bin directory
dir /s /B src\*.java > sources.txt
javac -d bin @sources.txt
del sources.txt

REM Check if compilation was successful
if %errorlevel% equ 0 (
    echo Running proxy server...
    java -cp bin app.ProxyServer
) else (
    echo Compilation failed. Server will not start.
)

pause