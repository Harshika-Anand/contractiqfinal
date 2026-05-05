package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
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
    
    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        // Register a new user to ensure we're logged in
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        
        String username = TestDataGenerator.generateUsername();
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.getValidTestPassword();
        
        dashboardPage = registerPage.register(username, email, "client", password);
    }
    
    @AfterMethod
    @Override
    public void tearDown() {
        super.tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify dashboard page is displayed after login
     */
    @Test
    public void testDashboardPageDisplay() {
        boolean result = dashboardPage.isPageDisplayed();
        printTestResult("TC001_DashboardPageDisplay", result);
        assert result : "Dashboard page should be displayed after login";
    }
    
    /**
     * TC002: Verify welcome message is displayed
     */
    @Test
    public void testWelcomeMessageDisplayed() {
        String welcomeMessage = dashboardPage.getWelcomeMessage();
        boolean result = welcomeMessage.contains("Welcome back");
        printTestResult("TC002_WelcomeMessageDisplayed", result);
        assert result : "Welcome message should be displayed";
    }
    
    /**
     * TC003: Verify Total Documents card is displayed
     */
    @Test
    public void testTotalDocumentsCardDisplayed() {
        boolean result = dashboardPage.isTotalDocumentsCardDisplayed();
        printTestResult("TC003_TotalDocumentsCardDisplayed", result);
        assert result : "Total Documents card should be displayed";
    }
    
    /**
     * TC004: Verify Recent Uploads card is displayed
     */
    @Test
    public void testRecentUploadsCardDisplayed() {
        boolean result = dashboardPage.isRecentUploadsCardDisplayed();
        printTestResult("TC004_RecentUploadsCardDisplayed", result);
        assert result : "Recent Uploads card should be displayed";
    }
    
    /**
     * TC005: Verify Clauses Extracted card is displayed
     */
    @Test
    public void testClausesExtractedCardDisplayed() {
        boolean result = dashboardPage.isClausesExtractedCardDisplayed();
        printTestResult("TC005_ClausesExtractedCardDisplayed", result);
        assert result : "Clauses Extracted card should be displayed";
    }
    
    /**
     * TC006: Verify all statistics cards are displayed
     */
    @Test
    public void testAllStatsCardsDisplayed() {
        boolean result = dashboardPage.areAllStatsCardsDisplayed();
        printTestResult("TC006_AllStatsCardsDisplayed", result);
        assert result : "All statistics cards should be displayed";
    }
    
    /**
     * TC007: Verify Recent Documents section is displayed
     */
    @Test
    public void testRecentDocumentsSectionDisplayed() {
        boolean result = dashboardPage.isRecentDocumentsSectionDisplayed();
        printTestResult("TC007_RecentDocumentsSectionDisplayed", result);
        assert result : "Recent Documents section should be displayed";
    }
    
    /**
     * TC008: Verify "No documents yet" message for new user
     */
    @Test
    public void testNoDocumentsMessageForNewUser() {
        boolean result = dashboardPage.isNoDocumentsMessageDisplayed();
        printTestResult("TC008_NoDocumentsMessageForNewUser", result);
        assert result : "No documents message should be displayed for new user";
    }
    
    /**
     * TC009: Verify View All link is displayed
     */
    @Test
    public void testViewAllLinkDisplayed() {
        boolean result = dashboardPage.isViewAllLinkDisplayed();
        printTestResult("TC009_ViewAllLinkDisplayed", result);
        assert result : "View All link should be displayed";
    }
    
    /**
     * TC010: Verify View All link navigates to Documents page
     */
    @Test
    public void testViewAllNavigatesToDocuments() {
        DocumentsPage documentsPage = dashboardPage.clickViewAll();
        boolean result = documentsPage.isPageDisplayed();
        printTestResult("TC010_ViewAllNavigatesToDocuments", result);
        assert result : "View All should navigate to Documents page";
    }
    
    /**
     * TC011: Verify initial document count is zero for new user
     */
    @Test
    public void testInitialDocumentCountZero() {
        int count = dashboardPage.getTotalDocumentsCount();
        boolean result = count == 0;
        printTestResult("TC011_InitialDocumentCountZero", result);
        assert result : "Initial document count should be zero";
    }
    
    /**
     * TC012: Verify navigation to Documents page
     */
    @Test
    public void testNavigateToDocuments() {
        DocumentsPage documentsPage = dashboardPage.goToDocuments();
        boolean result = documentsPage.isPageDisplayed();
        printTestResult("TC012_NavigateToDocuments", result);
        assert result : "Should be able to navigate to Documents page";
    }
    
    /**
     * TC013: Verify URL contains /dashboard
     */
    @Test
    public void testDashboardUrl() {
        String url = dashboardPage.getCurrentUrl();
        boolean result = url.contains("/dashboard");
        printTestResult("TC013_DashboardUrl", result);
        assert result : "URL should contain /dashboard";
    }
    
    /**
     * TC014: Verify initial clauses count is zero
     */
    @Test
    public void testInitialClausesCountZero() {
        int count = dashboardPage.getClausesExtractedCount();
        boolean result = count == 0;
        printTestResult("TC014_InitialClausesCountZero", result);
        assert result : "Initial clauses count should be zero";
    }
    
    /**
     * TC015: Verify recent documents count for new user
     */
    @Test
    public void testRecentDocumentsCountZero() {
        int count = dashboardPage.getRecentDocumentsCount();
        boolean result = count == 0;
        printTestResult("TC015_RecentDocumentsCountZero", result);
        assert result : "Recent documents count should be zero for new user";
    }
}
