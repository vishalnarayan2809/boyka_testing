package com.saucedemo.actions;

import com.saucedemo.pages.ConfirmationPage;
import io.github.boykaframework.exception.FrameworkError;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;

/**
 * Actions for ConfirmationPage
 * Contains all methods/actions for the order confirmation page
 */
public class ConfirmationPageActions {
    
    private final ConfirmationPage page;
    
    public ConfirmationPageActions(ConfirmationPage page) {
        this.page = page;
    }
    
    /**
     * Verify confirmation page is displayed
     * @return ConfirmationPageActions instance for method chaining
     */
    public ConfirmationPageActions verifyOrderComplete() {
        onElement(page.CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Get confirmation message text
     * @return the confirmation message text
     */
    public String getConfirmationMessage() {
        return onElement(page.CONFIRMATION_MESSAGE).getText();
    }
    
    /**
     * Get confirmation description text
     * @return the confirmation description text
     */
    public String getConfirmationText() {
        return onElement(page.CONFIRMATION_TEXT).getText();
    }
    
    /**
     * Verify confirmation message contains expected text
     * @param expectedText the expected text
     * @return ConfirmationPageActions instance for method chaining
     */
    public ConfirmationPageActions verifyConfirmationMessage(String expectedText) {
        onElement(page.CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
        onElement(page.CONFIRMATION_MESSAGE).verifyText().contains(expectedText);
        return this;
    }
    
    /**
     * Check if confirmation message is displayed
     * @return true if confirmation message is visible
     */
    public boolean isConfirmationDisplayed() {
        try {
            onElement(page.CONFIRMATION_MESSAGE).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
}
