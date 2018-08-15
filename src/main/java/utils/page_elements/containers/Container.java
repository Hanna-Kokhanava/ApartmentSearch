package utils.page_elements.containers;

import org.openqa.selenium.WebElement;
import utils.page_elements.elements.Element;

/**
 * Created on 27.03.2018
 */
public interface Container extends Element {
    void init(WebElement wrappedElement);
}
