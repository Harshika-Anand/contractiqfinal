# 🧪 ContractIQ Testing - Viva Preparation Document

## Complete Selenium WebDriver & TestNG Test Automation Guide

---

## 📌 **PART 1: Testing Framework Overview**

### **What is ContractIQ Testing Project?**
A **pure Java Selenium WebDriver** test automation project using **Page Object Model (POM)** design pattern for testing the ContractIQ web application. Uses TestNG for test execution and Maven for build management.

### **Purpose:**
- Automate functional testing of ContractIQ UI
- Verify all features work correctly
- Identify bugs before production
- Provide regression testing capability
- Generate test reports

### **Technology Stack:**
| Technology | Purpose | Version |
|-----------|---------|---------|
| **Java** | Programming Language | 8 or higher |
| **Selenium WebDriver** | Browser automation | 4.x |
| **TestNG** | Test framework | 7.x |
| **Maven** | Build & dependency management | 3.x |
| **ChromeDriver** | Chrome browser automation | Latest |
| **Eclipse IDE** | Development environment | Latest |

---

## 🗂️ **PART 2: Project Structure**

### **Complete Directory Layout:**
```
ContractIQTests/
│
├── src/test/java/
│   │
│   ├── pages/                           # Page Object Model classes
│   │   ├── BasePage.java               # Base class for all pages
│   │   ├── HomePage.java               # Home/Landing page
│   │   ├── LoginPage.java              # Login page
│   │   ├── RegisterPage.java           # Registration page
│   │   ├── DashboardPage.java          # Dashboard page (after login)
│   │   ├── DocumentsPage.java          # Documents upload & list page
│   │   └── NavbarComponent.java        # Navbar component (shared)
│   │
│   ├── tests/                           # Test classes
│   │   ├── BaseTest.java               # Base test setup/teardown
│   │   ├── HomePageTest.java           # Home page tests (11 tests)
│   │   ├── LoginPageTest.java          # Login tests (16 tests)
│   │   ├── RegisterPageTest.java       # Registration tests (19 tests)
│   │   ├── DashboardPageTest.java      # Dashboard tests (15 tests)
│   │   ├── DocumentsPageTest.java      # Documents tests (15 tests)
│   │   └── TestRunner.java             # Main test runner
│   │
│   ├── utils/                           # Utility classes
│   │   ├── ConfigReader.java           # Read config.properties
│   │   ├── WaitHelper.java             # Explicit wait utilities
│   │   ├── TestDataGenerator.java      # Generate test data
│   │   └── ScreenshotHelper.java       # Capture screenshots
│   │
│   └── resources/
│       └── testng-xslt.xsl            # Test report template
│
├── testdata/
│   └── testdata.json                   # Test data (users, credentials)
│
├── lib/                                # Selenium & dependencies JARs
├── drivers/                            # ChromeDriver executable
├── reports/                            # Test reports & screenshots
│   └── screenshots/                    # Screenshots on failure
│
├── config.properties                   # Application & test configuration
├── pom.xml                             # Maven configuration
├── testng.xml                          # TestNG suite configuration
├── SETUP_GUIDE.md                     # Setup instructions
├── README.md                          # Project documentation
└── .project                           # Eclipse project file
```

---

## 📋 **PART 3: Key Components Explained**

### **Component 1: BasePage.java (Page Object Base)**

**Purpose:** Base class for all page objects, contains common elements and methods

```java
public class BasePage {
    
    // WebDriver instance shared across all pages
    protected WebDriver driver;
    
    // Common locators (appear on multiple pages)
    private By logoutButton = By.xpath("//button[contains(text(), 'Logout')]");
    private By navbar = By.id("navbar");
    
    // Constructor - receives driver instance
    public BasePage(WebDriver driver) {
        this.driver = driver;
    }
    
    // Explicit waits - wait for elements to be ready
    protected WebDriverWait wait() {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    // Wait for element to be visible
    protected void waitForElementVisible(By locator) {
        wait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    // Wait for element to be clickable
    protected void waitForElementClickable(By locator) {
        wait().until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    // Safe click (scroll into view, wait for clickable, then click)
    protected void safeClick(By locator) {
        WebElement element = driver.findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        waitForElementClickable(locator);
        driver.findElement(locator).click();
    }
    
    // Safe text input (wait for element, clear, type)
    protected void safeType(By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    // Get text from element
    protected String getText(By locator) {
        return driver.findElement(locator).getText();
    }
    
    // Check if element is displayed
    protected boolean isElementDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    // Logout functionality (common across pages)
    public HomePage logout() {
        safeClick(logoutButton);
        return new HomePage(driver);
    }
    
    // Get page title
    public String getPageTitle() {
        return driver.getTitle();
    }
}
```

