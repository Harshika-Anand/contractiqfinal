package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * DocumentComparisonPage - Page Object for the Document Comparison feature
 * URL: http://localhost:5173/compare
 */
public class DocumentComparisonPage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Page elements
    private By pageHeader = By.xpath("//h1[contains(text(), 'Compare Contracts')]");
    private By pageSubtitle = By.xpath("//p[contains(text(), 'Select two documents')]");
    
    // Document selection dropdowns
    private By document1Dropdown = By.xpath("//label[contains(text(), 'Document 1')]/following-sibling::select");
    private By document2Dropdown = By.xpath("//label[contains(text(), 'Document 2')]/following-sibling::select");
    
    // Buttons
    private By compareButton = By.xpath("//button[contains(text(), 'Compare Documents')]");
    private By compareAgainButton = By.xpath("//button[contains(text(), 'Compare Again')]");
    private By backButton = By.xpath("//button[contains(text(), 'Back to Documents')]");
    
    // Filter buttons
    private By filterAllBtn = By.xpath("//button[contains(text(), 'All')]");
    private By filterMatchingBtn = By.xpath("//button[contains(text(), 'Matching')]");
    private By filterDifferentBtn = By.xpath("//button[contains(text(), 'Different')]");
    private By filterMissingBtn = By.xpath("//button[contains(text(), 'Missing')]");
    
    // Results sections
    private By summarySection = By.xpath("//h2[contains(text(), 'Comparison Summary')]");
    private By riskFlagsSection = By.xpath("//h2[contains(text(), 'Risk Flags')]");
    private By differencesSection = By.xpath("//h2[contains(text(), 'Clause Differences')]");
    private By similarityValue = By.xpath("//div[contains(text(), 'Overall Similarity')]/following-sibling::div");
    
    // ==================== CONSTRUCTOR ====================
    
    public DocumentComparisonPage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE VERIFICATION ====================
    
    /**
     * Check if comparison page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            return isElementDisplayed(pageHeader) && 
                   isElementDisplayed(document1Dropdown) && 
                   isElementDisplayed(document2Dropdown) && 
                   isElementDisplayed(compareButton);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if page header is displayed
     */
    public boolean isPageHeaderDisplayed() {
        return isElementDisplayed(pageHeader);
    }
    
    public String getPageHeader() {
        return getText(pageHeader);
    }
    
    /**
     * Check if document selection area is displayed
     */
    public boolean isDocumentSelectionDisplayed() {
        return isElementDisplayed(document1Dropdown) && isElementDisplayed(document2Dropdown);
    }
    
    // ==================== DOCUMENT SELECTION ====================
    
    /**
     * Select a document from dropdown 1
     */
    public void selectDocument1(String documentName) {
        click(document1Dropdown);
        pause(300);
        By option = By.xpath("//select[preceding-sibling::label[contains(text(), 'Document 1')]]/option[contains(text(), '" + documentName + "')]");
        click(option);
        pause(300);
    }
    
    /**
     * Select a document from dropdown 2
     */
    public void selectDocument2(String documentName) {
        click(document2Dropdown);
        pause(300);
        By option = By.xpath("//select[preceding-sibling::label[contains(text(), 'Document 2')]]/option[contains(text(), '" + documentName + "')]");
        click(option);
        pause(300);
    }
    
    /**
     * Get selected document 1
     */
    public String getSelectedDocument1() {
        try {
            return driver.findElement(document1Dropdown).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Get selected document 2
     */
    public String getSelectedDocument2() {
        try {
            return driver.findElement(document2Dropdown).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
    
    // ==================== COMPARISON ACTIONS ====================
    
    /**
     * Check if Compare button is displayed
     */
    public boolean isCompareButtonDisplayed() {
        return isElementDisplayed(compareButton);
    }
    
    /**
     * Check if Compare button is enabled
     */
    public boolean isCompareButtonEnabled() {
        try {
            String disabled = driver.findElement(compareButton).getAttribute("disabled");
            return disabled == null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Click Compare button
     */
    public void clickCompare() {
        click(compareButton);
        pause(3000);  // Wait for comparison to complete
    }
    
    /**
     * Perform complete comparison and wait for results
     */
    public void compareDocuments(String doc1, String doc2) {
        System.out.println("  Selecting Document 1: " + doc1);
        selectDocument1(doc1);
        System.out.println("  Selecting Document 2: " + doc2);
        selectDocument2(doc2);
        System.out.println("  Clicking Compare button...");
        clickCompare();
        System.out.println("  Waiting for comparison results...");
        pause(2000);
    }
    
    // ==================== RESULTS VERIFICATION ====================
    
    /**
     * Check if summary section is displayed
     */
    public boolean isSummarySectionDisplayed() {
        return isElementDisplayed(summarySection);
    }
    
    /**
     * Check if risk flags section is displayed
     */
    public boolean isRiskFlagsSectionDisplayed() {
        return isElementDisplayed(riskFlagsSection);
    }
    
    /**
     * Check if differences section is displayed
     */
    public boolean isDifferencesSectionDisplayed() {
        return isElementDisplayed(differencesSection);
    }
    
    /**
     * Get overall similarity percentage
     */
    public String getOverallSimilarity() {
        try {
            return getText(similarityValue).replace("%", "").trim();
        } catch (Exception e) {
            return "0";
        }
    }
    
    /**
     * Get overall similarity as integer
     */
    public int getOverallSimilarityAsInt() {
        try {
            String similarity = getOverallSimilarity();
            return Integer.parseInt(similarity);
        } catch (Exception e) {
            return 0;
        }
    }
    
    // ==================== FILTER ACTIONS ====================
    
    /**
     * Check if filter buttons are displayed
     */
    public boolean areFilterButtonsDisplayed() {
        return isElementDisplayed(filterAllBtn) && 
               isElementDisplayed(filterMatchingBtn) && 
               isElementDisplayed(filterDifferentBtn) && 
               isElementDisplayed(filterMissingBtn);
    }
    
    /**
     * Click filter button
     */
    public void clickFilterAll() {
        click(filterAllBtn);
        pause(500);
    }
    
    public void clickFilterMatching() {
        click(filterMatchingBtn);
        pause(500);
    }
    
    public void clickFilterDifferent() {
        click(filterDifferentBtn);
        pause(500);
    }
    
    public void clickFilterMissing() {
        click(filterMissingBtn);
        pause(500);
    }
    
    /**
     * Check if specific filter is active
     */
    public boolean isFilterActive(String filterName) {
        try {
            By filterBtn = By.xpath("//button[contains(text(), '" + filterName + "')]");
            String classes = driver.findElement(filterBtn).getAttribute("class");
            return classes.contains("bg-indigo-600") || classes.contains("bg-green-600") || 
                   classes.contains("bg-yellow-600") || classes.contains("bg-red-600");
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== NAVIGATION ====================
    
    /**
     * Check if Compare Again button is displayed
     */
    public boolean isCompareAgainButtonDisplayed() {
        return isElementDisplayed(compareAgainButton);
    }
    
    /**
     * Check if Back button is displayed
     */
    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton);
    }
    
    /**
     * Click Compare Again button
     */
    public void clickCompareAgain() {
        click(compareAgainButton);
        pause(1000);
    }
    
    /**
     * Click Back to Documents button
     */
    public void clickBackButton() {
        click(backButton);
        pause(2000);
    }
    
    // ==================== DATA EXTRACTION ====================
    
    /**
     * Get number of risk flags
     */
    public int getRiskFlagCount() {
        try {
            By riskFlags = By.xpath("//h2[contains(text(), 'Risk Flags')]/following-sibling::div//div[contains(@class, 'p-4')]");
            return driver.findElements(riskFlags).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get number of clause differences
     */
    public int getDifferenceCount() {
        try {
            By differences = By.xpath("//h2[contains(text(), 'Clause Differences')]/following-sibling::div//div[contains(@class, 'border-gray-200')]");
            return driver.findElements(differences).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Get matching categories count
     */
    public int getMatchingCategoriesCount() {
        try {
            By matching = By.xpath("//div[contains(@class, 'bg-green-50')]");
            return driver.findElements(matching).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
