package Listeners;

import Utils.Commons;
import io.qameta.allure.Attachment;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener{

    static Logger logger = Logger.getLogger(TestListener.class);

    @Override
    public void onTestFailure(ITestResult result) {

        logger.error("=========== Error \"+ result.getName() +\" test has failed ==========");
        Commons.captureScreenshot(result);
        WebDriver driver = (WebDriver)(result.getTestContext()).getAttribute("WebDriver");
        saveFailureTextLog("\"=========== Error \\\"+ result.getName() +\\\" test has failed ==========\"");
        saveFailureScreenshot(driver);
    }

    @Attachment(value = "{0}", type = "text/plain")
    public String saveFailureTextLog(String message){
        return message;
    }

    @Attachment
    public byte[]  saveFailureScreenshot(WebDriver driver){
        return ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
    }

    public void onFinish(ITestContext context) {
        logger.info(context.getName()+" Finished! ");
    }

    public void onTestStart(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+" Test Started! ");
    }

    public void onTestSuccess(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+" Test Success! ");
    }

    public void onTestSkipped(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+" Test Skipped! ");
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info(result.getMethod().getMethodName()+" Test Skipped but passed percentage! ");
    }

    public void onStart(ITestContext context) {
        logger.info(context.getName()+" Started! ");
    }
}