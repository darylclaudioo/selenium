package com.daryl.testng.actions;

import org.openqa.selenium.support.PageFactory;

import com.daryl.testng.locators.RegisterPageLocators;
import com.daryl.testng.utils.HelperClass;

public class RegisterPageActions {
    RegisterPageLocators registerPageLocators = null;

    public RegisterPageActions() {
        this.registerPageLocators = new RegisterPageLocators();
        PageFactory.initElements(HelperClass.getDriver(), registerPageLocators);
    }

    public void register(String username, String password) {
        registerPageLocators.username.sendKeys(username);
        registerPageLocators.password.sendKeys(password);
    }

    public void clickedRegisterButton() {
        registerPageLocators.registerButton.click();
    }

    public String getErrorMessage() {
        return registerPageLocators.errorMessage.getText();
    }
}
