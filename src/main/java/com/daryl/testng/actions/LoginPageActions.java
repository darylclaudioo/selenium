package com.daryl.testng.actions;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daryl.testng.locators.LoginPageLocators;
import com.daryl.testng.utils.HelperClass;
import com.daryl.testng.utils.PerformanceLogger;

public class LoginPageActions {
    private static final Logger logger = LoggerFactory.getLogger(LoginPageActions.class);
    
    LoginPageLocators loginPageLocators = null;
    private WebDriverWait wait;

    public LoginPageActions() {
        this.loginPageLocators = new LoginPageLocators();
        PageFactory.initElements(HelperClass.getDriver(), loginPageLocators);
        this.wait = new WebDriverWait(HelperClass.getDriver(), Duration.ofSeconds(5));
    }

    // Helper method to measure response time
    private long measureResponseTime(Runnable action, String actionName) {
        long startTime = System.currentTimeMillis();
        action.run();
        waitForPageLoad();
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        PerformanceLogger.getInstance().logButtonResponse(actionName, responseTime);
        return responseTime;
    }

    // Wait for page to be fully loaded
    private void waitForPageLoad() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) HelperClass.getDriver();
            wait.until(driver -> js.executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            // Continue if wait fails
        }
    }

    public void login(String strUserName, String strPassword) {
        loginPageLocators.userName.sendKeys(strUserName);
        loginPageLocators.password.sendKeys(strPassword);
    }

    public void clickedLoginButton() {
        long responseTime = measureResponseTime(() -> {
            loginPageLocators.loginButton.click();
        }, "Login Button");
    }

    public void clickTestsButton() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.testsButton));
            long responseTime = measureResponseTime(() -> {
                loginPageLocators.testsButton.click();
            }, "Tests Button");
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Tests button: " + e.getMessage(), e);
        }
    }

    // Click specific test from the test list
    public void clickTestFromList(String testName) {
        try {
            if (testName.equals("IQ test")) {
                wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.iqTestLink));
                long responseTime = measureResponseTime(() -> {
                    loginPageLocators.iqTestLink.click();
                }, "IQ Test Link");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to click test: " + testName + " - " + e.getMessage(), e);
        }
    }

    // Click "Proceed with the test" or "Start test" button
    public void clickProceedWithTestButton() {
        try {
            wait.until(ExpectedConditions.or(
                ExpectedConditions.elementToBeClickable(loginPageLocators.proceedWithTestButton),
                ExpectedConditions.elementToBeClickable(loginPageLocators.startTestButton)
            ));
            
            if (loginPageLocators.proceedWithTestButton.isDisplayed()) {
                long responseTime = measureResponseTime(() -> {
                    loginPageLocators.proceedWithTestButton.click();
                }, "Proceed with Test Button");
                System.out.println("Clicked 'Proceed with the test' button");
            } else if (loginPageLocators.startTestButton.isDisplayed()) {
                long responseTime = measureResponseTime(() -> {
                    loginPageLocators.startTestButton.click();
                }, "Start Test Button");
                System.out.println("Clicked 'Start test' button");
            } else {
                throw new RuntimeException("Neither 'Proceed with the test' nor 'Start test' button was found");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to click Proceed/Start test button: " + e.getMessage(), e);
        }
    }

    // Answer all questions in the test using do-while loop
    public void answerAllTestQuestions() {
        PerformanceLogger.getInstance().logAction("Test Status", "Starting test questions");
        int questionCount = 0;
        boolean hasMoreQuestions;
        
        try {
            do {
                questionCount++;
                System.out.println("\n=== Processing Question " + questionCount + " ===");
                
                // Wait for question to load
                try {
                    Thread.sleep(200); // Small delay for page stability
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                long answerStartTime = System.currentTimeMillis();
                String questionType = "Unknown";
                
                if (isEssayQuestion()) {
                    questionType = "Essay";
                    answerEssayQuestion();
                } else if (isMultipleChoiceQuestion()) {
                    questionType = "Multiple Choice";
                    answerMultipleChoiceQuestion();
                } else {
                    System.out.println("Unknown question type detected");
                }
                
                long answerDuration = System.currentTimeMillis() - answerStartTime;
                PerformanceLogger.getInstance().logQuestionProcessing(questionCount, questionType, answerDuration);
                
                // Check navigation buttons
                PerformanceLogger.getInstance().logNavigationCheck();
                hasMoreQuestions = isNextQuestionButtonPresent();
                boolean endTestPresent = isEndTestButtonPresent();
                
                PerformanceLogger.getInstance().logButtonStatus(hasMoreQuestions, endTestPresent);
                System.out.println("Next question button present: " + hasMoreQuestions);
                System.out.println("End test button present: " + endTestPresent);
                
                // Priority: If both buttons are present, prefer End test button
                if (endTestPresent) {
                    PerformanceLogger.getInstance().logNavigationDecision("End test button found - completing test");
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.endTestButton));
                        long responseTime = measureResponseTime(() -> {
                            loginPageLocators.endTestButton.click();
                        }, "End Test Button");
                        System.out.println("Clicked End test button");
                    } catch (Exception e) {
                        // Refresh elements and try again
                        PageFactory.initElements(HelperClass.getDriver(), loginPageLocators);
                        wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.endTestButton));
                        long responseTime = measureResponseTime(() -> {
                            loginPageLocators.endTestButton.click();
                        }, "End Test Button (Refreshed)");
                        System.out.println("Clicked End test button after refresh");
                    }
                    
                    // Wait for redirect
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    String currentUrl = HelperClass.getDriver().getCurrentUrl();
                    System.out.println("Test completed at URL: " + currentUrl);
                    break;
                } else if (hasMoreQuestions) {
                    PerformanceLogger.getInstance().logNavigationDecision("Proceeding to next question (Q" + questionCount + " → Q" + (questionCount + 1) + ")");
                    try {
                        wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.nextQuestionButton));
                        long responseTime = measureResponseTime(() -> {
                            loginPageLocators.nextQuestionButton.click();
                        }, String.format("Next Question Button (Q%d→Q%d)", questionCount, questionCount + 1));
                        System.out.println("Clicked Next question button");
                    } catch (Exception e) {
                        // Refresh elements and try again
                        PageFactory.initElements(HelperClass.getDriver(), loginPageLocators);
                        wait.until(ExpectedConditions.elementToBeClickable(loginPageLocators.nextQuestionButton));
                        long responseTime = measureResponseTime(() -> {
                            loginPageLocators.nextQuestionButton.click();
                        }, String.format("Next Question (Refreshed) (Q%d→Q%d)", questionCount, questionCount + 1));
                        System.out.println("Clicked Next question button after refresh");
                    }
                } else {
                    System.out.println("No Next question or End test button found - ending test");
                    break;
                }
                
            } while (hasMoreQuestions);
            
            PerformanceLogger.getInstance().logAction("Test Completion", "Completed " + questionCount + " questions");
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to complete test questions: " + e.getMessage(), e);
        }
    }

    public String getErrorMessage() {
        try {
            // Look for common error message elements
            List<WebElement> errorElements = HelperClass.getDriver().findElements(By.cssSelector(".alert-danger, .error, .invalid-feedback"));
            if (!errorElements.isEmpty()) {
                return errorElements.get(0).getText();
            }
            return "No error message found";
        } catch (Exception e) {
            return "Could not retrieve error message: " + e.getMessage();
        }
    }

    // Check if there's an essay question on the page
    private boolean isEssayQuestion() {
        try {
            List<WebElement> textareas = HelperClass.getDriver().findElements(By.tagName("textarea"));
            return !textareas.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if there's a multiple choice question on the page
    private boolean isMultipleChoiceQuestion() {
        try {
            List<WebElement> radioButtons = HelperClass.getDriver().findElements(By.xpath("//input[@type='radio']"));
            return !radioButtons.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Answer essay question
    private void answerEssayQuestion() {
        try {
            List<WebElement> textareas = HelperClass.getDriver().findElements(By.tagName("textarea"));
            if (!textareas.isEmpty()) {
                WebElement textarea = textareas.get(0);
                textarea.clear();
                textarea.sendKeys("This is a sample answer for the essay question.");
                System.out.println("Answered essay question");
            }
        } catch (Exception e) {
            System.err.println("Failed to answer essay question: " + e.getMessage());
        }
    }

    // Answer multiple choice question
    private void answerMultipleChoiceQuestion() {
        try {
            List<WebElement> radioButtons = HelperClass.getDriver().findElements(By.xpath("//input[@type='radio']"));
            if (!radioButtons.isEmpty()) {
                // Click the first radio button
                radioButtons.get(0).click();
                System.out.println("Selected first option for multiple choice question");
            }
        } catch (Exception e) {
            System.err.println("Failed to answer multiple choice question: " + e.getMessage());
        }
    }

    // Check if Next question button is present and clickable
    private boolean isNextQuestionButtonPresent() {
        try {
            List<WebElement> nextButtons = HelperClass.getDriver().findElements(
                By.xpath("//button[contains(@class, 'btn') and contains(text(), 'Next question')]")
            );
            return !nextButtons.isEmpty() && nextButtons.get(0).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // Check if End test button is present and clickable
    private boolean isEndTestButtonPresent() {
        try {
            List<WebElement> endButtons = HelperClass.getDriver().findElements(
                By.xpath("//button[contains(@class, 'btn') and contains(text(), 'End test')]")
            );
            return !endButtons.isEmpty() && endButtons.get(0).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
}
