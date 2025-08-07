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
    public final static int TIMEOUT = 10;

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
        driver.get(url);
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
