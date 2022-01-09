package PageObjects;

import Utils.Commons;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Set;

public class InterviewTaskPage {

    WebDriver driver;
    WebDriverWait wait;
    static Logger logger = Logger.getLogger(InterviewTaskPage.class);

    public InterviewTaskPage(WebDriver driver) {
        this.driver=driver;
        PageFactory.initElements(driver, this);
        Commons.setImplicitTimeout(driver, 5);
        Commons.setPageLoadTimeout(driver,20);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @FindBy(xpath="//input[@label='Invest']")
    WebElement txtInvestAmount;

    @FindBy(xpath="//button[text()='Refresh']")
    WebElement btnRefresh;

    //@FindBy(xpath="//input[@label='Invest']/../../../../following-sibling::div[2]/div/div/div")
    @FindBy(id="instance-crypto507-exampleFormControlSelect1")
    WebElement cryptoCurrencyControl;

    @FindBy(xpath="//button[text()='More Filters']")
    WebElement buttonMoreFilters;

    @FindBy(id="instance-crypto507-finMoreFilterCollapseAction")
    WebElement moreFiltersActions;

    @FindBy(id="finPriceFilter")
    WebElement providerBase;

    @FindBy(xpath="//button[text()='Load more +']")
    WebElement buttonLoadMore;

    @FindBy(xpath="//button[text()='Clear Filter']")
    WebElement buttonClearFilter;

    /**
     * Set Amount
     * @param amount
     */
    public void setAmount(int amount) {
        txtInvestAmount.clear();
        txtInvestAmount.sendKeys(Integer.toString(amount));
        wait.until(ExpectedConditions.attributeToBe(txtInvestAmount, "value", Integer.toString(amount)));
    }

    /**
     * Select Crypto Currency
     * @param cryptoCurrency
     */
    public void selectCryptoCurrency(String cryptoCurrency) {
        logger.info("Selecting Crypto Currency.");
        wait.until(ExpectedConditions.elementToBeClickable(cryptoCurrencyControl));
        WebElement crypto;
        cryptoCurrencyControl.click();
        //WebElement currency = cryptoCurrencyControl.findElement(By.xpath(".//img[@alt='" + cryptoCurrency + "']"));
        try{
            crypto = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@alt='" + cryptoCurrency + "']")));
            crypto.click();
        }catch (Exception e){
            cryptoCurrencyControl.click();
            cryptoCurrencyControl.findElement(By.xpath(".//img[@alt='" + cryptoCurrency + "']")).click();
        }

    }

    /**
     * Click Refresh button
     */
    public void clickRefreshButton() {
        logger.info("Clicking Refresh button.");
        wait.until(ExpectedConditions.elementToBeClickable(btnRefresh));
        btnRefresh.click();
        new WebDriverWait(driver, 5000).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[text()='visit now']/parent::div/parent::div/div/div[3]/span[2]")));
    }

