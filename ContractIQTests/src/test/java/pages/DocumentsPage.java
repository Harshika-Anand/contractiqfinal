package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;

/**
 * DocumentsPage - Page Object for the Documents page
 * URL: http://localhost:5173/documents
 * Note: This page requires user to be logged in
 */
public class DocumentsPage extends BasePage {
    
    // ==================== LOCATORS ====================
    
    // Page elements
    private By pageTitle = By.xpath("//h1[contains(text(), 'My Documents')]");
    private By pageSubtitle = By.xpath("//p[contains(text(), 'Upload PDFs or paste text')]");
    
    // Tabs
    private By uploadPdfTab = By.xpath("//button[contains(text(), 'Upload PDF')]");
    private By pasteTextTab = By.xpath("//button[contains(text(), 'Paste Text')]");
    
    // Upload PDF section
    private By uploadSectionTitle = By.xpath("//h2[contains(text(), 'Upload PDF Document')]");
    private By fileInput = By.cssSelector("input[type='file']");
    private By uploadArea = By.xpath("//div[contains(text(), 'Click to upload') or contains(@class, 'border-dashed')]");
    private By uploadProgress = By.xpath("//div[contains(@class, 'progress')]");
    
    // Paste Text section
    private By textSectionTitle = By.xpath("//h2[contains(text(), 'Paste Contract Text')]");
    private By documentNameInput = By.xpath("//input[@placeholder='e.g., Service Agreement v2.1']");
    private By contractTextArea = By.xpath("//textarea[contains(@placeholder, 'Paste contract text here')]");
    private By extractClausesButton = By.xpath("//button[contains(text(), 'Extract Clauses')]");
    
    // Document list
    private By documentsList = By.xpath("//div[contains(@class, 'Your Documents')]");
    private By documentItems = By.xpath("//div[contains(@class, 'p-4') and contains(@class, 'bg-gray-50')]");
    private By noDocumentsMessage = By.xpath("//p[contains(text(), 'No documents uploaded')]");
    
    // Document actions
    private By viewButtons = By.linkText("View");
    private By deleteButtons = By.xpath("//button[contains(text(), 'Delete')]");
    
    // ==================== CONSTRUCTOR ====================
    
    public DocumentsPage(WebDriver driver) {
        super(driver);
    }
    
    // ==================== PAGE ACTIONS ====================
    
    /**
     * Check if documents page is displayed
     */
    public boolean isPageDisplayed() {
        try {
            waitForLoadingComplete();
            return isElementDisplayed(pageTitle) || getCurrentUrl().contains("/documents");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get page title text
     */
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    /**
     * Click Upload PDF tab
     */
    public void clickUploadPdfTab() {
        click(uploadPdfTab);
        pause(500);
    }
    
    /**
     * Click Paste Text tab
     */
    public void clickPasteTextTab() {
        click(pasteTextTab);
        pause(500);
    }
    
    /**
     * Check if Upload PDF tab is active
     */
    public boolean isUploadPdfTabActive() {
        try {
            String classes = driver.findElement(uploadPdfTab).getAttribute("class");
            return classes.contains("indigo") || classes.contains("active");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if Paste Text tab is active
     */
    public boolean isPasteTextTabActive() {
        try {
            String classes = driver.findElement(pasteTextTab).getAttribute("class");
            return classes.contains("indigo") || classes.contains("active");
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Upload a PDF file
     */
    public void uploadPdfFile(String filePath) {
        clickUploadPdfTab();
        WebElement input = driver.findElement(fileInput);
        input.sendKeys(filePath);
        pause(3000); // Wait for upload to process
    }
    
    /**
     * Upload a PDF file using File object
     */
    public void uploadPdfFile(File file) {
        uploadPdfFile(file.getAbsolutePath());
    }
    
    /**
     * Enter document name (for text extraction)
     */
    public void enterDocumentName(String name) {
        clickPasteTextTab();
        pause(500);
        enterText(documentNameInput, name);
    }
    
    /**
     * Enter contract text (for text extraction)
     */
    public void enterContractText(String text) {
        enterText(contractTextArea, text);
    }
    
    /**
     * Click Extract Clauses button
     */
    public void clickExtractClauses() {
        click(extractClausesButton);
        pause(3000); // Wait for extraction to process
    }
    
    /**
     * Perform complete text extraction
     */
    public void extractClausesFromText(String documentName, String contractText) {
        clickPasteTextTab();
        pause(500);
        enterDocumentName(documentName);
        enterContractText(contractText);
        clickExtractClauses();
    }
    
    /**
     * Check if file input is displayed
     */
    public boolean isFileInputDisplayed() {
        try {
            return driver.findElement(fileInput) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Check if text area is displayed
     */
    public boolean isTextAreaDisplayed() {
        clickPasteTextTab();
        return isElementDisplayed(contractTextArea);
    }
    
    /**
     * Check if Extract Clauses button is displayed
     */
    public boolean isExtractClausesButtonDisplayed() {
        clickPasteTextTab();
        return isElementDisplayed(extractClausesButton);
    }
    
    /**
     * Get number of documents in the list
     */
    public int getDocumentCount() {
        try {
            return driver.findElements(documentItems).size();
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Check if "No documents" message is displayed
     */
    public boolean isNoDocumentsMessageDisplayed() {
        return isElementDisplayed(noDocumentsMessage);
    }
    
    /**
     * Click View button on first document
     */
    public void clickViewFirstDocument() {
        driver.findElements(viewButtons).get(0).click();
    }
    
    /**
     * Click Delete button on first document
     */
    public void clickDeleteFirstDocument() {
        driver.findElements(deleteButtons).get(0).click();
        pause(1000);
    }
    
    /**
     * Check if Upload PDF tab is displayed
     */
    public boolean isUploadPdfTabDisplayed() {
        return isElementDisplayed(uploadPdfTab);
    }
    
    /**
     * Check if Paste Text tab is displayed
     */
    public boolean isPasteTextTabDisplayed() {
        return isElementDisplayed(pasteTextTab);
    }
    
    /**
     * Check if both tabs are displayed
     */
    public boolean areBothTabsDisplayed() {
        return isUploadPdfTabDisplayed() && isPasteTextTabDisplayed();
    }
}
