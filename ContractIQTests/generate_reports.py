#!/usr/bin/env python3
"""
Test Report Generator and Display Script
=========================================
This script:
1. Runs Maven tests
2. Generates all three reports (Emailable, Index, XSLT)
3. Displays report summaries in the terminal
4. Opens reports in browser
"""

import subprocess
import os
import sys
from pathlib import Path
from xml.etree import ElementTree as ET
import webbrowser
import json

class TestReportDisplayer:
    def __init__(self, project_dir):
        self.project_dir = Path(project_dir)
        self.reports_dir = self.project_dir / "target" / "surefire-reports"
        self.testng_results = self.reports_dir / "testng-results.xml"
    
    def run_tests(self):
        """Run Maven tests"""
        print("\n" + "="*70)
        print("RUNNING TESTS...")
        print("="*70 + "\n")
        
        try:
            result = subprocess.run(
                ["mvn", "clean", "verify"],
                cwd=str(self.project_dir),
                capture_output=False
            )
            return result.returncode == 0
        except Exception as e:
            print(f"Error running tests: {e}")
            return False
    
    def parse_testng_results(self):
        """Parse TestNG results XML"""
        try:
            tree = ET.parse(str(self.testng_results))
            root = tree.getroot()
            
            return {
                'total': int(root.get('total', 0)),
                'passed': int(root.get('passed', 0)),
                'failed': int(root.get('failed', 0)),
                'skipped': int(root.get('skipped', 0)),
                'duration': root.get('duration', 'N/A')
            }
        except Exception as e:
            print(f"Error parsing TestNG results: {e}")
            return None
    
    def display_stats_table(self, stats):
        """Display statistics in a formatted table"""
        if not stats:
            return
        
        total = stats['total']
        passed = stats['passed']
        failed = stats['failed']
        skipped = stats['skipped']
        
        success_rate = (passed / total * 100) if total > 0 else 0
        
        print("\n" + "="*70)
        print("TEST EXECUTION SUMMARY")
        print("="*70)
        print(f"{'Metric':<30} {'Count':<15} {'Rate':<20}")
        print("-"*70)
        print(f"{'Total Tests':<30} {total:<15} {'-':<20}")
        print(f"{'✓ PASSED':<30} {passed:<15} {(passed/total*100 if total > 0 else 0):.2f}%")
        print(f"{'✗ FAILED':<30} {failed:<15} {(failed/total*100 if total > 0 else 0):.2f}%")
        print(f"{'- SKIPPED':<30} {skipped:<15} {(skipped/total*100 if total > 0 else 0):.2f}%")
        print("-"*70)
        print(f"{'SUCCESS RATE':<30} {'-':<15} {success_rate:.2f}%")
        print("="*70)
    
    def display_report_locations(self):
        """Display locations of generated reports"""
        print("\n" + "="*70)
        print("GENERATED REPORTS")
        print("="*70)
        
        reports = {
            "Emailable Report": "emailable-report.html",
            "Index Report": "index.html",
            "XSLT Report": "testng-results.html",
            "HTML (Aggregated)": "surefire-report.html",
            "XML Results": "testng-results.xml"
        }
        
        print(f"\nReports Directory: {self.reports_dir}\n")
        
        for report_name, filename in reports.items():
            filepath = self.reports_dir / filename
            if filepath.exists():
                size_kb = filepath.stat().st_size / 1024
                print(f"✓ {report_name:<25} | {filename:<30} | Size: {size_kb:.2f} KB")
            else:
                print(f"✗ {report_name:<25} | {filename:<30} | NOT FOUND")
        
        print("\n" + "="*70)
    
    def display_test_details(self):
        """Display detailed test results by suite"""
        print("\n" + "="*70)
        print("TEST SUITE DETAILS")
        print("="*70 + "\n")
        
        try:
            tree = ET.parse(str(self.testng_results))
            root = tree.getroot()
            
            for suite in root.findall('suite'):
                suite_name = suite.get('name', 'Unknown')
                suite_passed = suite.get('passed', 0)
                suite_failed = suite.get('failed', 0)
                suite_skipped = suite.get('skipped', 0)
                
                status = "✓ PASS" if int(suite_failed) == 0 else "✗ FAIL"
                
                print(f"{status} | {suite_name:<35} | "
                      f"Passed: {suite_passed:<3} | Failed: {suite_failed:<3} | Skipped: {suite_skipped:<3}")
                
                # Show test methods
                for test in suite.findall('test'):
                    test_name = test.get('name', 'Unknown')
                    for cls in test.findall('class'):
                        for method in cls.findall('test-method'):
                            method_name = method.get('name', 'Unknown')
                            method_status = method.get('status', 'UNKNOWN')
                            
                            if method_status == 'PASS':
                                status_icon = "    ✓"
                            elif method_status == 'FAIL':
                                status_icon = "    ✗"
                            else:
                                status_icon = "    -"
                            
                            print(f"{status_icon} {method_name:<30} [{method_status}]")
        
        except Exception as e:
            print(f"Error displaying test details: {e}")
    
    def display_quick_links(self):
        """Display quick links and instructions"""
        print("\n" + "="*70)
        print("QUICK ACCESS")
        print("="*70)
        print("\nTo view reports, open these files:")
        print(f"\n1. Emailable Report (sendable via email):")
        print(f"   {self.reports_dir}/emailable-report.html")
        print(f"\n2. Index Report (detailed test results):")
        print(f"   {self.reports_dir}/index.html")
        print(f"\n3. XSLT Report (custom formatted):")
        print(f"   {self.reports_dir}/testng-results.html")
        print("\n" + "="*70)
    
    def run(self, skip_tests=False):
        """Execute complete report generation and display"""
        
        # Run tests
        if not skip_tests:
            if not self.run_tests():
                print("\n✗ Tests failed to complete")
                return False
        
        # Parse results
        stats = self.parse_testng_results()
        
        # Display reports
        self.display_stats_table(stats)
        self.display_test_details()
        self.display_report_locations()
        self.display_quick_links()
        
        # Success message
        if stats and stats['failed'] == 0:
            print("\n🎉 ALL TESTS PASSED! 🎉\n")
        else:
            print("\n⚠️  SOME TESTS FAILED - CHECK REPORTS FOR DETAILS ⚠️\n")
        
        return True


def main():
    """Main entry point"""
    project_dir = r"d:\TestAutomationfinal\contractiqfinal\ContractIQTests"
    
    if not Path(project_dir).exists():
        print(f"Error: Project directory not found: {project_dir}")
        return False
    
    displayer = TestReportDisplayer(project_dir)
    
    # Check if user wants to skip running tests
    skip_tests = len(sys.argv) > 1 and sys.argv[1] == "--skip-tests"
    
    return displayer.run(skip_tests=skip_tests)


if __name__ == "__main__":
    try:
        success = main()
        sys.exit(0 if success else 1)
    except Exception as e:
        print(f"\n✗ Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
