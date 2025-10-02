package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

public class CartPage {
    public final Locator CART_ITEM = Locator.buildLocator().web(By.cssSelector(".cart_item")).name("Cart Item").build();
    public final Locator CHECKOUT_BUTTON = Locator.buildLocator().web(By.id("checkout")).name("Checkout Button").build();
    public final Locator CONTINUE_SHOPPING_BUTTON = Locator.buildLocator().web(By.id("continue-shopping")).name("Continue Shopping Button").build();
}
