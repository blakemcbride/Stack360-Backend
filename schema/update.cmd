@echo off
if "%1"=="" goto usage
if "%2"=="" goto usage
psql %1 postgres -c "drop schema public cascade;"
psql %1 postgres -c "create schema public;"
psql %1 postgres -c "\i %2"

goto end

:usage

echo Usage:  update  [database]  [file.sql]

:end


