package services;

import com.google.common.base.Strings;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.Nullable;
import utils.tools.PropertyLoader;
import utils.tools.files.ProjectDir;

import java.util.Properties;

import static com.google.common.base.Strings.isNullOrEmpty;

public class SearchLinkManager {

    private String searchEndPoint = "https://www.olx.pl/nieruchomosci/mieszkania/wynajem/warszawa/?";
    private String searchPattern = "search[%1$s]=%2$s";

    private enum FilterProperty {
        PRICE_TO("price.to"),
        PRICE_FROM("price.from"),
        ROOMS_NUMBER("rooms.number");

        private final String propKey;
        private static Properties rpProperties;

        FilterProperty(String key) {
            propKey = key;
        }

        @Nullable
        public String getValue() {
            String propValue = getRpProperties().getProperty(propKey);
            return isNullOrEmpty(propValue) ? null : propValue;
        }

        private Properties getRpProperties() {
            if (null == rpProperties) {
                rpProperties = PropertyLoader.loadPropertiesFromFile(ProjectDir.getProjectResource("search.properties"));
            }
            return rpProperties;
        }
    }

    private enum FilterUrlResource {
        PRICE_TO_FILTER("filter_float_price:to", FilterProperty.PRICE_TO.getValue()),
        PRICE_FROM_FILTER("filter_float_price:from", FilterProperty.PRICE_FROM.getValue()),
        ROOMS_NUMBER_FILTER("filter_enum_rooms", FilterProperty.ROOMS_NUMBER.getValue());

        private String resourceName;
        private String propertyValue;

        FilterUrlResource(String resourceName, String propertyValue) {
            this.resourceName = resourceName;
            this.propertyValue = propertyValue;
        }

        public String getResourceName() {
            return resourceName;
        }

        public String getPropertyValue() {
            return propertyValue;
        }
    }

    public String createURLWithFilters() {
        URIBuilder uriBuilder = getURIBuilder();

        String filterParams = addParameterToURL(FilterUrlResource.PRICE_TO_FILTER.getResourceName(), FilterUrlResource.PRICE_TO_FILTER.getPropertyValue());
        filterParams += addParameterToURL(FilterUrlResource.PRICE_FROM_FILTER.getResourceName(), FilterUrlResource.PRICE_FROM_FILTER.getPropertyValue());
        filterParams += addParameterToURL(FilterUrlResource.ROOMS_NUMBER_FILTER.getResourceName(), FilterUrlResource.ROOMS_NUMBER_FILTER.getPropertyValue());

        return uriBuilder.setPath(filterParams).toString();
    }

    private String addParameterToURL(String filterName, String filterValue) {
        if (Strings.isNullOrEmpty(filterValue)) {
            return "";
        }

        return String.format(searchPattern,
                filterName,
                filterValue)
                + "&";
    }


    /**
     * Returns {@link URIBuilder} with Endpoint RP
     *
     * @return URIBuilder
     */
    private URIBuilder getURIBuilder() {
        String[] endpointSplit = searchEndPoint.split("://");
        return new URIBuilder()
                .setScheme(endpointSplit[0].trim())
                .setHost(endpointSplit[1].trim());
    }
}
