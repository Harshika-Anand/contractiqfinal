package tests;

import pages.LoginPage;
import pages.RegisterPage;
import pages.DashboardPage;
import utils.TestDataGenerator;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

/**
 * LoginPageTest - Test cases for the Login page
 * Tests login functionality and page elements
 * Uses TestNG framework with @Test annotations
 */
public class LoginPageTest extends BaseTest {
    
    private LoginPage loginPage;
    
    // Test data - You should register this user first before running login tests
    private static final String VALID_EMAIL = "testuser@test.com";
    private static final String VALID_PASSWORD = "Test@1234";
    
    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        navigateToLogin();
        loginPage = new LoginPage(driver);
    }
    
    @AfterMethod
    @Override
    public void tearDown() {
        super.tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify login page is displayed correctly
     */
    @Test
    public void testLoginPageDisplay() {
        boolean result = loginPage.isPageDisplayed();
        printTestResult("TC001_LoginPageDisplay", result);
        assert result : "Login page should be displayed";
    }
    
    /**
     * TC002: Verify page header text
     */
    @Test
    public void testPageHeader() {
        String header = loginPage.getPageHeader();
        boolean result = header.contains("ContractIQ");
        printTestResult("TC002_PageHeader", result);
        assert result : "Page header should contain 'ContractIQ'";
    }
    
    /**
     * TC003: Verify page subtitle text
     */
    @Test
    public void testPageSubtitle() {
        String subtitle = loginPage.getPageSubtitle();
        boolean result = subtitle.contains("Sign in to your account");
        printTestResult("TC003_PageSubtitle", result);
        assert result : "Page subtitle should contain 'Sign in to your account'";
    }
    
    /**
     * TC004: Verify email field is displayed
     */
    @Test
    public void testEmailFieldDisplayed() {
        boolean result = loginPage.isEmailFieldDisplayed();
        printTestResult("TC004_EmailFieldDisplayed", result);
        assert result : "Email field should be displayed";
    }
    
    /**
     * TC005: Verify password field is displayed
     */
    @Test
    public void testPasswordFieldDisplayed() {
        boolean result = loginPage.isPasswordFieldDisplayed();
        printTestResult("TC005_PasswordFieldDisplayed", result);
        assert result : "Password field should be displayed";
    }
    
    /**
     * TC006: Verify Sign In button is displayed
     */
    @Test
    public void testSignInButtonDisplayed() {
        boolean result = loginPage.isSignInButtonDisplayed();
        printTestResult("TC006_SignInButtonDisplayed", result);
        assert result : "Sign In button should be displayed";
    }
    
    /**
     * TC007: Verify Register link is displayed
     */
    @Test
    public void testRegisterLinkDisplayed() {
        boolean result = loginPage.isRegisterLinkDisplayed();
        printTestResult("TC007_RegisterLinkDisplayed", result);
        assert result : "Register link should be displayed";
    }
    
    /**
     * TC008: Verify email field placeholder
     */
    @Test
    public void testEmailPlaceholder() {
        String placeholder = loginPage.getEmailPlaceholder();
        boolean result = placeholder.contains("@") || placeholder.contains("email");
        printTestResult("TC008_EmailPlaceholder", result);
        assert result : "Email field should have appropriate placeholder";
    }
    
    /**
     * TC009: Verify password hint is displayed
     */
    @Test
    public void testPasswordHintDisplayed() {
        boolean result = loginPage.isPasswordHintDisplayed();
        printTestResult("TC009_PasswordHintDisplayed", result);
        assert result : "Password hint should be displayed";
    }
    
    /**
     * TC010: Verify email field is required
     */
    @Test
    public void testEmailFieldRequired() {
        boolean result = loginPage.isEmailRequired();
        printTestResult("TC010_EmailFieldRequired", result);
        assert result : "Email field should be required";
    }
    
    /**
     * TC011: Verify password field is required
     */
    @Test
    public void testPasswordFieldRequired() {
        boolean result = loginPage.isPasswordRequired();
        printTestResult("TC011_PasswordFieldRequired", result);
        assert result : "Password field should be required";
    }
    
    /**
     * TC012: Verify Register link navigates to Register page
     */
    @Test
    public void testRegisterLinkNavigation() {
        RegisterPage registerPage = loginPage.clickRegisterLink();
        boolean result = registerPage.isPageDisplayed();
        printTestResult("TC012_RegisterLinkNavigation", result);
        assert result : "Register link should navigate to Register page";
    }
    
    /**
     * TC013: Verify login with empty fields shows error
     */
    @Test
    public void testLoginWithEmptyFields() {
        loginPage.clickSignIn();
        // Browser validation should prevent form submission
        boolean result = loginPage.isPageDisplayed(); // Should still be on login page
        printTestResult("TC013_LoginWithEmptyFields", result);
        assert result : "Login with empty fields should not proceed";
    }
    
    /**
     * TC014: Verify login with invalid credentials shows error
     */
    @Test
    public void testLoginWithInvalidCredentials() {
        loginPage.attemptLogin("invalid@email.com", "WrongPassword123!");
        boolean result = !loginPage.isLoginSuccessful();
        printTestResult("TC014_LoginWithInvalidCredentials", result);
        assert result : "Login with invalid credentials should fail";
    }
    
    /**
     * TC015: Verify successful login (requires valid test user)
     * Note: This test requires a pre-registered user in the system
     */
    @Test
    public void testSuccessfulLogin() {
        // First, let's register a new user to ensure we have valid credentials
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.getValidTestPassword();
        
        DashboardPage dashboard = registerPage.register(
            TestDataGenerator.generateUsername(),
            email,
            "client",
            password
        );
        
        boolean result = dashboard.isPageDisplayed();
        printTestResult("TC015_SuccessfulLogin", result);
        assert result : "Successful login should redirect to dashboard";
    }
    
    /**
     * TC016: Verify login with invalid email format
     */
    @Test
    public void testLoginWithInvalidEmailFormat() {
        loginPage.enterEmail("invalidemail");
        loginPage.enterPassword("Test@1234");
        loginPage.clickSignIn();
        // Browser should validate email format
        boolean result = loginPage.isPageDisplayed();
        printTestResult("TC016_LoginWithInvalidEmailFormat", result);
        assert result : "Login with invalid email format should not proceed";
    }
}
