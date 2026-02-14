package tests;

import pages.HomePage;
import pages.LoginPage;
import pages.RegisterPage;

/**
 * HomePageTest - Test cases for the Home/Landing page
 * Tests the main landing page functionality
 */
public class HomePageTest extends BaseTest {
    
    private HomePage homePage;
    
    /**
     * Setup before each test
     */
    public void beforeTest() {
        setUp();
        navigateToHome();
        homePage = new HomePage(driver);
    }
    
    /**
     * Cleanup after each test
     */
    public void afterTest() {
        tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify home page is displayed correctly
     */
    public void testHomePageDisplay() {
        printTestStarting("TC001_HomePageDisplay", "Verify home page loads and displays correctly");
        beforeTest();
        try {
            boolean result = homePage.isPageDisplayed();
            printTestResult("TC001_HomePageDisplay", result, "Home page elements not found");
            assert result : "Home page should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC002: Verify main title text
     */
    public void testMainTitle() {
        printTestStarting("TC002_MainTitle", "Verify main title contains 'Smart Contract Analysis'");
        beforeTest();
        try {
            String title = homePage.getMainTitle();
            boolean result = title.contains("Smart Contract Analysis");
            printTestResult("TC002_MainTitle", result, "Title text: '" + title + "' does not contain expected text");
            assert result : "Main title should contain 'Smart Contract Analysis'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC003: Verify subtitle text
     */
    public void testSubtitle() {
        printTestStarting("TC003_Subtitle", "Verify subtitle contains 'Made Simple'");
        beforeTest();
        try {
            String subtitle = homePage.getSubtitle();
            boolean result = subtitle.contains("Made Simple");
            printTestResult("TC003_Subtitle", result, "Subtitle text: '" + subtitle + "' does not contain expected text");
            assert result : "Subtitle should contain 'Made Simple'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify Get Started button is displayed
     */
    public void testGetStartedButtonDisplayed() {
        printTestStarting("TC004_GetStartedButtonDisplayed", "Verify 'Get Started Free' button is visible");
        beforeTest();
        try {
            boolean result = homePage.isGetStartedButtonDisplayed();
            printTestResult("TC004_GetStartedButtonDisplayed", result, "Get Started button not found on page");
            assert result : "Get Started button should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify Sign In button is displayed
     */
    public void testSignInButtonDisplayed() {
        printTestStarting("TC005_SignInButtonDisplayed", "Verify 'Sign In' button is visible");
        beforeTest();
        try {
            boolean result = homePage.isSignInButtonDisplayed();
            printTestResult("TC005_SignInButtonDisplayed", result, "Sign In button not found on page");
            assert result : "Sign In button should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify all feature cards are displayed
     */
    public void testFeatureCardsDisplayed() {
        printTestStarting("TC006_FeatureCardsDisplayed", "Verify all 3 feature cards are displayed");
        beforeTest();
        try {
            boolean result = homePage.areAllFeaturesDisplayed();
            printTestResult("TC006_FeatureCardsDisplayed", result, "One or more feature cards are missing");
            assert result : "All feature cards should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify Get Started button navigates to Register page
     */
    public void testGetStartedNavigatesToRegister() {
        printTestStarting("TC007_GetStartedNavigatesToRegister", "Click Get Started and verify navigation to Register");
        beforeTest();
        try {
            RegisterPage registerPage = homePage.clickGetStarted();
            boolean result = registerPage.isPageDisplayed();
            printTestResult("TC007_GetStartedNavigatesToRegister", result, "Register page did not load after clicking Get Started");
            assert result : "Clicking Get Started should navigate to Register page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify Sign In button navigates to Login page
     */
    public void testSignInNavigatesToLogin() {
        printTestStarting("TC008_SignInNavigatesToLogin", "Click Sign In and verify navigation to Login");
        beforeTest();
        try {
            LoginPage loginPage = homePage.clickSignIn();
            boolean result = loginPage.isPageDisplayed();
            printTestResult("TC008_SignInNavigatesToLogin", result, "Login page did not load after clicking Sign In");
            assert result : "Clicking Sign In should navigate to Login page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify Smart PDF Analysis card is displayed
     */
    public void testSmartPdfCardDisplayed() {
        printTestStarting("TC009_SmartPdfCardDisplayed", "Verify 'Smart PDF Analysis' feature card is visible");
        beforeTest();
        try {
            boolean result = homePage.isSmartPdfCardDisplayed();
            printTestResult("TC009_SmartPdfCardDisplayed", result, "Smart PDF Analysis card not found");
            assert result : "Smart PDF Analysis card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify Clause Detection card is displayed
     */
    public void testClauseDetectionCardDisplayed() {
        printTestStarting("TC010_ClauseDetectionCardDisplayed", "Verify 'Clause Detection' feature card is visible");
        beforeTest();
        try {
            boolean result = homePage.isClauseDetectionCardDisplayed();
            printTestResult("TC010_ClauseDetectionCardDisplayed", result, "Clause Detection card not found");
            assert result : "Clause Detection card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify Dashboard Analytics card is displayed
     */
    public void testDashboardAnalyticsCardDisplayed() {
        printTestStarting("TC011_DashboardAnalyticsCardDisplayed", "Verify 'Dashboard Analytics' feature card is visible");
        beforeTest();
        try {
            boolean result = homePage.isDashboardAnalyticsCardDisplayed();
            printTestResult("TC011_DashboardAnalyticsCardDisplayed", result, "Dashboard Analytics card not found");
            assert result : "Dashboard Analytics card should be displayed";
        } finally {
            afterTest();
        }
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Run all home page tests
     */
    public static void main(String[] args) {
        HomePageTest test = new HomePageTest();
        
        System.out.println("========================================");
        System.out.println("    CONTRACTIQ HOME PAGE TESTS");
        System.out.println("========================================\n");
        
        try {
            test.testHomePageDisplay();
            test.testMainTitle();
            test.testSubtitle();
            test.testGetStartedButtonDisplayed();
            test.testSignInButtonDisplayed();
            test.testFeatureCardsDisplayed();
            test.testGetStartedNavigatesToRegister();
            test.testSignInNavigatesToLogin();
            test.testSmartPdfCardDisplayed();
            test.testClauseDetectionCardDisplayed();
            test.testDashboardAnalyticsCardDisplayed();
        } catch (Exception e) {
            System.out.println("Test execution error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("    HOME PAGE TESTS COMPLETED");
        System.out.println("========================================");
    }
}
