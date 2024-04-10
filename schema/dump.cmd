@echo off
if "%~1"=="" goto usage
if "%~2"=="" goto usage

pg_dump -h localhost -s %1 -U postgres > %2
goto end

:usage
echo Usage:  dump  [database]  [file.sql]
exit /b 1

:end


