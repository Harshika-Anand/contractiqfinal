param(
    [switch]$SkipTests = $false
)

function Write-Header {
    param([string]$Text)
    Write-Host ""
    Write-Host ("=" * 75) -ForegroundColor Cyan
    Write-Host $Text -ForegroundColor Cyan
    Write-Host ("=" * 75) -ForegroundColor Cyan
}

function Write-StatusLine {
    param(
        [string]$Status,
        [string]$Message,
        [string]$Color = "White"
    )
    Write-Host $Status -ForegroundColor $Color -NoNewline
    Write-Host " | " -ForegroundColor Gray -NoNewline
    Write-Host $Message -ForegroundColor $Color
}

# Main execution
$projectDir = "d:\TestAutomationfinal\contractiqfinal\ContractIQTests"
$reportsDir = Join-Path $projectDir "target\surefire-reports"

Write-Header "ContractIQ - Test Execution & Report Generation"

# Step 1: Run tests
if (-not $SkipTests) {
    Write-Header "[1] Running Maven Tests"
    Push-Location $projectDir
    
    Write-Host "Executing: mvn clean verify" -ForegroundColor Yellow
    Write-Host ""
    
    & mvn clean verify
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "MAVEN BUILD FAILED!" -ForegroundColor Red
        Write-Host ""
        exit 1
    }
    
    Pop-Location
}

# Step 2: Display reports summary
Write-Header "[2] Test Results Summary"

$testngXml = Join-Path $reportsDir "testng-results.xml"
if (Test-Path $testngXml) {
    [xml]$xml = Get-Content $testngXml
    $root = $xml.DocumentElement
    
    $total = [int]$root.total
    $passed = [int]$root.passed
    $failed = [int]$root.failed
    $skipped = [int]$root.skipped
    $successRate = if ($total -gt 0) { [math]::Round(($passed/$total)*100, 2) } else { 0 }
    
    Write-Host ""
    Write-Host "Test Statistics:" -ForegroundColor Cyan
    Write-Host "-" * 75
    Write-StatusLine "Total Tests:" $total
    Write-StatusLine "[PASS]" "Passed: $passed" "Green"
    Write-StatusLine "[FAIL]" "Failed: $failed" "Red"
    Write-StatusLine "[SKIP]" "Skipped: $skipped" "Yellow"
    Write-Host "-" * 75
    Write-StatusLine "Success Rate:" "$successRate%" $(if ($successRate -eq 100) { "Green" } else { "Yellow" })
    Write-Host ""
    
    # Display test suites
    Write-Header "[3] Test Suites"
    Write-Host ""
    
    foreach ($suite in $xml.DocumentElement.suite) {
        $suiteName = $suite.name
        $suitePassed = $suite.passed
        $suiteFailed = $suite.failed
        $suiteSkipped = $suite.skipped
        
        $suiteStatus = if ([int]$suiteFailed -eq 0) { "[PASS]" } else { "[FAIL]" }
        $statusColor = if ([int]$suiteFailed -eq 0) { "Green" } else { "Red" }
        
        Write-Host $suiteStatus -ForegroundColor $statusColor -NoNewline
        Write-Host " $suiteName " -ForegroundColor Cyan -NoNewline
        Write-Host " | Passed: $suitePassed | Failed: $suiteFailed | Skipped: $suiteSkipped" -ForegroundColor Gray
    }
    
    Write-Host ""
}

# Step 3: Display report locations
Write-Header "[4] Generated Reports"
Write-Host ""

$reports = @{
    "Emailable Report" = "emailable-report.html"
    "Index Report" = "index.html"
    "XSLT Report" = "testng-results.html"
    "Aggregated Report" = "surefire-report.html"
    "XML Results" = "testng-results.xml"
}

foreach ($reportName in $reports.Keys) {
    $filename = $reports[$reportName]
    $filepath = Join-Path $reportsDir $filename
    
    if (Test-Path $filepath) {
        $sizeKB = [math]::Round((Get-Item $filepath).Length / 1024, 2)
        Write-statusLine "[OK]" "$reportName | $filename | Size: $sizeKB KB" "Green"
    } else {
        Write-StatusLine "[MISSING]" "$reportName | $filename" "Red"
    }
}

Write-Host ""

# Step 4: Display folder location
Write-Header "[5] Report Location"
Write-Host ""
Write-Host "Open this folder to view all reports:" -ForegroundColor Yellow
Write-Host "$reportsDir" -ForegroundColor Cyan
Write-Host ""

# Final message
Write-Host ""
if ($failed -eq 0) {
    Write-Host "SUCCESS! ALL TESTS PASSED!" -ForegroundColor Green
} else {
    Write-Host "WARNING: SOME TESTS FAILED" -ForegroundColor Red
    Write-Host "Check the reports for details" -ForegroundColor Yellow
}
Write-Host ""
Write-Header "Report Generation Complete"

Write-Host "To open reports in browser:" -ForegroundColor Yellow
Write-Host "  - Emailable: & '$reportsDir\emailable-report.html'" -ForegroundColor Gray
Write-Host "  - Index: & '$reportsDir\index.html'" -ForegroundColor Gray
Write-Host "  - XSLT: & '$reportsDir\testng-results.html'" -ForegroundColor Gray
Write-Host ""
