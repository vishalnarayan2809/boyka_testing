package com.saucedemo.dataproviders;

import org.testng.annotations.DataProvider;

/**
 * Data Provider class for SauceDemo Tests
 * Contains all data providers for data-driven testing
 */
public class TestDataProvider {
    
    /**
     * Data provider for valid login credentials
     * @return 2D array of valid username and password combinations
     */
    @DataProvider(name = "validLoginData")
    public static Object[][] getValidLoginData() {
        return new Object[][] {
            { "standard_user", "secret_sauce" },
            { "problem_user", "secret_sauce" },
            { "performance_glitch_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for invalid login credentials
     * @return 2D array of invalid username, password, and expected error message
     */
    @DataProvider(name = "invalidLoginData")
    public static Object[][] getInvalidLoginData() {
        return new Object[][] {
            { 
                "invalid_user", 
                "wrong_password", 
                "Epic sadface: Username and password do not match any user in this service" 
            },
            { 
                "locked_out_user", 
                "secret_sauce", 
                "Epic sadface: Sorry, this user has been locked out." 
            },
            { 
                "", 
                "secret_sauce", 
                "Epic sadface: Username is required" 
            },
            { 
                "standard_user", 
                "", 
                "Epic sadface: Password is required" 
            }
        };
    }
    
    /**
     * Data provider for checkout information
     * @return 2D array of first name, last name, and zip code
     */
    @DataProvider(name = "checkoutData")
    public static Object[][] getCheckoutData() {
        return new Object[][] {
            { "John", "Doe", "12345" },
            { "Jane", "Smith", "54321" },
            { "Bob", "Johnson", "67890" },
            { "Alice", "Williams", "98765" }
        };
    }
    
    /**
     * Data provider for single user testing
     * @return 2D array with standard user credentials
     */
    @DataProvider(name = "standardUser")
    public static Object[][] getStandardUser() {
        return new Object[][] {
            { "standard_user", "secret_sauce" }
        };
    }
    
    /**
     * Data provider for product names
     * @return 2D array of product names
     */
    @DataProvider(name = "productNames")
    public static Object[][] getProductNames() {
        return new Object[][] {
            { "Sauce Labs Backpack" },
            { "Sauce Labs Bike Light" },
            { "Sauce Labs Bolt T-Shirt" },
            { "Sauce Labs Fleece Jacket" }
        };
    }
    
    /**
     * Data provider combining user credentials and checkout data
     * @return 2D array of username, password, firstName, lastName, and zipCode
     */
    @DataProvider(name = "fullCheckoutData")
    public static Object[][] getFullCheckoutData() {
        return new Object[][] {
            { "standard_user", "secret_sauce", "John", "Doe", "12345" },
            { "standard_user", "secret_sauce", "Jane", "Smith", "54321" },
            { "problem_user", "secret_sauce", "Bob", "Johnson", "67890" }
        };
    }
}
