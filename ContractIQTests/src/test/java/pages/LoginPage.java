package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage - Page Object for the Login page
 * URL: http://localhost:5173/login
 */
public class LoginPage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Page elements
    private By pageHeader = By.xpath("//h1[contains(text(), 'ContractIQ')]");
    private By pageSubtitle = By.xpath("//p[contains(text(), 'Sign in to your account')]");
    
    // Form fields
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    
    // Buttons
    private By signInButton = By.xpath("//button[contains(text(), 'Sign In') or contains(text(), 'Signing in')]");
    
    // Links
    private By registerLink = By.linkText("Register here");
    
    // Labels
    private By emailLabel = By.xpath("//label[@for='email']");
    private By passwordLabel = By.xpath("//label[@for='password']");
    
    // Password hint
    private By passwordHint = By.xpath("//p[contains(text(), 'Must contain')]");
    
    // Error/Success messages (toast)
    private By errorToast = By.xpath("//*[contains(@class, 'toast')]");
    
    // ==================== CONSTRUCTOR ====================
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Check if login page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            return isElementDisplayed(emailInput) && 
                   isElementDisplayed(passwordInput) && 
                   isElementDisplayed(signInButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Enter email address
     */
    public void enterEmail(String email) {
        enterText(emailInput, email);
    }
    
    /**
     * Enter password
     */
    public void enterPassword(String password) {
        enterText(passwordInput, password);
    }
    
    /**
     * Click Sign In button
     */
    public void clickSignIn() {
        click(signInButton);
    }
    
    /**
     * Perform complete login
     */
    public DashboardPage login(String email, String password) {
        System.out.println("  Entering email: " + email);
        enterEmail(email);
        System.out.println("  Entering password...");
        enterPassword(password);
        System.out.println("  Clicking Sign In button...");
        clickSignIn();
        System.out.println("  Waiting for login to process...");
        pause(3000); // Wait for login to process
        return new DashboardPage(driver);
    }
    
    /**
     * Perform login and stay on login page (for invalid login tests)
     */
    public void attemptLogin(String email, String password) {
        System.out.println("  Attempting login with: " + email);
        enterEmail(email);
        enterPassword(password);
        clickSignIn();
        pause(3000);
    }
    
    /**
     * Click Register link
     */
    public RegisterPage clickRegisterLink() {
        click(registerLink);
        return new RegisterPage(driver);
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
     * Check if email field is displayed
     */
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailInput);
    }
    
    /**
     * Check if password field is displayed
     */
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordInput);
    }
    
    /**
     * Check if Sign In button is displayed
     */
    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }
    
    /**
     * Check if Register link is displayed
     */
    public boolean isRegisterLinkDisplayed() {
        return isElementDisplayed(registerLink);
    }
    
    /**
     * Get email field placeholder
     */
    public String getEmailPlaceholder() {
        return driver.findElement(emailInput).getAttribute("placeholder");
    }
    
    /**
     * Get password field placeholder
     */
    public String getPasswordPlaceholder() {
        return driver.findElement(passwordInput).getAttribute("placeholder");
    }
    
    /**
     * Check if password hint is displayed
     */
    public boolean isPasswordHintDisplayed() {
        return isElementDisplayed(passwordHint);
    }
    
    /**
     * Get password hint text
     */
    public String getPasswordHint() {
        return getText(passwordHint);
    }
    
    /**
     * Check if login was successful (redirected to dashboard)
     */
    public boolean isLoginSuccessful() {
        pause(2000);
        return getCurrentUrl().contains("/dashboard");
    }
    
    /**
     * Check if form validation error is shown for email
     */
    public boolean isEmailRequired() {
        return driver.findElement(emailInput).getAttribute("required") != null;
    }
    
    /**
     * Check if form validation error is shown for password
     */
    public boolean isPasswordRequired() {
        return driver.findElement(passwordInput).getAttribute("required") != null;
    }
}
