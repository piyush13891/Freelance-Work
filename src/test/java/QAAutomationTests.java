import PageObjects.*;
import Utils.TestBase;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import io.qameta.allure.*;

@Epic("QAAutomation")
@Feature("QAAutomation")
public class QAAutomationTests extends TestBase {

    static Logger logger = Logger.getLogger(QAAutomationTests.class);

    /**
     * Test 1 - Verify Refresh functionality with amount and currency.
     */
    @Test(priority=1, description =  "TC_001 - Verify Refresh functionality with amount and currency.")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Refresh functionality with amount and currency.")
    public void verifyRefreshButtonFunction() {

        logger.info("Test 1 - Verify Refresh functionality with amount and currency.");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.selectCryptoCurrency("Ethereum");
        interviewTaskPage.setAmount(2500);
        interviewTaskPage.clickRefreshButton();
        interviewTaskPage.verifyProviderAmountAndCurrency(2500,"ETH");
    }

    /**
     * Test 2 - Verify Payment Method Filter
     */
    @Test(priority=2, description =  "TC_002 - Verify Payment Method Filter")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Payment Method Filter\n")
    public void verifyPaymentMethodFilter()  {

        logger.info("Test 2 - Verify Payment Method Filter");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.clickMoreFiltersButton();
        interviewTaskPage.selectPaymentMethods("Sepa Transfer");
        interviewTaskPage.clickLoadMoreButton();
        interviewTaskPage.verifyProviderPaymentMethod("Sepa Transfer");
    }

    /**
     * Test 3 - Verify Clear Filters
     */
    @Test(priority=3, description =  "TC_003 - Verify Clear Filters.")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Clear Filters.")
    public void verifyClearFilters()  {

        logger.info("Test 3 - Verify Clear Filters");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.clickMoreFiltersButton();
        interviewTaskPage.selectPaymentMethods("Sepa Transfer");
        interviewTaskPage.clickClearFilterButton();
        interviewTaskPage.verifyFiltersCleared();
    }

    /**
     * Test 4 - Verify visit provider site(Visit Now)
     */
    @Test(priority=4, description =  "TC_004 - Verify visit provider site(Visit Now)")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify visit provider site(Visit Now)")
    public void verifyVisitProvider()  {

        logger.info("Test 4 - Verify visit provider site(Visit Now)");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.clickLoadMoreButton();
        interviewTaskPage.verifyProviderSiteVisits();
    }

    /**
     * Test 5 - Verify Review button.
     */
    @Test(priority=5, description =  "TC_005 - Verify Review button.")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Review button.")
    public void verifyReviewButton()  {

        logger.info("Test 5 - Verify Review button.");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.clickLoadMoreButton();
        interviewTaskPage.verifyProviderSiteReviews();
    }

    /**
     * Test 6 - Verify Provider Urls not broken
     */
    @Test(priority=6, description =  "TC_006 - Verify Provider Urls not broken")
    @Story("QAAutomation")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify Provider Urls not broken\n")
    public void verifyProvidersURLNotBroken()  {

        logger.info("Test 6 - Verify Provider Urls not broken");
        InterviewTaskPage interviewTaskPage =  new InterviewTaskPage(driver);
        interviewTaskPage.clickLoadMoreButton();
        interviewTaskPage.verifyProvidersURLNotBroken();
    }
}
