package com.daryl.testng.locators;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegisterPageLocators {
    @FindBy(name = "username")
    public WebElement username;

    @FindBy(name = "password")
    public WebElement password;

    @FindBy(xpath = "//button[text()='Daftar']")
    public WebElement registerButton;

    @FindBy(xpath = "//p[contains(text(), 'Registrasi berhasil') or contains(text(), 'Username sudah terdaftar')]")
    public WebElement errorMessage;
}
