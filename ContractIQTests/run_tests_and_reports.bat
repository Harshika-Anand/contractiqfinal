@echo off
REM =============================================================================
REM ContractIQ Test Automation - Report Generation Script
REM =============================================================================
REM This script runs all tests and generates formatted reports in the terminal
REM =============================================================================

setlocal enabledelayedexpansion

echo.
echo ===========================================================================
echo ContractIQ - Complete Test Execution and Report Generation
echo ===========================================================================
echo.

REM Change to project directory
cd /d "d:\TestAutomationfinal\contractiqfinal\ContractIQTests"

echo [1] Running Maven build and tests...
echo ===========================================================================
mvn clean verify

if errorlevel 1 (
    echo.
    echo ✗ Maven build failed
    echo ===========================================================================
    exit /b 1
)

echo.
echo [2] Generating formatted reports...
echo ===========================================================================

REM Run Python script to display reports
python generate_reports.py --skip-tests

if errorlevel 1 (
    echo.
    echo ✗ Report generation failed
    echo ===========================================================================
    exit /b 1
)

echo.
echo [3] Report Generation Complete!
echo ===========================================================================
echo.
echo Reports are located in:
echo   %cd%\target\surefire-reports\
echo.
echo Available Reports:
echo   - emailable-report.html  (For email distribution)
echo   - index.html              (Main test report)
echo   - testng-results.html     (Custom XSLT formatted)
echo.
echo ===========================================================================
echo.

pause
