package com.saucedemo.tests;

import com.saucedemo.actions.*;
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
 * This test class implements data-driven testing with separated actions
 * Pages contain only locators, Actions contain all methods
 */
public class SauceDemoTestsRefactored {

    // Action objects - contain all methods
    private LoginPageActions loginActions;
    private InventoryPageActions inventoryActions;
    private CartPageActions cartActions;
    private CheckoutPageActions checkoutActions;
    private ConfirmationPageActions confirmationActions;

    @BeforeMethod
    public void setUp() {
        int attempts = 0;
        while (true) {
            try {
                createSession(PlatformType.WEB, "test_web");
                WindowActions.onWindow().maximize();
                
                // Wait for page to load
                waitForIdPresent("user-name", 3000);
                
                // Initialize page objects (locators) and action objects (methods)
                LoginPage loginPage = new LoginPage();
                loginActions = new LoginPageActions(loginPage);
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
     * Test 1: Valid login with data-driven approach - 5 scenarios
     * Uses data provider to test multiple valid user credentials
     */
    @Test(
        priority = 1, 
        description = "Valid login test with 5 user credentials",
        dataProvider = "validLoginData",
        dataProviderClass = TestDataProvider.class
    )
    public void testValidLoginWithDataProvider(String username, String password) {
        // Perform login using actions
        inventoryActions = loginActions.login(username, password);
        
        // Verify successful login
        assertTrue(inventoryActions.isInventoryDisplayed(), 
            "Inventory page should be displayed after successful login");
    }

    /**
     * Test 2: Invalid login with data-driven approach - 5 scenarios
     * Uses data provider to test various invalid login scenarios
     */
    @Test(
        priority = 2, 
        description = "Invalid login test with 5 invalid credentials",
        dataProvider = "invalidLoginData",
        dataProviderClass = TestDataProvider.class
    )
    public void testInvalidLoginWithDataProvider(String username, String password, String expectedError) {
        // Attempt login with invalid credentials
        loginActions.attemptLogin(username, password);
        
        // Verify error message is displayed
        assertTrue(loginActions.isErrorMessageDisplayed(), 
            "Error message should be displayed for invalid credentials");
        
        // Verify error message contains expected text
        String actualError = loginActions.getErrorMessageText();
        assertTrue(actualError.contains(expectedError), 
            String.format("Expected error '%s' but got '%s'", expectedError, actualError));
    }

    /**
     * Test 3: Add product to cart using standard user - 5 scenarios
     * Uses page object methods for cart operations
     */
    @Test(
        priority = 3, 
        description = "Add product to cart and verify - 5 data sets",
        dataProvider = "addToCartData",
        dataProviderClass = TestDataProvider.class
    )
    public void testAddProductToCart(String username, String password) {
        // Login
        inventoryActions = loginActions.login(username, password);
        inventoryActions.verifyPageLoaded();
        
        // Add product to cart
        inventoryActions.addBackpackToCart();
        
        // Verify remove button is visible (indicates item was added)
        assertTrue(inventoryActions.isRemoveButtonVisible(), 
            "Remove button should be visible after adding item to cart");
        
        // Verify cart badge shows count
        String cartCount = inventoryActions.getCartBadgeCount();
        assertEquals(cartCount, "1", "Cart badge should show 1 item");
    }

    /**
     * Test 4: Remove product from cart - 5 scenarios
     * Demonstrates cart management using page objects
     */
    @Test(
        priority = 4, 
        description = "Remove product from cart and verify - 5 data sets",
        dataProvider = "removeFromCartData",
        dataProviderClass = TestDataProvider.class
    )
    public void testRemoveProductFromCart(String username, String password) {
        // Login and add item
        inventoryActions = loginActions.login(username, password);
        inventoryActions.verifyPageLoaded();
        inventoryActions.addBackpackToCart();
        
        // Remove product from cart
        inventoryActions.removeBackpackFromCart();
        
        // Verify add button is visible again
        assertTrue(inventoryActions.isAddButtonVisible(), 
            "Add button should be visible after removing item from cart");
        
        // Verify cart badge is gone
        assertFalse(inventoryActions.isCartBadgeDisplayed(), 
            "Cart badge should not be displayed when cart is empty");
    }

    /**
     * Test 5: Complete checkout flow with data-driven checkout information - 5 scenarios
     * Uses data provider for checkout details including edge cases (hyphens, apostrophes, international names)
     */
    @Test(
        priority = 5, 
        description = "Complete checkout flow with 5 diverse checkout data sets (edge cases included)",
        dataProvider = "checkoutData",
        dataProviderClass = TestDataProvider.class
    )
    public void testCompleteCheckoutFlow(String firstName, String lastName, String zipCode) {
        // Login with standard user
        inventoryActions = loginActions.login("standard_user", "secret_sauce");
        inventoryActions.verifyPageLoaded();
        
        // Add product to cart
        inventoryActions.addBackpackToCart();
        
        // Go to cart
        cartActions = inventoryActions.goToCart();
        cartActions.verifyCartItemPresent();
        
        // Proceed to checkout
        checkoutActions = cartActions.proceedToCheckout();
        
        // Complete checkout with provided data
        confirmationActions = checkoutActions.completeCheckout(firstName, lastName, zipCode);
        
        // Verify order completion
        assertTrue(confirmationActions.isConfirmationDisplayed(), 
            "Confirmation page should be displayed");
        
        String confirmationMsg = confirmationActions.getConfirmationMessage();
        assertTrue(confirmationMsg.contains("Thank you for your order"), 
            String.format("Expected confirmation message but got: %s", confirmationMsg));
    }

    /**
     * Test 6: End-to-end checkout with combined data - 5 scenarios
     * Uses full checkout data provider with user credentials and checkout info
     * Tests different users with diverse names (edge cases, special chars, international)
     */
    @Test(
        priority = 6, 
        description = "End-to-end checkout with 5 combined user and diverse checkout data sets",
        dataProvider = "fullCheckoutData",
        dataProviderClass = TestDataProvider.class
    )
    public void testEndToEndCheckout(String username, String password, 
                                     String firstName, String lastName, String zipCode) {
        // Complete flow using actions
        inventoryActions = loginActions.login(username, password);
        inventoryActions.verifyPageLoaded();
        inventoryActions.addBackpackToCart();
        
        cartActions = inventoryActions.goToCart();
        assertTrue(cartActions.hasItems(), "Cart should have items");
        
        checkoutActions = cartActions.proceedToCheckout();
        confirmationActions = checkoutActions.completeCheckout(firstName, lastName, zipCode);
        
        // Verify successful order
        confirmationActions.verifyConfirmationMessage("Thank you for your order!");
    }

    /**
     * Test 7: Verify inventory page elements after login - 5 scenarios
     */
    @Test(
        priority = 7, 
        description = "Verify inventory page loads correctly - 5 data sets",
        dataProvider = "inventoryPageData",
        dataProviderClass = TestDataProvider.class
    )
    public void testInventoryPageElements(String username, String password) {
        // Login
        inventoryActions = loginActions.login(username, password);
        
        // Verify page loaded
        inventoryActions.verifyPageLoaded();
        
        // Verify add button is available
        assertTrue(inventoryActions.isAddButtonVisible(), 
            "Add to cart button should be visible on inventory page");
        
        // Verify cart badge is not displayed initially
        assertFalse(inventoryActions.isCartBadgeDisplayed(), 
            "Cart badge should not be displayed when cart is empty");
    }

    /**
     * Test 8: Test cart badge updates correctly - 5 scenarios
     */
    @Test(
        priority = 8, 
        description = "Verify cart badge updates when adding items - 5 data sets",
        dataProvider = "cartBadgeData",
        dataProviderClass = TestDataProvider.class
    )
    public void testCartBadgeUpdate(String username, String password) {
        // Login
        inventoryActions = loginActions.login(username, password);
        inventoryActions.verifyPageLoaded();
        
        // Initial state - no badge
        assertFalse(inventoryActions.isCartBadgeDisplayed(), 
            "Cart badge should not be visible initially");
        
        // Add item
        inventoryActions.addBackpackToCart();
        
        // Badge should appear with count 1
        assertTrue(inventoryActions.isCartBadgeDisplayed(), 
            "Cart badge should be visible after adding item");
        assertEquals(inventoryActions.getCartBadgeCount(), "1", 
            "Cart badge should show count of 1");
        
        // Remove item
        inventoryActions.removeBackpackFromCart();
        
        // Badge should disappear
        sleep(1000); // Give time for badge to disappear
        assertFalse(inventoryActions.isCartBadgeDisplayed(), 
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
