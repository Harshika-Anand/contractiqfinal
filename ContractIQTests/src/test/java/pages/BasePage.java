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
    
    // Common locators across pages
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
        pause(500); // Brief pause after click for visual feedback
    }
    
    /**
     * Enter text in input field with visual delay
     */
    protected void enterText(By locator, String text) {
        WebElement element = waitForElement(locator);
        element.clear();
        pause(300);
        element.sendKeys(text);
        pause(500); // Brief pause after typing for visual feedback
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
     * Wait for loading to complete
     */
    protected void waitForLoadingComplete() {
        try {
            Thread.sleep(500); // Brief wait for loading to start
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception e) {
            // Loading might be too fast to catch, which is fine
        }
    }
    
    /**
     * Simple pause
     */
    protected void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
