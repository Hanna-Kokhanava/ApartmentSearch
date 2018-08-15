package cases;

import org.testng.annotations.Test;
import services.SearchLinkManager;

public class SearchApartments extends BaseTestCase {

    private SearchLinkManager searchService = new SearchLinkManager();

    @Test
    public void initializeServices() {
        driver.navigate().to(searchService.createURLWithFilters());
    }
}
