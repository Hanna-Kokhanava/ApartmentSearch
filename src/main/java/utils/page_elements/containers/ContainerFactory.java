package utils.page_elements.containers;

import org.openqa.selenium.WebElement;

/**
 * Created on 27.03.2018
 */
public interface ContainerFactory {
    <C extends Container> C create(Class<C> containerClass, WebElement wrappedElement);
}

