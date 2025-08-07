package com.daryl.testng.definitions;

import org.testng.Assert;

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
}
