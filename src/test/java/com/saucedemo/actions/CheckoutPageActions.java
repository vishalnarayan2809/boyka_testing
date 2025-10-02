package com.saucedemo.actions;

import com.saucedemo.pages.CheckoutPage;
import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.actions.elements.TextBoxActions;

/**
 * Actions for CheckoutPage
 * Contains all methods/actions for the checkout information and overview pages
 */
public class CheckoutPageActions {
    
    private final CheckoutPage page;
    
    public CheckoutPageActions(CheckoutPage page) {
        this.page = page;
    }
    
    /**
     * Enter first name
     * @param firstName the first name to enter
     * @return CheckoutPageActions instance for method chaining
     */
    public CheckoutPageActions enterFirstName(String firstName) {
        TextBoxActions.onTextBox(page.FIRST_NAME_FIELD).enterText(firstName);
        return this;
    }
    
    /**
     * Enter last name
     * @param lastName the last name to enter
     * @return CheckoutPageActions instance for method chaining
     */
    public CheckoutPageActions enterLastName(String lastName) {
        TextBoxActions.onTextBox(page.LAST_NAME_FIELD).enterText(lastName);
        return this;
    }
    
    /**
     * Enter zip code
     * @param zipCode the zip code to enter
     * @return CheckoutPageActions instance for method chaining
     */
    public CheckoutPageActions enterZipCode(String zipCode) {
        TextBoxActions.onTextBox(page.ZIP_CODE_FIELD).enterText(zipCode);
        return this;
    }
    
    /**
     * Fill all checkout information fields
     * @param firstName the first name
     * @param lastName the last name
     * @param zipCode the zip code
     * @return CheckoutPageActions instance for method chaining
     */
    public CheckoutPageActions fillCheckoutInformation(String firstName, String lastName, String zipCode) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterZipCode(zipCode);
        return this;
    }
    
    /**
     * Click continue button to go to overview page
     * @return CheckoutPageActions instance (now on overview page)
     */
    public CheckoutPageActions clickContinue() {
        ClickableActions.withMouse(page.CONTINUE_BUTTON).click();
        return this;
    }
    
    /**
     * Click finish button to complete the order
     * @return ConfirmationPageActions instance
     */
    public ConfirmationPageActions clickFinish() {
        ClickableActions.withMouse(page.FINISH_BUTTON).click();
        return new ConfirmationPageActions(new com.saucedemo.pages.ConfirmationPage());
    }
    
    /**
     * Click cancel button to return to cart
     * @return CartPageActions instance
     */
    public CartPageActions clickCancel() {
        ClickableActions.withMouse(page.CANCEL_BUTTON).click();
        return new CartPageActions(new com.saucedemo.pages.CartPage());
    }
    
    /**
     * Complete entire checkout flow with provided information
     * @param firstName the first name
     * @param lastName the last name
     * @param zipCode the zip code
     * @return ConfirmationPageActions instance
     */
    public ConfirmationPageActions completeCheckout(String firstName, String lastName, String zipCode) {
        fillCheckoutInformation(firstName, lastName, zipCode);
        clickContinue();
        return clickFinish();
    }
}
