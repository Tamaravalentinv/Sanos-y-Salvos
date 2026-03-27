@echo off
REM Sanos y Salvos Microservice Generator
REM Script para generar nuevos microservicios usando el archetype personalizado

SETLOCAL ENABLEDELAYEDEXPANSION

REM Colores para Windows
for /F %%A in ('echo prompt $H ^| cmd') do set "BS=%%A"

if "%~1"=="" (
    echo.
    echo ========================================
    echo Sanos y Salvos Microservice Generator
    echo ========================================
    echo.
    echo Uso: generate-microservice.bat NOMBRE PUERTO 
    echo.
    echo Parámetros:
    echo   NOMBRE   = Nombre del microservicio (ej: pagos, analytics)
    echo   PUERTO   = Puerto donde corre (ej: 8086, 8087)
    echo.
    echo Ejemplos:
    echo   generate-microservice.bat pagos 8086
    echo   generate-microservice.bat analytics 8087
    echo.
    goto end
)

set MICROSERVICE_NAME=%1
set PORT=%2

if "%PORT%"=="" (
    echo ERROR: Debes especificar el puerto
    echo Uso: generate-microservice.bat NOMBRE PUERTO
    goto end
)

set ARTIFACT_ID=ms-%MICROSERVICE_NAME%
set DB_NAME=sanosysalvos_%MICROSERVICE_NAME%
set PACKAGE=%MICROSERVICE_NAME%

echo.
echo ========================================
echo Generando %ARTIFACT_ID%...
echo ========================================
echo.
echo - Nombre: %MICROSERVICE_NAME%
echo - ArtifactId: %ARTIFACT_ID%
echo - Puerto: %PORT%
echo - Base de datos: %DB_NAME%
echo - Paquete: %PACKAGE%
echo.

mvn archetype:generate ^
  -DarchetypeGroupId=com.sanosysalvos ^
  -DarchetypeArtifactId=sanos-microservice-archetype ^
  -DarchetypeVersion=1.0.0 ^
  -DgroupId=com.sanosysalvos ^
  -DartifactId=%ARTIFACT_ID% ^
  -Dname="Microservicio de %MICROSERVICE_NAME%" ^
  -Ddescription="Microservicio para gestión de %MICROSERVICE_NAME%" ^
  -DdbName=%DB_NAME% ^
  -DserverPort=%PORT% ^
  -Dpackage=%PACKAGE% ^
  -DinteractiveMode=false

if %ERRORLEVEL% equ 0 (
    echo.
    echo ========================================
    echo Microservicio generado exitosamente!
    echo ========================================
    echo.
    echo Próximos pasos:
    echo.
    echo 1. Entra a la carpeta del proyecto:
    echo    cd %ARTIFACT_ID%
    echo.
    echo 2. Añade el módulo al pom.xml principal:
    echo    Abre sanos-y-salvos/pom.xml y añade:
    echo    ^<module^>%ARTIFACT_ID%^</module^>
    echo.
    echo 3. Compila el proyecto:
    echo    mvn clean package
    echo.
    echo 4. Crea la base de datos:
    echo    mysql -uroot -e "CREATE DATABASE IF NOT EXISTS %DB_NAME%;"
    echo.
    echo 5. Ejecuta el microservicio:
    echo    mvn spring-boot:run -pl %ARTIFACT_ID%
    echo.
) else (
    echo.
    echo ERROR: No se pudo generar el microservicio
    echo.
)

:end
