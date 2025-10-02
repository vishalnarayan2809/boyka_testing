package com.saucedemo.actions;

import com.saucedemo.pages.InventoryPage;
import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.JavascriptExecutor;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;
import static io.github.boykaframework.manager.ParallelSession.getSession;

/**
 * Actions for InventoryPage
 * Contains all methods/actions for the inventory/products page
 */
public class InventoryPageActions {
    
    private final InventoryPage page;
    
    public InventoryPageActions(InventoryPage page) {
        this.page = page;
    }
    
    /**
     * Verify that inventory page is loaded
     * @return InventoryPageActions instance for method chaining
     */
    public InventoryPageActions verifyPageLoaded() {
        onElement(page.INVENTORY_CONTAINER).verifyIsDisplayed().isTrue();
        waitForIdPresent("add-to-cart-sauce-labs-backpack", 2000);
        onElement(page.ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Check if inventory page is displayed
     * @return true if inventory container is displayed
     */
    public boolean isInventoryDisplayed() {
        try {
            onElement(page.INVENTORY_CONTAINER).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Add backpack to cart with retry logic
     * @return InventoryPageActions instance for method chaining
     */
    public InventoryPageActions addBackpackToCart() {
        onElement(page.ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        ClickableActions.withMouse(page.ADD_TO_CART_BACKPACK).click();
        
        // Lightweight JS polling for button id swap
        if (!waitForIdPresent("remove-sauce-labs-backpack", 2500)) {
            // Retry once if not flipped quickly
            ClickableActions.withMouse(page.ADD_TO_CART_BACKPACK).click();
            waitForIdPresent("remove-sauce-labs-backpack", 4000);
        }
        
        // Final assertion
        onElement(page.REMOVE_FROM_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Remove backpack from cart
     * @return InventoryPageActions instance for method chaining
     */
    public InventoryPageActions removeBackpackFromCart() {
        if (!isRemoveButtonVisible()) {
            // Ensure item is added first
            addBackpackToCart();
        }
        
        ClickableActions.withMouse(page.REMOVE_FROM_CART_BACKPACK).click();
        
        // Wait for id to switch back
        if (!waitForIdPresent("add-to-cart-sauce-labs-backpack", 3000)) {
            if (isRemoveButtonVisible()) {
                // Still remove, retry click once
                ClickableActions.withMouse(page.REMOVE_FROM_CART_BACKPACK).click();
                waitForIdPresent("add-to-cart-sauce-labs-backpack", 4000);
            }
        }
        
        onElement(page.ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Check if remove button is visible
     * @return true if remove button is displayed
     */
    public boolean isRemoveButtonVisible() {
        try {
            onElement(page.REMOVE_FROM_CART_BACKPACK).isDisplayed();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Check if add to cart button is visible
     * @return true if add button is displayed
     */
    public boolean isAddButtonVisible() {
        try {
            onElement(page.ADD_TO_CART_BACKPACK).isDisplayed();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Get cart badge count
     * @return cart badge text or empty string if not present
     */
    public String getCartBadgeCount() {
        try {
            if (isCartBadgeDisplayed()) {
                return onElement(page.CART_BADGE).getText();
            }
        } catch (Exception e) {
            // Return empty if badge not found
        }
        return "";
    }
    
    /**
     * Check if cart badge is displayed
     * @return true if cart badge is visible
     */
    public boolean isCartBadgeDisplayed() {
        try {
            // Use non-blocking check with JavaScript for better performance
            JavascriptExecutor js = (JavascriptExecutor) getSession().getDriver();
            Object element = js.executeScript(
                "return document.querySelector('.shopping_cart_badge') !== null;"
            );
            return Boolean.TRUE.equals(element);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verify cart badge shows expected count
     * @param expectedCount the expected count
     */
    public void verifyCartBadge(String expectedCount) {
        onElement(page.CART_BADGE).verifyIsDisplayed().isTrue();
        onElement(page.CART_BADGE).verifyText().isEqualTo(expectedCount);
    }
    
    /**
     * Click on cart link to go to cart page
     * @return CartPageActions instance
     */
    public CartPageActions goToCart() {
        ClickableActions.withMouse(page.CART_LINK).click();
        waitForUrlContains("cart", 4000);
        return new CartPageActions(new com.saucedemo.pages.CartPage());
    }
    
    // Helper methods
    
    private boolean waitForIdPresent(String id, long timeoutMs) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) getSession().getDriver();
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < timeoutMs) {
                Object exists = js.executeScript("return document.getElementById(arguments[0]) !== null;", id);
                if (Boolean.TRUE.equals(exists)) return true;
                sleep(100);
            }
        } catch (Exception ignored) { }
        return false;
    }
    
    private boolean waitForUrlContains(String fragment, long timeoutMs) {
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < timeoutMs) {
                String url = getSession().getDriver().getCurrentUrl();
                if (url.contains(fragment)) return true;
                sleep(100);
            }
        } catch (Exception ignored) {}
        return false;
    }
    
    private void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) { }
    }
}
