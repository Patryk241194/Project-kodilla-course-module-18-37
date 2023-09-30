call runcrud.bat

if "%ERRORLEVEL%" == "0" goto startchrome
echo.
echo runcrud has errors – breaking work
goto fail

:startchrome
start chrome http://localhost:8080/crud/v1/tasks
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Tasks from getTasks command have been displayed.