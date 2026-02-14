package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import utils.ConfigReader;
import utils.ScreenshotHelper;
import utils.WaitHelper;

import java.time.Duration;

/**
 * BaseTest - Parent class for all test classes
 * Handles browser setup, teardown, and common functionality
 * 
 * IMPORTANT: Before running tests, download the WebDriver for your browser:
 * - Chrome: https://chromedriver.chromium.org/downloads
 * - Firefox: https://github.com/mozilla/geckodriver/releases
 * - Edge: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/
 * 
 * Place the driver executable in the 'drivers' folder
 */
public class BaseTest {
    
    protected WebDriver driver;
    protected WaitHelper waitHelper;
    protected String baseUrl;
    
    /**
     * Initialize the WebDriver based on configuration
     * Called before each test
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
                System.out.println("Unknown browser. Defaulting to Chrome.");
                setupChrome(headless);
        }
        
        // Configure common settings
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.getImplicitWait()));
        driver.manage().deleteAllCookies();
        
        // Initialize helpers
        waitHelper = new WaitHelper(driver);
        baseUrl = ConfigReader.getBaseUrl();
        
        System.out.println("Browser started successfully. Base URL: " + baseUrl);
    }
    
    /**
     * Setup Chrome browser
     */
    private void setupChrome(boolean headless) {
        // Set driver path - Update this path based on your driver location
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
     * Cleanup after test
     * Called after each test
     */
    public void tearDown() {
        try {
            if (driver != null) {
                System.out.println("Closing browser...");
                driver.quit();
            }
        } catch (Exception e) {
            System.out.println("Browser already closed or error during cleanup");
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
        // Wait for page to load properly
        sleep(2000);
    }
    
    /**
     * Sleep/pause for specified milliseconds
     */
    protected void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to base URL (home page)
     */
    protected void navigateToHome() {
        navigateTo("");
    }
    
    /**
     * Navigate to login page
     */
    protected void navigateToLogin() {
        navigateTo("/login");
    }
    
    /**
     * Navigate to register page
     */
    protected void navigateToRegister() {
        navigateTo("/register");
    }
    
    /**
     * Navigate to dashboard
     */
    protected void navigateToDashboard() {
        navigateTo("/dashboard");
    }
    
    /**
     * Navigate to documents page
     */
    protected void navigateToDocuments() {
        navigateTo("/documents");
    }
    
    /**
     * Get current URL
     */
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    
    /**
     * Get page title
     */
    protected String getPageTitle() {
        return driver.getTitle();
    }
    
    /**
     * Take screenshot - useful for debugging
     */
    protected String captureScreenshot(String name) {
        return ScreenshotHelper.takeScreenshot(driver, name);
    }
    
    /**
     * Print test result with detailed failure message
     */
    protected void printTestResult(String testName, boolean passed) {
        System.out.println("");
        if (passed) {
            System.out.println("✓ TEST PASSED: " + testName);
            System.out.println("  Status: SUCCESS");
        } else {
            System.out.println("✗ TEST FAILED: " + testName);
            System.out.println("  Status: FAILED");
            System.out.println("  Reason: Expected condition was not met");
            System.out.println("  Current URL: " + (driver != null ? getCurrentUrl() : "N/A"));
            try {
                String screenshotPath = captureScreenshot("FAILED_" + testName);
                if (screenshotPath != null) {
                    System.out.println("  Screenshot: " + screenshotPath);
                }
            } catch (Exception e) {
                System.out.println("  Screenshot: Could not capture");
            }
        }
        System.out.println("");
        // Pause between tests so user can see what's happening
        sleep(1500);
    }
    
    /**
     * Print test result with custom failure reason
     */
    protected void printTestResult(String testName, boolean passed, String failureReason) {
        System.out.println("");
        if (passed) {
            System.out.println("✓ TEST PASSED: " + testName);
            System.out.println("  Status: SUCCESS");
        } else {
            System.out.println("✗ TEST FAILED: " + testName);
            System.out.println("  Status: FAILED");
            System.out.println("  Reason: " + failureReason);
            System.out.println("  Current URL: " + (driver != null ? getCurrentUrl() : "N/A"));
            try {
                String screenshotPath = captureScreenshot("FAILED_" + testName);
                if (screenshotPath != null) {
                    System.out.println("  Screenshot: " + screenshotPath);
                }
            } catch (Exception e) {
                System.out.println("  Screenshot: Could not capture");
            }
        }
        System.out.println("");
        // Pause between tests so user can see what's happening
        sleep(1500);
    }
    
    /**
     * Safe teardown that handles browser already closed
     */
    protected void safeTearDown() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.out.println("Browser already closed");
        }
        driver = null;
    }
    
    /**
     * Print test starting message
     */
    protected void printTestStarting(String testName, String description) {
        System.out.println("");
        System.out.println("----------------------------------------");
        System.out.println("▶ STARTING: " + testName);
        System.out.println("  Description: " + description);
        System.out.println("----------------------------------------");
    }
}
