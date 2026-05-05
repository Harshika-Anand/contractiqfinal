<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <html>
            <head>
                <title>TestNG XSLT Report - ContractIQ Test Suite</title>
                <style>
                    <xsl:text>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 10px;
                            background-color: #f5f5f5;
                        }
                        .header {
                            background-color: #2c3e50;
                            color: white;
                            padding: 15px;
                            border-radius: 5px;
                            margin-bottom: 20px;
                        }
                        .summary {
                            background-color: #ecf0f1;
                            padding: 15px;
                            border-left: 4px solid #3498db;
                            margin-bottom: 20px;
                            border-radius: 3px;
                        }
                        .passed {
                            background-color: #d4edda;
                            border-left-color: #28a745;
                        }
                        .failed {
                            background-color: #f8d7da;
                            border-left-color: #dc3545;
                        }
                        .skipped {
                            background-color: #fff3cd;
                            border-left-color: #ffc107;
                        }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-bottom: 20px;
                            background-color: white;
                            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        }
                        th {
                            background-color: #34495e;
                            color: white;
                            padding: 12px;
                            text-align: left;
                        }
                        td {
                            padding: 10px 12px;
                            border-bottom: 1px solid #ddd;
                        }
                        tr:hover {
                            background-color: #f9f9f9;
                        }
                        .pass-cell {
                            background-color: #d4edda;
                            color: #155724;
                            font-weight: bold;
                        }
                        .fail-cell {
                            background-color: #f8d7da;
                            color: #721c24;
                            font-weight: bold;
                        }
                        .skip-cell {
                            background-color: #fff3cd;
                            color: #856404;
                            font-weight: bold;
                        }
                        h1, h2, h3 {
                            color: #2c3e50;
                        }
                        .test-class {
                            margin-top: 20px;
                            border: 1px solid #bdc3c7;
                            border-radius: 5px;
                            overflow: hidden;
                        }
                        .test-class-header {
                            background-color: #ecf0f1;
                            padding: 10px 15px;
                            font-weight: bold;
                            cursive: pointer;
                        }
                        .test-methods {
                            padding: 10px;
                        }
                        .test-method {
                            padding: 8px;
                            margin: 5px 0;
                            border-left: 4px solid #95a5a6;
                        }
                        .test-method.passed {
                            border-left-color: #28a745;
                            background-color: #f0fff4;
                        }
                        .test-method.failed {
                            border-left-color: #dc3545;
                            background-color: #fff5f5;
                        }
                        .test-method.skipped {
                            border-left-color: #ffc107;
                            background-color: #fffbf0;
                        }
                        .stats {
                            display: flex;
                            gap: 15px;
                            margin-bottom: 20px;
                        }
                        .stat-box {
                            flex: 1;
                            padding: 15px;
                            border-radius: 5px;
                            text-align: center;
                            color: white;
                        }
                        .stat-passed {
                            background-color: #28a745;
                        }
                        .stat-failed {
                            background-color: #dc3545;
                        }
                        .stat-skipped {
                            background-color: #ffc107;
                            color: #333;
                        }
                        .stat-total {
                            background-color: #3498db;
                        }
                        .stat-number {
                            font-size: 32px;
                            font-weight: bold;
                        }
                        .stat-label {
                            font-size: 14px;
                            margin-top: 5px;
                        }
                    </xsl:text>
                </style>
            </head>
            <body>
                <div class="header">
                    <h1>ContractIQ - TestNG XSLT Report</h1>
                    <p>Generated XSLT Report - Test Execution Summary</p>
                </div>
                
                <xsl:apply-templates select="testng-results"/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="testng-results">
        <!-- Calculate totals -->
        <xsl:variable name="total" select="@total"/>
        <xsl:variable name="passed" select="@passed"/>
        <xsl:variable name="failed" select="@failed"/>
        <xsl:variable name="skipped" select="@skipped"/>
        
        <!-- Statistics Boxes -->
        <div class="stats">
            <div class="stat-box stat-passed">
                <div class="stat-number"><xsl:value-of select="$passed"/></div>
                <div class="stat-label">PASSED</div>
            </div>
            <div class="stat-box stat-failed">
                <div class="stat-number"><xsl:value-of select="$failed"/></div>
                <div class="stat-label">FAILED</div>
            </div>
            <div class="stat-box stat-skipped">
                <div class="stat-number"><xsl:value-of select="$skipped"/></div>
                <div class="stat-label">SKIPPED</div>
            </div>
            <div class="stat-box stat-total">
                <div class="stat-number"><xsl:value-of select="$total"/></div>
                <div class="stat-label">TOTAL</div>
            </div>
        </div>
        
        <!-- Summary Section -->
        <div class="summary">
            <h2>Test Execution Summary</h2>
            <table>
                <tr>
                    <th>Metric</th>
                    <th>Value</th>
                </tr>
                <tr>
                    <td>Total Tests</td>
                    <td><xsl:value-of select="$total"/></td>
                </tr>
                <tr>
                    <td>Passed</td>
                    <td class="pass-cell"><xsl:value-of select="$passed"/></td>
                </tr>
                <tr>
                    <td>Failed</td>
                    <td class="fail-cell"><xsl:value-of select="$failed"/></td>
                </tr>
                <tr>
                    <td>Skipped</td>
                    <td class="skip-cell"><xsl:value-of select="$skipped"/></td>
                </tr>
                <tr>
                    <td>Success Rate</td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="$total = 0">0%</xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="format-number($passed div $total * 100, '0.00')"/>%
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </table>
        </div>
        
        <!-- Test Details by Suite -->
        <h2>Test Suite Details</h2>
        <xsl:apply-templates select="suite"/>
    </xsl:template>
    
    <xsl:template match="suite">
        <div class="test-class">
            <div class="test-class-header">
                <xsl:variable name="suite-name" select="@name"/>
                <xsl:variable name="suite-passed" select="@passed"/>
                <xsl:variable name="suite-failed" select="@failed"/>
                <xsl:variable name="suite-skipped" select="@skipped"/>
                
                Suite: <xsl:value-of select="$suite-name"/> 
                [<span class="pass-cell"><xsl:value-of select="$suite-passed"/> Passed</span>]
                [<span class="fail-cell"><xsl:value-of select="$suite-failed"/> Failed</span>]
                [<span class="skip-cell"><xsl:value-of select="$suite-skipped"/> Skipped</span>]
            </div>
            
            <div class="test-methods">
                <xsl:apply-templates select="test"/>
            </div>
        </div>
    </xsl:template>
    
    <xsl:template match="test">
        <xsl:variable name="test-name" select="@name"/>
        
        <table style="margin-top: 10px;">
            <tr>
                <th colspan="5">Test: <xsl:value-of select="$test-name"/></th>
            </tr>
            <tr>
                <th>Method</th>
                <th>Status</th>
                <th>Duration (ms)</th>
                <th>Class</th>
                <th>Details</th>
            </tr>
            <xsl:apply-templates select="class"/>
        </table>
    </xsl:template>
    
    <xsl:template match="class">
        <xsl:variable name="class-name" select="@name"/>
        <xsl:apply-templates select="test-method">
            <xsl:with-param name="class-name" select="$class-name"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="test-method">
        <xsl:param name="class-name"/>
        <xsl:variable name="method-name" select="@name"/>
        <xsl:variable name="status" select="@status"/>
        <xsl:variable name="duration" select="@duration"/>
        
        <tr>
            <xsl:attribute name="class">
                <xsl:choose>
                    <xsl:when test="$status = 'PASS'">
                        <xsl:text>test-method passed</xsl:text>
                    </xsl:when>
                    <xsl:when test="$status = 'FAIL'">
                        <xsl:text>test-method failed</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>test-method skipped</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            
            <td><xsl:value-of select="$method-name"/></td>
            <td>
                <xsl:choose>
                    <xsl:when test="$status = 'PASS'">
                        <span class="pass-cell">✓ PASSED</span>
                    </xsl:when>
                    <xsl:when test="$status = 'FAIL'">
                        <span class="fail-cell">✗ FAILED</span>
                    </xsl:when>
                    <xsl:otherwise>
                        <span class="skip-cell">- SKIPPED</span>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
            <td><xsl:value-of select="$duration"/></td>
            <td><xsl:value-of select="$class-name"/></td>
            <td>
                <xsl:choose>
                    <xsl:when test="exception">
                        <details>
                            <summary>Error</summary>
                            <pre style="background: #f4f4f4; padding: 10px; overflow-x: auto;">
                                <xsl:value-of select="exception/@class"/>
                                <xsl:text>: </xsl:text>
                                <xsl:value-of select="exception"/>
                            </pre>
                        </details>
                    </xsl:when>
                    <xsl:otherwise>-</xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>