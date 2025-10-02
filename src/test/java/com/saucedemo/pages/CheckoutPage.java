package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

public class CheckoutPage {
    public final Locator FIRST_NAME_FIELD = Locator.buildLocator().web(By.id("first-name")).name("First Name Field").build();
    public final Locator LAST_NAME_FIELD = Locator.buildLocator().web(By.id("last-name")).name("Last Name Field").build();
    public final Locator ZIP_CODE_FIELD = Locator.buildLocator().web(By.id("postal-code")).name("Zip Code Field").build();
    public final Locator CONTINUE_BUTTON = Locator.buildLocator().web(By.id("continue")).name("Continue Button").build();
    public final Locator FINISH_BUTTON = Locator.buildLocator().web(By.id("finish")).name("Finish Button").build();
    public final Locator CANCEL_BUTTON = Locator.buildLocator().web(By.id("cancel")).name("Cancel Button").build();
}
