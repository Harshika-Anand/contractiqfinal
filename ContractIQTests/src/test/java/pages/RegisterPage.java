package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * RegisterPage - Page Object for the Registration page
 * URL: http://localhost:5173/register
 */
public class RegisterPage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Page elements
    private By pageHeader = By.xpath("//h1[contains(text(), 'ContractIQ')]");
    private By pageSubtitle = By.xpath("//p[contains(text(), 'Create your account')]");
    
    // Form fields
    private By usernameInput = By.name("username");
    private By emailInput = By.name("email");
    private By roleSelect = By.name("role");
    private By passwordInput = By.name("password");
    private By confirmPasswordInput = By.name("confirmPassword");
    
    // Labels
    private By usernameLabel = By.xpath("//label[contains(text(), 'Username')]");
    private By emailLabel = By.xpath("//label[contains(text(), 'Email')]");
    private By roleLabel = By.xpath("//label[contains(text(), 'Role')]");
    private By passwordLabel = By.xpath("//label[contains(text(), 'Password')]");
    
    // Buttons
    private By createAccountButton = By.xpath("//button[contains(text(), 'Create Account') or contains(text(), 'Creating')]");
    
    // Links - The actual link text on the page is "Sign in here"
    private By loginLink = By.linkText("Sign in here");
    
    // Password validation errors (shown in real-time)
    private By passwordErrors = By.xpath("//ul[@class='text-xs text-red-500']//li");
    
    // ==================== CONSTRUCTOR ====================
    
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Check if registration page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            return isElementDisplayed(usernameInput) && 
                   isElementDisplayed(emailInput) && 
                   isElementDisplayed(passwordInput);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Enter username
     */
    public void enterUsername(String username) {
        enterText(usernameInput, username);
    }
    
    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        enterText(emailInput, email);
    }
    
    /**
     * Select role from dropdown
     */
    public void selectRole(String role) {
        Select select = new Select(driver.findElement(roleSelect));
        select.selectByValue(role.toLowerCase());
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        enterText(passwordInput, password);
    }
    
    /**
     * Enter confirm password
     */
    public void enterConfirmPassword(String password) {
        enterText(confirmPasswordInput, password);
    }
    
    /**
     * Click Create Account button
     */
    public void clickCreateAccount() {
        click(createAccountButton);
    }
    
    /**
     * Perform complete registration
     */
    public DashboardPage register(String username, String email, String role, String password) {
        System.out.println("  Registering new user...");
        System.out.println("  Entering username: " + username);
        enterUsername(username);
        System.out.println("  Entering email: " + email);
        enterEmail(email);
        System.out.println("  Selecting role: " + role);
        selectRole(role);
        System.out.println("  Entering password...");
        enterPassword(password);
        System.out.println("  Confirming password...");
        enterConfirmPassword(password);
        System.out.println("  Clicking Create Account button...");
        clickCreateAccount();
        System.out.println("  Waiting for registration to process...");
        pause(3000); // Wait for registration to process
        return new DashboardPage(driver);
    }
    
    /**
     * Perform registration with different confirm password
     */
    public void registerWithMismatchedPasswords(String username, String email, String role, 
                                                 String password, String confirmPassword) {
        System.out.println("  Attempting registration with mismatched passwords...");
        enterUsername(username);
        enterEmail(email);
        selectRole(role);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickCreateAccount();
        pause(3000);
    }
    
    /**
     * Click Login link
     */
    public LoginPage clickLoginLink() {
        click(loginLink);
        return new LoginPage(driver);
    }
    
    /**
     * Get page header text
     */
    public String getPageHeader() {
        return getText(pageHeader);
    }
    
    /**
     * Get page subtitle text
     */
    public String getPageSubtitle() {
        return getText(pageSubtitle);
    }
    
    /**
     * Check if username field is displayed
     */
    public boolean isUsernameFieldDisplayed() {
        return isElementDisplayed(usernameInput);
    }
    
    /**
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailInput);
    }
    
    /**
     * Check if role dropdown is displayed
     */
    public boolean isRoleDropdownDisplayed() {
        return isElementDisplayed(roleSelect);
    }
    
    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordInput);
    }
    
    /**
     * Check if confirm password field is displayed
     */
    public boolean isConfirmPasswordFieldDisplayed() {
        return isElementDisplayed(confirmPasswordInput);
    }
    
    /**
     * Check if Create Account button is displayed
     */
    public boolean isCreateAccountButtonDisplayed() {
        return isElementDisplayed(createAccountButton);
    }
    
    /**
     * Check if Login link is displayed
     */
    public boolean isLoginLinkDisplayed() {
        return isElementDisplayed(loginLink);
    }
    
    /**
     * Get available roles from dropdown
     */
    public String[] getAvailableRoles() {
        Select select = new Select(driver.findElement(roleSelect));
        return select.getOptions().stream()
                .map(option -> option.getAttribute("value"))
                .toArray(String[]::new);
    }
    
    /**
     * Get username placeholder
     */
    public String getUsernamePlaceholder() {
        return driver.findElement(usernameInput).getAttribute("placeholder");
    }
    
    /**
     * Get email placeholder
     */
    public String getEmailPlaceholder() {
        return driver.findElement(emailInput).getAttribute("placeholder");
    }
    
    /**
     * Check if registration was successful (redirected to dashboard)
     */
    public boolean isRegistrationSuccessful() {
        pause(2000);
        return getCurrentUrl().contains("/dashboard");
    }
    
    /**
     * Get number of password validation errors displayed
     */
    public int getPasswordErrorCount() {
        try {
            return driver.findElements(passwordErrors).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
