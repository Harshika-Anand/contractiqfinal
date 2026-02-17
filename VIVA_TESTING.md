# ContractIQ Testing - Detailed Documentation

> **Selenium WebDriver Test Automation with Page Object Model**

---

## 1. Overview

The testing module is a comprehensive Selenium WebDriver test automation project that tests the ContractIQ web application using the **Page Object Model (POM)** design pattern.

### Test Summary

| Test Suite | Test Cases | Description |
|------------|------------|-------------|
| HomePageTest | 11 tests | Landing page functionality |
| LoginPageTest | 16 tests | Login functionality |
| RegisterPageTest | 19 tests | Registration functionality |
| DashboardPageTest | 15 tests | Dashboard after login |
| DocumentsPageTest | 15 tests | Document upload & text extraction |
| **Total** | **76 tests** | Complete application coverage |

---

## 2. Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | JDK 8+ | Programming Language |
| Selenium WebDriver | 4.x | Browser Automation |
| ChromeDriver | Matches Chrome | Chrome Browser Driver |
| Page Object Model | - | Design Pattern |
| Properties Files | - | Configuration Management |

---

## 3. Project Structure

```
ContractIQTests/
├── src/test/java/
│   │
│   ├── pages/                      # PAGE OBJECT MODEL CLASSES
│   │   ├── BasePage.java           # Parent class with common methods
│   │   ├── HomePage.java           # Home/Landing page
│   │   ├── LoginPage.java          # Login page
│   │   ├── RegisterPage.java       # Registration page
│   │   ├── DashboardPage.java      # Dashboard page
│   │   ├── DocumentsPage.java      # Documents page
│   │   └── NavbarComponent.java    # Navigation bar component
│   │
│   ├── tests/                      # TEST CLASSES
│   │   ├── BaseTest.java           # Browser setup/teardown
│   │   ├── HomePageTest.java       # Home page tests (11)
│   │   ├── LoginPageTest.java      # Login tests (16)
│   │   ├── RegisterPageTest.java   # Registration tests (19)
│   │   ├── DashboardPageTest.java  # Dashboard tests (15)
│   │   ├── DocumentsPageTest.java  # Documents tests (15)
│   │   └── TestRunner.java         # Main test runner
│   │
│   └── utils/                      # UTILITY CLASSES
│       ├── ConfigReader.java       # Read config.properties
│       ├── WaitHelper.java         # Explicit wait utilities
│       ├── TestDataGenerator.java  # Generate test data
│       └── ScreenshotHelper.java   # Screenshot capture
│
├── config.properties               # Test configuration
├── drivers/
│   └── chromedriver.exe            # Chrome WebDriver
├── lib/                            # Selenium JAR files
├── testdata/
│   └── testdata.json               # Test data
└── reports/
    └── screenshots/                # Test screenshots
```

---

## 4. Configuration (config.properties)

```properties
# ContractIQ Test Automation Configuration

# Application URL (Frontend)
base.url=http://localhost:5173

# Browser to use (chrome, firefox, edge)
browser=chrome

# Implicit wait timeout in seconds
implicit.wait=10

# Explicit wait timeout in seconds
explicit.wait=15

# Run tests in headless mode (true/false)
headless=false

# Backend API URL
api.url=http://localhost:5000

# Test user credentials (for tests that need existing users)
test.user.email=testuser@test.com
test.user.password=Test@1234

# Screenshot settings
screenshot.on.failure=true
screenshot.directory=reports/screenshots

# Report settings
report.directory=reports
```

### ConfigReader Utility

```java
package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties;
    
    static {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream("config.properties");
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    
    public static String getBrowser() {
        return properties.getProperty("browser", "chrome");
    }
    
    public static int getImplicitWait() {
        return Integer.parseInt(properties.getProperty("implicit.wait", "10"));
    }
    
    public static int getExplicitWait() {
        return Integer.parseInt(properties.getProperty("explicit.wait", "15"));
    }
    
    public static boolean isHeadless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
}
```

