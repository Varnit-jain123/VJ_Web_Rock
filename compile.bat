@echo off
setlocal enabledelayedexpansion

echo Compiling VJWebRock Demo App...

REM Config
set SRC_DIR=src
set WEB_INF_DIR=WEB-INF
set CLASSES_DIR=%WEB_INF_DIR%\classes
set LIB_DIR=%WEB_INF_DIR%\lib
set TOMCAT_LIB=c:\tomcat9\lib

REM Cleanup/Setup
if not exist %CLASSES_DIR% mkdir %CLASSES_DIR%

REM Setup Classpath
REM Include servlet-api from Tomcat, and everything in WEB-INF/lib
set CP=".;%TOMCAT_LIB%\servlet-api.jar;%LIB_DIR%\*"

echo Compiling...
dir /s /b %SRC_DIR%\*.java > sources.txt
javac -cp %CP% -d %CLASSES_DIR% @sources.txt
if %errorlevel% neq 0 (
    echo Compilation FAILED!
    del sources.txt
    pause
    exit /b %errorlevel%
)
del sources.txt

echo Compilation Successful! Results are in %CLASSES_DIR%
pause
