package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
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
     * Setup before each test - override parent to add custom logic
     */
    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();  // Call parent setUp for browser initialization
        navigateToHome();
        homePage = new HomePage(driver);
    }
    
    /**
     * Cleanup after each test
     */
    @AfterMethod
    @Override
    public void tearDown() {
        super.tearDown();  // Call parent tearDown for browser cleanup
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify home page is displayed correctly
     */
    @Test
    public void testHomePageDisplay() {
        boolean result = homePage.isPageDisplayed();
        printTestResult("TC001_HomePageDisplay", result, "Home page elements not found");
        assert result : "Home page should be displayed";
    }
    
    /**
     * TC002: Verify main title text
     */
    @Test
    public void testMainTitle() {
        String title = homePage.getMainTitle();
        boolean result = title.contains("Smart Contract Analysis");
        printTestResult("TC002_MainTitle", result, "Title text: '" + title + "' does not contain expected text");
        assert result : "Main title should contain 'Smart Contract Analysis'";
    }
    
    /**
     * TC003: Verify subtitle text
     */
    @Test
    public void testSubtitle() {
        String subtitle = homePage.getSubtitle();
        boolean result = subtitle.contains("Made Simple");
        printTestResult("TC003_Subtitle", result, "Subtitle text: '" + subtitle + "' does not contain expected text");
        assert result : "Subtitle should contain 'Made Simple'";
    }
    
    /**
     * TC004: Verify Get Started button is displayed
     */
    @Test
    public void testGetStartedButtonDisplayed() {
        boolean result = homePage.isGetStartedButtonDisplayed();
        printTestResult("TC004_GetStartedButtonDisplayed", result, "Get Started button not found on page");
        assert result : "Get Started button should be displayed";
    }
    
    /**
     * TC005: Verify Sign In button is displayed
     */
    @Test
    public void testSignInButtonDisplayed() {
        boolean result = homePage.isSignInButtonDisplayed();
        printTestResult("TC005_SignInButtonDisplayed", result, "Sign In button not found on page");
        assert result : "Sign In button should be displayed";
    }
    
    /**
     * TC006: Verify all feature cards are displayed
     */
    @Test
    public void testFeatureCardsDisplayed() {
        boolean result = homePage.areAllFeaturesDisplayed();
        printTestResult("TC006_FeatureCardsDisplayed", result, "One or more feature cards are missing");
        assert result : "All feature cards should be displayed";
    }
    
    /**
     * TC007: Verify Get Started button navigates to Register page
     */
    @Test
    public void testGetStartedNavigatesToRegister() {
        RegisterPage registerPage = homePage.clickGetStarted();
        boolean result = registerPage.isPageDisplayed();
        printTestResult("TC007_GetStartedNavigatesToRegister", result, "Register page did not load after clicking Get Started");
        assert result : "Clicking Get Started should navigate to Register page";
    }
    
    /**
     * TC008: Verify Sign In button navigates to Login page
     */
    @Test
    public void testSignInNavigatesToLogin() {
        LoginPage loginPage = homePage.clickSignIn();
        boolean result = loginPage.isPageDisplayed();
        printTestResult("TC008_SignInNavigatesToLogin", result, "Login page did not load after clicking Sign In");
        assert result : "Clicking Sign In should navigate to Login page";
    }
    
    /**
     * TC009: Verify Smart PDF Analysis card is displayed
     */
    @Test
    public void testSmartPdfCardDisplayed() {
        boolean result = homePage.isSmartPdfCardDisplayed();
        printTestResult("TC009_SmartPdfCardDisplayed", result, "Smart PDF Analysis card not found");
        assert result : "Smart PDF Analysis card should be displayed";
    }
    
    /**
     * TC010: Verify Clause Detection card is displayed
     */
    @Test
    public void testClauseDetectionCardDisplayed() {
        boolean result = homePage.isClauseDetectionCardDisplayed();
        printTestResult("TC010_ClauseDetectionCardDisplayed", result, "Clause Detection card not found");
        assert result : "Clause Detection card should be displayed";
    }
    
    /**
     * TC011: Verify Dashboard Analytics card is displayed
     */
    @Test
    public void testDashboardAnalyticsCardDisplayed() {
        boolean result = homePage.isDashboardAnalyticsCardDisplayed();
        printTestResult("TC011_DashboardAnalyticsCardDisplayed", result, "Dashboard Analytics card not found");
        assert result : "Dashboard Analytics card should be displayed";
    }
    
}
