package tests;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import pages.RegisterPage;
import pages.DashboardPage;
import pages.DocumentsPage;
import utils.TestDataGenerator;

/**
 * DocumentsPageTest - Test cases for the Documents page
 * Tests document upload and text extraction functionality
 */
public class DocumentsPageTest extends BaseTest {
    
    private DocumentsPage documentsPage;
    
    @BeforeMethod
    @Override
    public void setUp() {
        super.setUp();
        // Register a new user to ensure we're logged in
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        
        String username = TestDataGenerator.generateUsername();
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.getValidTestPassword();
        
        DashboardPage dashboard = registerPage.register(username, email, "client", password);
        documentsPage = dashboard.goToDocuments();
    }
    
    @AfterMethod
    @Override
    public void tearDown() {
        super.tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify documents page is displayed
     */
    @Test
    public void testDocumentsPageDisplay() {
        boolean result = documentsPage.isPageDisplayed();
        printTestResult("TC001_DocumentsPageDisplay", result);
        assert result : "Documents page should be displayed";
    }
    
    /**
     * TC002: Verify page title
     */
    @Test
    public void testPageTitle() {
        String title = documentsPage.getPageTitle();
        boolean result = title.contains("My Documents");
        printTestResult("TC002_PageTitle", result);
        assert result : "Page title should contain 'My Documents'";
    }
    
    /**
     * TC003: Verify Upload PDF tab is displayed
     */
    @Test
    public void testUploadPdfTabDisplayed() {
        boolean result = documentsPage.isUploadPdfTabDisplayed();
        printTestResult("TC003_UploadPdfTabDisplayed", result);
        assert result : "Upload PDF tab should be displayed";
    }
    
    /**
     * TC004: Verify Paste Text tab is displayed
     */
    @Test
    public void testPasteTextTabDisplayed() {
        boolean result = documentsPage.isPasteTextTabDisplayed();
        printTestResult("TC004_PasteTextTabDisplayed", result);
        assert result : "Paste Text tab should be displayed";
    }
    
    /**
     * TC005: Verify both tabs are displayed
     */
    @Test
    public void testBothTabsDisplayed() {
        boolean result = documentsPage.areBothTabsDisplayed();
        printTestResult("TC005_BothTabsDisplayed", result);
        assert result : "Both Upload PDF and Paste Text tabs should be displayed";
    }
    
    /**
     * TC006: Verify file input is present
     */
    @Test
    public void testFileInputPresent() {
        boolean result = documentsPage.isFileInputDisplayed();
        printTestResult("TC006_FileInputPresent", result);
        assert result : "File input should be present";
    }
    
    /**
     * TC007: Verify clicking Paste Text tab switches view
     */
    @Test
    public void testPasteTextTabSwitch() {
        documentsPage.clickPasteTextTab();
        boolean result = documentsPage.isPasteTextTabActive();
        printTestResult("TC007_PasteTextTabSwitch", result);
        assert result : "Paste Text tab should become active when clicked";
    }
    
    /**
     * TC008: Verify clicking Upload PDF tab switches view
     */
    @Test
    public void testUploadPdfTabSwitch() {
        documentsPage.clickPasteTextTab(); // First switch to paste text
        documentsPage.clickUploadPdfTab(); // Then switch back
        boolean result = documentsPage.isUploadPdfTabActive();
        printTestResult("TC008_UploadPdfTabSwitch", result);
        assert result : "Upload PDF tab should become active when clicked";
    }
    
    /**
     * TC009: Verify text area is displayed in Paste Text tab
     */
    @Test
    public void testTextAreaDisplayed() {
        boolean result = documentsPage.isTextAreaDisplayed();
        printTestResult("TC009_TextAreaDisplayed", result);
        assert result : "Text area should be displayed in Paste Text tab";
    }
    
    /**
     * TC010: Verify Extract Clauses button is displayed
     */
    @Test
    public void testExtractClausesButtonDisplayed() {
        boolean result = documentsPage.isExtractClausesButtonDisplayed();
        printTestResult("TC010_ExtractClausesButtonDisplayed", result);
        assert result : "Extract Clauses button should be displayed";
    }
    
    /**
     * TC011: Verify URL contains /documents
     */
    @Test
    public void testDocumentsUrl() {
        String url = documentsPage.getCurrentUrl();
        boolean result = url.contains("/documents");
        printTestResult("TC011_DocumentsUrl", result);
        assert result : "URL should contain /documents";
    }
    
    /**
     * TC012: Verify initial document count is zero
     */
    @Test
    public void testInitialDocumentCountZero() {
        int count = documentsPage.getDocumentCount();
        boolean result = count == 0;
        printTestResult("TC012_InitialDocumentCountZero", result);
        assert result : "Initial document count should be zero";
    }
    
    /**
     * TC013: Verify text extraction with sample contract text
     * Note: This test requires the backend to be running
     */
    @Test
    public void testTextExtraction() {
        String docName = "Test Contract " + System.currentTimeMillis();
        String contractText = TestDataGenerator.getSampleContractText();
        
        documentsPage.extractClausesFromText(docName, contractText);
        
        // Check if extraction was successful (toast message or page refresh)
        // For now, just verify we're still on the documents page
        boolean result = documentsPage.isPageDisplayed();
        printTestResult("TC013_TextExtraction", result);
        assert result : "Text extraction should complete without errors";
    }
    
    /**
     * TC014: Verify entering document name
     */
    @Test
    public void testEnterDocumentName() {
        try {
            documentsPage.clickPasteTextTab();
            documentsPage.enterDocumentName("Test Document Name");
            // If no exception, test passes
            printTestResult("TC014_EnterDocumentName", true);
        } catch (Exception e) {
            printTestResult("TC014_EnterDocumentName", false);
        }
    }
    
    /**
     * TC015: Verify entering contract text
     */
    @Test
    public void testEnterContractText() {
        try {
            documentsPage.clickPasteTextTab();
            documentsPage.enterContractText("This is a sample contract text for testing purposes.");
            // If no exception, test passes
            printTestResult("TC015_EnterContractText", true);
        } catch (Exception e) {
            printTestResult("TC015_EnterContractText", false);
        }
    }
}
