# 🎯 Page Object Model Implementation - Quick Start Guide

## ✅ What Was Implemented

### 1. **Page Object Model Structure**
Created 5 page object classes with complete separation of concerns:

- **LoginPage.java** - Login page locators and actions
- **InventoryPage.java** - Product inventory page
- **CartPage.java** - Shopping cart page  
- **CheckoutPage.java** - Checkout process
- **ConfirmationPage.java** - Order confirmation

### 2. **Centralized Data Providers**
Created **TestDataProvider.java** with 6 data providers:

- `validLoginData` - Multiple valid user credentials
- `invalidLoginData` - Invalid login scenarios with error messages
- `checkoutData` - Checkout information sets
- `standardUser` - Single standard user
- `productNames` - Product name list
- `fullCheckoutData` - Combined user + checkout data

### 3. **Refactored Test Class**
Created **SauceDemoTestsRefactored.java** with 8 data-driven tests:

1. `testValidLoginWithDataProvider` - Tests 3 different users
2. `testInvalidLoginWithDataProvider` - Tests 4 invalid scenarios
3. `testAddProductToCart` - Add product functionality
4. `testRemoveProductFromCart` - Remove product functionality
5. `testCompleteCheckoutFlow` - Full checkout with 4 data sets
6. `testEndToEndCheckout` - Complete flow with combined data
7. `testInventoryPageElements` - Page validation
8. `testCartBadgeUpdate` - Badge state testing

---

## 🚀 How to Run

### Run All POM Tests:
```powershell
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored
```

### Run Specific Test:
```powershell
# Test valid logins with 3 users
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored#testValidLoginWithDataProvider

# Test invalid logins with 4 scenarios
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored#testInvalidLoginWithDataProvider

# Test checkout with 4 different data sets
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored#testCompleteCheckoutFlow
```

### Run Both Old and New Tests:
```powershell
# Run everything
.\run-tests.ps1
```

---

## 📊 Test Comparison

### Before (Old Approach):
- ❌ Locators mixed with test logic
- ❌ Hardcoded test data
- ❌ No reusability
- ❌ Difficult to maintain
- 5 tests total

### After (POM + Data-Driven):
- ✅ Locators in page classes
- ✅ External data providers
- ✅ Reusable page methods
- ✅ Easy to maintain
- 8 tests with multiple data sets = **25+ test iterations**

---

## 🎓 Quick Examples

### Example 1: Simple Login Test
```java
@Test(dataProvider = "validLoginData", 
      dataProviderClass = TestDataProvider.class)
public void testLogin(String username, String password) {
    // One line login using page object
    inventoryPage = loginPage.login(username, password);
    
    // Verify using page method
    assertTrue(inventoryPage.isInventoryDisplayed());
}
```

### Example 2: Method Chaining
```java
// Fluent API with method chaining
confirmationPage = loginPage
    .enterUsername("user")
    .enterPassword("pass")
    .clickLoginButton()
    .addBackpackToCart()
    .goToCart()
    .proceedToCheckout()
    .completeCheckout("John", "Doe", "12345");
```

### Example 3: Data-Driven Testing
```java
// This one test method runs 4 times with different data
@Test(dataProvider = "checkoutData", 
      dataProviderClass = TestDataProvider.class)
public void testCheckout(String firstName, String lastName, String zip) {
    // Test runs with:
    // 1. John, Doe, 12345
    // 2. Jane, Smith, 54321
    // 3. Bob, Johnson, 67890
    // 4. Alice, Williams, 98765
}
```

---

## 📁 Project Structure

```
src/test/java/com/saucedemo/
│
├── pages/                      ← ALL LOCATORS HERE
│   ├── LoginPage.java
│   ├── InventoryPage.java
│   ├── CartPage.java
│   ├── CheckoutPage.java
│   └── ConfirmationPage.java
│
├── dataproviders/              ← ALL TEST DATA HERE
│   └── TestDataProvider.java
│
└── tests/                      ← ALL TESTS HERE
    ├── SauceDemoTests.java            (Original)
    └── SauceDemoTestsRefactored.java  (POM + Data-Driven) ⭐
```

---

## 🔑 Key Benefits

### 1. **Maintainability**
If a locator changes, update only one place:
```java
// Before: Update in every test
// After: Update once in LoginPage.java
private static final Locator USERNAME_FIELD = Locator.buildLocator()
    .web(By.id("user-name"))  // ← Change here once
    .name("Username Field")
    .build();
```

### 2. **Reusability**
Page methods used across multiple tests:
```java
// Used in 6 different tests
inventoryPage = loginPage.login("user", "pass");
```

### 3. **Readability**
Tests read like business requirements:
```java
// Clear and business-focused
loginPage.login(username, password);
inventoryPage.addBackpackToCart();
cartPage.proceedToCheckout();
checkoutPage.completeCheckout(firstName, lastName, zip);
confirmationPage.verifyOrderComplete();
```

### 4. **Data-Driven Testing**
One test, multiple scenarios:
```java
// 1 test method = 3 test executions
@Test(dataProvider = "validLoginData")
public void testLogin(String user, String pass) {
    // Runs 3 times automatically
}
```

---

## 📝 Adding New Tests

### Step 1: Add Test Data (if needed)
```java
// In TestDataProvider.java
@DataProvider(name = "newData")
public static Object[][] getNewData() {
    return new Object[][] {
        { "data1", "data2" },
        { "data3", "data4" }
    };
}
```

### Step 2: Create Test Method
```java
// In SauceDemoTestsRefactored.java
@Test(dataProvider = "newData", 
      dataProviderClass = TestDataProvider.class)
public void testNewFeature(String param1, String param2) {
    // Use page objects
    loginPage.login(param1, param2);
    inventoryPage.verifyPageLoaded();
}
```

### Step 3: Run Test
```powershell
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored#testNewFeature
```

---

## 🛠️ Troubleshooting

### Issue: Tests not running
**Solution**: Use the helper script
```powershell
.\run-tests.ps1 test -Dtest=SauceDemoTestsRefactored
```

### Issue: Compilation errors
**Solution**: Clean and recompile
```powershell
.\run-tests.ps1 clean test-compile
```

### Issue: Data provider not found
**Solution**: Ensure `dataProviderClass` is set
```java
@Test(
    dataProvider = "validLoginData",
    dataProviderClass = TestDataProvider.class  // ← Must include this
)
```

---

## 📚 Documentation

- **Complete Guide**: `POM_IMPLEMENTATION_GUIDE.md` - Detailed documentation
- **This Guide**: `POM_QUICK_START.md` - Quick reference

---

## ✨ Summary

### Files Created:
✅ 5 Page Object classes  
✅ 1 Data Provider class  
✅ 1 Refactored test class  
✅ 2 Documentation files  

### Tests Created:
✅ 8 test methods  
✅ 25+ test iterations (with data providers)  
✅ 100% separation of concerns  

### Status:
✅ **Compiled Successfully**  
✅ **Ready to Run**  
✅ **Fully Documented**  

---

**Created**: October 2, 2025  
**Java Version**: 21 LTS  
**Framework**: Boyka 2.8.0 + TestNG 7.8.0  
**Pattern**: Page Object Model + Data-Driven Testing
