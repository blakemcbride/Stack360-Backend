@echo off
setlocal enabledelayedexpansion

set SVN_BASE=svn://svn.arahant.com/Arahant/ArahantBackend

REM Check number of arguments
if not "%~3"=="" goto arg_check
echo Usage:  make-patch  starting-rev  ending-rev  trunk/branch-number
exit /b 1

:arg_check
REM Check if arguments are numeric
for /f "delims=0123456789" %%a in ("%1") do set numcheck1=%%a
for /f "delims=0123456789" %%a in ("%2") do set numcheck2=%%a
if not "!numcheck1!"=="" if not "!numcheck2!"=="" (
    echo Arguments must be numeric.
    exit /b 1
)

REM Check if the first number is less than the second
if %1 geq %2 (
    echo The second argument must be greater than the first.
    exit /b 1
)

REM Set SVN_PATH based on the third argument
if "%3"=="trunk" (
    set SVN_PATH=%SVN_BASE%/trunk/schema/ArahantSchema.sql
) else (
    set SVN_PATH=%SVN_BASE%/branches/%3/schema/ArahantSchema.sql
)

echo Exporting schema %1
svn export -r%1 %SVN_PATH% schema-%1.sql
echo Exporting schema %2
svn export -r%2 %SVN_PATH% schema-%2.sql

echo Generating PostgreSQL patch file.
echo. > P_%2.sql
echo -- PostgreSQL patch from revision %1 to revision %2 >> P_%2.sql
echo. >> P_%2.sql
java -jar DBSync4.jar -ip schema-%1.sql -ip schema-%2.sql -op >> P_%2.sql

echo Generating Microsoft SQL Server patch file.
echo. > M_%2.sql
echo -- Microsoft SQL Server patch from revision %1 to revision %2 >> M_%2.sql
echo. >> M_%2.sql
java -jar DBSync4.jar -ip schema-%1.sql -ip schema-%2.sql -om >> M_%2.sql

echo Process complete.

del schema-%1.sql schema-%2.sql
endlocal

