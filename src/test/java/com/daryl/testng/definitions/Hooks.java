package com.daryl.testng.definitions;

import com.daryl.testng.utils.HelperClass;
import com.daryl.testng.utils.PerformanceLogger;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

    @Before
    public static void setUp(Scenario scenario) {
        PerformanceLogger.getInstance().startTask("Test Scenario: " + scenario.getName());
        PerformanceLogger.getInstance().logAction("Test Setup", "Initializing WebDriver");
        HelperClass.setUpDriver();
        PerformanceLogger.getInstance().logAction("Test Setup", "WebDriver initialized successfully");
    }

    @After
    public static void tearDown(Scenario scenario) {
        PerformanceLogger.getInstance().logAction("Test Teardown", "Scenario Status: " + scenario.getStatus());
        
        if (scenario.isFailed()) {
            PerformanceLogger.getInstance().logError("Test Scenario", "Scenario failed: " + scenario.getName());
        }
        
        PerformanceLogger.getInstance().logAction("Test Teardown", "Closing WebDriver");
        HelperClass.tearDown();
        PerformanceLogger.getInstance().endTask("Test Scenario: " + scenario.getName());
        // Don't close PerformanceLogger here to avoid stream closed errors
    }

    @AfterAll
    public static void closePerformanceLogger() {
        PerformanceLogger.getInstance().close();
    }
}
