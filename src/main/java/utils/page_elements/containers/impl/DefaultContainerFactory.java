package utils.page_elements.containers.impl;

import org.openqa.selenium.WebElement;
import utils.page_elements.containers.Container;
import utils.page_elements.containers.ContainerFactory;

/**
 * Created on 27.03.2018
 */
public class DefaultContainerFactory implements ContainerFactory {

    @Override
    public <C extends Container> C create(final Class<C> containerClass, final WebElement wrappedElement) {
        final C container = createInstanceOf(containerClass);
        container.init(wrappedElement);
        return container;
    }

    private <C extends Container> C createInstanceOf(final Class<C> containerClass) {
        try {
            return containerClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
