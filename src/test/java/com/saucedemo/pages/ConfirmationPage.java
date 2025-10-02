package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.By;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;

/**
 * Page Object Model for SauceDemo Order Confirmation Page
 * Contains all locators and actions for the order confirmation page
 */
public class ConfirmationPage {
    
    // Locators
    private static final Locator CONFIRMATION_MESSAGE = Locator.buildLocator()
        .web(By.cssSelector(".complete-header"))
        .name("Confirmation Message")
        .build();
    
    private static final Locator CONFIRMATION_TEXT = Locator.buildLocator()
        .web(By.cssSelector(".complete-text"))
        .name("Confirmation Text")
        .build();
    
    private static final Locator BACK_HOME_BUTTON = Locator.buildLocator()
        .web(By.id("back-to-products"))
        .name("Back Home Button")
        .build();
    
    // Actions/Methods
    
    /**
     * Verify confirmation page is displayed
     * @return ConfirmationPage instance for method chaining
     */
    public ConfirmationPage verifyOrderComplete() {
        onElement(CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Get confirmation message text
     * @return the confirmation message text
     */
    public String getConfirmationMessage() {
        return onElement(CONFIRMATION_MESSAGE).getText();
    }
    
    /**
     * Get confirmation description text
     * @return the confirmation description text
     */
    public String getConfirmationText() {
        return onElement(CONFIRMATION_TEXT).getText();
    }
    
    /**
     * Verify confirmation message contains expected text
     * @param expectedText the expected text
     * @return ConfirmationPage instance for method chaining
     */
    public ConfirmationPage verifyConfirmationMessage(String expectedText) {
        onElement(CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
        onElement(CONFIRMATION_MESSAGE).verifyText().contains(expectedText);
        return this;
    }
    
    /**
     * Check if confirmation message is displayed
     * @return true if confirmation message is visible
     */
    public boolean isConfirmationDisplayed() {
        try {
            onElement(CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
}
