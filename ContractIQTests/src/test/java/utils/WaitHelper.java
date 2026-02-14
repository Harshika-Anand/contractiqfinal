package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * WaitHelper - Utility class for explicit waits
 * Provides methods for waiting elements to be visible, clickable etc.
 */
public class WaitHelper {
    
    private WebDriver driver;
    private WebDriverWait wait;
    
    public WaitHelper(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWait()));
    }
    
    public WaitHelper(WebDriver driver, int timeoutInSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
    }
    
    /**
     * Wait for element to be visible
     */
    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    public WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Wait for element to be present in DOM
     */
    public WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    /**
     * Wait for element to disappear
     */
    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for text to be present in element
     */
    public boolean waitForTextPresent(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    
    /**
     * Wait for URL to contain specific text
     */
    public boolean waitForUrlContains(String urlPart) {
        return wait.until(ExpectedConditions.urlContains(urlPart));
    }
    
    /**
     * Wait for URL to be exact
     */
    public boolean waitForUrlToBe(String url) {
        return wait.until(ExpectedConditions.urlToBe(url));
    }
    
    /**
     * Wait for page title to contain text
     */
    public boolean waitForTitleContains(String titlePart) {
        return wait.until(ExpectedConditions.titleContains(titlePart));
    }
    
    /**
     * Wait for alert to be present
     */
    public void waitForAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
    }
    
    /**
     * Simple sleep - use sparingly
     */
    public static void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
