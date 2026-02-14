package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotHelper - Takes screenshots during test execution
 * Useful for capturing evidence of test results
 */
public class ScreenshotHelper {
    
    private static final String SCREENSHOT_DIR = "reports/screenshots/";
    
    static {
        // Create screenshots directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(SCREENSHOT_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Take screenshot with auto-generated name
     */
    public static String takeScreenshot(WebDriver driver) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return takeScreenshot(driver, "screenshot_" + timestamp);
    }
    
    /**
     * Take screenshot with specific name
     */
    public static String takeScreenshot(WebDriver driver, String fileName) {
        try {
            if (driver == null) {
                System.out.println("Cannot take screenshot: driver is null");
                return null;
            }
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String destination = SCREENSHOT_DIR + fileName + ".png";
            Files.copy(source.toPath(), Paths.get(destination));
            System.out.println("Screenshot saved: " + destination);
            return destination;
        } catch (Exception e) {
            // Catch all exceptions including WebDriverException when browser crashes
            System.out.println("Could not capture screenshot (browser may have closed): " + e.getClass().getSimpleName());
            return null;
        }
    }
    
    /**
     * Take screenshot on test failure
     */
    public static String takeScreenshotOnFailure(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return takeScreenshot(driver, "FAILED_" + testName + "_" + timestamp);
    }
}
