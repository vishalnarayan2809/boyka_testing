# Page Object Model (POM) Implementation Guide

## 📚 Table of Contents
- [Overview](#overview)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Page Objects](#page-objects)
- [Data Providers](#data-providers)
- [Test Classes](#test-classes)
- [Running Tests](#running-tests)
- [Best Practices](#best-practices)

---

## Overview

This implementation follows the **Page Object Model (POM)** design pattern combined with **Data-Driven Testing** to create maintainable, scalable, and reusable test automation code.

### Key Benefits:
✅ **Separation of Concerns** - Locators, actions, and tests are separated  
✅ **Maintainability** - Changes in UI only require updates to page classes  
✅ **Reusability** - Page methods can be reused across multiple tests  
✅ **Readability** - Tests are more readable and business-focused  
✅ **Data-Driven** - External data providers enable testing multiple scenarios  

---

## Architecture

### Design Pattern Layers:

```
┌─────────────────────────────────────────────┐
│           Test Layer                        │
│  (SauceDemoTestsRefactored.java)           │
│  - Test methods with @Test annotations     │
│  - Uses page objects and data providers    │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│        Data Provider Layer                  │
│     (TestDataProvider.java)                 │
│  - @DataProvider methods                    │
│  - Test data sets                           │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│         Page Object Layer                   │
│  - LoginPage.java                           │
│  - InventoryPage.java                       │
│  - CartPage.java                            │
│  - CheckoutPage.java                        │
│  - ConfirmationPage.java                    │
│  → Contains locators and actions            │
└─────────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────────┐
│       Boyka Framework Layer                 │
│  - Element actions                          │
│  - Driver management                        │
│  - Session management                       │
└─────────────────────────────────────────────┘
```

---

## Project Structure

```
src/test/java/com/saucedemo/
├── pages/                          # Page Object Model classes
│   ├── LoginPage.java             # Login page locators and actions
│   ├── InventoryPage.java         # Product inventory page
│   ├── CartPage.java              # Shopping cart page
│   ├── CheckoutPage.java          # Checkout information and overview
│   └── ConfirmationPage.java     # Order confirmation page
├── dataproviders/                 # Data Provider classes
│   └── TestDataProvider.java     # Centralized test data
└── tests/                         # Test classes
    ├── SauceDemoTests.java        # Original test implementation
    └── SauceDemoTestsRefactored.java  # POM + Data-Driven tests
```

---

## Page Objects

### 1. LoginPage.java

**Purpose**: Handles all login page interactions

**Locators**:
- `USERNAME_FIELD` - Username input field
- `PASSWORD_FIELD` - Password input field
- `LOGIN_BUTTON` - Login submit button
- `ERROR_MESSAGE` - Error message element

**Key Methods**:
```java
// Method chaining for fluent API
loginPage.enterUsername("user")
         .enterPassword("pass")
         .clickLoginButton();

// Or use convenience method
inventoryPage = loginPage.login("user", "pass");

// For invalid login (stays on login page)
loginPage.attemptLogin("invalid", "wrong");
loginPage.verifyErrorMessage("Epic sadface...");
```

---

### 2. InventoryPage.java

**Purpose**: Manages product inventory page interactions

**Locators**:
- `INVENTORY_CONTAINER` - Main inventory container
- `ADD_TO_CART_BACKPACK` - Add backpack button
- `REMOVE_FROM_CART_BACKPACK` - Remove backpack button
- `CART_BADGE` - Cart item count badge
- `CART_LINK` - Shopping cart icon

**Key Methods**:
```java
// Verify page loaded
inventoryPage.verifyPageLoaded();

// Add/remove products
inventoryPage.addBackpackToCart();
inventoryPage.removeBackpackFromCart();

// Check cart state
boolean hasItems = inventoryPage.isCartBadgeDisplayed();
String count = inventoryPage.getCartBadgeCount();

// Navigate to cart
cartPage = inventoryPage.goToCart();
```

---

### 3. CartPage.java

**Purpose**: Handles shopping cart operations

**Locators**:
- `CART_ITEM` - Cart item element
- `CHECKOUT_BUTTON` - Proceed to checkout button
- `CONTINUE_SHOPPING_BUTTON` - Return to shopping

**Key Methods**:
```java
// Verify cart contents
cartPage.verifyCartItemPresent();
boolean hasItems = cartPage.hasItems();

// Proceed to checkout
checkoutPage = cartPage.proceedToCheckout();

// Or continue shopping
inventoryPage = cartPage.continueShopping();
```

---

### 4. CheckoutPage.java

**Purpose**: Manages checkout information and overview

**Locators**:
- `FIRST_NAME_FIELD` - First name input
- `LAST_NAME_FIELD` - Last name input
- `ZIP_CODE_FIELD` - Postal code input
- `CONTINUE_BUTTON` - Continue to overview
- `FINISH_BUTTON` - Complete order

**Key Methods**:
```java
// Fill checkout form with method chaining
checkoutPage.enterFirstName("John")
            .enterLastName("Doe")
            .enterZipCode("12345")
            .clickContinue()
            .clickFinish();

// Or use convenience method
confirmationPage = checkoutPage.completeCheckout("John", "Doe", "12345");
```

---

### 5. ConfirmationPage.java

**Purpose**: Validates order confirmation

**Locators**:
- `CONFIRMATION_MESSAGE` - "Thank you" header
- `CONFIRMATION_TEXT` - Order details text

**Key Methods**:
```java
// Verify order completion
confirmationPage.verifyOrderComplete();
confirmationPage.verifyConfirmationMessage("Thank you for your order!");

// Get confirmation details
String message = confirmationPage.getConfirmationMessage();
boolean success = confirmationPage.isConfirmationDisplayed();
```

---

## Data Providers

### TestDataProvider.java

**Purpose**: Centralized test data management for data-driven testing

#### Available Data Providers:

**1. validLoginData**
```java
@DataProvider(name = "validLoginData")
// Returns: username, password
{ "standard_user", "secret_sauce" }
{ "problem_user", "secret_sauce" }
{ "performance_glitch_user", "secret_sauce" }
```

**2. invalidLoginData**
```java
@DataProvider(name = "invalidLoginData")
// Returns: username, password, expectedErrorMessage
{ "invalid_user", "wrong_password", "Epic sadface: Username and password..." }
{ "locked_out_user", "secret_sauce", "Epic sadface: Sorry, this user has been locked out." }
{ "", "secret_sauce", "Epic sadface: Username is required" }
{ "standard_user", "", "Epic sadface: Password is required" }
```

**3. checkoutData**
```java
@DataProvider(name = "checkoutData")
// Returns: firstName, lastName, zipCode
{ "John", "Doe", "12345" }
{ "Jane", "Smith", "54321" }
{ "Bob", "Johnson", "67890" }
{ "Alice", "Williams", "98765" }
```

**4. standardUser**
```java
@DataProvider(name = "standardUser")
// Returns: username, password
{ "standard_user", "secret_sauce" }
```

**5. fullCheckoutData**
```java
@DataProvider(name = "fullCheckoutData")
// Returns: username, password, firstName, lastName, zipCode
{ "standard_user", "secret_sauce", "John", "Doe", "12345" }
{ "standard_user", "secret_sauce", "Jane", "Smith", "54321" }
{ "problem_user", "secret_sauce", "Bob", "Johnson", "67890" }
```

---

## Test Classes

### SauceDemoTestsRefactored.java

**Purpose**: Refactored tests using POM and Data-Driven approach

#### Test Methods:

**1. testValidLoginWithDataProvider**
```java
@Test(
    dataProvider = "validLoginData",
    dataProviderClass = TestDataProvider.class
)
public void testValidLoginWithDataProvider(String username, String password)
```
- Tests multiple valid user logins
- Uses imported data provider
- Verifies inventory page loads

**2. testInvalidLoginWithDataProvider**
```java
@Test(
    dataProvider = "invalidLoginData",
    dataProviderClass = TestDataProvider.class
)
public void testInvalidLoginWithDataProvider(String username, String password, String expectedError)
```
- Tests various invalid login scenarios
- Validates error messages
- Data-driven with multiple test cases

**3. testAddProductToCart**
```java
@Test(
    dataProvider = "standardUser",
    dataProviderClass = TestDataProvider.class
)
public void testAddProductToCart(String username, String password)
```
- Tests adding product to cart
- Verifies cart badge updates
- Uses page object methods

**4. testRemoveProductFromCart**
- Tests removing product from cart
- Verifies cart badge disappears
- Validates button state changes

**5. testCompleteCheckoutFlow**
```java
@Test(
    dataProvider = "checkoutData",
    dataProviderClass = TestDataProvider.class
)
public void testCompleteCheckoutFlow(String firstName, String lastName, String zipCode)
```
- Tests complete checkout process
- Data-driven with multiple checkout data sets
- Verifies order confirmation

**6. testEndToEndCheckout**
```java
@Test(
    dataProvider = "fullCheckoutData",
    dataProviderClass = TestDataProvider.class
)
public void testEndToEndCheckout(String username, String password, 
                                 String firstName, String lastName, String zipCode)
```
- Complete end-to-end flow
- Combined user and checkout data
- Full workflow validation

**7. testInventoryPageElements**
- Verifies inventory page elements
- Validates initial page state

**8. testCartBadgeUpdate**
- Tests cart badge visibility and updates
- Validates badge count changes

---

## Running Tests

### Run All Refactored Tests:
```powershell
.\run-tests.ps1

# Or manually
mvn test -Dtest=SauceDemoTestsRefactored
```

### Run Specific Test Method:
```powershell
mvn test -Dtest=SauceDemoTestsRefactored#testValidLoginWithDataProvider
```

### Run with TestNG XML:
```xml
<test name="POM Tests">
    <classes>
        <class name="com.saucedemo.tests.SauceDemoTestsRefactored"/>
    </classes>
</test>
```

### View Test Reports:
```powershell
# Open HTML report
Start-Process "target\surefire-reports\index.html"
```

---

## Best Practices

### 1. **Page Object Design**
✅ **DO**:
- Keep locators private and static
- Return page objects from methods for chaining
- Name methods clearly (e.g., `clickLoginButton()`, `enterUsername()`)
- Use descriptive locator names in Boyka Locator builder

❌ **DON'T**:
- Put assertions in page objects (keep them in tests)
- Make page objects aware of other unrelated pages
- Hardcode test data in page objects

### 2. **Data Provider Usage**
✅ **DO**:
- Import data providers using `dataProviderClass`
- Keep data providers in separate package
- Use meaningful data provider names
- Document what each data set contains

❌ **DON'T**:
- Define data providers directly in test classes
- Mix test logic with data provider methods
- Create duplicate data providers

### 3. **Test Organization**
✅ **DO**:
- Use meaningful test method names
- Add descriptions to @Test annotations
- Set priorities for test execution order
- Use setUp/tearDown for session management

❌ **DON'T**:
- Mix POM and non-POM approaches in same test
- Create dependencies between test methods
- Leave hardcoded waits (use framework methods)

### 4. **Method Chaining**
```java
// Good - Fluent API
inventoryPage = loginPage
    .enterUsername("user")
    .enterPassword("pass")
    .clickLoginButton();

// Also Good - Step by step
loginPage.enterUsername("user");
loginPage.enterPassword("pass");
inventoryPage = loginPage.clickLoginButton();
```

### 5. **Error Handling**
```java
// Good - Helper methods with try-catch
public boolean isElementDisplayed() {
    try {
        onElement(LOCATOR).verifyIsDisplayed().isTrue();
        return true;
    } catch (AssertionError | FrameworkError e) {
        return false;
    }
}

// Use in tests
assertTrue(inventoryPage.isInventoryDisplayed(), 
    "Inventory should be displayed");
```

---

## Migration from Old to New

### Old Approach (Without POM):
```java
@Test
public void testLogin() {
    TextBoxActions.onTextBox(USERNAME_FIELD).enterText("user");
    TextBoxActions.onTextBox(PASSWORD_FIELD).enterText("pass");
    ClickableActions.withMouse(LOGIN_BUTTON).click();
    onElement(INVENTORY_CONTAINER).verifyIsDisplayed().isTrue();
}
```

### New Approach (With POM):
```java
@Test(dataProvider = "validLoginData", 
      dataProviderClass = TestDataProvider.class)
public void testLogin(String username, String password) {
    inventoryPage = loginPage.login(username, password);
    assertTrue(inventoryPage.isInventoryDisplayed());
}
```

---

## Summary

### What Changed:
1. ✅ **Created 5 Page Object classes** with locators and methods
2. ✅ **Centralized test data** in TestDataProvider class
3. ✅ **Refactored 8 test methods** to use POM and data providers
4. ✅ **Improved maintainability** - UI changes only affect page classes
5. ✅ **Enhanced reusability** - Methods used across multiple tests
6. ✅ **Better readability** - Tests focus on business logic

### Key Files:
- 📄 `pages/*.java` - Page object classes (5 files)
- 📄 `dataproviders/TestDataProvider.java` - Data providers
- 📄 `tests/SauceDemoTestsRefactored.java` - Refactored tests
- 📄 `POM_IMPLEMENTATION_GUIDE.md` - This documentation

---

**Last Updated**: October 2, 2025  
**Java Version**: 21 LTS  
**Framework**: Boyka 2.8.0  
**Test Framework**: TestNG 7.8.0
