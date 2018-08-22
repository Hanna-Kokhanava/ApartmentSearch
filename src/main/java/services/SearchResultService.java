package services;

import model.ApartmentResultItem;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.server.browserlaunchers.Sleeper;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.tools.reporting.ApartmentsInfoReport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchResultService {
    private WebDriverWait waiter;
    private WebDriver driver;

    private ApartmentsInfoReport report = new ApartmentsInfoReport();

    public SearchResultService(WebDriver driver, WebDriverWait waiter) {
        this.driver = driver;
        this.waiter = waiter;
    }

    /**
     * Waits for Page completely loaded
     */
    private void waitForPageLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = driver -> Objects.requireNonNull((JavascriptExecutor) driver)
                .executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(pageLoadCondition);
    }

    public void getPersonInfoList() {
        report.createReportFile();
        String title, location, date, price, link;
        //With stable internet connection, after filter applying, page need time to refresh result items
        Sleeper.sleepTightInSeconds(2);

//        do {
//            waitForPageLoad();
            Sleeper.sleepTightInSeconds(2);
            List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'offer-wrapper')]"));
            for (WebElement element : elements) {
                try {
                    Actions action = new Actions(driver);
                    action.moveToElement(element).perform();

                    link = element.findElement(By.className("detailsLinkPromoted")).getAttribute("href");
                    title = element.findElement(By.xpath("/a[contains(@class, 'detailsLinkPromoted')]/strong")).getText();
                    date = "";
                    location = element.findElement(By.xpath("//*[contains(@class, 'breadcrumb x-normal')]/span")).getText();
                    price = element.findElement(By.xpath("//p[contains(@class, 'price')]/strong")).getText();
                } catch (NoSuchElementException | StaleElementReferenceException e) {
                    continue;
                }
                reportPersonInfoToFile(new ApartmentResultItem(title, location, date, price, link));
            }

//        } while (clickOnNextPageButton());
    }

    private void reportPersonInfoToFile(ApartmentResultItem item) {
        Map<ApartmentsInfoReport.Columns, String> reportInfo = new HashMap<>();
        reportInfo.put(ApartmentsInfoReport.Columns.TITLE, item.getTitle());
        reportInfo.put(ApartmentsInfoReport.Columns.LOCATION, item.getLocation());
        reportInfo.put(ApartmentsInfoReport.Columns.DATE, item.getDate());
        reportInfo.put(ApartmentsInfoReport.Columns.PRICE, item.getPrice());
        reportInfo.put(ApartmentsInfoReport.Columns.LINK, item.getLink());
        try {
            report.saveApartmentInfo(reportInfo);
        } catch (IOException e) {
            System.out.println("Error occured during writing info to report file");
        }
    }
}
