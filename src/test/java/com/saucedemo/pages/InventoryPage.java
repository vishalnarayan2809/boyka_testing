package com.saucedemo.pages;

import io.github.boykaframework.actions.elements.ClickableActions;
import io.github.boykaframework.builders.Locator;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import static io.github.boykaframework.actions.elements.ElementActions.onElement;
import static io.github.boykaframework.manager.ParallelSession.getSession;

/**
 * Page Object Model for SauceDemo Inventory/Products Page
 * Contains all locators and actions for the products page
 */
public class InventoryPage {
    
    // Locators
    private static final Locator INVENTORY_CONTAINER = Locator.buildLocator()
        .web(By.id("inventory_container"))
        .name("Inventory Container")
        .build();
    
    private static final Locator ADD_TO_CART_BACKPACK = Locator.buildLocator()
        .web(By.id("add-to-cart-sauce-labs-backpack"))
        .name("Add Backpack to Cart")
        .build();
    
    private static final Locator REMOVE_FROM_CART_BACKPACK = Locator.buildLocator()
        .web(By.id("remove-sauce-labs-backpack"))
        .name("Remove Backpack from Cart")
        .build();
    
    private static final Locator CART_BADGE = Locator.buildLocator()
        .web(By.cssSelector(".shopping_cart_badge"))
        .name("Cart Badge")
        .build();
    
    private static final Locator CART_LINK = Locator.buildLocator()
        .web(By.id("shopping_cart_container"))
        .name("Cart Link")
        .build();
    
    // Actions/Methods
    
    /**
     * Verify that inventory page is loaded
     * @return InventoryPage instance for method chaining
     */
    public InventoryPage verifyPageLoaded() {
        onElement(INVENTORY_CONTAINER).verifyIsDisplayed().isTrue();
        waitForIdPresent("add-to-cart-sauce-labs-backpack", 2000);
        onElement(ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Check if inventory page is displayed
     * @return true if inventory container is displayed
     */
    public boolean isInventoryDisplayed() {
        try {
            onElement(INVENTORY_CONTAINER).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Add backpack to cart with retry logic
     * @return InventoryPage instance for method chaining
     */
    public InventoryPage addBackpackToCart() {
        onElement(ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        ClickableActions.withMouse(ADD_TO_CART_BACKPACK).click();
        
        // Lightweight JS polling for button id swap
        if (!waitForIdPresent("remove-sauce-labs-backpack", 2500)) {
            // Retry once if not flipped quickly
            ClickableActions.withMouse(ADD_TO_CART_BACKPACK).click();
            waitForIdPresent("remove-sauce-labs-backpack", 4000);
        }
        
        // Final assertion
        onElement(REMOVE_FROM_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Remove backpack from cart
     * @return InventoryPage instance for method chaining
     */
    public InventoryPage removeBackpackFromCart() {
        if (!isRemoveButtonVisible()) {
            // Ensure item is added first
            addBackpackToCart();
        }
        
        ClickableActions.withMouse(REMOVE_FROM_CART_BACKPACK).click();
        
        // Wait for id to switch back
        if (!waitForIdPresent("add-to-cart-sauce-labs-backpack", 3000)) {
            if (isRemoveButtonVisible()) {
                // Still remove, retry click once
                ClickableActions.withMouse(REMOVE_FROM_CART_BACKPACK).click();
                waitForIdPresent("add-to-cart-sauce-labs-backpack", 4000);
            }
        }
        
        onElement(ADD_TO_CART_BACKPACK).verifyIsDisplayed().isTrue();
        return this;
    }
    
    /**
     * Check if remove button is visible
     * @return true if remove button is displayed
     */
    public boolean isRemoveButtonVisible() {
        try {
            onElement(REMOVE_FROM_CART_BACKPACK).isDisplayed();
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
            onElement(ADD_TO_CART_BACKPACK).isDisplayed();
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
                return onElement(CART_BADGE).getText();
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
            onElement(CART_BADGE).verifyIsDisplayed().isTrue();
            return true;
        } catch (AssertionError | FrameworkError e) {
            return false;
        }
    }
    
    /**
     * Verify cart badge shows expected count
     * @param expectedCount the expected count
     */
    public void verifyCartBadge(String expectedCount) {
        onElement(CART_BADGE).verifyIsDisplayed().isTrue();
        onElement(CART_BADGE).verifyText().isEqualTo(expectedCount);
    }
    
    /**
     * Click on cart link to go to cart page
     * @return CartPage instance
     */
    public CartPage goToCart() {
        ClickableActions.withMouse(CART_LINK).click();
        waitForUrlContains("cart", 4000);
        return new CartPage();
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
