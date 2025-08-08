package com.daryl.testng.definitions;

import org.testng.Assert;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

import com.daryl.testng.actions.LoginPageActions;
import com.daryl.testng.utils.HelperClass;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LoginPageDefinitions {
    LoginPageActions objLogin = new LoginPageActions();

    @Given("User has opened the browser")
    public void userHasOpenedTheBrowser() {
        HelperClass.setUpDriver();;
    }

    @And("User has navigated to the login page of SmartEdu app {string}")
    public void userHasNavigatedToTheLoginPageOfSmartEduApp(String url) {
        HelperClass.openPage(url);
    }

    @When("User enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        objLogin.login(username, password);
    }

    @And("User clicks on the login button")
    public void userClicksOnTheLoginButton() {
        objLogin.clickedLoginButton();
    }

    @Then("User should be able to see {string} notification message")
    public void userShouldBeAbleToSeeNotificationMessage(String message) {
        String actualMessage = objLogin.getErrorMessage();
        
        if (actualMessage.equals(message)) {
            Assert.assertEquals(actualMessage, message, "Test Passed");
        } else {
            Assert.fail("Unknown notification type: " + actualMessage);
        }
    }

    @Then("User should be directed to the dashboard page")
    public void userShouldBeDirectedToTheDashboardPage() {
        // Wait for page to load after login
        WebDriverWait wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(1));
        
        try {
            // Wait for URL to change from login page
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("192.168.0.22/")));
        } catch (Exception e) {
            // If URL doesn't change, try waiting for dashboard elements
            System.out.println("URL didn't change, checking for dashboard elements...");
        }
        
        // Get current URL and page source
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        String pageSource = HelperClass.getDriver().getPageSource();
        
        System.out.println("Current URL after login: " + currentUrl); // Debug output
        System.out.println("Page title: " + HelperClass.getDriver().getTitle()); // Debug output
        
        // Multiple verification strategies
        boolean urlVerification = currentUrl.contains("courses/") || 
                                 currentUrl.contains("index.php") || 
                                 currentUrl.contains("id_session=") ||
                                 currentUrl.contains("main/");
        
        boolean contentVerification = pageSource.contains("Student one") || 
                                    pageSource.contains("Logout") ||
                                    pageSource.contains("Tests") ||
                                    pageSource.contains("Ini ujian") ||
                                    pageSource.contains("Dashboard") ||
                                    pageSource.contains("SMART") && pageSource.contains("EDU");
        
        boolean notOnLoginPage = !currentUrl.endsWith("192.168.0.22/") && 
                                !pageSource.contains("formLogin") &&
                                !pageSource.contains("name=\"login\"");
        
        System.out.println("URL verification: " + urlVerification);
        System.out.println("Content verification: " + contentVerification);
        System.out.println("Not on login page: " + notOnLoginPage);
        
        Assert.assertTrue(notOnLoginPage && (urlVerification || contentVerification), 
                         "User is not on the dashboard page. Current URL: " + currentUrl);
    }

    @When("User clicks on the Tests button")
    public void userClicksOnTheTestsButton() {
        objLogin.clickTestsButton();
    }

    @Then("User should be directed to the Tests page")
    public void userShouldBeDirectedToTheTestsPage() {
        // Verify that we are on the tests/exercise page
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("exercise.php"), 
                         "User is not on the Tests page - Expected URL to contain 'exercise.php' but was: " + currentUrl);
        
        // Additional verification: check if the page contains exercise/test elements
        String pageSource = HelperClass.getDriver().getPageSource();
        Assert.assertTrue(pageSource.contains("Nama latihan") || pageSource.contains("Status") || pageSource.contains("Not attempted"), 
                         "Tests page content not found");
    }

    @When("User clicks on {string} from the test list")
    public void userClicksOnTestFromTheTestList(String testName) {
        objLogin.clickTestFromList(testName);
    }

    @Then("User should be directed to the test overview page")
    public void userShouldBeDirectedToTheTestOverviewPage() {
        // Verify that we are on the test overview page
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("overview.php"), 
                         "User is not on the test overview page - Expected URL to contain 'overview.php' but was: " + currentUrl);
        
        // Additional verification: check if the page contains either "Proceed with the test" or "Start test" button
        String pageSource = HelperClass.getDriver().getPageSource();
        boolean hasButton = pageSource.contains("Proceed with the test") || pageSource.contains("Start test");
        Assert.assertTrue(hasButton, 
                         "Test overview page content not found - Neither 'Proceed with the test' nor 'Start test' button found");
    }

    @When("User clicks on {string} button")
    public void userClicksOnButton(String buttonText) {
        if (buttonText.equals("Proceed with the test") || buttonText.equals("Start test") || buttonText.equals("Start Test")) {
            objLogin.clickProceedWithTestButton(); // This method already handles both button types
        }
    }

    @Then("User should be directed to the test execution page")
    public void userShouldBeDirectedToTheTestExecutionPage() {
        // Verify that we are on the test execution page
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        System.out.println("Current URL after clicking Proceed: " + currentUrl);
        
        // The test execution page might have different URL patterns
        Assert.assertTrue(currentUrl.contains("exercise") || currentUrl.contains("test") || currentUrl.contains("submit"), 
                         "User is not on the test execution page - Current URL: " + currentUrl);
    }

    @When("User answers all questions in the test")
    public void userAnswersAllQuestionsInTheTest() {
        objLogin.answerAllTestQuestions();
    }

    @Then("User should be directed to the test result page")
    public void userShouldBeDirectedToTheTestResultPage() {
        // Wait a moment for potential redirect after submit
        try {
            Thread.sleep(3000); // Increased wait time for redirect
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Verify that we are on the test result page
        String currentUrl = HelperClass.getDriver().getCurrentUrl();
        System.out.println("Current URL after completing test: " + currentUrl);
        
        // Accept both submit page and result page URLs
        boolean isOnResultPage = currentUrl.contains("result.php") || 
                               currentUrl.contains("exercise_result") || 
                               currentUrl.contains("exercise_submit");
        
        if (!isOnResultPage) {
            // Try to wait a bit more for redirect
            try {
                Thread.sleep(2000);
                currentUrl = HelperClass.getDriver().getCurrentUrl();
                System.out.println("URL after additional wait: " + currentUrl);
                isOnResultPage = currentUrl.contains("result.php") || 
                               currentUrl.contains("exercise_result") || 
                               currentUrl.contains("exercise_submit");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        Assert.assertTrue(isOnResultPage, 
                         "User is not on the test result/submit page - Current URL: " + currentUrl);
        
        // Additional verification: check if the page contains result elements
        String pageSource = HelperClass.getDriver().getPageSource();
        System.out.println("Page contains 'Result': " + pageSource.contains("Result"));
        System.out.println("Page contains 'Saved': " + pageSource.contains("Saved"));
        System.out.println("Page contains 'Exercise': " + pageSource.contains("Exercise"));
        
        boolean hasResultContent = pageSource.contains("Result") || 
                                 pageSource.contains("Saved") || 
                                 pageSource.contains("answers saved") ||
                                 pageSource.contains("submitted") ||
                                 pageSource.contains("Exercise") ||
                                 pageSource.contains("test") ||
                                 pageSource.contains("score") ||
                                 pageSource.contains("IQ test");
                         
        Assert.assertTrue(hasResultContent, 
                         "Test result/submit page content not found");
    }
}
