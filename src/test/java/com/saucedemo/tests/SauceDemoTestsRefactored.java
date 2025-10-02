package com.saucedemo.tests;

import com.saucedemo.dataproviders.TestDataProvider;
import com.saucedemo.pages.*;
import io.github.boykaframework.actions.drivers.WindowActions;
import io.github.boykaframework.enums.PlatformType;
import io.github.boykaframework.exception.FrameworkError;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.github.boykaframework.manager.ParallelSession.*;
import static org.testng.Assert.*;

/**
 * SauceDemo UI automation tests using Boyka Framework with Page Object Model
 * This test class implements data-driven testing using imported data providers
 */
public class SauceDemoTestsRefactored {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private ConfirmationPage confirmationPage;

    @BeforeMethod
    public void setUp() {
        int attempts = 0;
        while (true) {
            try {
                createSession(PlatformType.WEB, "test_web");
                WindowActions.onWindow().maximize();
                
                // Wait for page to load
                waitForIdPresent("user-name", 3000);
                
                // Initialize page objects
                loginPage = new LoginPage();
                break;
            } catch (FrameworkError fe) {
                String msg = fe.getMessage();
                if (msg != null && msg.contains("ERR_CONNECTION_RESET") && attempts < 1) {
                    attempts++;
                    clearSession();
                    sleep(1200);
                    continue;
                }
                throw fe;
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        clearSession();
    }

    /**
     * Test 1: Valid login with data-driven approach
     * Uses data provider to test multiple valid user credentials
     */
    @Test(
        priority = 1, 
        description = "Valid login test with multiple user credentials",
        dataProvider = "validLoginData",
        dataProviderClass = TestDataProvider.class
    )
    public void testValidLoginWithDataProvider(String username, String password) {
        // Perform login using page object
        inventoryPage = loginPage.login(username, password);
        
        // Verify successful login
        assertTrue(inventoryPage.isInventoryDisplayed(), 
            "Inventory page should be displayed after successful login");
    }

    /**
     * Test 2: Invalid login with data-driven approach
     * Uses data provider to test various invalid login scenarios
     */
    @Test(
        priority = 2, 
        description = "Invalid login test with multiple invalid credentials",
        dataProvider = "invalidLoginData",
        dataProviderClass = TestDataProvider.class
    )
    public void testInvalidLoginWithDataProvider(String username, String password, String expectedError) {
        // Attempt login with invalid credentials
        loginPage.attemptLogin(username, password);
        
        // Verify error message is displayed
        assertTrue(loginPage.isErrorMessageDisplayed(), 
            "Error message should be displayed for invalid credentials");
        
        // Verify error message contains expected text
        String actualError = loginPage.getErrorMessageText();
        assertTrue(actualError.contains(expectedError), 
            String.format("Expected error '%s' but got '%s'", expectedError, actualError));
    }

    /**
     * Test 3: Add product to cart using standard user
     * Uses page object methods for cart operations
     */
    @Test(
        priority = 3, 
        description = "Add product to cart and verify",
        dataProvider = "standardUser",
        dataProviderClass = TestDataProvider.class
    )
    public void testAddProductToCart(String username, String password) {
        // Login
        inventoryPage = loginPage.login(username, password);
        inventoryPage.verifyPageLoaded();
        
        // Add product to cart
        inventoryPage.addBackpackToCart();
        
        // Verify remove button is visible (indicates item was added)
        assertTrue(inventoryPage.isRemoveButtonVisible(), 
            "Remove button should be visible after adding item to cart");
        
        // Verify cart badge shows count
        String cartCount = inventoryPage.getCartBadgeCount();
        assertEquals(cartCount, "1", "Cart badge should show 1 item");
    }

    /**
     * Test 4: Remove product from cart
     * Demonstrates cart management using page objects
     */
    @Test(
        priority = 4, 
        description = "Remove product from cart and verify",
        dataProvider = "standardUser",
        dataProviderClass = TestDataProvider.class
    )
    public void testRemoveProductFromCart(String username, String password) {
        // Login and add item
        inventoryPage = loginPage.login(username, password);
        inventoryPage.verifyPageLoaded();
        inventoryPage.addBackpackToCart();
        
        // Remove product from cart
        inventoryPage.removeBackpackFromCart();
        
        // Verify add button is visible again
        assertTrue(inventoryPage.isAddButtonVisible(), 
            "Add button should be visible after removing item from cart");
        
        // Verify cart badge is gone
        assertFalse(inventoryPage.isCartBadgeDisplayed(), 
            "Cart badge should not be displayed when cart is empty");
    }

    /**
     * Test 5: Complete checkout flow with data-driven checkout information
     * Uses data provider for checkout details
     */
    @Test(
        priority = 5, 
        description = "Complete checkout flow with multiple checkout data sets",
        dataProvider = "checkoutData",
        dataProviderClass = TestDataProvider.class
    )
    public void testCompleteCheckoutFlow(String firstName, String lastName, String zipCode) {
        // Login with standard user
        inventoryPage = loginPage.login("standard_user", "secret_sauce");
        inventoryPage.verifyPageLoaded();
        
        // Add product to cart
        inventoryPage.addBackpackToCart();
        
        // Go to cart
        cartPage = inventoryPage.goToCart();
        cartPage.verifyCartItemPresent();
        
        // Proceed to checkout
        checkoutPage = cartPage.proceedToCheckout();
        
        // Complete checkout with provided data
        confirmationPage = checkoutPage.completeCheckout(firstName, lastName, zipCode);
        
        // Verify order completion
        assertTrue(confirmationPage.isConfirmationDisplayed(), 
            "Confirmation page should be displayed");
        
        String confirmationMsg = confirmationPage.getConfirmationMessage();
        assertTrue(confirmationMsg.contains("Thank you for your order"), 
            String.format("Expected confirmation message but got: %s", confirmationMsg));
    }

    /**
     * Test 6: End-to-end checkout with combined data
     * Uses full checkout data provider with user credentials and checkout info
     */
    @Test(
        priority = 6, 
        description = "End-to-end checkout with combined user and checkout data",
        dataProvider = "fullCheckoutData",
        dataProviderClass = TestDataProvider.class
    )
    public void testEndToEndCheckout(String username, String password, 
                                     String firstName, String lastName, String zipCode) {
        // Complete flow using page objects
        inventoryPage = loginPage.login(username, password);
        inventoryPage.verifyPageLoaded();
        inventoryPage.addBackpackToCart();
        
        cartPage = inventoryPage.goToCart();
        assertTrue(cartPage.hasItems(), "Cart should have items");
        
        checkoutPage = cartPage.proceedToCheckout();
        confirmationPage = checkoutPage.completeCheckout(firstName, lastName, zipCode);
        
        // Verify successful order
        confirmationPage.verifyConfirmationMessage("Thank you for your order!");
    }

    /**
     * Test 7: Verify inventory page elements after login
     */
    @Test(
        priority = 7, 
        description = "Verify inventory page loads correctly",
        dataProvider = "standardUser",
        dataProviderClass = TestDataProvider.class
    )
    public void testInventoryPageElements(String username, String password) {
        // Login
        inventoryPage = loginPage.login(username, password);
        
        // Verify page loaded
        inventoryPage.verifyPageLoaded();
        
        // Verify add button is available
        assertTrue(inventoryPage.isAddButtonVisible(), 
            "Add to cart button should be visible on inventory page");
        
        // Verify cart badge is not displayed initially
        assertFalse(inventoryPage.isCartBadgeDisplayed(), 
            "Cart badge should not be displayed when cart is empty");
    }

    /**
     * Test 8: Test cart badge updates correctly
     */
    @Test(
        priority = 8, 
        description = "Verify cart badge updates when adding items",
        dataProvider = "standardUser",
        dataProviderClass = TestDataProvider.class
    )
    public void testCartBadgeUpdate(String username, String password) {
        // Login
        inventoryPage = loginPage.login(username, password);
        inventoryPage.verifyPageLoaded();
        
        // Initial state - no badge
        assertFalse(inventoryPage.isCartBadgeDisplayed(), 
            "Cart badge should not be visible initially");
        
        // Add item
        inventoryPage.addBackpackToCart();
        
        // Badge should appear with count 1
        assertTrue(inventoryPage.isCartBadgeDisplayed(), 
            "Cart badge should be visible after adding item");
        assertEquals(inventoryPage.getCartBadgeCount(), "1", 
            "Cart badge should show count of 1");
        
        // Remove item
        inventoryPage.removeBackpackFromCart();
        
        // Badge should disappear
        sleep(1000); // Give time for badge to disappear
        assertFalse(inventoryPage.isCartBadgeDisplayed(), 
            "Cart badge should disappear after removing all items");
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
    
    private void sleep(long millis) {
        try { Thread.sleep(millis); } catch (InterruptedException ignored) { }
    }
}