**Why BasePage is important:**
- ✅ Code reusability (common methods)
- ✅ Maintenance (change once, affects all pages)
- ✅ Consistency (all pages use same methods)
- ✅ Safety (explicit waits prevent flakiness)

---

### **Component 2: HomePage.java (Page Object)**

**Purpose:** Represents the Home/Landing page, contains elements & actions specific to this page

```java
public class HomePage extends BasePage {
    
    // Page-specific locators
    private By heroTitle = By.xpath("//h1[contains(text(), 'Smart Contract')]");
    private By getStartedButton = By.xpath("//a[contains(text(), 'Get Started')]");
    private By loginLink = By.xpath("//a[contains(text(), 'Login')]");
    private By registerLink = By.xpath("//a[contains(text(), 'Register')]");
    private By featureCards = By.xpath("//div[@class='feature-card']");
    private By navbarLogo = By.xpath("//a[@class='logo']");
    
    // Constructor
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    // Verify page loaded (assertion)
    public boolean isHomePageLoaded() {
        return isElementDisplayed(heroTitle);
    }
    
    // Click Get Started button (navigation to register)
    public RegisterPage clickGetStarted() {
        safeClick(getStartedButton);
        return new RegisterPage(driver);
    }
    
    // Click Login link
    public LoginPage clickLogin() {
        safeClick(loginLink);
        return new LoginPage(driver);
    }
    
    // Click Register link
    public RegisterPage clickRegister() {
        safeClick(registerLink);
        return new RegisterPage(driver);
    }
    
    // Verify all feature cards are displayed
    public int getFeatureCardCount() {
        return driver.findElements(featureCards).size();
    }
    
    // Verify hero title text
    public String getHeroTitle() {
        return getText(heroTitle);
    }
}
```

**Key concepts:**
- ✅ Each page object represents one page
- ✅ Contains locators (By elements)
- ✅ Contains actions (click, type, navigate)
- ✅ Returns other page objects for navigation
- ✅ Contains verification methods (assertions)

---

### **Component 3: LoginPage.java**

**Purpose:** Represents Login page with login functionality

```java
public class LoginPage extends BasePage {
    
    // Locators
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By loginButton = By.xpath("//button[contains(text(), 'Login')]");
    private By registerLink = By.xpath("//a[contains(text(), 'Register')]");
    private By errorMessage = By.xpath("//div[@class='error-message']");
    private By rememberMeCheckbox = By.id("rememberMe");
    private By forgotPasswordLink = By.xpath("//a[contains(text(), 'Forgot')]");
    
    // Constructor
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    // Verify page loaded
    public boolean isLoginPageLoaded() {
        return isElementDisplayed(loginButton);
    }
    
    // Enter email
    public LoginPage enterEmail(String email) {
        safeType(emailInput, email);
        return this;
    }
    
    // Enter password
    public LoginPage enterPassword(String password) {
        safeType(passwordInput, password);
        return this;
    }
    
    // Click Login button → returns Dashboard
    public DashboardPage clickLoginButton() {
        safeClick(loginButton);
        return new DashboardPage(driver);
    }
    
    // Complete login (Fluent interface - chain methods)
    public DashboardPage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
    
    // Check if error message displayed
    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage);
    }
    
    // Get error message text
    public String getErrorMessage() {
        return getText(errorMessage);
    }
    
    // Click Register link
    public RegisterPage clickRegisterLink() {
        safeClick(registerLink);
        return new RegisterPage(driver);
    }
}
```

**Fluent Interface Pattern:**
```java
// Instead of:
LoginPage loginPage = new LoginPage(driver);
loginPage.enterEmail("test@example.com");
loginPage.enterPassword("password");
DashboardPage dashboard = loginPage.clickLoginButton();

// We can do:
DashboardPage dashboard = new LoginPage(driver)
    .enterEmail("test@example.com")
    .enterPassword("password")
    .clickLoginButton();
```

---

### **Component 4: RegisterPage.java**

**Purpose:** Registration page with signup functionality

