package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

public class InventoryPage {
    public final Locator INVENTORY_CONTAINER = Locator.buildLocator().web(By.id("inventory_container")).name("Inventory Container").build();
    public final Locator ADD_TO_CART_BACKPACK = Locator.buildLocator().web(By.id("add-to-cart-sauce-labs-backpack")).name("Add Backpack to Cart").build();
    public final Locator REMOVE_FROM_CART_BACKPACK = Locator.buildLocator().web(By.id("remove-sauce-labs-backpack")).name("Remove Backpack from Cart").build();
    public final Locator CART_BADGE = Locator.buildLocator().web(By.cssSelector(".shopping_cart_badge")).name("Cart Badge").build();
    public final Locator CART_LINK = Locator.buildLocator().web(By.id("shopping_cart_container")).name("Cart Link").build();
}
