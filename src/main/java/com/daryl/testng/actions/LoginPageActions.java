package com.daryl.testng.actions;

import org.openqa.selenium.support.PageFactory;
import com.daryl.testng.locators.LoginPageLocators;
import com.daryl.testng.utils.HelperClass;

public class LoginPageActions {

    LoginPageLocators loginPageLocators = null;

    public LoginPageActions() {

        this.loginPageLocators = new LoginPageLocators();

        PageFactory.initElements(HelperClass.getDriver(), loginPageLocators);
    }

    public void login(String strUserName, String strPassword) {
        loginPageLocators.userName.sendKeys(strUserName);
        loginPageLocators.password.sendKeys(strPassword);
    }

    public void clickedLoginButton() {
        loginPageLocators.login.click();
    }

    // Get the error message of Login Page
    public String getErrorMessage() {
        return loginPageLocators.errorMessage.getText();
    }

}
