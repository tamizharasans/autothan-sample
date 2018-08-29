package com.autothan.test;

import com.autothan.base.MovieObj;
import org.apache.commons.exec.util.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestAutoThon {

	WebDriver driver;

	private static Properties prop;
	private static Map<String, String> WikiMap = new HashMap<>();
	private static MovieObj movieObj = new MovieObj();
	private static List<MovieObj> movieObjList = new ArrayList<>();
    static String modeOfExec=System.getProperty("modeOfExec");

	static{
		InputStream inputStream;
		try {
			prop = new Properties();
			inputStream = ClassLoader.class.getResourceAsStream("/movieNames.properties");
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(modeOfExec==null){
			modeOfExec="GUI";
		}
	}

	public static String getPropertyValue(String key){
		return prop.getProperty(key);
	}

	@BeforeTest
	public void setup(){
		String exePath = "src/test/resources/drivers/chromedriver";
		System.setProperty("webdriver.chrome.driver", exePath);
	}



	public void testGoogleSearch(String movieName) throws Exception{
			movieObj = new MovieObj();
			movieObj.setMovieName(movieName);
			List<WebElement> findElements = driver.findElements(By.xpath("//*[@id='rso']//h3/a"));

			/*for(int i=0; i<findElements.size();i++)
		{
		if (findElements.get(i).getText().contains("Wikipedia")){
				movieObj.setWikiLink(findElements.get(i).toString());
				findElements.get(i).click();
				String directorName=driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[1]/tbody/tr[3]/td/a")).getText();
				movieObj.setWikiDirName(directorName);
				continue;
			}
			if (findElements.get(i).getText().contains("IMDb")){
				movieObj.setImdbLink(findElements.get(i).toString());
				findElements.get(i).click();
				String directorName1=driver.findElement(By.xpath("//*[@id=\"title-overview-widget\"]/div[3]/div[1]/div[2]/a")).getText();
				movieObj.setWikiDirName(directorName1);
				break;
			}
		}*/

			driver.findElement(By.xpath("//a[contains(@href,'https://en.wikipedia.org/wiki/')]")).click();
			String directorName = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[1]/tbody/tr[3]/td/a")).getText();

			driver.findElement(By.xpath("//a[contains(@href,'https://www.imdb.com/title/')]")).click();
			String directorName1 = driver.findElement(By.xpath("//*[@id=\"title-overview-widget\"]/div[3]/div[1]/div[2]/a")).getText();
			Assert.assertEquals(directorName, directorName1);
	}



	@AfterSuite
	public void quit(){
		if("GUI".equalsIgnoreCase(modeOfExec)) {
			driver.quit();
		}
	}


	@Test
	public void testAutothon() throws Exception {

		if("GUI".equalsIgnoreCase(modeOfExec)) {
			driver = new ChromeDriver();
			String pageUrl = "http://www.google.com";

			/* Cast webdriver object to Javascript Executor object. */
			JavascriptExecutor jsExecutor = (JavascriptExecutor) this.driver;

			/* Javascript that will create new FirefoxWindow. */
			String jsOpenNewWindow = "window.open('" + pageUrl + "');";
			/* Run above javascript. */
			for (int i = 0; i < 2; i++) {
				jsExecutor.executeScript(jsOpenNewWindow);
			}


			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());

			for (int i = 1; i <= 2; i++) {
				driver.switchTo().window(tabs.get(i));
				if (!"invalidMovieName".equals(getPropertyValue(String.valueOf(i)))) {
					driver.get("http://www.google.com/search?q=" + getPropertyValue(String.valueOf(i)));
					testGoogleSearch(getPropertyValue(String.valueOf(i)));
				}
			}
		}else if("REST".equalsIgnoreCase(modeOfExec)) {
			ParseWikiPage parseWikiPage=new ParseWikiPage();
			parseWikiPage.testParsePageContent();
		}




	}
	
}
