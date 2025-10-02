package com.saucedemo.pages;

import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.actions.elements.TextBoxActions;
import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

/**
 * Page Object Model for SauceDemo Checkout Page
 * Contains all locators and actions for the checkout information and overview pages
 */
public class CheckoutPage {
    
    // Locators - Checkout Information Page
    private static final Locator FIRST_NAME_FIELD = Locator.buildLocator()
        .web(By.id("first-name"))
        .name("First Name Field")
        .build();
    
    private static final Locator LAST_NAME_FIELD = Locator.buildLocator()
        .web(By.id("last-name"))
        .name("Last Name Field")
        .build();
    
    private static final Locator ZIP_CODE_FIELD = Locator.buildLocator()
        .web(By.id("postal-code"))
        .name("Zip Code Field")
        .build();
    
    private static final Locator CONTINUE_BUTTON = Locator.buildLocator()
        .web(By.id("continue"))
        .name("Continue Button")
        .build();
    
    // Locators - Checkout Overview Page
    private static final Locator FINISH_BUTTON = Locator.buildLocator()
        .web(By.id("finish"))
        .name("Finish Button")
        .build();
    
    private static final Locator CANCEL_BUTTON = Locator.buildLocator()
        .web(By.id("cancel"))
        .name("Cancel Button")
        .build();
    
    // Actions/Methods
    
    /**
     * Enter first name
     * @param firstName the first name to enter
     * @return CheckoutPage instance for method chaining
     */
    public CheckoutPage enterFirstName(String firstName) {
        TextBoxActions.onTextBox(FIRST_NAME_FIELD).enterText(firstName);
        return this;
    }
    
    /**
     * Enter last name
     * @param lastName the last name to enter
     * @return CheckoutPage instance for method chaining
     */
    public CheckoutPage enterLastName(String lastName) {
        TextBoxActions.onTextBox(LAST_NAME_FIELD).enterText(lastName);
        return this;
    }
    
    /**
     * Enter zip code
     * @param zipCode the zip code to enter
     * @return CheckoutPage instance for method chaining
     */
    public CheckoutPage enterZipCode(String zipCode) {
        TextBoxActions.onTextBox(ZIP_CODE_FIELD).enterText(zipCode);
        return this;
    }
    
    /**
     * Fill all checkout information fields
     * @param firstName the first name
     * @param lastName the last name
     * @param zipCode the zip code
     * @return CheckoutPage instance for method chaining
     */
    public CheckoutPage fillCheckoutInformation(String firstName, String lastName, String zipCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }
    
    /**
     * Click continue button to go to overview page
     * @return CheckoutPage instance (now on overview page)
     */
    public CheckoutPage clickContinue() {
        ClickableActions.withMouse(CONTINUE_BUTTON).click();
        return this;
    }
    
    /**
     * Click finish button to complete the order
     * @return ConfirmationPage instance
     */
    public ConfirmationPage clickFinish() {
        ClickableActions.withMouse(FINISH_BUTTON).click();
        return new ConfirmationPage();
    }
    
    /**
     * Click cancel button to return to cart
     * @return CartPage instance
     */
    public CartPage clickCancel() {
        ClickableActions.withMouse(CANCEL_BUTTON).click();
        return new CartPage();
    }
    
    /**
     * Complete entire checkout flow with provided information
     * @param firstName the first name
     * @param lastName the last name
     * @param zipCode the zip code
     * @return ConfirmationPage instance
     */
    public ConfirmationPage completeCheckout(String firstName, String lastName, String zipCode) {
        fillCheckoutInformation(firstName, lastName, zipCode);
        clickContinue();
        return clickFinish();
    }
}
