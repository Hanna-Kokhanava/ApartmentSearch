package utils.page_elements.elements;

import org.openqa.selenium.WebElement;
import utils.page_elements.elements.Element;

/**
 * Created on 27.03.2018
 */
public interface ElementFactory {
    <E extends Element> E create(Class<E> elementClass, WebElement wrappedElement);
}
