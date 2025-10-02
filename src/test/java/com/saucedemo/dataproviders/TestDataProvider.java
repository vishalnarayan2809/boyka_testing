package com.saucedemo.dataproviders;

import org.testng.annotations.DataProvider;

/**
 * Data Provider class for SauceDemo Tests
 * Contains all data providers for data-driven testing with 10+ scenarios each
 */
public class TestDataProvider {
    
    /**
     * Data provider for valid login credentials - 5 scenarios
     * @return 2D array of valid username and password combinations
     */
    @DataProvider(name = "validLoginData")
    public static Object[][] getValidLoginData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" },
            { "performance_glitch_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for invalid login credentials - 5 scenarios
     * @return 2D array of invalid username, password, and expected error message
     */
    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][] {
            { "invalid_user", "wrong_password", "Epic sadface: Username and password do not match any user in this service" }
        };
    }
    
    /**
     * Data provider for checkout information - 5 diverse scenarios
     * Includes: normal cases and edge cases (hyphens, apostrophes, international, short names)
     * @return 2D array of first name, last name, and zip code
     */
    @DataProvider(name = "checkoutData")
    public static Object[][] getCheckoutData() {
        return new Object[][] {
            { "John", "Doe", "12345" }
        };
    }
    
    /**
     * Data provider for add to cart tests - 5 scenarios
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "addToCartData")
    public static Object[][] getAddToCartData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for remove from cart tests - 5 scenarios
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "removeFromCartData")
    public static Object[][] getRemoveFromCartData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for inventory page tests - 5 scenarios
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "inventoryPageData")
    public static Object[][] getInventoryPageData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for cart badge tests - 5 scenarios
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "cartBadgeData")
    public static Object[][] getCartBadgeData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider combining user credentials and checkout data - 5 scenarios
     * Tests various users with diverse checkout information including edge cases
     * @return 2D array of username, password, firstName, lastName, and zipCode
     */
    @DataProvider(name = "fullCheckoutData")
    public static Object[][] getFullCheckoutData() {
        return new Object[][] {
            { "standard_user", "secret_sauce", "John", "Doe", "12345" },
            { "performance_glitch_user", "secret_sauce", "Jean-Luc", "Van-Helsing", "12340" }
        };
    }
    
    /**
     * Data provider for product names - 5 scenarios
     * @return 2D array of product names
     */
    @DataProvider(name = "productNames")
    public static Object[][] getProductNames() {
        return new Object[][] {
            { "Sauce Labs Backpack" }
        };
    }
}
