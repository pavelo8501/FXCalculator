@echo off
echo Starting the installation script...

REM Navigate to the project root directory
cd /d %~dp0
echo Changed directory to script location: %cd%

REM Change to the frontend directory
cd frontend\FXCalculatorAdmin || (
    echo Failed to change directory to frontend
    exit /b 1
)
echo Changed directory to script location: %cd%


echo Launching npm install....
call npm install && (
    echo npm install completed successfully. Proceeding...
) || (
    echo npm install failed
    exit /b 1
)
echo npm installed

echo Building Angular project...
call ng build --base-href http://localhost:8080/admin/ --output-hashing none
echo Built Angular project (Complete).

cd /d %~dp0
echo Changed directory back to script location: %cd%

echo Removing old static files...
rmdir /s /q src\main\resources\static\admin
mkdir src\main\resources\static\admin
echo Copying new Angular build files to static directory...
xcopy frontend\FXCalculatorAdmin\dist\fxcalculator-admin\browser\* src\main\resources\static\admin /s /e /y

echo Building backend project...
call gradlew build

echo Installation and build completed successfully.