---

## 5. Page Object Model (POM)

### 5.1 What is Page Object Model?

POM is a design pattern where:
- Each web page has a corresponding **Page Class**
- Page classes contain **locators** (element identifiers) and **methods** (actions)
- Test classes use page classes to interact with the application

### 5.2 Benefits of POM

| Benefit | Description |
|---------|-------------|
| **Maintainability** | UI changes only require updates in one place (page class) |
| **Reusability** | Same page methods used across multiple tests |
| **Readability** | Tests read like user stories |
| **Separation** | Separates test logic from page interaction logic |
| **DRY** | Don't Repeat Yourself - no duplicate locators |

### 5.3 BasePage.java - Parent Class

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * BasePage - Parent class for all page objects
 * Contains common methods used across all pages
 */
public class BasePage {
    
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    // Common locators
    protected By toastMessage = By.cssSelector("[class*='toast']");
    protected By loadingSpinner = By.cssSelector(".animate-spin");
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for element to be visible and return it
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable and return it
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Click on element with visual delay
     */
    protected void click(By locator) {
        waitForClickable(locator).click();
        pause(500);  // Visual feedback delay
    }
    
    /**
     * Enter text in input field
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        pause(300);
        element.sendKeys(text);
        pause(500);  // Visual feedback delay
    }
    
    /**
     * Get text from element
     */
    protected String getText(By locator) {
        return waitForElement(locator).getText();
    }
    
