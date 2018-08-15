package utils.page_elements.elements.impl;

import org.openqa.selenium.WebElement;
import utils.page_elements.elements.Element;
import utils.page_elements.elements.ElementFactory;

import java.lang.reflect.InvocationTargetException;

import static java.text.MessageFormat.format;

/**
 * Created on 27.03.2018
 */
public class DefaultElementFactory implements ElementFactory {
    @Override
    public <E extends Element> E create(Class<E> elementClass, WebElement wrappedElement) {
        try {
            return findImplementationFor(elementClass)
                    .getDeclaredConstructor(WebElement.class)
                    .newInstance(wrappedElement);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private <E extends Element> Class<? extends E> findImplementationFor(final Class<E> elementClass) {
        try {
            return (Class<? extends E>) Class.forName(format("{0}.{1}Impl",
                    getClass().getPackage().getName(), elementClass.getSimpleName()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
