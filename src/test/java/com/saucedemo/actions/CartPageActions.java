package com.saucedemo.actions;

import com.saucedemo.pages.CartPage;
import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.JavascriptExecutor;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;
import static io.github.boykaframework.manager.ParallelSession.getSession;

/**
 * Actions for CartPage
 * Contains all methods/actions for the shopping cart page
 */
public class CartPageActions {
    
    private final CartPage page;
    
    public CartPageActions(CartPage page) {
        this.page = page;
    }
    
    /**
     * Verify cart page is loaded and item is present
     * @return CartPageActions instance for method chaining
     */
    public CartPageActions verifyCartItemPresent() {
        if (!waitForCssPresent(".cart_item", 3000)) {
            // Fall back to framework assertion
            onElement(page.CART_ITEM).verifyIsDisplayed().isTrue();
        }
        return this;
    }
    
    /**
     * Check if cart has items
     * @return true if cart item is displayed
     */
    public boolean hasItems() {
        try {
            onElement(page.CART_ITEM).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Click checkout button to proceed to checkout
     * @return CheckoutPageActions instance
     */
    public CheckoutPageActions proceedToCheckout() {
        ClickableActions.withMouse(page.CHECKOUT_BUTTON).click();
        return new CheckoutPageActions(new com.saucedemo.pages.CheckoutPage());
    }
    
    /**
     * Click continue shopping button
     * @return InventoryPageActions instance
     */
    public InventoryPageActions continueShopping() {
        ClickableActions.withMouse(page.CONTINUE_SHOPPING_BUTTON).click();
        return new InventoryPageActions(new com.saucedemo.pages.InventoryPage());
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
