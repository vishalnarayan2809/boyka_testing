# 🏗️ Page Object Model Architecture Diagram

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                          TEST EXECUTION LAYER                       │
│                   (SauceDemoTestsRefactored.java)                   │
│                                                                     │
│  @Test(dataProvider="validLoginData", dataProviderClass=...)       │
│  testValidLoginWithDataProvider(username, password) {              │
│      inventoryPage = loginPage.login(username, password);          │
│      assertTrue(inventoryPage.isInventoryDisplayed());             │
│  }                                                                  │
└─────────────────────────────────────────────────────────────────────┘
                               ↓ uses
┌─────────────────────────────────────────────────────────────────────┐
│                        DATA PROVIDER LAYER                          │
│                      (TestDataProvider.java)                        │
│                                                                     │
│  @DataProvider(name = "validLoginData")                            │
│  public static Object[][] getValidLoginData() {                    │
│      return new Object[][] {                                       │
│          { "standard_user", "secret_sauce" },                      │
│          { "problem_user", "secret_sauce" },                       │
│          { "performance_glitch_user", "secret_sauce" }             │
│      };                                                             │
│  }                                                                  │
└─────────────────────────────────────────────────────────────────────┘
                               ↓ provides data to
┌─────────────────────────────────────────────────────────────────────┐
│                       PAGE OBJECT LAYER                             │
│                         (pages/*.java)                              │
│                                                                     │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐            │
│  │ LoginPage    │→│InventoryPage │→│  CartPage    │            │
│  │              │  │              │  │              │            │
│  │ + USERNAME   │  │ + INVENTORY  │  │ + CART_ITEM  │            │
│  │ + PASSWORD   │  │ + ADD_BUTTON │  │ + CHECKOUT   │            │
│  │ + LOGIN_BTN  │  │ + CART_BADGE │  │              │            │
│  │              │  │              │  │              │            │
│  │ + login()    │  │ + addToCart()│  │ + checkout() │            │
│  │ + verify()   │  │ + goToCart() │  │ + hasItems() │            │
│  └──────────────┘  └──────────────┘  └──────────────┘            │
│                                              ↓                       │
│  ┌──────────────┐  ┌──────────────────────────────┐               │
│  │CheckoutPage  │→│  ConfirmationPage           │               │
│  │              │  │                              │               │
│  │ + FIRST_NAME │  │ + CONFIRMATION_MSG           │               │
│  │ + LAST_NAME  │  │                              │               │
│  │ + ZIP_CODE   │  │ + verifyOrderComplete()      │               │
│  │              │  │ + getConfirmationMessage()   │               │
│  │ + fillForm() │  │                              │               │
│  │ + complete() │  │                              │               │
│  └──────────────┘  └──────────────────────────────┘               │
└─────────────────────────────────────────────────────────────────────┘
                               ↓ uses
┌─────────────────────────────────────────────────────────────────────┐
│                    BOYKA FRAMEWORK LAYER                            │
│                  (io.github.boykaframework.*)                       │
│                                                                     │
│  • TextBoxActions.onTextBox(locator).enterText(text)              │
│  • ClickableActions.withMouse(locator).click()                     │
│  • ElementActions.onElement(locator).verifyIsDisplayed()           │
│  • WindowActions.onWindow().maximize()                             │
│  • ParallelSession (createSession, clearSession)                   │
└─────────────────────────────────────────────────────────────────────┘
                               ↓ controls
┌─────────────────────────────────────────────────────────────────────┐
│                      SELENIUM WEBDRIVER                             │
│                         (Browser)                                   │
│                                                                     │
│               Chrome (with incognito mode)                          │
│            + Disabled password warnings                             │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Test Flow Example

```
User runs: .\run-tests.ps1 test -Dtest=...#testValidLoginWithDataProvider

┌────────────────────────────────────────────────────────────────┐
│ Step 1: TestNG reads @Test annotation                         │
│ - Finds dataProvider = "validLoginData"                       │
│ - Finds dataProviderClass = TestDataProvider.class            │
└────────────────────────────────────────────────────────────────┘
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ Step 2: TestNG calls TestDataProvider.getValidLoginData()     │
│ - Returns 3 data sets:                                        │
│   1. ["standard_user", "secret_sauce"]                        │
│   2. ["problem_user", "secret_sauce"]                         │
│   3. ["performance_glitch_user", "secret_sauce"]              │
└────────────────────────────────────────────────────────────────┘
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ Step 3: TestNG runs test method 3 times                       │
│                                                                │
│ ┌──────────────────────────────────────────────────────────┐ │
│ │ Iteration 1: ("standard_user", "secret_sauce")           │ │
│ │ @BeforeMethod: setUp() → Create browser session          │ │
│ │ @Test: testValidLogin("standard_user", "secret_sauce")   │ │
│ │   → loginPage.login(username, password)                  │ │
│ │       → LoginPage.enterUsername("standard_user")         │ │
│ │       → LoginPage.enterPassword("secret_sauce")          │ │
│ │       → LoginPage.clickLoginButton()                     │ │
│ │       → Returns new InventoryPage()                      │ │
│ │   → inventoryPage.isInventoryDisplayed()                 │ │
│ │       → Verify inventory container visible               │ │
│ │   → assertTrue(true) ✅ PASS                              │ │
│ │ @AfterMethod: tearDown() → Close browser                 │ │
│ └──────────────────────────────────────────────────────────┘ │
│                                                                │
│ ┌──────────────────────────────────────────────────────────┐ │
│ │ Iteration 2: ("problem_user", "secret_sauce")            │ │
│ │ ... same flow with different data ...                    │ │
│ │ → assertTrue(true) ✅ PASS                                │ │
│ └──────────────────────────────────────────────────────────┘ │
│                                                                │
│ ┌──────────────────────────────────────────────────────────┐ │
│ │ Iteration 3: ("performance_glitch_user", "secret_sauce") │ │
│ │ ... same flow with different data ...                    │ │
│ │ → assertTrue(true) ✅ PASS                                │ │
│ └──────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────┘
                            ↓
┌────────────────────────────────────────────────────────────────┐
│ Step 4: TestNG generates report                               │
│ Tests run: 3, Failures: 0, Errors: 0, Skipped: 0             │
│ BUILD SUCCESS ✅                                               │
└────────────────────────────────────────────────────────────────┘
```

---

## File Dependency Diagram

```
SauceDemoTestsRefactored.java
├─── imports TestDataProvider.class
│    │
│    └─── TestDataProvider.java
│         └─── @DataProvider methods
│              ├─── validLoginData
│              ├─── invalidLoginData
│              ├─── checkoutData
│              └─── fullCheckoutData
│
└─── uses Page Objects
     │
     ├─── LoginPage.java
     │    ├─── Locators: USERNAME_FIELD, PASSWORD_FIELD, LOGIN_BUTTON
     │    └─── Methods: login(), attemptLogin(), verifyError()
     │
     ├─── InventoryPage.java
     │    ├─── Locators: INVENTORY_CONTAINER, ADD_BUTTON, CART_BADGE
     │    └─── Methods: addToCart(), removeFromCart(), goToCart()
     │
     ├─── CartPage.java
     │    ├─── Locators: CART_ITEM, CHECKOUT_BUTTON
     │    └─── Methods: verifyItems(), proceedToCheckout()
     │
     ├─── CheckoutPage.java
     │    ├─── Locators: FIRST_NAME, LAST_NAME, ZIP_CODE, BUTTONS
     │    └─── Methods: fillForm(), completeCheckout()
     │
     └─── ConfirmationPage.java
          ├─── Locators: CONFIRMATION_MESSAGE
          └─── Methods: verifyComplete(), getMessage()
```

---

## Data Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      DATA SOURCES                           │
│                                                             │
│  TestDataProvider.java                                      │
│  ┌───────────────────────────────────────────────────┐     │
│  │ validLoginData                                    │     │
│  │ ├─ ["standard_user", "secret_sauce"]             │     │
│  │ ├─ ["problem_user", "secret_sauce"]              │     │
│  │ └─ ["performance_glitch_user", "secret_sauce"]   │     │
│  └───────────────────────────────────────────────────┘     │
│                                                             │
│  ┌───────────────────────────────────────────────────┐     │
│  │ checkoutData                                      │     │
│  │ ├─ ["John", "Doe", "12345"]                      │     │
│  │ ├─ ["Jane", "Smith", "54321"]                    │     │
│  │ ├─ ["Bob", "Johnson", "67890"]                   │     │
│  │ └─ ["Alice", "Williams", "98765"]                │     │
│  └───────────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────┘
                            ↓
                     [Data Provider]
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    TEST METHODS                             │
│                                                             │
│  @Test(dataProvider="validLoginData")                      │
│  testLogin(String user, String pass)                       │
│      ↓                                                      │
│  Receives each data set one at a time                      │
│      ↓                                                      │
│  Iteration 1: user="standard_user", pass="secret_sauce"    │
│  Iteration 2: user="problem_user", pass="secret_sauce"     │
│  Iteration 3: user="performance_glitch_user", pass="..."   │
└─────────────────────────────────────────────────────────────┘
                            ↓
                     [Pass to Page Objects]
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    PAGE OBJECTS                             │
│                                                             │
│  loginPage.login(user, pass)                               │
│      ↓                                                      │
│  enterUsername(user)    → TextBox action                   │
│  enterPassword(pass)    → TextBox action                   │
│  clickLoginButton()     → Click action                     │
│      ↓                                                      │
│  Return inventoryPage                                       │
└─────────────────────────────────────────────────────────────┘
                            ↓
                    [Boyka Framework]
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                    BROWSER ACTIONS                          │
│                                                             │
│  Chrome Browser (Incognito Mode)                           │
│  ├─ Navigate to https://www.saucedemo.com                  │
│  ├─ Enter "standard_user" in username field                │
│  ├─ Enter "secret_sauce" in password field                 │
│  ├─ Click login button                                     │
│  └─ Verify inventory page loaded                           │
└─────────────────────────────────────────────────────────────┘
```

---

## Interaction Sequence

```
Test Method Call Sequence for testCompleteCheckoutFlow():

1. @BeforeMethod setUp()
   ├─ createSession(WEB, "test_web")
   ├─ WindowActions.maximize()
   └─ loginPage = new LoginPage()

2. @Test testCompleteCheckoutFlow(firstName, lastName, zip)
   ├─ inventoryPage = loginPage.login("standard_user", "secret_sauce")
   │  ├─ enterUsername("standard_user")
   │  ├─ enterPassword("secret_sauce")
   │  └─ clickLoginButton() → new InventoryPage()
   │
   ├─ inventoryPage.verifyPageLoaded()
   │  └─ verify INVENTORY_CONTAINER displayed
   │
   ├─ inventoryPage.addBackpackToCart()
   │  ├─ verify ADD_BUTTON visible
   │  ├─ click ADD_BUTTON
   │  └─ verify REMOVE_BUTTON visible
   │
   ├─ cartPage = inventoryPage.goToCart()
   │  ├─ click CART_LINK
   │  └─ new CartPage()
   │
   ├─ cartPage.verifyCartItemPresent()
   │  └─ verify CART_ITEM visible
   │
   ├─ checkoutPage = cartPage.proceedToCheckout()
   │  ├─ click CHECKOUT_BUTTON
   │  └─ new CheckoutPage()
   │
   ├─ confirmationPage = checkoutPage.completeCheckout(firstName, lastName, zip)
   │  ├─ enterFirstName(firstName)
   │  ├─ enterLastName(lastName)
   │  ├─ enterZipCode(zip)
   │  ├─ clickContinue()
   │  ├─ clickFinish()
   │  └─ new ConfirmationPage()
   │
   └─ confirmationPage.verifyConfirmationMessage("Thank you for your order!")
      └─ verify CONFIRMATION_MESSAGE contains text

3. @AfterMethod tearDown()
   └─ clearSession()

Repeat steps 1-3 for each data set in checkoutData (4 iterations)
```

---

## Component Relationships

```
┌────────────────────────────────────────────────────────────────┐
│                         LEGEND                                 │
│  → uses/calls                                                  │
│  ─ contains/has                                                │
│  ↔ returns/navigates to                                        │
└────────────────────────────────────────────────────────────────┘

LoginPage
  │
  ├─ USERNAME_FIELD (Locator)
  ├─ PASSWORD_FIELD (Locator)
  ├─ LOGIN_BUTTON (Locator)
  ├─ ERROR_MESSAGE (Locator)
  │
  ├─ enterUsername() → uses TextBoxActions
  ├─ enterPassword() → uses TextBoxActions
  ├─ clickLoginButton() → uses ClickableActions ↔ returns InventoryPage
  └─ verifyErrorMessage() → uses ElementActions

InventoryPage
  │
  ├─ INVENTORY_CONTAINER (Locator)
  ├─ ADD_BUTTON (Locator)
  ├─ REMOVE_BUTTON (Locator)
  ├─ CART_BADGE (Locator)
  ├─ CART_LINK (Locator)
  │
  ├─ verifyPageLoaded() → uses ElementActions
  ├─ addBackpackToCart() → uses ClickableActions
  ├─ removeBackpackFromCart() → uses ClickableActions
  └─ goToCart() → uses ClickableActions ↔ returns CartPage

CartPage
  │
  ├─ CART_ITEM (Locator)
  ├─ CHECKOUT_BUTTON (Locator)
  │
  ├─ verifyCartItemPresent() → uses ElementActions
  └─ proceedToCheckout() → uses ClickableActions ↔ returns CheckoutPage

CheckoutPage
  │
  ├─ FIRST_NAME_FIELD (Locator)
  ├─ LAST_NAME_FIELD (Locator)
  ├─ ZIP_CODE_FIELD (Locator)
  ├─ CONTINUE_BUTTON (Locator)
  ├─ FINISH_BUTTON (Locator)
  │
  ├─ fillCheckoutInformation() → uses TextBoxActions
  └─ completeCheckout() → uses Multiple Actions ↔ returns ConfirmationPage

ConfirmationPage
  │
  ├─ CONFIRMATION_MESSAGE (Locator)
  │
  ├─ verifyOrderComplete() → uses ElementActions
  └─ verifyConfirmationMessage() → uses ElementActions
```

---

## 📊 Complete Implementation Overview

```
PROJECT STRUCTURE
═══════════════════════════════════════════════════════════════

src/test/java/com/saucedemo/
│
├── pages/ (665 lines total)
│   ├── LoginPage.java          (120 lines) ✅
│   ├── InventoryPage.java      (230 lines) ✅
│   ├── CartPage.java           (100 lines) ✅
│   ├── CheckoutPage.java       (135 lines) ✅
│   └── ConfirmationPage.java   (80 lines)  ✅
│
├── dataproviders/ (110 lines total)
│   └── TestDataProvider.java  (110 lines)  ✅
│       ├── validLoginData (3 sets)
│       ├── invalidLoginData (4 sets)
│       ├── checkoutData (4 sets)
│       ├── standardUser (1 set)
│       ├── productNames (4 sets)
│       └── fullCheckoutData (3 sets)
│
└── tests/ (666 lines total)
    ├── SauceDemoTests.java              (376 lines) [Original]
    └── SauceDemoTestsRefactored.java    (290 lines) ✅
        ├── testValidLoginWithDataProvider       (3 iterations)
        ├── testInvalidLoginWithDataProvider     (4 iterations)
        ├── testAddProductToCart                 (1 iteration)
        ├── testRemoveProductFromCart            (1 iteration)
        ├── testCompleteCheckoutFlow             (4 iterations)
        ├── testEndToEndCheckout                 (3 iterations)
        ├── testInventoryPageElements            (1 iteration)
        └── testCartBadgeUpdate                  (1 iteration)

TOTAL: 1,441 lines of production code + 1,150 lines of documentation
═══════════════════════════════════════════════════════════════
```

---

**Last Updated**: October 2, 2025  
**Status**: ✅ Complete and Tested  
**Architecture**: Page Object Model + Data-Driven Testing  
