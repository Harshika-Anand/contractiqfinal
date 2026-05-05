# ContractIQ Test Automation - Report Generation Guide

## Overview

This guide explains how to generate and view all three test reports: **Emailable**, **Index**, and **XSLT** reports.

---

## Three Report Types

### 1. **Emailable Report** (`emailable-report.html`)
- **Purpose**: Professional, email-friendly format
- **Use Case**: Send test results to stakeholders via email
- **Features**: Clean HTML formatting, embeds all styles
- **File**: `target/surefire-reports/emailable-report.html`

### 2. **Index Report** (`index.html`)
- **Purpose**: Comprehensive detailed test results
- **Use Case**: Main testing dashboard with full test details
- **Features**: Test class breakdown, method-by-method results, charts
- **File**: `target/surefire-reports/index.html`

### 3. **XSLT Report** (`testng-results.html`)
- **Purpose**: Custom formatted XML transformation
- **Use Case**: Detailed analytics and custom formatting
- **Features**: Statistics boxes, test suite details, error information
- **File**: `target/surefire-reports/testng-results.html`

---

## How to Generate Reports

### Method 1: Using PowerShell Script (Recommended for Windows)

```powershell
# Run tests and generate reports
powershell -ExecutionPolicy Bypass -File "ContractIQTests\run_tests_and_reports.ps1"

# OR just view existing reports (skip test execution)
powershell -ExecutionPolicy Bypass -File "ContractIQTests\run_tests_and_reports.ps1" -SkipTests
```

**Output:**
- Shows test statistics in terminal
- Lists all generated reports
- Shows success rate and test breakdown

### Method 2: Using Batch File

```batch
# Run from command prompt
cd ContractIQTests
run_tests_and_reports.bat
```

**Output:**
- Executes Maven tests
- Displays report summary
- Shows report locations

### Method 3: Manual Maven Build

```bash
cd ContractIQTests
mvn clean verify
```

Reports will be generated in:
```
ContractIQTests/target/surefire-reports/
```

---

## View Reports

### Option 1: Open in Browser from PowerShell

```powershell
# Emailable Report
& 'ContractIQTests\target\surefire-reports\emailable-report.html'

# Index Report
& 'ContractIQTests\target\surefire-reports\index.html'

# XSLT Report
& 'ContractIQTests\target\surefire-reports\testng-results.html'
```

### Option 2: Open Folder and Browse Manually

1. Navigate to: `ContractIQTests\target\surefire-reports\`
2. Double-click any `.html` file to open in browser

### Option 3: Start Command (CMD)

```cmd
start "" "ContractIQTests\target\surefire-reports\index.html"
```

---

## Report Contents

### Test Statistics (displayed in terminal)

```
===========================================================================
Test Statistics:
Total Tests:   91
Passed:        62 (68.13%)
Failed:        15
Skipped:       14
===========================================================================
```

### Test Suites (by module)

- Home Page Tests
- Login Page Tests
- Register Page Tests
- Dashboard Page Tests
- Documents Page Tests
- Document Comparison Tests

### Test Details

Each report contains:
- ✓ Pass/Fail status
- ✓ Execution time
- ✓ Error messages for failures
- ✓ Test method names
- ✓ Class/Package information

---

## Report File Locations

```
ContractIQTests/
├── target/
│   └── surefire-reports/
│       ├── emailable-report.html        [EMAIL FORMAT]
│       ├── index.html                   [MAIN DASHBOARD]
│       ├── testng-results.html          [XSLT FORMAT]
│       ├── testng-results.xml           [RAW DATA]
│       └── ContractIQ Test Suite/
│           ├── Dashboard Page Tests.html
│           ├── Document Comparison Tests.html
│           ├── Documents Page Tests.html
│           ├── Home Page Tests.html
│           ├── Login Page Tests.html
│           └── Register Page Tests.html
```

---

## Terminal Output Example

When you run the report script, you'll see:

```
===========================================================================
ContractIQ - Test Execution & Report Generation
===========================================================================

Test Statistics:
- * 75
Total Tests: | 91
[PASS] | Passed: 62
[FAIL] | Failed: 15
[SKIP] | Skipped: 14
- * 75
Success Rate: | 68.13%

===========================================================================
[3] Test Suites
===========================================================================

[PASS] ContractIQ Test Suite  | Passed: 62 | Failed: 15 | Skipped: 14

===========================================================================
[4] Generated Reports
===========================================================================

[OK] | Emailable Report | emailable-report.html | Size: 1021.88 KB
[OK] | Index Report | index.html | Size: 1078.8 KB
[OK] | XSLT Report | testng-results.html | Size: 850.45 KB
[OK] | XML Results | testng-results.xml | Size: 1097.92 KB

===========================================================================
Report Generation Complete
===========================================================================
```

---

## Troubleshooting

### XSLT Report Not Generated

If `testng-results.html` is missing:

1. Ensure `src/test/resources/testng-xslt.xsl` exists
2. Run full Maven build: `mvn clean verify`
3. Check Maven output for errors

### Reports Not Found

1. Verify you're in the `ContractIQTests` directory
2. Check Maven built successfully: Look for `[SUCCESS]` message
3. Reports should be in: `target/surefire-reports/`

### Encoding Issues

If reports show garbled text:
1. Open with UTF-8 encoding
2. Use modern browsers (Chrome, Firefox, Edge)
3. Try Safari or IE compatibility mode

---

## Automation Tips

### Schedule Tests Nightly

**Windows Task Scheduler:**
```batch
powershell -ExecutionPolicy Bypass -File "C:\path\to\run_tests_and_reports.ps1"
```

### Email Reports Automatically

```powershell
$reportPath = "ContractIQTests\target\surefire-reports\emailable-report.html"
# Use your email service to send $reportPath
```

### CI/CD Integration

```yml
# Example GitHub Actions
- name: Run Tests & Generate Reports
  run: |
    cd ContractIQTests
    mvn clean verify
    powershell -ExecutionPolicy Bypass -File run_tests_and_reports.ps1 -SkipTests
```

---

## Scripts Reference

### run_tests_and_reports.ps1
- **Purpose**: PowerShell wrapper for running tests and displaying reports
- **Features**: Colored output, file size display, success/failure indicators
- **Usage**: Fast terminal-based report viewing

### run_tests_and_reports.bat
- **Purpose**: Batch file for Windows Command Prompt
- **Features**: Maven integration, clear instructions
- **Usage**: Compatible with older Windows systems

### generate_reports.py
- **Purpose**: Python script for advanced report processing
- **Features**: XML parsing, detailed statistics
- **Usage**: Standalone report generation without Maven

---

## Key Metrics Explained

| Metric | Meaning |
|--------|---------|
| **Total Tests** | All test methods executed |
| **Passed** | Tests with successful execution |
| **Failed** | Tests that threw exceptions or assertions failed |
| **Skipped** | Tests marked with @Ignore or conditions not met |
| **Success Rate** | (Passed / Total) × 100% |

---

## Contact & Support

For issues with reports:
1. Check `testng-failed.xml` for failure details
2. Review test execution logs in Maven output
3. Verify test environment setup (drivers, dependencies)

---

**Last Updated:** April 19, 2026
**Project:** ContractIQ Test Automation Suite
