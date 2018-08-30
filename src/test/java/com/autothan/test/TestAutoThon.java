package com.autothan.test;

import com.autothan.base.MovieObj;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class TestAutoThon {

	//TODO Catch Assert Failure DirectorName


	private static Properties prop;
	private static Map<String, String> WikiMap = new HashMap<>();
	private static MovieObj movieObj = new MovieObj();
    static String modeOfExec=System.getProperty("modeOfExec");
    private static final String PROPERTY_FILE_NAME = "movieNames.properties";
    private static Map<String, String> wikiUrlMap = new HashMap<>();
    private static List<MovieObj> movieObjList = new ArrayList<>();
	static String threadCnt=System.getProperty("threadCnt");

	static{
		InputStream inputStream;
		try {
			prop = new Properties();
			inputStream = ClassLoader.class.getResourceAsStream("/"+PROPERTY_FILE_NAME);
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if(modeOfExec==null){
			modeOfExec="GUI";
		}

		if(threadCnt==null){
			threadCnt="4";
		}
	}

	public static String getPropertyValue(String key){
		return prop.getProperty(key);
	}


	@BeforeSuite
	public void getWikiUrls() throws Exception {

		String exePath = "src/test/resources/drivers/chromedriver";
		System.setProperty("webdriver.chrome.driver", exePath);
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

			List<Integer> tabs = new ArrayList<>();
			for(int i=0;i<=Integer.parseInt(threadCnt);i++) {
				tabs.add(i);
			}
			tabs.parallelStream().forEach(tab -> {
				System.out.println("Tab :: " + tab);
				ChromeDriver driver = new ChromeDriver();
				String movieName = getPropertyValue(String.valueOf(tab));

					String searchFor = "http://www.google.com/search?q=" + movieName;
					System.out.println("Searching for ::: " + searchFor + "\t" +"Switching window to ::: " +  "\t" + driver.getCurrentUrl() + "\t" + "Tab :: " + tab);
					driver.get(searchFor);
					boolean foundWikiLink=false;
					try {
						List<WebElement> findElements = driver.findElements(By.xpath("//*[@id='rso']//h3/a"));
						for (int i = 0; i < findElements.size(); i++) {
							if (findElements.get(i).getText().contains("Wikipedia")) {
								String wikiLink = driver.findElement(By.xpath("//a[contains(@href,'https://en.wikipedia.org/wiki/')]")).getAttribute("href");
								wikiUrlMap.put(movieName, wikiLink);
								foundWikiLink=true;
								break;
							}
						}

						if(!foundWikiLink){
						   wikiUrlMap.put(movieName, "Wiki Link NOT found for this movie");
						}

					} catch (Exception e) {
						System.out.println("Exception occurred :: " + (e!=null?e.getMessage():null));
					} finally {
						driver.close();
					}

			});
	}


	@Test
	public void testNavigateToWiki() throws Exception {


			List<Integer> tabs = new ArrayList<>();
			for(int i=0;i<=Integer.parseInt(threadCnt);i++) {
				tabs.add(i);
			}
			tabs.parallelStream().forEach(tab -> {

					if("GUI".equalsIgnoreCase(modeOfExec)) {
						ChromeDriver driver = new ChromeDriver();
						try {
							System.out.println("Tab :: " + tab);
							MovieObj movieObj = new MovieObj();
							movieObj.setMovieId(String.valueOf(tab+1));
							String movieName = getPropertyValue(String.valueOf(tab));
							movieObj.setMovieName(movieName);
							String wikiLink = wikiUrlMap.get(movieName);
							if (!"Wiki Link NOT found for this movie".contains(wikiLink)) {
								driver.get(wikiLink);
								movieObj.setWikiUrl(wikiLink);
								movieObj.setWikiSnapShotUrl(getScreenshot("wiki_" + movieName,driver));
								String wikiDirName = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[1]/tbody/tr[3]/td/a")).getText();
								movieObj.setWikiDirName(wikiDirName);
								movieObj.setImdbUrl(driver.findElement(By.xpath("//a[contains(@href,'https://www.imdb.com/title/')]")).getAttribute("href"));
								driver.findElement(By.xpath("//a[contains(@href,'https://www.imdb.com/title/')]")).click();
								movieObj.setImdbSnapShotUrl(getScreenshot("imdb_" + movieName,driver));
								String imdbDirName = driver.findElement(By.xpath("//*[@id=\"title-overview-widget\"]/div[3]/div[1]/div[2]/a")).getText();
								movieObj.setImdbDirName(imdbDirName);
							} else {
								movieObj.setErrorMessage("Wiki Link NOT found for this movie");
							}
							if (!StringUtils.equalsIgnoreCase(movieObj.getWikiDirName(),movieObj.getImdbDirName())) {
								movieObj.setErrorMessage("IMDB Director Name did not Match with WIKI page Director Name");
							}
							movieObjList.add(movieObj);
						} catch (Exception e) {
							System.out.println("Exception occurred :: " +e);
							e.printStackTrace();
						} finally {
							driver.close();
						}
					}else if("REST".equalsIgnoreCase(modeOfExec)) {
						ParseWikiPage parseWikiPage=new ParseWikiPage();
						try {
							String movieName = getPropertyValue(String.valueOf(tab));
							String wikiLink = wikiUrlMap.get(movieName);
							parseWikiPage.testParsePageContent(movieName, String.valueOf(tab+1), wikiLink, movieObjList);
						}catch (Exception e) {
							System.out.println("Exception occurred :: " + (e != null ? e.getMessage() : null));
						}
					}



			});

		GenerateReport generateReport=new GenerateReport();
		generateReport.genReport(movieObjList);

		}

	/**
	 * This Method will screen shot of the page
	 * @return
	 */
	public String getScreenshot(String fileName, WebDriver driver){
		File fl=null;
		String filePath="";
		try {
			fl = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			String tagetTestClassesPath = this.getClass().getClassLoader().getResource(".").getPath();
			filePath=tagetTestClassesPath+"snapshots/"+fileName+".png";
			FileUtils.copyFile(fl, new File(filePath));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return filePath;
	}
	
}