    /**
     * Click More Filters button
     */
    public void clickMoreFiltersButton(){
        logger.info("Clicking More Filters button.");
        wait.until(ExpectedConditions.elementToBeClickable(buttonMoreFilters));
        buttonMoreFilters.click();

        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[text()='Close Filter']")));
        }catch (Exception e){
            buttonMoreFilters.click();
        }

    }

    /**
     * Select Payment Method in Filters
     * @param paymentMethodName
     */
    public void selectPaymentMethods(String paymentMethodName){

        logger.info("Select Payment Method in Filters.");
        WebElement chkPaymentMethod = providerBase.findElement(By.xpath("//input[@name='paymentMethods' and @value='" + paymentMethodName + "']/parent::div"));
        wait.until(ExpectedConditions.elementToBeClickable(chkPaymentMethod));
        if(!chkPaymentMethod.isSelected()){
            chkPaymentMethod.click();
        }
    }

    /**
     * click on Clear Filter button
     */
    public void clickClearFilterButton(){

        logger.info("Clicking on Clear Filter button");
        wait.until(ExpectedConditions.elementToBeClickable(buttonClearFilter));
        Commons.getElementInToView(driver,buttonClearFilter);
        Actions actions = new Actions(driver);
        actions.moveToElement(buttonClearFilter).click().build().perform();
        //buttonClearFilter.click();
    }

    /**
     * Click on Load More button
     */
    public void clickLoadMoreButton(){

        logger.info("Clicking on Load More button");
        wait.until(ExpectedConditions.elementToBeClickable(buttonLoadMore));
        Commons.getElementInToView(driver,buttonLoadMore);
        buttonLoadMore.click();
    }

    /**
     * Verify that Providers URLs(Visit Now) are not broken
     */
    public void verifyProvidersURLNotBroken() {

        logger.info("Verifying that Providers URLs(Visit Now) are not broken");
        SoftAssert softAssert = new SoftAssert();

        List<WebElement> allProviders = providerBase.findElements(By.xpath("div[3]/div//a[text()='visit now']"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allProviders));
        String baseHandle = driver.getWindowHandle();

        for(WebElement provider : allProviders){

            String providerURL=null;
            Commons.getElementInToView(driver,provider);
            provider.click();
            Set<String> windowHandles = driver.getWindowHandles();

            for(String handle:windowHandles){
                if(!handle.equalsIgnoreCase(baseHandle)){
                    driver.switchTo().window(handle);
                    providerURL= driver.getCurrentUrl();
                    driver.close();
                    driver.switchTo().window(baseHandle);
                }
            }

            softAssert = verifyURLAccessibility(providerURL, softAssert);
            softAssert.assertAll();
        }
    }

    /**
     * Verify URL not broken
     * @param providerURL
     */
    public SoftAssert verifyURLAccessibility(String providerURL, SoftAssert softAssert){

        logger.info("Verifying URL not broken");
        try {
            HttpURLConnection huc;
            int respCode;

            huc = (HttpURLConnection)(new URL(providerURL).openConnection());
            huc.setInstanceFollowRedirects(true);
            huc.setConnectTimeout(5000);
            huc.setRequestProperty ("Accept", "*/*");
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");
            huc.setRequestMethod("HEAD");
            huc.connect();
            respCode = huc.getResponseCode();

            if(respCode == 403){
                logger.info(providerURL+"Link is working but some access or cookies issues might be.");
                softAssert.fail("Link is working but some access or cookies issues might be." + providerURL);
            }
            else if(respCode >= 400){
                logger.info(providerURL+" is a broken link");
                softAssert.fail("Link is broken:: " + providerURL);
                return softAssert;
            }
            else{
                logger.info(providerURL+" is a valid link");
                softAssert.assertTrue(true, "Link is valid :: " + providerURL);
            }

        } catch (MalformedURLException e) {
            softAssert.fail("Link URL malformed:: " + providerURL);
            e.printStackTrace();
            return softAssert;
        } catch (IOException e) {
            softAssert.fail("Link not valid:: " + providerURL);
            e.printStackTrace();
            return softAssert;
        }
        return softAssert;
    }

    /**
     * Verify Provider Reviews buttons
     */
    public void verifyProviderSiteReviews() {

        logger.info("Verifying Provider Reviews buttons");
        List<WebElement> allProviders = providerBase.findElements(By.xpath(".//a[text()='visit now']/parent::div/following-sibling::div/a[text()='review']"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allProviders));
        String baseHandle = driver.getWindowHandle();

        for(WebElement provider : allProviders){

            String providerReviewsURL= provider.getAttribute("href");
            Commons.getElementInToView(driver, provider);
            provider.click();

            Set<String> windowHandles = driver.getWindowHandles();
            for(String handle:windowHandles){
                if(!handle.equalsIgnoreCase(baseHandle)){
                    driver.switchTo().window(handle);
                    Assert.assertTrue(driver.getCurrentUrl().equalsIgnoreCase(providerReviewsURL));
                    driver.close();
                    driver.switchTo().window(baseHandle);
                }
            }
        }
    }

    /**
     * Verify Providers Visit Now buttons
     */
    public void verifyProviderSiteVisits() {

        logger.info("Verifying Providers Visit Now buttons");
        List<WebElement> allProviders = providerBase.findElements(By.xpath(".//a[text()='visit now']"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allProviders));

        String baseHandle = driver.getWindowHandle();
        for(WebElement provider : allProviders){

            String[] arr = provider.getAttribute("href").split("/");
            String providerName = arr[arr.length-1];

            Commons.getElementInToView(driver,provider);
            provider.click();
            Set<String> windowHandles = driver.getWindowHandles();
            for(String handle:windowHandles){
                if(!handle.equalsIgnoreCase(baseHandle)){
                    driver.switchTo().window(handle);
                    Assert.assertTrue(driver.getCurrentUrl().contains(providerName));
                    driver.close();
                    driver.switchTo().window(baseHandle);
                }
            }
        }
    }

    /**
     * Verify Clear Filter Functionality
     */
    public void verifyFiltersCleared() {

        logger.info("Verifying Clear Filter Functionality");
        List<WebElement> chkPaymentMethods =moreFiltersActions.findElements(By.xpath(".//input[@name='paymentMethods']"));
        List<WebElement> chkFeatures= moreFiltersActions.findElements(By.xpath(".//input[@name='functions']"));
        List<WebElement> chkUsability= moreFiltersActions.findElements(By.xpath(".//input[@name='easeOfUse']"));
        List<WebElement> chkSupport= moreFiltersActions.findElements(By.xpath(".//input[@name='support']"));

        for(WebElement paymentMethod : chkPaymentMethods){
            Assert.assertTrue(!paymentMethod.isSelected(), "Failed || Payment Method is not cleared.");
        }
        for(WebElement features : chkFeatures){
            Assert.assertTrue(!features.isSelected(), "Failed || Feature is not cleared.");
        }
        for(WebElement usability : chkUsability){
            Assert.assertTrue(!usability.isSelected(), "Failed || Usability is not cleared.");
        }
        for(WebElement support : chkSupport){
            Assert.assertTrue(!support.isSelected(), "Failed || Support is not cleared.");
        }

        WebElement inputFees = moreFiltersActions.findElement(By.xpath(".//p[text()='Fees']/following-sibling::div/input"));
        WebElement inputSafety =moreFiltersActions.findElement(By.xpath(".//p[text()='Safety']/following-sibling::div/input"));
        WebElement inputCoinSelection = moreFiltersActions.findElement(By.xpath(".//p[text()='Coin selection']/following-sibling::div/input"));
        WebElement inputRating = moreFiltersActions.findElement(By.xpath(".//p[text()='Rating']/following-sibling::div/input"));

        Assert.assertEquals(inputFees.getAttribute("value"), "1", "FAILED | Fees not cleared");
        Assert.assertEquals(inputSafety.getAttribute("value"), "1", "FAILED | Safety not cleared");
        Assert.assertEquals(inputCoinSelection.getAttribute("value"), "1", "FAILED | Coin Selection not cleared");
        Assert.assertEquals(inputRating.getAttribute("value"), "1", "FAILED | Rating not cleared");
    }

    /**
     * Verify Providers Payment Method
     * @param paymentMethodName
     */
    public void verifyProviderPaymentMethod(String paymentMethodName) {

        logger.info("Verifying Providers Payment Method");
        List<WebElement> allProviders = providerBase.findElements(By.xpath("div[3]/div"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allProviders));

        boolean paymentMethodFound=false;
        int counter=0;
        for(WebElement provider : allProviders) {

            List<WebElement> providerPaymentMethods = provider.findElements(By.xpath("//div[text()='Payment methods']/following-sibling::div")).
                    get(counter).findElements(By.xpath("span"));
            for(WebElement paymentMethod : providerPaymentMethods){
                if(paymentMethod.getAttribute("data-tip").equalsIgnoreCase(paymentMethodName)){
                    paymentMethodFound=true;
                    break;
                }
            }
            Assert.assertTrue(paymentMethodFound, "FAILED | Payment Method Not Found for Provider Number" + counter+1);
        }
    }

    /**
     * Verify Providers amount and Currency
     * @param amount
     * @param cryptoCurrency
     */
    public void verifyProviderAmountAndCurrency(int amount, String cryptoCurrency){

        logger.info("Verifying Providers amount and Currency");
        new WebDriverWait(driver, 5000).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        List<WebElement> allProviders = providerBase.findElements(By.xpath("div[3]/div"));
        wait.until(ExpectedConditions.visibilityOfAllElements(allProviders));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(".//span[text() = '"+ cryptoCurrency +"']")));


        String actualAmount;
        String actualCurrency;

        for(WebElement provider : allProviders){

            actualCurrency = provider.findElements(By.xpath(".//span[text() = '"+cryptoCurrency+"']")).get(1).getText();
            System.out.println(provider.findElements(By.xpath(".//div[contains(text(), '"+ amount +"')]")).size());
            actualAmount = provider.findElements(By.xpath(".//div[contains(text(), '"+ amount +"')]")).get(1).getText();

            System.out.println(actualAmount + "" + actualCurrency);
            Assert.assertTrue(actualAmount.contains(Integer.toString(amount)), "FAILED || Amount Mismatch");
            Assert.assertEquals(actualCurrency.toLowerCase(), cryptoCurrency.toLowerCase(), "FAILED || Crypto Currency Mismatch");
        }
    }
}
