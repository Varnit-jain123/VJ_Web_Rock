@echo off
setlocal enabledelayedexpansion

echo Building VJWebRock Core Library...

REM Config
set SRC_DIR=src
set BUILD_DIR=build
set LIB_DIR=lib
set DIST_DIR=dist
set JAR_NAME=vj-webrock.jar

REM Cleanup
if exist %BUILD_DIR% rd /s /q %BUILD_DIR%
mkdir %BUILD_DIR%
if not exist %DIST_DIR% mkdir %DIST_DIR%

REM Setup Classpath
set CP="!LIB_DIR!\*"

echo Compiling...
dir /s /b !SRC_DIR!\*.java > sources.txt
javac -cp %CP% -d %BUILD_DIR% @sources.txt
if %errorlevel% neq 0 (
    echo Compilation FAILED!
    del sources.txt
    pause
    exit /b %errorlevel%
)
del sources.txt

echo Packaging JAR...
jar cvf !DIST_DIR!\%JAR_NAME% -C %BUILD_DIR% .
if %errorlevel% neq 0 (
    echo Packaging FAILED!
    pause
    exit /b %errorlevel%
)

REM Copy to demo app lib
if exist "..\web\WEB-INF\lib" (
    echo Syncing JAR to demo web folder...
    copy "!DIST_DIR!\%JAR_NAME%" "..\web\WEB-INF\lib\"
)
if exist "..\WEB-INF\lib" (
    echo Syncing JAR to root deployment...
    copy "!DIST_DIR!\%JAR_NAME%" "..\WEB-INF\lib\"
)

echo Build Successful! Result: %DIST_DIR%\%JAR_NAME%
pause
