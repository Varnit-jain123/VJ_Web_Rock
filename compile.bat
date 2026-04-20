@echo off
echo Compiling VJWebRock Project...

REM Defining the classpath
set CLASSPATH=".;c:\tomcat9\lib\servlet-api.jar;WEB-INF\lib\*;WEB-INF\classes"

REM Compiling all Java files in the bobby package
javac -cp %CLASSPATH% WEB-INF\classes\bobby\test\*.java

if %errorlevel% neq 0 (
    echo Compilation FAILED!
    pause
    exit /b %errorlevel%
)

echo Compilation Successful!
pause
