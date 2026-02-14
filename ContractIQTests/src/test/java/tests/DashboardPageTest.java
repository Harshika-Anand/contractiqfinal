package tests;

import pages.RegisterPage;
import pages.DashboardPage;
import pages.DocumentsPage;
import utils.TestDataGenerator;

/**
 * DashboardPageTest - Test cases for the Dashboard page
 * Tests dashboard functionality after user login
 */
public class DashboardPageTest extends BaseTest {
    
    private DashboardPage dashboardPage;
    
    /**
     * Setup before each test - registers new user and navigates to dashboard
     */
    public void beforeTest() {
        setUp();
        // Register a new user to ensure we're logged in
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        
        String username = TestDataGenerator.generateUsername();
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.getValidTestPassword();
        
        dashboardPage = registerPage.register(username, email, "client", password);
    }
    
    /**
     * Cleanup after each test
     */
    public void afterTest() {
        tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify dashboard page is displayed after login
     */
    public void testDashboardPageDisplay() {
        beforeTest();
        try {
            boolean result = dashboardPage.isPageDisplayed();
            printTestResult("TC001_DashboardPageDisplay", result);
            assert result : "Dashboard page should be displayed after login";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC002: Verify welcome message is displayed
     */
    public void testWelcomeMessageDisplayed() {
        beforeTest();
        try {
            String welcomeMessage = dashboardPage.getWelcomeMessage();
            boolean result = welcomeMessage.contains("Welcome back");
            printTestResult("TC002_WelcomeMessageDisplayed", result);
            assert result : "Welcome message should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC003: Verify Total Documents card is displayed
     */
    public void testTotalDocumentsCardDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.isTotalDocumentsCardDisplayed();
            printTestResult("TC003_TotalDocumentsCardDisplayed", result);
            assert result : "Total Documents card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify Recent Uploads card is displayed
     */
    public void testRecentUploadsCardDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.isRecentUploadsCardDisplayed();
            printTestResult("TC004_RecentUploadsCardDisplayed", result);
            assert result : "Recent Uploads card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify Clauses Extracted card is displayed
     */
    public void testClausesExtractedCardDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.isClausesExtractedCardDisplayed();
            printTestResult("TC005_ClausesExtractedCardDisplayed", result);
            assert result : "Clauses Extracted card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify all statistics cards are displayed
     */
    public void testAllStatsCardsDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.areAllStatsCardsDisplayed();
            printTestResult("TC006_AllStatsCardsDisplayed", result);
            assert result : "All statistics cards should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify Recent Documents section is displayed
     */
    public void testRecentDocumentsSectionDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.isRecentDocumentsSectionDisplayed();
            printTestResult("TC007_RecentDocumentsSectionDisplayed", result);
            assert result : "Recent Documents section should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify "No documents yet" message for new user
     */
    public void testNoDocumentsMessageForNewUser() {
        beforeTest();
        try {
            boolean result = dashboardPage.isNoDocumentsMessageDisplayed();
            printTestResult("TC008_NoDocumentsMessageForNewUser", result);
            assert result : "No documents message should be displayed for new user";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify View All link is displayed
     */
    public void testViewAllLinkDisplayed() {
        beforeTest();
        try {
            boolean result = dashboardPage.isViewAllLinkDisplayed();
            printTestResult("TC009_ViewAllLinkDisplayed", result);
            assert result : "View All link should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify View All link navigates to Documents page
     */
    public void testViewAllNavigatesToDocuments() {
        beforeTest();
        try {
            DocumentsPage documentsPage = dashboardPage.clickViewAll();
            boolean result = documentsPage.isPageDisplayed();
            printTestResult("TC010_ViewAllNavigatesToDocuments", result);
            assert result : "View All should navigate to Documents page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify initial document count is zero for new user
     */
    public void testInitialDocumentCountZero() {
        beforeTest();
        try {
            int count = dashboardPage.getTotalDocumentsCount();
            boolean result = count == 0;
            printTestResult("TC011_InitialDocumentCountZero", result);
            assert result : "Initial document count should be zero";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC012: Verify navigation to Documents page
     */
    public void testNavigateToDocuments() {
        beforeTest();
        try {
            DocumentsPage documentsPage = dashboardPage.goToDocuments();
            boolean result = documentsPage.isPageDisplayed();
            printTestResult("TC012_NavigateToDocuments", result);
            assert result : "Should be able to navigate to Documents page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC013: Verify URL contains /dashboard
     */
    public void testDashboardUrl() {
        beforeTest();
        try {
            String url = dashboardPage.getCurrentUrl();
            boolean result = url.contains("/dashboard");
            printTestResult("TC013_DashboardUrl", result);
            assert result : "URL should contain /dashboard";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC014: Verify initial clauses count is zero
     */
    public void testInitialClausesCountZero() {
        beforeTest();
        try {
            int count = dashboardPage.getClausesExtractedCount();
            boolean result = count == 0;
            printTestResult("TC014_InitialClausesCountZero", result);
            assert result : "Initial clauses count should be zero";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC015: Verify recent documents count for new user
     */
    public void testRecentDocumentsCountZero() {
        beforeTest();
        try {
            int count = dashboardPage.getRecentDocumentsCount();
            boolean result = count == 0;
            printTestResult("TC015_RecentDocumentsCountZero", result);
            assert result : "Recent documents count should be zero for new user";
        } finally {
            afterTest();
        }
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Run all dashboard page tests
     */
    public static void main(String[] args) {
        DashboardPageTest test = new DashboardPageTest();
        
        System.out.println("========================================");
        System.out.println("    CONTRACTIQ DASHBOARD PAGE TESTS");
        System.out.println("========================================\n");
        
        try {
            test.testDashboardPageDisplay();
            test.testWelcomeMessageDisplayed();
            test.testTotalDocumentsCardDisplayed();
            test.testRecentUploadsCardDisplayed();
            test.testClausesExtractedCardDisplayed();
            test.testAllStatsCardsDisplayed();
            test.testRecentDocumentsSectionDisplayed();
            test.testNoDocumentsMessageForNewUser();
            test.testViewAllLinkDisplayed();
            test.testViewAllNavigatesToDocuments();
            test.testInitialDocumentCountZero();
            test.testNavigateToDocuments();
            test.testDashboardUrl();
            test.testInitialClausesCountZero();
            test.testRecentDocumentsCountZero();
        } catch (Exception e) {
            System.out.println("Test execution error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("    DASHBOARD PAGE TESTS COMPLETED");
        System.out.println("========================================");
    }
}
