package com.daryl.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPageLocators {
    @FindBy(name = "username")
    public WebElement userName;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(xpath = "html/body/form/button")
    public WebElement login;

    @FindBy(xpath = "//body[contains(text(), 'Login failed!') or contains(text(), 'Login success!')]")
    public WebElement errorMessage;
}