```java
public class RegisterPage extends BasePage {
    
    // Locators
    private By usernameInput = By.id("username");
    private By emailInput = By.id("email");
    private By passwordInput = By.id("password");
    private By confirmPasswordInput = By.id("confirmPassword");
    private By roleDropdown = By.id("role");
    private By registerButton = By.xpath("//button[contains(text(), 'Register')]");
    private By loginLink = By.xpath("//a[contains(text(), 'Login')]");
    private By errorMessages = By.xpath("//div[@class='error']");
    private By successMessage = By.xpath("//div[@class='success']");
    
    public RegisterPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isRegisterPageLoaded() {
        return isElementDisplayed(registerButton);
    }
    
    public RegisterPage enterUsername(String username) {
        safeType(usernameInput, username);
        return this;
    }
    
    public RegisterPage enterEmail(String email) {
        safeType(emailInput, email);
        return this;
    }
    
    public RegisterPage enterPassword(String password) {
        safeType(passwordInput, password);
        return this;
    }
    
    public RegisterPage enterConfirmPassword(String password) {
        safeType(confirmPasswordInput, password);
        return this;
    }
    
    public RegisterPage selectRole(String role) {
        Select select = new Select(driver.findElement(roleDropdown));
        select.selectByValue(role);
        return this;
    }
    
    public DashboardPage clickRegisterButton() {
        safeClick(registerButton);
        return new DashboardPage(driver);
    }
    
    // Complete registration flow
    public DashboardPage register(String username, String email, String password, String role) {
        enterUsername(username);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(password);
        selectRole(role);
        return clickRegisterButton();
    }
    
    public boolean isSuccessMessageDisplayed() {
        return isElementDisplayed(successMessage);
    }
    
    public String getErrorMessage() {
        return getText(errorMessages);
    }
}
```

---

### **Component 5: DashboardPage.java**

**Purpose:** Dashboard after login, shows statistics and recent documents

```java
public class DashboardPage extends BasePage {
    
    // Locators
    private By welcomeMessage = By.xpath("//h1[contains(text(), 'Welcome')]");
    private By totalDocumentsCard = By.xpath("//div[@class='stat-card' and contains(text(), 'Documents')]");
    private By totalClausesCard = By.xpath("//div[@class='stat-card' and contains(text(), 'Clauses')]");
    private By documentsLink = By.xpath("//a[contains(text(), 'Documents')]");
    private By recentDocumentsList = By.xpath("//div[@class='recent-documents']/div");
    
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isDashboardLoaded() {
        return isElementDisplayed(welcomeMessage);
    }
    
    public String getWelcomeMessage() {
        return getText(welcomeMessage);
    }
    
    public String getTotalDocumentsCount() {
        return getText(totalDocumentsCard);
    }
    
    public String getTotalClausesCount() {
        return getText(totalClausesCard);
    }
    
    public int getRecentDocumentsCount() {
        return driver.findElements(recentDocumentsList).size();
    }
    
    public DocumentsPage clickDocumentsLink() {
        safeClick(documentsLink);
        return new DocumentsPage(driver);
    }
}
```

---

### **Component 6: DocumentsPage.java**

**Purpose:** Document upload and management page

```java
public class DocumentsPage extends BasePage {
    
    // Locators
    private By fileInput = By.id("fileInput");
    private By uploadButton = By.xpath("//button[contains(text(), 'Upload')]");
    private By documentsGrid = By.xpath("//div[@class='document-card']");
    private By successMessage = By.xpath("//div[@class='success-message']");
    private By errorMessage = By.xpath("//div[@class='error-message']");
    
    public DocumentsPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isDocumentsPageLoaded() {
        return isElementDisplayed(uploadButton);
    }
    
    // Upload PDF file
    public DocumentsPage uploadFile(String filePath) {
        driver.findElement(fileInput).sendKeys(filePath);
        safeClick(uploadButton);
        return this;
    }
    
    // Get count of documents displayed
    public int getDocumentsCount() {
        return driver.findElements(documentsGrid).size();
    }
    
    // Check if upload successful
    public boolean isUploadSuccessful() {
        return isElementDisplayed(successMessage);
    }
    
    // Get success message
    public String getSuccessMessage() {
        return getText(successMessage);
    }
    
    // Get error message if upload fails
    public String getErrorMessage() {
        return getText(errorMessage);
    }
}
```

---

### **Component 7: BaseTest.java (Test Setup)**

**Purpose:** Setup and teardown for all tests

```java
public class BaseTest {
    
    protected WebDriver driver;
    protected HomePage homePage;
    protected LoginPage loginPage;
    protected RegisterPage registerPage;
    protected DashboardPage dashboardPage;
    protected DocumentsPage documentsPage;
    
    // Configuration
    protected String baseURL = "http://localhost:5173";
    
    // Setup method - runs BEFORE each test
    @BeforeMethod
    public void setup() {
        // Set ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
        
        // Create ChromeDriver instance
        driver = new ChromeDriver();
        
        // Maximize browser window
        driver.manage().window().maximize();
        
        // Set implicit wait (20 seconds)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        
        // Navigate to application
        driver.get(baseURL);
        
        // Initialize page objects
        homePage = new HomePage(driver);
    }
    
    // Teardown method - runs AFTER each test
    @AfterMethod
    public void teardown(ITestResult result) {
        // Take screenshot if test fails
        if (ITestResult.FAILURE == result.getStatus()) {
            takeScreenshot(result.getName());
        }
        
        // Close browser
        if (driver != null) {
            driver.quit();
        }
    }
    
    // Take screenshot utility
    protected void takeScreenshot(String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            String filename = "./reports/screenshots/" + testName + "_" + timestamp + ".png";
            FileUtils.copyFile(srcFile, new File(filename));
            System.out.println("Screenshot saved: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
```

