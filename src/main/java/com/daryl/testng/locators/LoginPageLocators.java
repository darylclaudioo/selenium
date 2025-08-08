package com.daryl.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class LoginPageLocators {
    @FindBy(name = "login")
    public WebElement userName;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(xpath = "//button[@id='formLogin_submitAuth']")
    public WebElement loginButton;

    @FindBy(xpath = "//div[text()='Login failed - incorrect login or password.']")
    public WebElement errorMessage;

    // Locator for Tests button - multiple options to increase reliability
    @FindBy(xpath = "//a[contains(@title,'Tests') or text()='Tests' or contains(@href,'exercise')]")
    public WebElement testsButton;

    // Locator for IQ test link from test list
    @FindBy(xpath = "//a[contains(text(),'IQ test') or @title='IQ test']")
    public WebElement iqTestLink;

    // Locator for "Proceed with the test" button
    @FindBy(xpath = "//a[contains(text(),'Proceed with the test') or contains(@class,'btn-success')]")
    public WebElement proceedWithTestButton;

    // Locator for "Start test" button (when first time taking the test)
    @FindBy(xpath = "//a[contains(text(),'Start test') or contains(text(),'Start') and contains(@class,'btn')]")
    public WebElement startTestButton;

    // Locators for test questions
    @FindBy(xpath = "//textarea")
    public WebElement essayTextArea;

    @FindBy(xpath = "//input[@type='checkbox'] | //input[@type='radio'] | //option")
    public List<WebElement> multipleChoiceOptions;

    // More robust locators for navigation buttons - multiple strategies
    @FindBy(xpath = "//button[contains(text(),'Next question')] | //input[@value='Next question'] | //a[contains(text(),'Next question')] | //button[contains(@class,'btn') and contains(text(),'Next')] | //input[contains(@value,'Next')]")
    public WebElement nextQuestionButton;

    @FindBy(xpath = "//button[contains(text(),'End test')] | //input[@value='End test'] | //a[contains(text(),'End test')] | //button[contains(@class,'btn-warning')] | //button[contains(@class,'btn') and contains(text(),'End')] | //input[contains(@value,'End')]")
    public WebElement endTestButton;
}
