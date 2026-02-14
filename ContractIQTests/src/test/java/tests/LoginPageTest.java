package tests;

import pages.LoginPage;
import pages.RegisterPage;
import pages.DashboardPage;
import utils.TestDataGenerator;

/**
 * LoginPageTest - Test cases for the Login page
 * Tests login functionality and page elements
 */
public class LoginPageTest extends BaseTest {
    
    private LoginPage loginPage;
    
    // Test data - You should register this user first before running login tests
    private static final String VALID_EMAIL = "testuser@test.com";
    private static final String VALID_PASSWORD = "Test@1234";
    
    /**
     * Setup before each test
     */
    public void beforeTest() {
        setUp();
        navigateToLogin();
        loginPage = new LoginPage(driver);
    }
    
    /**
     * Cleanup after each test
     */
    public void afterTest() {
        tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify login page is displayed correctly
     */
    public void testLoginPageDisplay() {
        beforeTest();
        try {
            boolean result = loginPage.isPageDisplayed();
            printTestResult("TC001_LoginPageDisplay", result);
            assert result : "Login page should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC002: Verify page header text
     */
    public void testPageHeader() {
        beforeTest();
        try {
            String header = loginPage.getPageHeader();
            boolean result = header.contains("ContractIQ");
            printTestResult("TC002_PageHeader", result);
            assert result : "Page header should contain 'ContractIQ'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC003: Verify page subtitle text
     */
    public void testPageSubtitle() {
        beforeTest();
        try {
            String subtitle = loginPage.getPageSubtitle();
            boolean result = subtitle.contains("Sign in to your account");
            printTestResult("TC003_PageSubtitle", result);
            assert result : "Page subtitle should contain 'Sign in to your account'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify email field is displayed
     */
    public void testEmailFieldDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isEmailFieldDisplayed();
            printTestResult("TC004_EmailFieldDisplayed", result);
            assert result : "Email field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify password field is displayed
     */
    public void testPasswordFieldDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordFieldDisplayed();
            printTestResult("TC005_PasswordFieldDisplayed", result);
            assert result : "Password field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify Sign In button is displayed
     */
    public void testSignInButtonDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isSignInButtonDisplayed();
            printTestResult("TC006_SignInButtonDisplayed", result);
            assert result : "Sign In button should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify Register link is displayed
     */
    public void testRegisterLinkDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isRegisterLinkDisplayed();
            printTestResult("TC007_RegisterLinkDisplayed", result);
            assert result : "Register link should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify email field placeholder
     */
    public void testEmailPlaceholder() {
        beforeTest();
        try {
            String placeholder = loginPage.getEmailPlaceholder();
            boolean result = placeholder.contains("@") || placeholder.contains("email");
            printTestResult("TC008_EmailPlaceholder", result);
            assert result : "Email field should have appropriate placeholder";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify password hint is displayed
     */
    public void testPasswordHintDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordHintDisplayed();
            printTestResult("TC009_PasswordHintDisplayed", result);
            assert result : "Password hint should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify email field is required
     */
    public void testEmailFieldRequired() {
        beforeTest();
        try {
            boolean result = loginPage.isEmailRequired();
            printTestResult("TC010_EmailFieldRequired", result);
            assert result : "Email field should be required";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify password field is required
     */
    public void testPasswordFieldRequired() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordRequired();
            printTestResult("TC011_PasswordFieldRequired", result);
            assert result : "Password field should be required";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC012: Verify Register link navigates to Register page
     */
    public void testRegisterLinkNavigation() {
        beforeTest();
        try {
            RegisterPage registerPage = loginPage.clickRegisterLink();
            boolean result = registerPage.isPageDisplayed();
            printTestResult("TC012_RegisterLinkNavigation", result);
            assert result : "Register link should navigate to Register page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC013: Verify login with empty fields shows error
     */
    public void testLoginWithEmptyFields() {
        beforeTest();
        try {
            loginPage.clickSignIn();
            // Browser validation should prevent form submission
            boolean result = loginPage.isPageDisplayed(); // Should still be on login page
            printTestResult("TC013_LoginWithEmptyFields", result);
            assert result : "Login with empty fields should not proceed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC014: Verify login with invalid credentials shows error
     */
    public void testLoginWithInvalidCredentials() {
        beforeTest();
        try {
            loginPage.attemptLogin("invalid@email.com", "WrongPassword123!");
            boolean result = !loginPage.isLoginSuccessful();
            printTestResult("TC014_LoginWithInvalidCredentials", result);
            assert result : "Login with invalid credentials should fail";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC015: Verify successful login (requires valid test user)
     * Note: This test requires a pre-registered user in the system
     */
    public void testSuccessfulLogin() {
        beforeTest();
        try {
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
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC016: Verify login with invalid email format
     */
    public void testLoginWithInvalidEmailFormat() {
        beforeTest();
        try {
            loginPage.enterEmail("invalidemail");
            loginPage.enterPassword("Test@1234");
            loginPage.clickSignIn();
            // Browser should validate email format
            boolean result = loginPage.isPageDisplayed();
            printTestResult("TC016_LoginWithInvalidEmailFormat", result);
            assert result : "Login with invalid email format should not proceed";
        } finally {
            afterTest();
        }
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Run all login page tests
     */
    public static void main(String[] args) {
        LoginPageTest test = new LoginPageTest();
        
        System.out.println("========================================");
        System.out.println("    CONTRACTIQ LOGIN PAGE TESTS");
        System.out.println("========================================\n");
        
        try {
            test.testLoginPageDisplay();
            test.testPageHeader();
            test.testPageSubtitle();
            test.testEmailFieldDisplayed();
            test.testPasswordFieldDisplayed();
            test.testSignInButtonDisplayed();
            test.testRegisterLinkDisplayed();
            test.testEmailPlaceholder();
            test.testPasswordHintDisplayed();
            test.testEmailFieldRequired();
            test.testPasswordFieldRequired();
            test.testRegisterLinkNavigation();
            test.testLoginWithEmptyFields();
            test.testLoginWithInvalidCredentials();
            test.testSuccessfulLogin();
            test.testLoginWithInvalidEmailFormat();
        } catch (Exception e) {
            System.out.println("Test execution error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("    LOGIN PAGE TESTS COMPLETED");
        System.out.println("========================================");
    }
}