**Why BaseTest is important:**
- ✅ Setup browser once for each test
- ✅ Teardown (cleanup) after each test
- ✅ Screenshot on failure for debugging
- ✅ Consistent initialization across tests

---

### **Component 8: Test Classes**

#### **HomePageTest.java (11 Tests)**

```java
public class HomePageTest extends BaseTest {
    
    @Test(priority = 1)
    public void testHomePageLoaded() {
        // Verify home page is loaded
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page not loaded");
    }
    
    @Test(priority = 2)
    public void testHeroTitleDisplayed() {
        // Verify hero title contains expected text
        String title = homePage.getHeroTitle();
        Assert.assertTrue(title.contains("Smart Contract"), "Hero title incorrect");
    }
    
    @Test(priority = 3)
    public void testGetStartedButtonNavigatesToRegister() {
        // Click Get Started → should go to Register page
        RegisterPage registerPage = homePage.clickGetStarted();
        Assert.assertTrue(registerPage.isRegisterPageLoaded(), "Register page not loaded");
    }
    
    @Test(priority = 4)
    public void testLoginLinkNavigatesToLogin() {
        LoginPage loginPage = homePage.clickLogin();
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page not loaded");
    }
    
    @Test(priority = 5)
    public void testRegisterLinkNavigatesToRegister() {
        RegisterPage registerPage = homePage.clickRegister();
        Assert.assertTrue(registerPage.isRegisterPageLoaded(), "Register page not loaded");
    }
    
    @Test(priority = 6)
    public void testFeatureCardsDisplayed() {
        int cardCount = homePage.getFeatureCardCount();
        Assert.assertTrue(cardCount > 0, "Feature cards not displayed");
    }
    
    @Test(priority = 7)
    public void testPageTitleCorrect() {
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.contains("ContractIQ"), "Page title incorrect");
    }
    
    // Additional tests...
}
```

#### **LoginPageTest.java (16 Tests)**

```java
public class LoginPageTest extends BaseTest {
    
    private String validEmail = "test@example.com";
    private String validPassword = "Password@123";
    private String invalidEmail = "invalid@test.com";
    private String invalidPassword = "wrongpassword";
    
    @BeforeClass
    public void beforeClass() {
        // Register a test user first
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.register("testuser", validEmail, validPassword, "client");
    }
    
    @Test(priority = 1)
    public void testLoginPageLoaded() {
        homePage.clickLogin();
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageLoaded(), "Login page not loaded");
    }
    
    @Test(priority = 2)
    public void testValidLogin() {
        LoginPage loginPage = homePage.clickLogin();
        DashboardPage dashboard = loginPage.login(validEmail, validPassword);
        Assert.assertTrue(dashboard.isDashboardLoaded(), "Dashboard not loaded after login");
    }
    
    @Test(priority = 3)
    public void testInvalidEmailLogin() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.login(invalidEmail, validPassword);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not shown for invalid email");
    }
    
    @Test(priority = 4)
    public void testInvalidPasswordLogin() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.login(validEmail, invalidPassword);
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not shown for invalid password");
    }
    
    @Test(priority = 5)
    public void testEmptyEmailLogin() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.enterPassword(validPassword);
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not shown for empty email");
    }
    
    @Test(priority = 6)
    public void testEmptyPasswordLogin() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.enterEmail(validEmail);
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error message not shown for empty password");
    }
    
    @Test(priority = 7)
    public void testLoginWithEmptyFields() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.clickLoginButton();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Error not shown for empty fields");
    }
    
    @Test(priority = 8)
    public void testErrorMessageContent() {
        LoginPage loginPage = homePage.clickLogin();
        loginPage.login(invalidEmail, "wrongpass");
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Invalid"), "Error message text incorrect");
    }
    
    @Test(priority = 9)
    public void testRegisterLinkFromLoginPage() {
        LoginPage loginPage = homePage.clickLogin();
        RegisterPage registerPage = loginPage.clickRegisterLink();
        Assert.assertTrue(registerPage.isRegisterPageLoaded(), "Register page not loaded");
    }
    
    // Additional tests...
}
```

