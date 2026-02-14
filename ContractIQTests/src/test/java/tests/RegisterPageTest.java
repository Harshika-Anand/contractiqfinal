package tests;

import pages.RegisterPage;
import pages.LoginPage;
import pages.DashboardPage;
import utils.TestDataGenerator;

/**
 * RegisterPageTest - Test cases for the Registration page
 * Tests registration functionality and page elements
 */
public class RegisterPageTest extends BaseTest {
    
    private RegisterPage registerPage;
    
    /**
     * Setup before each test
     */
    public void beforeTest() {
        setUp();
        navigateToRegister();
        registerPage = new RegisterPage(driver);
    }
    
    /**
     * Cleanup after each test
     */
    public void afterTest() {
        tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify registration page is displayed correctly
     */
    public void testRegisterPageDisplay() {
        beforeTest();
        try {
            boolean result = registerPage.isPageDisplayed();
            printTestResult("TC001_RegisterPageDisplay", result);
            assert result : "Register page should be displayed";
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
            String header = registerPage.getPageHeader();
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
            String subtitle = registerPage.getPageSubtitle();
            boolean result = subtitle.contains("Create your account");
            printTestResult("TC003_PageSubtitle", result);
            assert result : "Page subtitle should contain 'Create your account'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify username field is displayed
     */
    public void testUsernameFieldDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isUsernameFieldDisplayed();
            printTestResult("TC004_UsernameFieldDisplayed", result);
            assert result : "Username field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify email field is displayed
     */
    public void testEmailFieldDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isEmailFieldDisplayed();
            printTestResult("TC005_EmailFieldDisplayed", result);
            assert result : "Email field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify role dropdown is displayed
     */
    public void testRoleDropdownDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isRoleDropdownDisplayed();
            printTestResult("TC006_RoleDropdownDisplayed", result);
            assert result : "Role dropdown should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify password field is displayed
     */
    public void testPasswordFieldDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isPasswordFieldDisplayed();
            printTestResult("TC007_PasswordFieldDisplayed", result);
            assert result : "Password field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify confirm password field is displayed
     */
    public void testConfirmPasswordFieldDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isConfirmPasswordFieldDisplayed();
            printTestResult("TC008_ConfirmPasswordFieldDisplayed", result);
            assert result : "Confirm password field should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify Create Account button is displayed
     */
    public void testCreateAccountButtonDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isCreateAccountButtonDisplayed();
            printTestResult("TC009_CreateAccountButtonDisplayed", result);
            assert result : "Create Account button should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify Login link is displayed
     */
    public void testLoginLinkDisplayed() {
        beforeTest();
        try {
            boolean result = registerPage.isLoginLinkDisplayed();
            printTestResult("TC010_LoginLinkDisplayed", result);
            assert result : "Login link should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify role dropdown has correct options
     */
    public void testRoleDropdownOptions() {
        beforeTest();
        try {
            String[] roles = registerPage.getAvailableRoles();
            boolean hasClient = false, hasLawyer = false, hasAdmin = false;
            
            for (String role : roles) {
                if (role.equals("client")) hasClient = true;
                if (role.equals("lawyer")) hasLawyer = true;
                if (role.equals("admin")) hasAdmin = true;
            }
            
            boolean result = hasClient && hasLawyer && hasAdmin;
            printTestResult("TC011_RoleDropdownOptions", result);
            assert result : "Role dropdown should have client, lawyer, and admin options";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC012: Verify Login link navigates to Login page
     */
    public void testLoginLinkNavigation() {
        beforeTest();
        try {
            LoginPage loginPage = registerPage.clickLoginLink();
            boolean result = loginPage.isPageDisplayed();
            printTestResult("TC012_LoginLinkNavigation", result);
            assert result : "Login link should navigate to Login page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC013: Verify successful registration with valid data
     */
    public void testSuccessfulRegistration() {
        beforeTest();
        try {
            String username = TestDataGenerator.generateUsername();
            String email = TestDataGenerator.generateEmail();
            String password = TestDataGenerator.getValidTestPassword();
            
            DashboardPage dashboard = registerPage.register(username, email, "client", password);
            boolean result = dashboard.isPageDisplayed();
            
            printTestResult("TC013_SuccessfulRegistration", result);
            assert result : "Successful registration should redirect to dashboard";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC014: Verify registration fails with mismatched passwords
     */
    public void testRegistrationWithMismatchedPasswords() {
        beforeTest();
        try {
            String username = TestDataGenerator.generateUsername();
            String email = TestDataGenerator.generateEmail();
            
            registerPage.registerWithMismatchedPasswords(
                username, 
                email, 
                "client", 
                "Test@1234", 
                "Different@1234"
            );
            
            // Should still be on register page
            boolean result = !registerPage.isRegistrationSuccessful();
            printTestResult("TC014_RegistrationMismatchedPasswords", result);
            assert result : "Registration with mismatched passwords should fail";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC015: Verify registration fails with weak password
     */
    public void testRegistrationWithWeakPassword() {
        beforeTest();
        try {
            String username = TestDataGenerator.generateUsername();
            String email = TestDataGenerator.generateEmail();
            String weakPassword = "weak";
            
            registerPage.enterUsername(username);
            registerPage.enterEmail(email);
            registerPage.selectRole("client");
            registerPage.enterPassword(weakPassword);
            registerPage.enterConfirmPassword(weakPassword);
            
            // Check if password errors are shown
            int errorCount = registerPage.getPasswordErrorCount();
            boolean result = errorCount > 0;
            
            printTestResult("TC015_RegistrationWeakPassword", result);
            assert result : "Weak password should show validation errors";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC016: Verify registration with lawyer role
     */
    public void testRegistrationAsLawyer() {
        beforeTest();
        try {
            String username = TestDataGenerator.generateUsername();
            String email = TestDataGenerator.generateEmail();
            String password = TestDataGenerator.getValidTestPassword();
            
            DashboardPage dashboard = registerPage.register(username, email, "lawyer", password);
            boolean result = dashboard.isPageDisplayed();
            
            printTestResult("TC016_RegistrationAsLawyer", result);
            assert result : "Registration as lawyer should succeed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC017: Verify registration with admin role
     */
    public void testRegistrationAsAdmin() {
        beforeTest();
        try {
            String username = TestDataGenerator.generateUsername();
            String email = TestDataGenerator.generateEmail();
            String password = TestDataGenerator.getValidTestPassword();
            
            DashboardPage dashboard = registerPage.register(username, email, "admin", password);
            boolean result = dashboard.isPageDisplayed();
            
            printTestResult("TC017_RegistrationAsAdmin", result);
            assert result : "Registration as admin should succeed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC018: Verify username placeholder
     */
    public void testUsernamePlaceholder() {
        beforeTest();
        try {
            String placeholder = registerPage.getUsernamePlaceholder();
            boolean result = placeholder != null && !placeholder.isEmpty();
            printTestResult("TC018_UsernamePlaceholder", result);
            assert result : "Username field should have a placeholder";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC019: Verify email placeholder
     */
    public void testEmailPlaceholder() {
        beforeTest();
        try {
            String placeholder = registerPage.getEmailPlaceholder();
            boolean result = placeholder != null && placeholder.contains("@");
            printTestResult("TC019_EmailPlaceholder", result);
            assert result : "Email field should have appropriate placeholder";
        } finally {
            afterTest();
        }
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Run all registration page tests
     */
    public static void main(String[] args) {
        RegisterPageTest test = new RegisterPageTest();
        
        System.out.println("========================================");
        System.out.println("    CONTRACTIQ REGISTER PAGE TESTS");
        System.out.println("========================================\n");
        
        try {
            test.testRegisterPageDisplay();
            test.testPageHeader();
            test.testPageSubtitle();
            test.testUsernameFieldDisplayed();
            test.testEmailFieldDisplayed();
            test.testRoleDropdownDisplayed();
            test.testPasswordFieldDisplayed();
            test.testConfirmPasswordFieldDisplayed();
            test.testCreateAccountButtonDisplayed();
            test.testLoginLinkDisplayed();
            test.testRoleDropdownOptions();
            test.testLoginLinkNavigation();
            test.testSuccessfulRegistration();
            test.testRegistrationWithMismatchedPasswords();
            test.testRegistrationWithWeakPassword();
            test.testRegistrationAsLawyer();
            test.testRegistrationAsAdmin();
            test.testUsernamePlaceholder();
            test.testEmailPlaceholder();
        } catch (Exception e) {
            System.out.println("Test execution error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("    REGISTER PAGE TESTS COMPLETED");
        System.out.println("========================================");
    }
}
