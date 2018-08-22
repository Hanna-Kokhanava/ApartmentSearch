package cases;

import org.testng.annotations.Test;
import services.SearchLinkManager;
import services.SearchResultService;

public class SearchApartments extends BaseSearch {

    private SearchLinkManager searchService = new SearchLinkManager();
    private SearchResultService searchResultService;

    @Test
    public void initializeServices() {
        searchResultService = new SearchResultService(driver, waiter);

        driver.navigate().to(searchService.createURLWithFilters());
        searchResultService.getPersonInfoList();
    }
}
