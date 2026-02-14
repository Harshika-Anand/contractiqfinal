@echo off
echo ========================================
echo ContractIQ Test Automation - Quick Start
echo ========================================
echo.

echo Checking Java installation...
java -version
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java JDK 8 or higher
    pause
    exit /b 1
)
echo.

echo Checking for required folders...
if not exist "drivers\chromedriver.exe" (
    echo WARNING: chromedriver.exe not found in drivers folder!
    echo Please download ChromeDriver and place it in the drivers folder
)

if not exist "lib" (
    echo WARNING: lib folder is empty!
    echo Please add Selenium JAR files to the lib folder
)

echo.
echo ========================================
echo Setup Instructions:
echo ========================================
echo 1. Download Selenium JARs and place in 'lib' folder
echo 2. Download ChromeDriver and place in 'drivers' folder
echo 3. Import this project into Eclipse
echo 4. Add all JARs from lib folder to Build Path
echo 5. Start the ContractIQ application:
echo    - Backend: cd contractiqB ^&^& python app.py
echo    - Frontend: cd contractiqF ^&^& npm run dev
echo 6. Run TestRunner.java in Eclipse
echo.
echo ========================================
pause
