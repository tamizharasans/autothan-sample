package com.autothan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {

    WebDriver driver;
    By titleText =By.className("tp-mask-wrap");

    public HomePage(WebDriver driver){
        this.driver = driver;
    }


    //Get the title of Login Page
    public String getHomePageTitle(){
        return	driver.findElement(titleText).getText();
    }

}
