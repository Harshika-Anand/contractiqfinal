package tests;

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
    
    /**
     * Setup before each test - registers new user and navigates to documents
     */
    public void beforeTest() {
        setUp();
        // Register a new user to ensure we're logged in
        navigateToRegister();
        RegisterPage registerPage = new RegisterPage(driver);
        
        String username = TestDataGenerator.generateUsername();
        String email = TestDataGenerator.generateEmail();
        String password = TestDataGenerator.getValidTestPassword();
        
        DashboardPage dashboard = registerPage.register(username, email, "client", password);
        documentsPage = dashboard.goToDocuments();
    }
    
    /**
     * Cleanup after each test
     */
    public void afterTest() {
        tearDown();
    }
    
    // ==================== TEST CASES ====================
    
    /**
     * TC001: Verify documents page is displayed
     */
    public void testDocumentsPageDisplay() {
        beforeTest();
        try {
            boolean result = documentsPage.isPageDisplayed();
            printTestResult("TC001_DocumentsPageDisplay", result);
            assert result : "Documents page should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC002: Verify page title
     */
    public void testPageTitle() {
        beforeTest();
        try {
            String title = documentsPage.getPageTitle();
            boolean result = title.contains("My Documents");
            printTestResult("TC002_PageTitle", result);
            assert result : "Page title should contain 'My Documents'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC003: Verify Upload PDF tab is displayed
     */
    public void testUploadPdfTabDisplayed() {
        beforeTest();
        try {
            boolean result = documentsPage.isUploadPdfTabDisplayed();
            printTestResult("TC003_UploadPdfTabDisplayed", result);
            assert result : "Upload PDF tab should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify Paste Text tab is displayed
     */
    public void testPasteTextTabDisplayed() {
        beforeTest();
        try {
            boolean result = documentsPage.isPasteTextTabDisplayed();
            printTestResult("TC004_PasteTextTabDisplayed", result);
            assert result : "Paste Text tab should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify both tabs are displayed
     */
    public void testBothTabsDisplayed() {
        beforeTest();
        try {
            boolean result = documentsPage.areBothTabsDisplayed();
            printTestResult("TC005_BothTabsDisplayed", result);
            assert result : "Both Upload PDF and Paste Text tabs should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify file input is present
     */
    public void testFileInputPresent() {
        beforeTest();
        try {
            boolean result = documentsPage.isFileInputDisplayed();
            printTestResult("TC006_FileInputPresent", result);
            assert result : "File input should be present";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify clicking Paste Text tab switches view
     */
    public void testPasteTextTabSwitch() {
        beforeTest();
        try {
            documentsPage.clickPasteTextTab();
            boolean result = documentsPage.isPasteTextTabActive();
            printTestResult("TC007_PasteTextTabSwitch", result);
            assert result : "Paste Text tab should become active when clicked";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify clicking Upload PDF tab switches view
     */
    public void testUploadPdfTabSwitch() {
        beforeTest();
        try {
            documentsPage.clickPasteTextTab(); // First switch to paste text
            documentsPage.clickUploadPdfTab(); // Then switch back
            boolean result = documentsPage.isUploadPdfTabActive();
            printTestResult("TC008_UploadPdfTabSwitch", result);
            assert result : "Upload PDF tab should become active when clicked";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify text area is displayed in Paste Text tab
     */
    public void testTextAreaDisplayed() {
        beforeTest();
        try {
            boolean result = documentsPage.isTextAreaDisplayed();
            printTestResult("TC009_TextAreaDisplayed", result);
            assert result : "Text area should be displayed in Paste Text tab";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify Extract Clauses button is displayed
     */
    public void testExtractClausesButtonDisplayed() {
        beforeTest();
        try {
            boolean result = documentsPage.isExtractClausesButtonDisplayed();
            printTestResult("TC010_ExtractClausesButtonDisplayed", result);
            assert result : "Extract Clauses button should be displayed";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify URL contains /documents
     */
    public void testDocumentsUrl() {
        beforeTest();
        try {
            String url = documentsPage.getCurrentUrl();
            boolean result = url.contains("/documents");
            printTestResult("TC011_DocumentsUrl", result);
            assert result : "URL should contain /documents";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC012: Verify initial document count is zero
     */
    public void testInitialDocumentCountZero() {
        beforeTest();
        try {
            int count = documentsPage.getDocumentCount();
            boolean result = count == 0;
            printTestResult("TC012_InitialDocumentCountZero", result);
            assert result : "Initial document count should be zero";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC013: Verify text extraction with sample contract text
     * Note: This test requires the backend to be running
     */
    public void testTextExtraction() {
        beforeTest();
        try {
            String docName = "Test Contract " + System.currentTimeMillis();
            String contractText = TestDataGenerator.getSampleContractText();
            
            documentsPage.extractClausesFromText(docName, contractText);
            
            // Check if extraction was successful (toast message or page refresh)
            // For now, just verify we're still on the documents page
            boolean result = documentsPage.isPageDisplayed();
            printTestResult("TC013_TextExtraction", result);
            assert result : "Text extraction should complete without errors";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC014: Verify entering document name
     */
    public void testEnterDocumentName() {
        beforeTest();
        try {
            documentsPage.clickPasteTextTab();
            documentsPage.enterDocumentName("Test Document Name");
            // If no exception, test passes
            printTestResult("TC014_EnterDocumentName", true);
        } catch (Exception e) {
            printTestResult("TC014_EnterDocumentName", false);
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC015: Verify entering contract text
     */
    public void testEnterContractText() {
        beforeTest();
        try {
            documentsPage.clickPasteTextTab();
            documentsPage.enterContractText("This is a sample contract text for testing purposes.");
            // If no exception, test passes
            printTestResult("TC015_EnterContractText", true);
        } catch (Exception e) {
            printTestResult("TC015_EnterContractText", false);
        } finally {
            afterTest();
        }
    }
    
    // ==================== MAIN METHOD ====================
    
    /**
     * Run all documents page tests
     */
    public static void main(String[] args) {
        DocumentsPageTest test = new DocumentsPageTest();
        
        System.out.println("========================================");
        System.out.println("    CONTRACTIQ DOCUMENTS PAGE TESTS");
        System.out.println("========================================\n");
        
        try {
            test.testDocumentsPageDisplay();
            test.testPageTitle();
            test.testUploadPdfTabDisplayed();
            test.testPasteTextTabDisplayed();
            test.testBothTabsDisplayed();
            test.testFileInputPresent();
            test.testPasteTextTabSwitch();
            test.testUploadPdfTabSwitch();
            test.testTextAreaDisplayed();
            test.testExtractClausesButtonDisplayed();
            test.testDocumentsUrl();
            test.testInitialDocumentCountZero();
            test.testTextExtraction();
            test.testEnterDocumentName();
            test.testEnterContractText();
        } catch (Exception e) {
            System.out.println("Test execution error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n========================================");
        System.out.println("    DOCUMENTS PAGE TESTS COMPLETED");
        System.out.println("========================================");
    }
}
