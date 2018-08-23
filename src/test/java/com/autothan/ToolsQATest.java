package com.autothan;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


import java.util.concurrent.TimeUnit;

public class ToolsQATest {

	WebDriver driver;
	HomePage homePage;

	
	@BeforeTest
	public void setup(){
		String exePath = "src/test/resources/drivers/chromedriver";
		System.setProperty("webdriver.chrome.driver", exePath);
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://toolsqa.com/");
	}


	@Test
	public void testHomePageAppearCorrect(){
		//Create Login Page object
		homePage = new HomePage(driver);
		//Verify login page title
		String homePageTitle = homePage.getHomePageTitle();
		Assert.assertTrue(homePageTitle.contains("Selenium Online Trainings"));
	}

	@AfterSuite
	public void quitDriver() {
		driver.quit();
	}
	
}
