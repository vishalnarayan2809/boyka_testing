package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

public class ConfirmationPage {
    public final Locator CONFIRMATION_MESSAGE = Locator.buildLocator().web(By.cssSelector(".complete-header")).name("Confirmation Message").build();
    public final Locator CONFIRMATION_TEXT = Locator.buildLocator().web(By.cssSelector(".complete-text")).name("Confirmation Text").build();
    public final Locator BACK_HOME_BUTTON = Locator.buildLocator().web(By.id("back-to-products")).name("Back Home Button").build();
}