#### **RegisterPageTest.java (19 Tests)**

```java
public class RegisterPageTest extends BaseTest {
    
    private String uniqueEmail = "user_" + System.currentTimeMillis() + "@test.com";
    private String validPassword = "Secure@Password123";
    private String invalidPassword = "pass"; // Too short
    
    @Test(priority = 1)
    public void testValidRegistration() {
        RegisterPage registerPage = homePage.clickRegister();
        DashboardPage dashboard = registerPage.register(
            "newuser",
            uniqueEmail,
            validPassword,
            "client"
        );
        Assert.assertTrue(dashboard.isDashboardLoaded(), "Dashboard not loaded after registration");
    }
    
    @Test(priority = 2)
    public void testRegistrationWithExistingEmail() {
        RegisterPage registerPage = homePage.clickRegister();
        // Try to register with same email
        registerPage.register("anotheruser", uniqueEmail, validPassword, "client");
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for duplicate email");
    }
    
    @Test(priority = 3)
    public void testPasswordMismatch() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.enterPassword("Password@123");
        registerPage.enterConfirmPassword("DifferentPass@456");
        registerPage.clickRegisterButton();
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for password mismatch");
    }
    
    @Test(priority = 4)
    public void testWeakPassword() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.register("testuser", "test@example.com", "123", "client");
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for weak password");
    }
    
    @Test(priority = 5)
    public void testEmptyUsername() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.enterEmail("user@example.com");
        registerPage.enterPassword(validPassword);
        registerPage.enterConfirmPassword(validPassword);
        registerPage.clickRegisterButton();
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for empty username");
    }
    
    @Test(priority = 6)
    public void testEmptyEmail() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.enterUsername("testuser");
        registerPage.enterPassword(validPassword);
        registerPage.clickRegisterButton();
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for empty email");
    }
    
    @Test(priority = 7)
    public void testInvalidEmailFormat() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.register("testuser", "invalidemail", validPassword, "client");
        Assert.assertTrue(registerPage.isErrorMessageDisplayed(), "Error not shown for invalid email");
    }
    
    @Test(priority = 8)
    public void testRoleSelection() {
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.selectRole("lawyer");
        // Verify dropdown value changed
        // (This is more of a UI check)
    }
    
    // Additional tests...
}
```

#### **DashboardPageTest.java (15 Tests)**

```java
public class DashboardPageTest extends BaseTest {
    
    private String testUserEmail = "dashboardtest@example.com";
    private String testUserPassword = "TestPass@123";
    
    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        // Register and login test user
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.register("dashboarduser", testUserEmail, testUserPassword, "client");
    }
    
    @Test(priority = 1)
    public void testDashboardLoaded() {
        DashboardPage dashboard = homePage.clickLogin();
        dashboard.login(testUserEmail, testUserPassword);
        Assert.assertTrue(dashboard.isDashboardLoaded(), "Dashboard not loaded");
    }
    
    @Test(priority = 2)
    public void testWelcomeMessageDisplayed() {
        DashboardPage dashboard = homePage.clickLogin();
        dashboard.login(testUserEmail, testUserPassword);
        String welcomeMsg = dashboard.getWelcomeMessage();
        Assert.assertTrue(welcomeMsg.contains("Welcome"), "Welcome message not displayed");
    }
    
    @Test(priority = 3)
    public void testStatisticsCardsDisplayed() {
        DashboardPage dashboard = homePage.clickLogin();
        dashboard.login(testUserEmail, testUserPassword);
        String docCount = dashboard.getTotalDocumentsCount();
        Assert.assertNotNull(docCount, "Total documents count not displayed");
    }
    
    @Test(priority = 4)
    public void testDocumentsLinkNavigation() {
        DashboardPage dashboard = homePage.clickLogin();
        dashboard.login(testUserEmail, testUserPassword);
        DocumentsPage documentsPage = dashboard.clickDocumentsLink();
        Assert.assertTrue(documentsPage.isDocumentsPageLoaded(), "Documents page not loaded");
    }
    
    @Test(priority = 5)
    public void testLogoutFromDashboard() {
        DashboardPage dashboard = homePage.clickLogin();
        dashboard.login(testUserEmail, testUserPassword);
        HomePage homePage = dashboard.logout();
        Assert.assertTrue(homePage.isHomePageLoaded(), "Home page not loaded after logout");
    }
    
    // Additional tests...
}
```

#### **DocumentsPageTest.java (15 Tests)**

