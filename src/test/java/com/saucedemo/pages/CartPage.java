package com.saucedemo.pages;

import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.builders.Locator;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;
import static io.github.boykaframework.manager.ParallelSession.getSession;

/**
 * Page Object Model for SauceDemo Cart Page
 * Contains all locators and actions for the shopping cart page
 */
public class CartPage {
    
    // Locators
    private static final Locator CART_ITEM = Locator.buildLocator()
        .web(By.cssSelector(".cart_item"))
        .name("Cart Item")
        .build();
    
    private static final Locator CHECKOUT_BUTTON = Locator.buildLocator()
        .web(By.id("checkout"))
        .name("Checkout Button")
        .build();
    
    private static final Locator CONTINUE_SHOPPING_BUTTON = Locator.buildLocator()
        .web(By.id("continue-shopping"))
        .name("Continue Shopping Button")
        .build();
    
    // Actions/Methods
    
    /**
     * Verify cart page is loaded and item is present
     * @return CartPage instance for method chaining
     */
    public CartPage verifyCartItemPresent() {
        if (!waitForCssPresent(".cart_item", 3000)) {
            // Fall back to framework assertion
            onElement(CART_ITEM).verifyIsDisplayed().isTrue();
        }
        return this;
    }
    
    /**
     * Check if cart has items
     * @return true if cart item is displayed
     */
    public boolean hasItems() {
        try {
            onElement(CART_ITEM).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Click checkout button to proceed to checkout
     * @return CheckoutPage instance
     */
    public CheckoutPage proceedToCheckout() {
        ClickableActions.withMouse(CHECKOUT_BUTTON).click();
        return new CheckoutPage();
    }
    
    /**
     * Click continue shopping button
     * @return InventoryPage instance
     */
    public InventoryPage continueShopping() {
        ClickableActions.withMouse(CONTINUE_SHOPPING_BUTTON).click();
        return new InventoryPage();
    }
    
    // Helper methods
    
    private boolean waitForCssPresent(String selector, long timeoutMs) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getSession().getDriver();
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < timeoutMs) {
                Long count = (Long) js.executeScript("return document.querySelectorAll(arguments[0]).length;", selector);
                if (count != null && count > 0) return true;
                sleep(100);
            }
        } catch (Exception ignored) {}
        return false;
    }
    
    private void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) { }
    }
}
