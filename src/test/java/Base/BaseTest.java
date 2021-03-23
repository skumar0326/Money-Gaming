package Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.ExtentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {
	public WebDriver driver;
	public Properties prop;
	public ExtentTest test;
    public FileUtils FileUtil;
 	public ExtentReports rep;
	public ExtentTest scenario;


	public BaseTest(){
		//init the prop file
		if(prop==null){
			prop=new Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//main//resources//projectconfig.properties");
				prop.load(fs);
				} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void openBrowser(){
	    String bType = prop.getProperty("bType");
		infoLog("Opening browser "+ bType );

			if(bType.equals("Firefox")) {
				System.setProperty("webdriver.gecko.driver", prop.getProperty("geckopath"));
				driver = new FirefoxDriver();
			}else if (bType.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver" , prop.getProperty("chromepath"));
				driver= new ChromeDriver();
			}
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		infoLog( "Browser opened successfully "+ bType);
}


	public void navigate(String urlKey){
		infoLog("Navigating to "+prop.getProperty(urlKey) );
		driver.get(prop.getProperty(urlKey));
	}

	//clicking on element
	public void click(String locatorKey){
		infoLog("Clicking on " + locatorKey);
		getElement(locatorKey).click();
		infoLog("Clicked successfully on " + locatorKey);
	}

	//type in search fields
	public void type(String locatorKey,String data) {
		infoLog( "Typing in " + locatorKey + ". Data - " + data);
		getElement(locatorKey).sendKeys(data);
		infoLog("Typed successfully in " + locatorKey);

	}


	//selecting text from dropdown list
	public void doSelectByVisibleText(String locatorKey, String text) {
		infoLog("Selecting  from " +  locatorKey + ". Text -" + text );
		Select select = new Select(getElement(locatorKey));
		select.selectByVisibleText(text);
		infoLog("Selected successfully from " +  locatorKey  );
	}

	// finding element and returning it
	public WebElement getElement(String locatorKey){
		WebElement e=null;
		try{
		if(locatorKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			}
		}catch(Exception ex){
			// fail the test and report the error
			reportFailure(ex.getMessage());
			ex.printStackTrace();

		}
		return e;
	}
	/***********************Validations***************************/

	public boolean isElementPresent(String locatorKey){
		List<WebElement> elementList=null;
		if(locatorKey.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(locatorKey)));
		else if(locatorKey.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(locatorKey)));
		else{
			reportFailure("Locator not correct - " + locatorKey);
			 }
		
		if(elementList.size()==0)
			return false;	
		else
			return true;
	}
	
	public boolean verifyText(String locatorKey,String expectedText){
		String actualText=getElement(locatorKey).getText().trim();
		System.out.println(actualText);
		if(actualText.equals(expectedText))
			return true;
		else 
			return false;
	}


	public  String getPageTitle(){
		String actualTitle = driver.getTitle();
		return actualTitle;
	}

	public void verifyTitle(String expectedTitle){
		infoLog("Verifying  title of page  "  +  expectedTitle );
		String actualTitle = driver.getTitle();

		  if (actualTitle.equals(expectedTitle)){
		  	passLog("Title  matches");
		  } else
		    reportFailure("Title Does not match");
	}



	public void acceptAlertIfPresent() {
	     infoLog("Accepting the Alert");
		try {
			WebDriverWait wait = new WebDriverWait(driver, 15);
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
		infoLog("Accepted Alert Successfully");
		 }catch(Exception e) {
			 reportFailure("Alert not present");
		}
	}

	public void waitForPageToLoad() {
		wait(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		while(!state.equals("complete")){
			wait(2);
			state = (String)js.executeScript("return document.readyState");
		}
		// check for jquery status
		int i=0;
		while(i!=10){

			Long d= (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if(d.longValue() == 0 )
				break;
			else
				wait(2);
			i++;

		}

	}

	public void wait(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/*****************************Reporting********************************/

	public void infoLog(String msg) {
		scenario.log(Status.INFO, msg);
	}

	public void  passLog(String msg){
		scenario.log(Status.PASS , msg);
	}

	public void reportFailure(String errMsg) {
        // fail in extent reports
        scenario.log(Status.FAIL, errMsg);
		// take screenshot and put in reports
        takeScreenShot();
        //fail in junit
		Assert.fail();



    }
	public void quit() {
		if(rep!=null)
			rep.flush();
		if(driver !=null)
			driver.quit();
	}
	public void initReports(String scenarioName) {
		//intialize and starting the extent reports
		rep = ExtentManager.getInstance();
		scenario = rep.createTest(scenarioName);
		scenario.log(Status.INFO, "Starting " +scenarioName);
	}




	public void takeScreenShot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// take screenshot
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath + screenshotFile));
			//put screenshot file in reports
			scenario.log(Status.INFO, "Screenshot-> " + scenario.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath + screenshotFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	}
	

	

