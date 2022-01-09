package Utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TestBase {

    public static Properties prop;
    public WebDriver driver;
    static Logger logger = Logger.getLogger(TestBase.class);
    String path = System.getProperty("user.dir");

    @BeforeSuite(alwaysRun = true, description = "Setting up Suite")
    public void setupSuite(){
    }

    @BeforeTest(alwaysRun = true, description = "Setting up Test")
    public void setupTest(){
        logger.info("Reading Config properties in Test Setup.");
        readConfigs();
        PropertyConfigurator.configure(path + "\\src\\main\\resources\\log4j.properties");
    }

    @BeforeMethod(alwaysRun = true, description = "Setting up Test Method")
    public void setupMethod(ITestResult result, ITestContext context){

        logger.info("Initializing Driver and other properties.");
        logger.info("Current Project dir path: " + path);

       if(prop.getProperty("BrowserName").equalsIgnoreCase("Chrome")){
            System.setProperty("webdriver.chrome.driver", path+"\\src\\main\\resources\\chromedriver.exe");
            driver = new ChromeDriver();
        }
        else if(prop.getProperty("BrowserName").equalsIgnoreCase("Firefox")) {
            System.setProperty("webdriver.gecko.driver", path+"\\src\\main\\resources\\geckodriver.exe");
            driver = new FirefoxDriver();
        }
        else{
            logger.error("Unsupported Browser. Please use 'Firefox' or 'Chrome'");
            Assert.fail("Unsupported Browser. Please use 'Firefox' or 'Chrome'");
        }

        driver.get(prop.getProperty("WebURL"));
        Commons.setImplicitTimeout(driver, 10);
        Commons.maximizeBrowserWindow(driver);
        context.setAttribute("WebDriver", driver); //Set attribute for using in Listener


        logger.info("Completed Initializing Driver and other properties.");
    }

    public void readConfigs(){
        try {
            prop = new Properties();
            logger.info("Reading Configurations property file.");
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+ "\\src\\main\\resources\\config.properties");
            prop.load(ip);
            logger.info("Configurations Property file loaded successfully.");
        } catch (FileNotFoundException e) {
            logger.info("Configurations Property file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("Configurations Property file loading failed.");
            e.printStackTrace();
        }
    }

    public WebDriver getDriver()
    {
        return driver;
    }

    @AfterMethod(alwaysRun = true, description =  "Cleaning up after Test Method")
    public void cleanupMethod(){
        logger.info("Cleaning up.");
        if(driver!=null) {
            driver.quit();
        }
        logger.info("Clean up Completed.");
    }

    @AfterSuite(alwaysRun = true, description =  "Cleaning up after Test Suite")
    public void cleanUpSuite(){
    }
}
