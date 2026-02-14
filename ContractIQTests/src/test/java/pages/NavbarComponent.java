package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * NavbarComponent - Page Object for the Navigation bar
 * This is present on all pages when user is logged in
 */
public class NavbarComponent extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Logo/Brand
    private By logo = By.xpath("//a[contains(text(), 'ContractIQ')]");
    
    // Navigation links
    private By dashboardLink = By.linkText("Dashboard");
    private By documentsLink = By.linkText("Documents");
    
    // User menu
    private By userMenuButton = By.xpath("//button[contains(@class, 'flex') and contains(@class, 'items-center')]");
    private By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");
    private By profileLink = By.xpath("//a[contains(text(), 'Profile')]");
    
    // ==================== CONSTRUCTOR ====================
    
    public NavbarComponent(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Click on logo to go to home/dashboard
     */
    public void clickLogo() {
        click(logo);
    }
    
    /**
     * Navigate to Dashboard
     */
    public DashboardPage goToDashboard() {
        click(dashboardLink);
        return new DashboardPage(driver);
    }
    
    /**
     * Navigate to Documents
     */
    public DocumentsPage goToDocuments() {
        click(documentsLink);
        return new DocumentsPage(driver);
    }
    
    /**
     * Click logout button
     */
    public HomePage logout() {
        try {
            // Try direct logout button first
            if (isElementDisplayed(logoutButton)) {
                click(logoutButton);
            } else {
                // May need to open user menu first
                click(userMenuButton);
                pause(500);
                click(logoutButton);
            }
        } catch (Exception e) {
            // Try finding logout button by different method
            click(By.xpath("//*[contains(text(), 'Logout') or contains(text(), 'Log out')]"));
        }
        pause(1000);
        return new HomePage(driver);
    }
    
    /**
     * Check if Dashboard link is displayed
     */
    public boolean isDashboardLinkDisplayed() {
        return isElementDisplayed(dashboardLink);
    }
    
    /**
     * Check if Documents link is displayed
     */
    public boolean isDocumentsLinkDisplayed() {
        return isElementDisplayed(documentsLink);
    }
    
    /**
     * Check if user is logged in (navbar shows Dashboard link)
     */
    public boolean isUserLoggedIn() {
        return isDashboardLinkDisplayed();
    }
    
    /**
     * Check if Logout button is displayed
     */
    public boolean isLogoutButtonDisplayed() {
        return isElementDisplayed(logoutButton);
    }
    
    /**
     * Check if logo is displayed
     */
    public boolean isLogoDisplayed() {
        return isElementDisplayed(logo);
    }
}
