package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage - Page Object for the Home/Landing page
 * URL: http://localhost:5173/
 */
public class HomePage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Header/Title
    private By pageTitle = By.xpath("//h1[contains(text(), 'Smart Contract Analysis')]");
    private By subtitle = By.xpath("//span[contains(text(), 'Made Simple')]");
    
    // Buttons
    private By getStartedButton = By.linkText("Get Started Free");
    private By signInButton = By.linkText("Sign In");
    
    // Feature cards
    private By smartPdfCard = By.xpath("//h3[contains(text(), 'Smart PDF Analysis')]");
    private By clauseDetectionCard = By.xpath("//h3[contains(text(), 'Clause Detection')]");
    private By dashboardAnalyticsCard = By.xpath("//h3[contains(text(), 'Dashboard Analytics')]");
    
    // Description
    private By pageDescription = By.xpath("//p[contains(text(), 'Upload PDF contracts')]");
    
    // ==================== CONSTRUCTOR ====================
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Check if home page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            return isElementDisplayed(pageTitle) && isElementDisplayed(getStartedButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get main title text
     */
    public String getMainTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Get subtitle text
     */
    public String getSubtitle() {
        return getText(subtitle);
    }
    
    /**
     * Click Get Started Free button
     */
    public RegisterPage clickGetStarted() {
        click(getStartedButton);
        return new RegisterPage(driver);
    }
    
    /**
     * Click Sign In button
     */
    public LoginPage clickSignIn() {
        click(signInButton);
        return new LoginPage(driver);
    }
    
    /**
     * Check if Smart PDF Analysis feature card is displayed
     */
    public boolean isSmartPdfCardDisplayed() {
        return isElementDisplayed(smartPdfCard);
    }
    
    /**
     * Check if Clause Detection feature card is displayed
     */
    public boolean isClauseDetectionCardDisplayed() {
        return isElementDisplayed(clauseDetectionCard);
    }
    
    /**
     * Check if Dashboard Analytics feature card is displayed
     */
    public boolean isDashboardAnalyticsCardDisplayed() {
        return isElementDisplayed(dashboardAnalyticsCard);
    }
    
    /**
     * Check if all feature cards are displayed
     */
    public boolean areAllFeaturesDisplayed() {
        return isSmartPdfCardDisplayed() && 
               isClauseDetectionCardDisplayed() && 
               isDashboardAnalyticsCardDisplayed();
    }
    
    /**
     * Check if Get Started button is displayed
     */
    public boolean isGetStartedButtonDisplayed() {
        return isElementDisplayed(getStartedButton);
    }
    
    /**
     * Check if Sign In button is displayed
     */
    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }
    
    /**
     * Get page description text
     */
    public String getPageDescription() {
        return getText(pageDescription);
    }
}