```java
public class DocumentsPageTest extends BaseTest {
    
    private String testUserEmail = "doctest@example.com";
    private String testUserPassword = "TestPass@123";
    private String samplePdfPath = "./testdata/sample_contract.pdf";
    
    @BeforeMethod
    @Override
    public void setup() {
        super.setup();
        // Register, login, navigate to documents
        RegisterPage registerPage = homePage.clickRegister();
        registerPage.register("docuser", testUserEmail, testUserPassword, "client");
    }
    
    @Test(priority = 1)
    public void testDocumentsPageLoaded() {
        DocumentsPage documentsPage = homePage.clickLogin();
        DashboardPage dashboard = (DashboardPage) documentsPage.login(testUserEmail, testUserPassword);
        documentsPage = dashboard.clickDocumentsLink();
        Assert.assertTrue(documentsPage.isDocumentsPageLoaded(), "Documents page not loaded");
    }
    
    @Test(priority = 2)
    public void testFileUpload() {
        DocumentsPage documentsPage = // Navigate to documents page
        documentsPage.uploadFile(samplePdfPath);
        Assert.assertTrue(documentsPage.isUploadSuccessful(), "File upload failed");
    }
    
    @Test(priority = 3)
    public void testUploadSuccessMessage() {
        DocumentsPage documentsPage = // Navigate to documents
        documentsPage.uploadFile(samplePdfPath);
        String successMsg = documentsPage.getSuccessMessage();
        Assert.assertTrue(successMsg.contains("success"), "Success message not shown");
    }
    
    @Test(priority = 4)
    public void testDocumentAddedToList() {
        DocumentsPage documentsPage = // Navigate
        int initialCount = documentsPage.getDocumentsCount();
        documentsPage.uploadFile(samplePdfPath);
        int finalCount = documentsPage.getDocumentsCount();
        Assert.assertTrue(finalCount > initialCount, "Document not added to list");
    }
    
    @Test(priority = 5)
    public void testUploadInvalidFile() {
        DocumentsPage documentsPage = // Navigate
        documentsPage.uploadFile("./testdata/notapdf.txt");
        Assert.assertTrue(documentsPage.isErrorMessageDisplayed(), "Error not shown for invalid file");
    }
    
    @Test(priority = 6)
    public void testUploadLargeFile() {
        // Try uploading file > 10MB
        documentsPage.uploadFile("./testdata/large_file.pdf");
        Assert.assertTrue(documentsPage.isErrorMessageDisplayed(), "Error not shown for large file");
    }
    
    @Test(priority = 7)
    public void testMultipleUploads() {
        DocumentsPage documentsPage = // Navigate
        documentsPage.uploadFile(samplePdfPath);
        documentsPage.uploadFile(samplePdfPath);
        int docCount = documentsPage.getDocumentsCount();
        Assert.assertTrue(docCount >= 2, "Multiple uploads not working");
    }
    
    // Additional tests...
}
```

---

### **Component 9: Utility Classes**

#### **ConfigReader.java**

```java
public class ConfigReader {
    
    private static Properties properties;
    
    static {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("config.properties");
            properties.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    // Usage:
    // String baseURL = ConfigReader.getProperty("base.url");
}
```

#### **WaitHelper.java**

```java
public class WaitHelper {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    public void waitForElementPresence(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    public void waitForElementVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public void waitForElementClickable(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public void waitForElementInvisibility(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}
```

#### **ScreenshotHelper.java**

```java
public class ScreenshotHelper {
    
    public static void captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date());
            String filename = "./reports/screenshots/" + testName + "_" + timestamp + ".png";
            FileUtils.copyFile(srcFile, new File(filename));
            System.out.println("Screenshot saved: " + filename);
        } catch (IOException e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
        }
    }
}
```

---

## ⚙️ **PART 4: Maven & Build Configuration**

### **pom.xml (Key Dependencies)**

```xml
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.contractiq</groupId>
    <artifactId>ContractIQTests</artifactId>
    <version>1.0</version>
    
    <dependencies>
        <!-- Selenium WebDriver -->
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.x.x</version>
        </dependency>
        
        <!-- TestNG -->
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.x.x</version>
        </dependency>
        
        <!-- WebDriverManager (auto manages ChromeDriver) -->
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.x.x</version>
        </dependency>
        
        <!-- Apache Commons IO (file operations) -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.11.0</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Maven Surefire Plugin (run tests) -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### **testng.xml (Test Suite Configuration)**

```xml
<suite name="ContractIQ Test Suite" verbose="2">
    
    <test name="Home Page Tests">
        <classes>
            <class name="tests.HomePageTest" />
        </classes>
    </test>
    
    <test name="Login Tests">
        <classes>
            <class name="tests.LoginPageTest" />
        </classes>
    </test>
    
    <test name="Registration Tests">
        <classes>
            <class name="tests.RegisterPageTest" />
        </classes>
    </test>
    
    <test name="Dashboard Tests">
        <classes>
            <class name="tests.DashboardPageTest" />
        </classes>
    </test>
    
    <test name="Documents Tests">
        <classes>
            <class name="tests.DocumentsPageTest" />
        </classes>
    </test>
    
