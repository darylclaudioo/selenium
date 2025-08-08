package com.daryl.testng.utils;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class HelperClass {

    private static HelperClass helperClass;
    private static WebDriver driver;
    public final static int TIMEOUT = 5; // Reduced from 10 to 5 seconds for faster execution

    private HelperClass() {
        // Setup Chrome driver
        WebDriverManager.chromedriver().browserVersion("138.0.0.0").setup();

        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);

        // Remove navigator.webdriver via JS
        ((JavascriptExecutor) driver).executeScript(
            "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
    }

    public static void openPage(String url) {
        long startTime = System.currentTimeMillis();
        PerformanceLogger.getInstance().startTask("Page Navigation: " + url);
        
        driver.get(url);
        
        long loadTime = System.currentTimeMillis() - startTime;
        String currentUrl = driver.getCurrentUrl();
        String pageName = extractPageName(currentUrl);
        
        PerformanceLogger.getInstance().logPageLoad(pageName, currentUrl, loadTime);
        PerformanceLogger.getInstance().endTask("Page Navigation: " + url);
    }
    
    private static String extractPageName(String url) {
        if (url.contains("exercise_result.php")) return "Test Result Page";
        if (url.contains("exercise_submit.php")) return "Test Submit Page";
        if (url.contains("exercise.php")) return "Test List Page";
        if (url.contains("overview.php")) return "Test Overview Page";
        if (url.contains("index.php")) return "Dashboard Page";
        if (url.endsWith("/")) return "Login Page";
        return "Unknown Page";
    }

    public static WebDriver getDriver() {
        return driver;
    }

    public static void setUpDriver() {
        if (helperClass == null) {
            helperClass = new HelperClass();
        }
    }

    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        helperClass = null;
    }
}
