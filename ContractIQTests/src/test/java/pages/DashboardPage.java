package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * DashboardPage - Page Object for the Dashboard page
 * URL: http://localhost:5173/dashboard
 * Note: This page requires user to be logged in
 */
public class DashboardPage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Page elements
    private By welcomeMessage = By.xpath("//h1[contains(text(), 'Welcome back')]");
    private By pageSubtitle = By.xpath("//p[contains(text(), 'contract analysis overview')]");
    
    // Statistics cards
    private By totalDocumentsCard = By.xpath("//p[contains(text(), 'Total Documents')]");
    private By totalDocumentsValue = By.xpath("//p[contains(text(), 'Total Documents')]/following-sibling::p");
    
    private By recentUploadsCard = By.xpath("//p[contains(text(), 'Recent Uploads')]");
    private By recentUploadsValue = By.xpath("//p[contains(text(), 'Recent Uploads')]/following-sibling::p");
    
    private By clausesExtractedCard = By.xpath("//p[contains(text(), 'Clauses Extracted')]");
    private By clausesExtractedValue = By.xpath("//p[contains(text(), 'Clauses Extracted')]/following-sibling::p");
    
    // Recent documents section
    private By recentDocumentsTitle = By.xpath("//h2[contains(text(), 'Recent Documents')]");
    private By viewAllLink = By.linkText("View All →");
    
    // Empty state
    private By noDocumentsMessage = By.xpath("//p[contains(text(), 'No documents yet')]");
    private By uploadFirstDocumentButton = By.xpath("//a[contains(text(), 'Upload Your First Document')]");
    
    // Document list items
    private By documentListItems = By.xpath("//div[contains(@class, 'bg-gray-50 rounded-lg')]");
    private By documentViewLinks = By.xpath("//a[contains(text(), 'View')]");
    
    // Navigation
    private By documentsNavLink = By.linkText("Documents");
    private By dashboardNavLink = By.linkText("Dashboard");
    
    // ==================== CONSTRUCTOR ====================
    
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Check if dashboard page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            waitForLoadingComplete();
            return isElementDisplayed(welcomeMessage) || getCurrentUrl().contains("/dashboard");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get welcome message text
     */
    public String getWelcomeMessage() {
        waitForLoadingComplete();
        return getText(welcomeMessage);
    }
    
    /**
     * Get username from welcome message
     */
    public String getLoggedInUsername() {
        String welcome = getWelcomeMessage();
        // Welcome message format: "Welcome back, username!"
        return welcome.replace("Welcome back, ", "").replace("!", "");
    }
    
    /**
     * Check if Total Documents card is displayed
     */
    public boolean isTotalDocumentsCardDisplayed() {
        return isElementDisplayed(totalDocumentsCard);
    }
    
    /**
     * Get Total Documents count
     */
    public int getTotalDocumentsCount() {
        try {
            String value = getText(totalDocumentsValue);
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Check if Recent Uploads card is displayed
     */
    public boolean isRecentUploadsCardDisplayed() {
        return isElementDisplayed(recentUploadsCard);
    }
    
    /**
     * Get Recent Uploads count
     */
    public int getRecentUploadsCount() {
        try {
            String value = getText(recentUploadsValue);
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Check if Clauses Extracted card is displayed
     */
    public boolean isClausesExtractedCardDisplayed() {
        return isElementDisplayed(clausesExtractedCard);
    }
    
    /**
     * Get Clauses Extracted count
     */
    public int getClausesExtractedCount() {
        try {
            String value = getText(clausesExtractedValue);
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Check if all statistics cards are displayed
     */
    public boolean areAllStatsCardsDisplayed() {
        return isTotalDocumentsCardDisplayed() && 
               isRecentUploadsCardDisplayed() && 
               isClausesExtractedCardDisplayed();
    }
    
    /**
     * Check if Recent Documents section is displayed
     */
    public boolean isRecentDocumentsSectionDisplayed() {
        return isElementDisplayed(recentDocumentsTitle);
    }
    
    /**
     * Check if "No documents yet" message is displayed
     */
    public boolean isNoDocumentsMessageDisplayed() {
        return isElementDisplayed(noDocumentsMessage);
    }
    
    /**
     * Click "Upload Your First Document" button
     */
    public DocumentsPage clickUploadFirstDocument() {
        click(uploadFirstDocumentButton);
        return new DocumentsPage(driver);
    }
    
    /**
     * Click "View All" link to go to documents page
     */
    public DocumentsPage clickViewAll() {
        click(viewAllLink);
        return new DocumentsPage(driver);
    }
    
    /**
     * Get number of recent documents displayed
     */
    public int getRecentDocumentsCount() {
        try {
            return driver.findElements(documentListItems).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Click on first document's View link
     */
    public void clickFirstDocumentView() {
        driver.findElements(documentViewLinks).get(0).click();
    }
    
    /**
     * Navigate to Documents page using nav link
     */
    public DocumentsPage goToDocuments() {
        click(documentsNavLink);
        return new DocumentsPage(driver);
    }
    
    /**
     * Check if View All link is displayed
     */
    public boolean isViewAllLinkDisplayed() {
        return isElementDisplayed(viewAllLink);
    }
}