</suite>
```

---

## 🧪 **PART 5: Page Object Model (POM) Advantages**

### **Why POM?**

| Advantage | Explanation |
|-----------|-------------|
| **Maintainability** | Change locator in one place (page object), not in every test |
| **Reusability** | Common methods used across multiple tests |
| **Readability** | Tests read like sentences: `page.enterEmail().enterPassword().clickLogin()` |
| **Scalability** | Easy to add new pages and tests |
| **Separation of Concerns** | UI locators separate from test logic |

### **POM Example:**

```
Without POM:
@Test
public void testLogin() {
    driver.findElement(By.id("email")).sendKeys("test@example.com");
    driver.findElement(By.id("password")).sendKeys("password");
    driver.findElement(By.xpath("//button[contains(text(), 'Login')]")).click();
    Assert.assertTrue(driver.findElement(By.xpath("//h1")).isDisplayed());
}

With POM:
@Test
public void testLogin() {
    DashboardPage dashboard = new LoginPage(driver)
        .enterEmail("test@example.com")
        .enterPassword("password")
        .clickLoginButton();
    Assert.assertTrue(dashboard.isDashboardLoaded());
}
```

---

## ⏱️ **PART 6: Waits in Selenium**

### **Types of Waits:**

```java
// 1. IMPLICIT WAIT (global, applies to all elements)
driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
// Waits UP TO 10 seconds for ANY element

// 2. EXPLICIT WAIT (specific to element)
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("element")));
// Waits specifically for this element

// 3. FLUENT WAIT (with polling)
Wait<WebDriver> fluentWait = new FluentWait<>(driver)
    .withTimeout(Duration.ofSeconds(30))
    .pollingEvery(Duration.ofSeconds(2))
    .ignoring(NoSuchElementException.class);
fluentWait.until(driver -> driver.findElement(By.id("element")));

// Common Expected Conditions
ExpectedConditions.presenceOfElementLocated(locator)          // Present in DOM
ExpectedConditions.visibilityOfElementLocated(locator)        // Visible on page
ExpectedConditions.elementToBeClickable(locator)              // Can click it
ExpectedConditions.invisibilityOfElementLocated(locator)      // Not visible
ExpectedConditions.textToBePresentInElement(locator, text)    // Specific text
```

**When to use what:**
- **Implicit Wait**: General use for all elements
- **Explicit Wait**: Wait for specific dynamic elements
- **Fluent Wait**: Custom polling for complex scenarios

---

## 🚀 **PART 7: Running Tests**

### **Run via Maven:**
```bash
# Run all tests
mvn clean test

# Run specific test class
mvn clean test -Dtest=LoginPageTest

# Run specific test method
mvn clean test -Dtest=LoginPageTest#testValidLogin
```

### **Run in Eclipse:**
```
Right-click testng.xml → Run As → TestNG Suite
```

### **Generate Report:**
```bash
# Run tests and generate TestNG HTML report
mvn clean test
# Report generated in: target/surefire-reports/index.html
```

---

## 📊 **PART 8: Test Reports & Artifacts**

### **TestNG Report Includes:**
- ✅ Total tests run
- ✅ Tests passed/failed/skipped
- ✅ Execution time
- ✅ Stack trace for failures
- ✅ Screenshots for failed tests
- ✅ Grouped by test class

### **Report Location:**
```
target/
  ├── surefire-reports/
  │   ├── index.html           (Main report)
  │   ├── emailable-report.html (Email-friendly)
  │   └── testng-results.xml   (XML format)
  └── screenshots/
      └── FailedTest_2025-02-06_10-30-45.png
