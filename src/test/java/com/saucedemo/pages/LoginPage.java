package com.saucedemo.pages;

import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.actions.elements.TextBoxActions;
import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;

/**
 * Page Object Model for SauceDemo Login Page
 * Contains all locators and actions for the login page
 */
public class LoginPage {
    
    // Locators
    private static final Locator USERNAME_FIELD = Locator.buildLocator()
        .web(By.id("user-name"))
        .name("Username Field")
        .build();
    
    private static final Locator PASSWORD_FIELD = Locator.buildLocator()
        .web(By.id("password"))
        .name("Password Field")
        .build();
    
    private static final Locator LOGIN_BUTTON = Locator.buildLocator()
        .web(By.id("login-button"))
        .name("Login Button")
        .build();
    
    private static final Locator ERROR_MESSAGE = Locator.buildLocator()
        .web(By.cssSelector("[data-test='error']"))
        .name("Error Message")
        .build();
    
    // Actions/Methods
    
    /**
     * Enter username in the username field
     * @param username the username to enter
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterUsername(String username) {
        TextBoxActions.onTextBox(USERNAME_FIELD).enterText(username);
        return this;
    }
    
    /**
     * Enter password in the password field
     * @param password the password to enter
     * @return LoginPage instance for method chaining
     */
    public LoginPage enterPassword(String password) {
        TextBoxActions.onTextBox(PASSWORD_FIELD).enterText(password);
        return this;
    }
    
    /**
     * Click the login button
     * @return InventoryPage instance
     */
    public InventoryPage clickLoginButton() {
        ClickableActions.withMouse(LOGIN_BUTTON).click();
        return new InventoryPage();
    }
    
    /**
     * Perform complete login action
     * @param username the username
     * @param password the password
     * @return InventoryPage instance
     */
    public InventoryPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }
    
    /**
     * Perform login and stay on login page (for invalid login scenarios)
     * @param username the username
     * @param password the password
     * @return LoginPage instance
     */
    public LoginPage attemptLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        ClickableActions.withMouse(LOGIN_BUTTON).click();
        return this;
    }
    
    /**
     * Verify error message is displayed
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            onElement(ERROR_MESSAGE).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
    
    /**
     * Get the error message text
     * @return the error message text
     */
    public String getErrorMessageText() {
        return onElement(ERROR_MESSAGE).getText();
    }
    
    /**
     * Verify error message contains expected text
     * @param expectedText the expected text
     */
    public void verifyErrorMessage(String expectedText) {
        onElement(ERROR_MESSAGE).verifyIsDisplayed().isTrue();
        onElement(ERROR_MESSAGE).verifyText().contains(expectedText);
    }
}
