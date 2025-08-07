// package com.daryl.testng.definitions;

// import org.testng.Assert;

// import com.daryl.testng.actions.RegisterPageActions;

// import io.cucumber.java.en.And;
// import io.cucumber.java.en.Then;
// import io.cucumber.java.en.When;

// public class RegisterPageDefinitions {
//     RegisterPageActions objRegister = new RegisterPageActions();

//     @When("User enters new username {string} and password {string}")
//     public void user_enters_new_username_and_password(String username, String password) {
//         objRegister.register(username, password);
//     }

//     @And("User clicks on the register button")
//     public void user_clicks_on_the_register_button() {
//         objRegister.clickedRegisterButton();
//     }

//     @Then("User should be able to see {string} registration notification message")
//     public void user_should_be_able_to_see_registration_notification_message(String message) {
//         String actualMessage = objRegister.getErrorMessage();
//         if (actualMessage.equals(message)) {
//             Assert.assertTrue(actualMessage.contains(message), "Test Passed");
//         } else {
//             Assert.fail("Unknown notification type: " + actualMessage);
//         }
//     }

//     @When("User enters existing username {string} and password {string}")
//     public void user_enters_existing_username_and_password(String username, String password) {
//         objRegister.register(username, password);
//     }

//     @Then("User should be able to see {string} notification message")
//     public void user_should_be_able_to_see_error_message(String message) {
//         String actualMessage = objRegister.getErrorMessage();
//         if (actualMessage.equals(message)) {
//             Assert.assertTrue(actualMessage.contains(message), "Test Passed");
//         } else {
//             Assert.fail("Unknown notification type: " + actualMessage);
//         }
//     }
// }


package com.daryl.testng.definitions;

import com.daryl.testng.utils.HelperClass;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class RegisterPageDefinitions {

    WebDriver driver = HelperClass.getDriver();

    @Given("User has opened the browser")
    public void userHasOpenedTheBrowser() {
        HelperClass.setUpDriver();
    }

    @And("User has navigated to the register page of SmartEdu app {string}")
    public void userHasNavigatedToTheRegisterPageOfSmartEduApp(String url) {
        HelperClass.openPage(url);
    }

    @When("User registers with username {string} and password {string}")
    public void userRegistersWithUsernameAndPassword(String username, String password) {
        driver.findElement(By.name("username")).sendKeys(username);
        driver.findElement(By.name("password")).sendKeys(password);
    }

    @When("User clicks on the register button")
    public void userClicksOnTheRegisterButton() {
        driver.findElement(By.xpath("//button[contains(text(),'Daftar')]")).click();
    }

    @Then("User should see notification message {string}")
    public void userShouldSeeNotificationMessage(String expectedMessage) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//p[contains(text(),'Registrasi berhasil untuk')]")
        ));

        String actualText = notification.getText();
        Assert.assertTrue(actualText.contains(expectedMessage),
            "Expected message to contain: " + expectedMessage + " but found: " + actualText);
    }
}