```

---

## ⚡ **PART 9: Common Viva Questions**

### **Q1: What is Selenium WebDriver?**
**A:** Selenium WebDriver is a tool for automating web browsers. It:
- Controls browser through code (Java, Python, etc.)
- Interacts with elements (click, type, submit)
- Verifies UI behavior
- Works with Chrome, Firefox, Safari, Edge
- Used for automated testing

### **Q2: Explain Page Object Model (POM)**
**A:** POM is a design pattern where:
- Each web page has a corresponding page object class
- Page object contains all element locators
- Page object has methods for page interactions
- Tests call page object methods (not directly using locators)
- Benefits: Maintainability, reusability, readability

### **Q3: What are the different waits in Selenium?**
**A:** Three types:
1. **Implicit Wait** - Global timeout for all elements
2. **Explicit Wait** - Wait for specific element with condition
3. **Fluent Wait** - With custom polling interval
- Explicit wait is most reliable for synchronization issues

### **Q4: What is TestNG? Why use it?**
**A:** TestNG is a testing framework for Java:
- Provides annotations (@Test, @BeforeMethod, @AfterMethod)
- Supports parameterization (run same test with different data)
- Groups tests
- Generates HTML reports
- Supports test dependency
- Better than JUnit

### **Q5: Explain the setup and teardown in tests**
**A:**
- **@BeforeMethod** - Runs before EACH test (setup browser, login)
- **@AfterMethod** - Runs after EACH test (close browser, cleanup)
- **@BeforeClass** - Runs once BEFORE all tests in class
- **@AfterClass** - Runs once AFTER all tests in class
- Ensures clean state for each test

### **Q6: How do you handle dynamic elements?**
**A:** Use explicit waits:
```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
wait.until(ExpectedConditions.elementToBeClickable(By.id("element")));
```
Wait for element to be ready before interacting, prevents ElementNotVisibleException

### **Q7: How do you take screenshots on test failure?**
**A:** In @AfterMethod:
```java
if (ITestResult.FAILURE == result.getStatus()) {
    TakesScreenshot screenshot = (TakesScreenshot) driver;
    File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
    FileUtils.copyFile(srcFile, new File("./reports/failed_test.png"));
}
```

### **Q8: What is the difference between `findElement()` and `findElements()`?**
**A:**
- **findElement()** - Returns first element matching locator (throws NoSuchElementException if not found)
- **findElements()** - Returns List of all matching elements (returns empty list if none found)

### **Q9: How do you parameterize tests in TestNG?**
**A:** Using @DataProvider:
```java
@DataProvider
public Object[][] testData() {
    return new Object[][] {
        {"test@example.com", "password1"},
        {"user2@example.com", "password2"}
    };
}

@Test(dataProvider = "testData")
public void testLogin(String email, String password) {
    loginPage.login(email, password);
}
```
Same test runs multiple times with different data

### **Q10: What is Maven and why use it?**
**A:** Maven is a build tool for Java projects:
- Manages dependencies (JARs)
- Executes tests via Surefire plugin
- Compiles code, creates JAR
- Standardized project structure
- `mvn clean test` - cleans and runs all tests

### **Q11: Explain the test execution flow**
**A:**
1. @BeforeClass - runs once
2. For each test:
   - @BeforeMethod - setup
   - @Test - execute test
   - @AfterMethod - teardown
3. @AfterClass - runs once

### **Q12: How do you handle alerts in Selenium?**
**A:** 
```java
// Switch to alert
Alert alert = driver.switchTo().alert();

// Get alert text
String text = alert.getText();

// Accept (OK)
alert.accept();

// Dismiss (Cancel)
alert.dismiss();

// Send text
alert.sendKeys("text");
```

### **Q13: What is implicit vs explicit wait? Which is better?**
**A:**
| Implicit | Explicit |
|----------|----------|
| Global, applies to all | Specific element |
| Simple setup | More complex |
| Can cause flakiness | More reliable |
| Wait: 10 seconds for any element | Wait: Specific condition for specific element |
| Explicit wait is BETTER (more control)

### **Q14: How do you select from a dropdown?**
**A:**
```java
Select select = new Select(driver.findElement(By.id("dropdown")));

// By visible text
select.selectByVisibleText("Option 1");

// By value
select.selectByValue("val1");

// By index
select.selectByIndex(0);

// Get selected option
String selected = select.getFirstSelectedOption().getText();
```

### **Q15: What is test priority in TestNG?**
**A:**
```java
@Test(priority = 1)
public void test1() { }  // Runs first

@Test(priority = 2)
public void test2() { }  // Runs second

@Test(priority = 3)
public void test3() { }  // Runs third

// Lower priority = runs first (executes in ascending order)
```

---

## ✅ **Preparation Checklist**

- [ ] Understand Selenium WebDriver basics
- [ ] Know Page Object Model pattern
- [ ] Explain different wait types
- [ ] Understand TestNG annotations
- [ ] Know how to write POM classes
- [ ] Know test setup & teardown
- [ ] Understand assertion statements
- [ ] Know how to handle dropdowns/alerts
- [ ] Understand Maven & pom.xml
- [ ] Know how to generate test reports
- [ ] Understand test parallelization
- [ ] Know how to parameterize tests
- [ ] Explain screenshot on failure
- [ ] Understand test execution flow
- [ ] Know common Selenium exceptions & handling

---

**Good luck bhai! 🚀 Testing mein brilliant ho ja! 💪**
