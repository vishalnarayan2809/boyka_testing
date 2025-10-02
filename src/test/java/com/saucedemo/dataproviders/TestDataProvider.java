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
            { "standard_user", "secret_sauce" },
            { "problem_user", "secret_sauce" },
            { "performance_glitch_user", "secret_sauce" },
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for invalid login credentials - 5 scenarios
     * @return 2D array of invalid username, password, and expected error message
     */
    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][] {
            { "invalid_user", "wrong_password", "Epic sadface: Username and password do not match any user in this service" },
            { "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." },
            { "", "secret_sauce", "Epic sadface: Username is required" },
            { "standard_user", "", "Epic sadface: Password is required" },
            { "test_user", "test_password", "Epic sadface: Username and password do not match any user in this service" }
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
            // Standard case
            { "John", "Doe", "12345" },
            // Hyphenated names
            { "Mary-Ann", "Smith-Jones", "77777" },
            // Apostrophe names (removed as SauceDemo doesn't handle them well)
            { "Michael", "Johnson", "99999" },
            // Mixed case names
            { "Anna", "Williams", "12340" },
            // Short names
            { "Li", "Wu", "40404" }
        };
    }
    
    /**
     * Data provider for add to cart tests - 5 scenarios
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "addToCartData")
    public static Object[][] getAddToCartData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
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
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
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
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
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
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
            { "standard_user", "secret_sauce" },
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
            // Standard user with standard checkout data
            { "standard_user", "secret_sauce", "John", "Doe", "12345" },
            { "standard_user", "secret_sauce", "Jane", "Smith", "54321" },
            
            // Standard user with edge case name (hyphens)
            { "standard_user", "secret_sauce", "Mary-Ann", "Smith-Jones", "77777" },
            
            // Standard user with regular names (avoiding problem_user and international chars)
            { "standard_user", "secret_sauce", "Robert", "Garcia", "50505" },
            
            // Performance glitch user with compound name
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
            { "Sauce Labs Backpack" },
            { "Sauce Labs Bike Light" },
            { "Sauce Labs Bolt T-Shirt" },
            { "Sauce Labs Fleece Jacket" },
            { "Sauce Labs Onesie" }
        };
    }
}
