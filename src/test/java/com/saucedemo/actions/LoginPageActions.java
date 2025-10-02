package com.saucedemo.actions;

import com.saucedemo.pages.LoginPage;
import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.actions.elements.TextBoxActions;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;

/**
 * Actions for LoginPage
 * Contains all methods/actions for the login page
 */
public class LoginPageActions {
    
    private final LoginPage page;
    
    public LoginPageActions(LoginPage page) {
        this.page = page;
    }
    
    /**
     * Enter username in the username field
     * @param username the username to enter
     * @return LoginPageActions instance for method chaining
     */
    public LoginPageActions enterUsername(String username) {
        TextBoxActions.onTextBox(page.USERNAME_FIELD).enterText(username);
        return this;
    }
    
    /**
     * Enter password in the password field
     * @param password the password to enter
     * @return LoginPageActions instance for method chaining
     */
    public LoginPageActions enterPassword(String password) {
        TextBoxActions.onTextBox(page.PASSWORD_FIELD).enterText(password);
        return this;
    }
    
    /**
     * Click the login button
     * @return InventoryPageActions instance
     */
    public InventoryPageActions clickLoginButton() {
        ClickableActions.withMouse(page.LOGIN_BUTTON).click();
        return new InventoryPageActions(new com.saucedemo.pages.InventoryPage());
    }
    
    /**
     * Perform complete login action
     * @param username the username
     * @param password the password
     * @return InventoryPageActions instance
     */
    public InventoryPageActions login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        return clickLoginButton();
    }
    
    /**
     * Perform login and stay on login page (for invalid login scenarios)
     * @param username the username
     * @param password the password
     * @return LoginPageActions instance
     */
    public LoginPageActions attemptLogin(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        ClickableActions.withMouse(page.LOGIN_BUTTON).click();
        return this;
    }
    
    /**
     * Verify error message is displayed
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        try {
            onElement(page.ERROR_MESSAGE).verifyIsDisplayed().isTrue();
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
        return onElement(page.ERROR_MESSAGE).getText();
    }
    
    /**
     * Verify error message contains expected text
     * @param expectedText the expected text
     */
    public void verifyErrorMessage(String expectedText) {
        onElement(page.ERROR_MESSAGE).verifyIsDisplayed().isTrue();
        onElement(page.ERROR_MESSAGE).verifyText().contains(expectedText);
    }
}
