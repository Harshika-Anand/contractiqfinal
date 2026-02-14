package tests;

/**
 * TestRunner - Main class to run all test suites
 * Run this class to execute all tests in the project
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                              ║");
        System.out.println("║            CONTRACTIQ SELENIUM TEST AUTOMATION               ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Author: Test Automation Student                             ║");
        System.out.println("║  Project: ContractIQ - Smart Contract Analysis               ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        long startTime = System.currentTimeMillis();
        int totalTests = 0;
        int passedTests = 0;
        int failedTests = 0;
        
        // ==================== RUN HOME PAGE TESTS ====================
        System.out.println("\n▶ Running Home Page Tests...\n");
        try {
            HomePageTest.main(args);
            totalTests += 11;
            passedTests += 11; // Assuming all pass - adjust based on actual results
        } catch (Exception e) {
            System.out.println("Home Page Tests encountered errors: " + e.getMessage());
            failedTests++;
        }
        
        // ==================== RUN LOGIN PAGE TESTS ====================
        System.out.println("\n▶ Running Login Page Tests...\n");
        try {
            LoginPageTest.main(args);
            totalTests += 16;
            passedTests += 16;
        } catch (Exception e) {
            System.out.println("Login Page Tests encountered errors: " + e.getMessage());
            failedTests++;
        }
        
        // ==================== RUN REGISTER PAGE TESTS ====================
        System.out.println("\n▶ Running Register Page Tests...\n");
        try {
            RegisterPageTest.main(args);
            totalTests += 19;
            passedTests += 19;
        } catch (Exception e) {
            System.out.println("Register Page Tests encountered errors: " + e.getMessage());
            failedTests++;
        }
        
        // ==================== RUN DASHBOARD PAGE TESTS ====================
        System.out.println("\n▶ Running Dashboard Page Tests...\n");
        try {
            DashboardPageTest.main(args);
            totalTests += 15;
            passedTests += 15;
        } catch (Exception e) {
            System.out.println("Dashboard Page Tests encountered errors: " + e.getMessage());
            failedTests++;
        }
        
        // ==================== RUN DOCUMENTS PAGE TESTS ====================
        System.out.println("\n▶ Running Documents Page Tests...\n");
        try {
            DocumentsPageTest.main(args);
            totalTests += 15;
            passedTests += 15;
        } catch (Exception e) {
            System.out.println("Documents Page Tests encountered errors: " + e.getMessage());
            failedTests++;
        }
        
        // ==================== SUMMARY ====================
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     TEST EXECUTION SUMMARY                   ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                              ║");
        System.out.println("║  Total Test Cases:    76                                     ║");
        System.out.println("║  Test Suites Run:     5                                      ║");
        System.out.println("║  Execution Time:      " + String.format("%-10s", duration + "s") + "                           ║");
        System.out.println("║                                                              ║");
        System.out.println("║  Test Suites:                                                ║");
        System.out.println("║    • HomePageTest       (11 tests)                           ║");
        System.out.println("║    • LoginPageTest      (16 tests)                           ║");
        System.out.println("║    • RegisterPageTest   (19 tests)                           ║");
        System.out.println("║    • DashboardPageTest  (15 tests)                           ║");
        System.out.println("║    • DocumentsPageTest  (15 tests)                           ║");
        System.out.println("║                                                              ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("✓ Test execution completed!");
    }
}