    /**
     * Check if element is displayed
     */
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Wait for element to disappear
     */
    protected void waitForElementToDisappear(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Wait for URL to contain specific text
     */
    protected void waitForUrlContains(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Check if toast message is displayed
     */
    public boolean isToastDisplayed() {
        try {
            return waitForElement(toastMessage).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Get toast message text
     */
    public String getToastMessage() {
        try {
            return getText(toastMessage);
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Wait for loading spinner to complete
     */
    protected void waitForLoadingComplete() {
        try {
            Thread.sleep(500);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception e) {
            // Loading might be too fast
        }
    }
    
    /**
     * Simple pause for visual feedback
     */
    protected void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

### 5.4 LoginPage.java - Page Object Example

```java
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
    
    // Password hint
    private By passwordHint = By.xpath("//p[contains(text(), 'Must contain')]");
    
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
     * Perform complete login and return DashboardPage
     */
    public DashboardPage login(String email, String password) {
        System.out.println("  Entering email: " + email);
        enterEmail(email);
        System.out.println("  Entering password...");
        enterPassword(password);
        System.out.println("  Clicking Sign In button...");
        clickSignIn();
        System.out.println("  Waiting for login to process...");
        pause(3000);
        return new DashboardPage(driver);
    }
    
    /**
     * Attempt login without expecting success (for negative tests)
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
    
    // ==================== GETTER METHODS ====================
    
    public String getPageHeader() {
        return getText(pageHeader);
    }
    
    public String getPageSubtitle() {
        return getText(pageSubtitle);
    }
    
    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(emailInput);
    }
    
    public boolean isPasswordFieldDisplayed() {
        return isElementDisplayed(passwordInput);
    }
    
    public boolean isSignInButtonDisplayed() {
        return isElementDisplayed(signInButton);
    }
    
    public boolean isRegisterLinkDisplayed() {
        return isElementDisplayed(registerLink);
    }
    
    public boolean isPasswordHintDisplayed() {
        return isElementDisplayed(passwordHint);
    }
    
    public String getEmailPlaceholder() {
        try {
            return driver.findElement(emailInput).getAttribute("placeholder");
        } catch (Exception e) {
            return "";
        }
    }
    
    public boolean isEmailRequired() {
        try {
            String required = driver.findElement(emailInput).getAttribute("required");
            return required != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean isPasswordRequired() {
        try {
            String required = driver.findElement(passwordInput).getAttribute("required");
            return required != null;
        } catch (Exception e) {
            return false;
        }
    }
}
```

### 5.5 RegisterPage.java

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class RegisterPage extends BasePage {
    
    // Locators
    private By usernameInput = By.name("username");
    private By emailInput = By.name("email");
    private By roleDropdown = By.name("role");
    private By passwordInput = By.name("password");
    private By confirmPasswordInput = By.name("confirmPassword");
    private By createAccountButton = By.xpath("//button[contains(text(), 'Create Account')]");
    private By loginLink = By.xpath("//a[contains(text(), 'Sign in')]");
    private By passwordErrorBox = By.xpath("//div[contains(@class, 'bg-red-50')]");
    private By passwordSuccessIndicator = By.xpath("//p[contains(text(), 'Strong password')]");
    
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Check if page is displayed
     */
    public boolean isPageDisplayed() {
        return isElementDisplayed(usernameInput) && 
               isElementDisplayed(emailInput) && 
               isElementDisplayed(passwordInput);
    }
    
    /**
     * Fill registration form
     */
    public void fillForm(String username, String email, String role, String password) {
        enterText(usernameInput, username);
        enterText(emailInput, email);
        selectRole(role);
        enterText(passwordInput, password);
        enterText(confirmPasswordInput, password);
    }
    
    /**
     * Select role from dropdown
     */
    public void selectRole(String role) {
        Select select = new Select(driver.findElement(roleDropdown));
        select.selectByValue(role);
    }
    
    /**
     * Click Create Account button
     */
    public void clickCreateAccount() {
        click(createAccountButton);
        pause(3000);
    }
    
    /**
     * Complete registration and return DashboardPage
     */
    public DashboardPage register(String username, String email, String role, String password) {
        fillForm(username, email, role, password);
        clickCreateAccount();
        return new DashboardPage(driver);
    }
    
    /**
     * Check if password error box is displayed
     */
    public boolean isPasswordErrorDisplayed() {
        return isElementDisplayed(passwordErrorBox);
    }
    
    /**
     * Check if password success indicator is displayed
     */
    public boolean isPasswordSuccessDisplayed() {
        return isElementDisplayed(passwordSuccessIndicator);
    }
    
    // Field display checks
    public boolean isUsernameFieldDisplayed() { return isElementDisplayed(usernameInput); }
    public boolean isEmailFieldDisplayed() { return isElementDisplayed(emailInput); }
    public boolean isRoleDropdownDisplayed() { return isElementDisplayed(roleDropdown); }
    public boolean isPasswordFieldDisplayed() { return isElementDisplayed(passwordInput); }
    public boolean isConfirmPasswordFieldDisplayed() { return isElementDisplayed(confirmPasswordInput); }
    public boolean isCreateAccountButtonDisplayed() { return isElementDisplayed(createAccountButton); }
}
```

### 5.6 DocumentsPage.java

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DocumentsPage extends BasePage {
    
    // Locators
    private By pageTitle = By.xpath("//h1[contains(text(), 'My Documents')]");
    private By uploadPdfTab = By.xpath("//button[contains(text(), 'Upload PDF')]");
    private By pasteTextTab = By.xpath("//button[contains(text(), 'Paste Text')]");
    private By fileInput = By.cssSelector("input[type='file']");
    private By textArea = By.tagName("textarea");
    private By extractClausesButton = By.xpath("//button[contains(text(), 'Extract Clauses')]");
    private By documentList = By.xpath("//div[contains(@class, 'space-y-4')]");
    
    public DocumentsPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isPageDisplayed() {
        return isElementDisplayed(pageTitle);
    }
    
    public String getPageTitle() {
        return getText(pageTitle);
    }
    
    // Tab methods
    public boolean isUploadPdfTabDisplayed() { return isElementDisplayed(uploadPdfTab); }
    public boolean isPasteTextTabDisplayed() { return isElementDisplayed(pasteTextTab); }
    public boolean areBothTabsDisplayed() { 
        return isUploadPdfTabDisplayed() && isPasteTextTabDisplayed(); 
    }
    
    public void clickUploadPdfTab() { click(uploadPdfTab); }
    public void clickPasteTextTab() { click(pasteTextTab); }
    
    public boolean isUploadPdfTabActive() {
        String classes = driver.findElement(uploadPdfTab).getAttribute("class");
        return classes.contains("border-indigo-600") || classes.contains("text-indigo-600");
    }
    
    public boolean isPasteTextTabActive() {
        String classes = driver.findElement(pasteTextTab).getAttribute("class");
        return classes.contains("border-indigo-600") || classes.contains("text-indigo-600");
    }
    
    // File input
    public boolean isFileInputDisplayed() { return isElementDisplayed(fileInput); }
    
    // Text extraction
    public void enterContractText(String text) {
        clickPasteTextTab();
        pause(500);
        enterText(textArea, text);
    }
    
    public void clickExtractClauses() {
        click(extractClausesButton);
        pause(3000);
    }
}
```

---

## 6. Base Test Class

### BaseTest.java

```java
package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;
import utils.WaitHelper;
import java.time.Duration;

/**
 * BaseTest - Parent class for all test classes
 * Handles browser setup, teardown, and common functionality
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    protected String baseUrl;
    
    /**
     * Initialize WebDriver - called before each test
     */
    public void setUp() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        boolean headless = ConfigReader.isHeadless();
        
        System.out.println("Starting browser: " + browser);
        System.out.println("Headless mode: " + headless);
        
        switch (browser) {
            case "chrome":
                setupChrome(headless);
                break;
            case "firefox":
                setupFirefox(headless);
                break;
            case "edge":
                setupEdge(headless);
                break;
            default:
                setupChrome(headless);
        }
        
        // Configure common settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
            Duration.ofSeconds(ConfigReader.getImplicitWait())
        );
        driver.manage().deleteAllCookies();
        
        // Initialize helpers
        waitHelper = new WaitHelper(driver);
        baseUrl = ConfigReader.getBaseUrl();
        
        System.out.println("Browser started. Base URL: " + baseUrl);
    }
    
    /**
     * Setup Chrome browser
     */
    private void setupChrome(boolean headless) {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        
        driver = new ChromeDriver(options);
    }
    
    /**
     * Setup Firefox browser
     */
    private void setupFirefox(boolean headless) {
        System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
        
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        
        driver = new FirefoxDriver(options);
    }
    
    /**
     * Setup Edge browser
     */
    private void setupEdge(boolean headless) {
        System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
        
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless");
        }
        
        driver = new EdgeDriver(options);
    }
    
    /**
     * Cleanup - called after each test
     */
    public void tearDown() {
        try {
            if (driver != null) {
                System.out.println("Closing browser...");
                driver.quit();
            }
        } catch (Exception e) {
            System.out.println("Browser already closed");
        }
        driver = null;
    }
    
    /**
     * Navigate to a specific page
     */
    protected void navigateTo(String path) {
        String url = baseUrl + path;
        System.out.println("Navigating to: " + url);
        driver.get(url);
        sleep(2000);  // Wait for page load
    }
    
    /**
     * Navigation helper methods
     */
    protected void navigateToHome() { navigateTo(""); }
    protected void navigateToLogin() { navigateTo("/login"); }
    protected void navigateToRegister() { navigateTo("/register"); }
    protected void navigateToDashboard() { navigateTo("/dashboard"); }
    protected void navigateToDocuments() { navigateTo("/documents"); }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Sleep utility
     */
    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Print test result in formatted way
     */
    protected void printTestResult(String testName, boolean passed) {
        String status = passed ? "✓ PASSED" : "✗ FAILED";
        System.out.println("  " + testName + ": " + status);
    }
}
```

---

## 7. Test Classes

### 7.1 LoginPageTest.java

```java
package tests;

import pages.LoginPage;
import pages.DashboardPage;
import utils.TestDataGenerator;

/**
 * LoginPageTest - Test cases for the Login page
 * Tests: 16 total
 */
public class LoginPageTest extends BaseTest {
    
    private LoginPage loginPage;
    
    // Test credentials
    private static final String VALID_EMAIL = "testuser@test.com";
    private static final String VALID_PASSWORD = "Test@1234";
    
    public void beforeTest() {
        setUp();
        navigateToLogin();
        loginPage = new LoginPage(driver);
    }
    
    public void afterTest() {
        tearDown();
    }
    
    // ==================== UI ELEMENT TESTS ====================
    
    /**
     * TC001: Verify login page is displayed correctly
     */
    public void testLoginPageDisplay() {
        beforeTest();
        try {
            boolean result = loginPage.isPageDisplayed();
            printTestResult("TC001_LoginPageDisplay", result);
            assert result : "Login page should be displayed";
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
            String header = loginPage.getPageHeader();
            boolean result = header.contains("ContractIQ");
            printTestResult("TC002_PageHeader", result);
            assert result : "Header should contain 'ContractIQ'";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC003: Verify page subtitle
     */
    public void testPageSubtitle() {
        beforeTest();
        try {
            String subtitle = loginPage.getPageSubtitle();
            boolean result = subtitle.contains("Sign in to your account");
            printTestResult("TC003_PageSubtitle", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC004: Verify email field is displayed
     */
    public void testEmailFieldDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isEmailFieldDisplayed();
            printTestResult("TC004_EmailFieldDisplayed", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC005: Verify password field is displayed
     */
    public void testPasswordFieldDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordFieldDisplayed();
            printTestResult("TC005_PasswordFieldDisplayed", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC006: Verify Sign In button is displayed
     */
    public void testSignInButtonDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isSignInButtonDisplayed();
            printTestResult("TC006_SignInButtonDisplayed", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC007: Verify Register link is displayed
     */
    public void testRegisterLinkDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isRegisterLinkDisplayed();
            printTestResult("TC007_RegisterLinkDisplayed", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC008: Verify email field placeholder
     */
    public void testEmailPlaceholder() {
        beforeTest();
        try {
            String placeholder = loginPage.getEmailPlaceholder();
            boolean result = placeholder.contains("@") || placeholder.toLowerCase().contains("email");
            printTestResult("TC008_EmailPlaceholder", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC009: Verify password hint is displayed
     */
    public void testPasswordHintDisplayed() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordHintDisplayed();
            printTestResult("TC009_PasswordHintDisplayed", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC010: Verify email field is required
     */
    public void testEmailFieldRequired() {
        beforeTest();
        try {
            boolean result = loginPage.isEmailRequired();
            printTestResult("TC010_EmailFieldRequired", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC011: Verify password field is required
     */
    public void testPasswordFieldRequired() {
        beforeTest();
        try {
            boolean result = loginPage.isPasswordRequired();
            printTestResult("TC011_PasswordFieldRequired", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    // ==================== NAVIGATION TESTS ====================
    
    /**
     * TC012: Verify Register link navigates to Register page
     */
    public void testRegisterLinkNavigation() {
        beforeTest();
        try {
            loginPage.clickRegisterLink();
            sleep(2000);
            boolean result = getCurrentUrl().contains("/register");
            printTestResult("TC012_RegisterLinkNavigation", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    // ==================== FUNCTIONAL TESTS ====================
    
    /**
     * TC013: Verify login with valid credentials
     * PRE-REQUISITE: User must be registered first
     */
    public void testValidLogin() {
        beforeTest();
        try {
            DashboardPage dashboard = loginPage.login(VALID_EMAIL, VALID_PASSWORD);
            sleep(2000);
            boolean result = getCurrentUrl().contains("/dashboard");
            printTestResult("TC013_ValidLogin", result);
            assert result : "Should navigate to dashboard after login";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC014: Verify login with invalid email
     */
    public void testInvalidEmailLogin() {
        beforeTest();
        try {
            loginPage.attemptLogin("invalid@test.com", VALID_PASSWORD);
            boolean result = loginPage.isToastDisplayed() || getCurrentUrl().contains("/login");
            printTestResult("TC014_InvalidEmailLogin", result);
            assert result : "Should show error or stay on login page";
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC015: Verify login with invalid password
     */
    public void testInvalidPasswordLogin() {
        beforeTest();
        try {
            loginPage.attemptLogin(VALID_EMAIL, "wrongpassword");
            boolean result = loginPage.isToastDisplayed() || getCurrentUrl().contains("/login");
            printTestResult("TC015_InvalidPasswordLogin", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    /**
     * TC016: Verify login with empty fields
     */
    public void testEmptyFieldsLogin() {
        beforeTest();
        try {
            loginPage.clickSignIn();
            sleep(1000);
            // Should stay on login page (HTML5 validation)
            boolean result = getCurrentUrl().contains("/login");
            printTestResult("TC016_EmptyFieldsLogin", result);
            assert result;
        } finally {
            afterTest();
        }
    }
    
    // ==================== RUN ALL TESTS ====================
    
    public void runAllTests() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RUNNING LOGIN PAGE TESTS");
        System.out.println("=".repeat(50));
        
        testLoginPageDisplay();
        testPageHeader();
        testPageSubtitle();
        testEmailFieldDisplayed();
        testPasswordFieldDisplayed();
        testSignInButtonDisplayed();
        testRegisterLinkDisplayed();
        testEmailPlaceholder();
        testPasswordHintDisplayed();
        testEmailFieldRequired();
        testPasswordFieldRequired();
        testRegisterLinkNavigation();
        testValidLogin();
        testInvalidEmailLogin();
        testInvalidPasswordLogin();
        testEmptyFieldsLogin();
        
        System.out.println("\nLogin Page Tests Complete!\n");
    }
}
```

### 7.2 RegisterPageTest.java (Summary)

```java
/**
 * RegisterPageTest - Test cases for Registration (19 tests)
 * 
 * Tests include:
 * TC001 - Page display
 * TC002 - Page header
 * TC003 - Page subtitle
 * TC004 - Username field displayed
 * TC005 - Email field displayed
 * TC006 - Role dropdown displayed
 * TC007 - Password field displayed
 * TC008 - Confirm password field displayed
 * TC009 - Create Account button displayed
 * TC010 - Login link displayed
 * TC011 - Role options (admin, lawyer, client)
 * TC012 - Password validation (weak password shows error)
 * TC013 - Password validation (strong password shows success)
 * TC014 - Password mismatch shows error
 * TC015 - Login link navigation
 * TC016 - Successful registration with new user
 * TC017 - Registration with existing username
 * TC018 - Registration with existing email
 * TC019 - Registration with empty fields
 */
```

### 7.3 DocumentsPageTest.java (Summary)

```java
/**
 * DocumentsPageTest - Test cases for Documents page (15 tests)
 * 
 * Tests include:
 * TC001 - Page display
 * TC002 - Page title
 * TC003 - Upload PDF tab displayed
 * TC004 - Paste Text tab displayed
 * TC005 - Both tabs displayed
 * TC006 - File input present
 * TC007 - Paste Text tab switch
 * TC008 - Upload PDF tab switch
 * TC009 - Text area displayed in Paste Text tab
 * TC010 - Extract Clauses button displayed
 * TC011 - Text extraction with sample contract text
 * TC012 - Text extraction with empty text (error)
 * TC013 - Text extraction with short text (error)
 * TC014 - Documents list displayed
 * TC015 - Navigation back to Dashboard
 */
```

---

## 8. Test Data Generator

```java
package utils;

import java.util.Random;
import java.util.UUID;

/**
 * TestDataGenerator - Generates random test data
 */
public class TestDataGenerator {
    
    private static Random random = new Random();
    
    /**
     * Generate unique username
     */
    public static String generateUsername() {
        return "testuser_" + System.currentTimeMillis();
    }
    
    /**
     * Generate unique email
     */
    public static String generateEmail() {
        return "testuser_" + System.currentTimeMillis() + "@test.com";
    }
    
    /**
     * Generate valid password meeting all requirements:
     * - 8+ characters
     * - Uppercase, lowercase, digit, special char
     */
    public static String generateValidPassword() {
        return "Test@" + (random.nextInt(9000) + 1000) + "Pass";
    }
    
    /**
     * Generate invalid/weak password
     */
    public static String generateInvalidPassword() {
        return "weak";
    }
    
    /**
     * Get a specific valid test password
     */
    public static String getValidTestPassword() {
        return "Test@1234";
    }
    
    /**
     * Sample contract text for testing clause extraction
     */
    public static String getSampleContractText() {
        return "This Agreement is entered into as of the Effective Date. " +
               "TERMINATION: Either party may terminate this agreement with 30 days notice. " +
               "PAYMENT TERMS: Client agrees to pay all invoices within 30 days of receipt. " +
               "CONFIDENTIALITY: Both parties agree to keep proprietary information confidential. " +
               "LIABILITY: Neither party shall be liable for indirect or consequential damages. " +
               "INTELLECTUAL PROPERTY: All work product shall remain the property of the creator.";
    }
    
    /**
     * Generate random string of specified length
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
```

---

## 9. Test Runner

```java
package tests;

/**
 * TestRunner - Main class to run all tests
 * Run this class as Java Application
 */
public class TestRunner {
    
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       CONTRACTIQ SELENIUM TEST AUTOMATION                ║");
        System.out.println("║       Total Tests: 76                                    ║");
        System.out.println("╠══════════════════════════════════════════════════════════╣");
        System.out.println("║  HomePageTest:      11 tests                             ║");
        System.out.println("║  LoginPageTest:     16 tests                             ║");
        System.out.println("║  RegisterPageTest:  19 tests                             ║");
        System.out.println("║  DashboardPageTest: 15 tests                             ║");
        System.out.println("║  DocumentsPageTest: 15 tests                             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        
        System.out.println("\nStarting tests at: " + java.time.LocalDateTime.now());
        System.out.println("Make sure both backend (5000) and frontend (5173) are running!\n");
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Run Home Page Tests
            HomePageTest homeTests = new HomePageTest();
            homeTests.runAllTests();
            
            // Run Login Page Tests
            LoginPageTest loginTests = new LoginPageTest();
            loginTests.runAllTests();
            
            // Run Register Page Tests
            RegisterPageTest registerTests = new RegisterPageTest();
            registerTests.runAllTests();
            
            // Run Dashboard Page Tests
            DashboardPageTest dashboardTests = new DashboardPageTest();
            dashboardTests.runAllTests();
            
            // Run Documents Page Tests
            DocumentsPageTest documentsTests = new DocumentsPageTest();
            documentsTests.runAllTests();
            
        } catch (Exception e) {
            System.out.println("Error during test execution: " + e.getMessage());
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime) / 1000;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("Total execution time: " + duration + " seconds");
        System.out.println("=".repeat(60));
    }
}
```

---

## 10. Running the Tests

### Prerequisites
1. Java JDK 8+ installed
2. Chrome browser installed
3. ChromeDriver matching Chrome version
4. Selenium JARs in `lib/` folder
5. Backend running on port 5000
6. Frontend running on port 5173

### Setup Steps

1. **Download Selenium JARs:**
   - Go to https://www.selenium.dev/downloads/
   - Download selenium-java-4.x.x.zip
   - Extract all JARs to `ContractIQTests/lib/`

2. **Download ChromeDriver:**
   - Check Chrome version: `chrome://version/`
   - Download matching driver from https://chromedriver.chromium.org/
   - Place `chromedriver.exe` in `ContractIQTests/drivers/`

3. **Import in Eclipse:**
   - File → Import → Existing Projects
   - Select ContractIQTests folder
   - Add all JARs to Build Path

4. **Run Tests:**
   - Right-click `TestRunner.java`
   - Run As → Java Application

---

## 11. Wait Strategies

### Implicit Wait
```java
// Set globally - waits for element to be present
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
```

### Explicit Wait
```java
// Wait for specific condition
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

// Wait for visibility
WebElement element = wait.until(
    ExpectedConditions.visibilityOfElementLocated(By.id("email"))
);

// Wait for clickable
WebElement button = wait.until(
    ExpectedConditions.elementToBeClickable(By.id("submit"))
);

// Wait for URL change
wait.until(ExpectedConditions.urlContains("/dashboard"));

// Wait for invisibility (loading spinner)
wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading")));
```

### Key Differences

| Implicit Wait | Explicit Wait |
|---------------|---------------|
| Global setting | Per element/condition |
| Only waits for presence | Waits for specific conditions |
| Less flexible | More control |
| Can slow down tests | More efficient |

---

## 12. Locator Strategies

### By ID (Best)
```java
By.id("email")
```

### By Name
```java
By.name("password")
```

### By CSS Selector
```java
By.cssSelector("input[type='email']")
By.cssSelector(".submit-button")
By.cssSelector("#login-form")
```

### By XPath
```java
// Text content
By.xpath("//button[contains(text(), 'Sign In')]")

// Attribute
By.xpath("//input[@placeholder='Enter email']")

// Partial match
By.xpath("//h1[contains(text(), 'ContractIQ')]")

// Parent-child
By.xpath("//div[@class='form']//input[@type='text']")
```

### By Link Text
```java
By.linkText("Register here")
By.partialLinkText("Register")
```

### Priority (Best to Worst)

1. **ID** - Unique, fast
2. **Name** - Usually unique in forms
3. **CSS Selector** - Fast, flexible
4. **XPath** - Most flexible, slower
5. **Link Text** - Only for links

---

## 13. Viva Questions - Testing

**Q: What is Page Object Model?**
> A design pattern where each web page has a corresponding class containing locators and methods. It separates test logic from page interaction logic.

**Q: Why use Page Object Model?**
> Maintainability (UI changes need one update), Reusability (same methods across tests), Readability (tests read like user stories).

**Q: What is the difference between implicit and explicit wait?**
> Implicit wait is global, waits for element presence. Explicit wait is per-element, waits for specific conditions (visibility, clickability).

**Q: What is WebDriverWait?**
> A class that implements explicit wait. You specify a condition to wait for and a timeout.

**Q: What is ExpectedConditions?**
> A class with predefined conditions like `visibilityOfElementLocated`, `elementToBeClickable`, `urlContains`.

**Q: How do you locate elements?**
> By ID, Name, CSS Selector, XPath, LinkText. ID is preferred as it's unique and fast.

**Q: What is the best locator strategy?**
> ID > Name > CSS Selector > XPath. ID is fastest and most reliable.

**Q: How do you handle dynamic elements?**
> Use explicit waits, partial matches in XPath, or CSS selectors with attribute contains.

**Q: How many test cases do you have?**
> 76 total: HomePage (11), Login (16), Register (19), Dashboard (15), Documents (15).

**Q: What browsers does your framework support?**
> Chrome, Firefox, and Edge. Configured in config.properties.

**Q: How do you generate test data?**
> TestDataGenerator class generates unique usernames, emails, passwords using timestamps.

**Q: How do you handle authentication in tests?**
> Register new user in beforeTest(), then use that session for the test.

---

*Testing documentation complete for viva preparation.*
