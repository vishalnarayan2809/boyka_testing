package com.saucedemo.pages;

import io.github.boykaframework.builders.Locator;
import org.openqa.selenium.By;

public class LoginPage {
    public final Locator USERNAME_FIELD = Locator.buildLocator().web(By.id("user-name")).name("Username Field").build();
    public final Locator PASSWORD_FIELD = Locator.buildLocator().web(By.id("password")).name("Password Field").build();
    public final Locator LOGIN_BUTTON = Locator.buildLocator().web(By.id("login-button")).name("Login Button").build();
    public final Locator ERROR_MESSAGE = Locator.buildLocator().web(By.cssSelector("[data-test='error']")).name("Error Message").build();
}
